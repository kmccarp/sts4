/*******************************************************************************
 * Copyright (c) 2016, 2023 VMware Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Pivotal, Inc. - initial API and implementation
 *******************************************************************************/
package org.springframework.ide.vscode.commons.languageserver.completion;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CancellationException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

import org.eclipse.lsp4j.Command;
import org.eclipse.lsp4j.CompletionItem;
import org.eclipse.lsp4j.CompletionList;
import org.eclipse.lsp4j.InsertTextFormat;
import org.eclipse.lsp4j.MarkupContent;
import org.eclipse.lsp4j.MarkupKind;
import org.eclipse.lsp4j.Position;
import org.eclipse.lsp4j.TextDocumentPositionParams;
import org.eclipse.lsp4j.TextEdit;
import org.eclipse.lsp4j.jsonrpc.CancelChecker;
import org.eclipse.lsp4j.jsonrpc.messages.Either;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ide.vscode.commons.languageserver.completion.DocumentEdits.TextReplace;
import org.springframework.ide.vscode.commons.languageserver.util.SimpleLanguageServer;
import org.springframework.ide.vscode.commons.languageserver.util.SimpleTextDocumentService;
import org.springframework.ide.vscode.commons.languageserver.util.SortKeys;
import org.springframework.ide.vscode.commons.util.BadLocationException;
import org.springframework.ide.vscode.commons.util.Renderable;
import org.springframework.ide.vscode.commons.util.StringUtil;
import org.springframework.ide.vscode.commons.util.text.IRegion;
import org.springframework.ide.vscode.commons.util.text.TextDocument;

import com.google.common.collect.ImmutableList;
import com.google.gson.JsonPrimitive;

/**
 * Adapts a {@link ICompletionEngine}, wrapping it, to implement {@link VscodeCompletionEngine}
 */
public class VscodeCompletionEngineAdapter implements VscodeCompletionEngine {

	private static final Logger log = LoggerFactory.getLogger(VscodeCompletionEngineAdapter.class);

	public static class LazyCompletionResolver {
		private int nextId = 0; //Used to assign unique id to completion items.

		private Map<String, Consumer<CompletionItem>> resolvers = new HashMap<>();

		private String nextId() {
			//Warning: it's tempting to return 'int' and use a Integer object as id but...
			// Looks like that breaks things because the Integer becomes a Double after being
			// serialized and deserialized to json.
			return ""+(nextId++);
		}

		public synchronized String resolveLater(ICompletionProposal completion, TextDocument doc) {
			String id = nextId();
			resolvers.put(id, (unresolved) -> {
				try {
					resolveItem(doc, completion, unresolved);
				} catch (Exception e) {
					log.error("", e);
				}
			});
			return id;
		}

		public synchronized void resolveNow(CancelChecker cancelToken, CompletionItem unresolved) {
			Object id = unresolved.getData();
			if (id!=null) {
				Consumer<CompletionItem> resolver = resolvers.get(id instanceof JsonPrimitive ? ((JsonPrimitive)id).getAsString() : id);
				if (resolver!=null) {
					resolver.accept(unresolved);
					unresolved.setData(null); //No longer needed after item is resolved.
				} else {
					log.warn("Couldn't resolve completion item. Did it already get flushed from the resolver's cache? "+unresolved.getLabel());
				}
			}
		}

		public synchronized void clear() {
			resolvers.clear();
		}
	}

	private final static int DEFAULT_MAX_COMPLETIONS = 50;
	private int maxCompletions = DEFAULT_MAX_COMPLETIONS; //TODO: move this to CompletionEngineOptions.

	private SimpleLanguageServer server;
	private ICompletionEngine engine;
	private final LazyCompletionResolver resolver;
	private Optional<CompletionFilter> filter;

	/**
	 * By setting a non-null {@link LazyCompletionResolver} you can enable lazy completion resolution.
	 * By default lazy resolution is not implemented.
	 * <p>
	 * The resolver is injected rather than created locally to allow sharing it between multiple
	 * engines.
	 */
	public VscodeCompletionEngineAdapter(SimpleLanguageServer server, ICompletionEngine engine, LazyCompletionResolver resolver, Optional<CompletionFilter> filter) {
		this.server = server;
		this.engine = engine;
		this.resolver = resolver;
		this.filter = filter;
	}

	public void setMaxCompletions(int maxCompletions) {
		this.maxCompletions = maxCompletions;
	}

	@Override
	public CompletionList getCompletions(CancelChecker cancelToken, TextDocumentPositionParams params) {
		long start = System.currentTimeMillis();

		SimpleTextDocumentService documents = server.getTextDocumentService();
		
		log.info("completion handling - retrieve lastest snapshot for: " + params.getTextDocument().getUri());

		TextDocument doc = documents.getLatestSnapshot(params);
		if (doc != null) {
			
			CompletionList list = new CompletionList();

			try {
				log.info("Starting completion handling for: " + params.getTextDocument().getUri());

				if (resolver!=null) {
					//Assumes we don't have more than one completion request in flight from the client.
					// So when a new request arrives we can forget about the old unresolved items:
					resolver.clear();
				}
				
				cancelToken.checkCanceled();

				//TODO: This callable is a 'big lump of work' so can't be canceled in pieces.
				// Should we push using of reactive streams down further and compose this all
				// using reactive style? If not then this is overkill could just as well use
				// only standard Java API such as Executor and CompletableFuture directly.
				int offset = doc.toOffset(params.getPosition());

				// get completions
				Collection<ICompletionProposal> rawCompletions = engine.getCompletions(doc, offset);
				
				cancelToken.checkCanceled();
				
				List<ICompletionProposal> completions = filter(rawCompletions);
				Collections.sort(completions, ScoreableProposal.COMPARATOR);
				
				cancelToken.checkCanceled();
	
				list.setIsIncomplete(false);
				List<CompletionItem> items = new ArrayList<>(completions.size());
				SortKeys sortkeys = new SortKeys();
				int count = 0;

				for (ICompletionProposal c : completions) {
					count++;

					if (maxCompletions > 0 && count>maxCompletions) {
						list.setIsIncomplete(true);
						break;
					}
					try {
						items.add(adaptItem(doc, c, sortkeys));
					} catch (Exception e) {
						log.error("error computing completion", e);
					}
				}
				
				cancelToken.checkCanceled();
				
				list.setItems(items);
				//This is a hack is no  longer  needed but keeping it as  a reference:
				// See: https://bugs.eclipse.org/bugs/show_bug.cgi?id=535823
				// Reason  hack is not needed is because of the fix in: https://www.pivotaltracker.com/story/show/159667257
	
				//				if (LspClient.currentClient()==Client.ECLIPSE) {
				//					list.setIsIncomplete(true); 
				//				}
				return list;
			}
			catch (CancellationException e) {
				log.info("compututing completions cancellled", e);
				throw e;
			}
			catch (Exception e) {
				log.info("error while compututing completions", e);
			}
			finally {
				long end = System.currentTimeMillis();
				log.info("Got {} completions in " + (end - start) + "ms", list.getItems().size());
			}
		}

		log.info("no completions computed due to missing document snapshot for: ", params.getTextDocument().getUri());
		return SimpleTextDocumentService.NO_COMPLETIONS;
	}

	private CompletionItem adaptItem(TextDocument doc, ICompletionProposal completion, SortKeys sortkeys) throws Exception {
		CompletionItem item = new CompletionItem();
		item.setLabel(completion.getLabel());
		item.setKind(completion.getKind());
		item.setSortText(sortkeys.next());
		item.setFilterText(completion.getFilterText());
		item.setDetail(completion.getDetail());
		if (completion.isDeprecated()) {
			item.setDeprecated(completion.isDeprecated());
		}
		resolveEdits(doc, completion, item); //Warning. Its not allowed by LSP spec to resolveEdits
											//lazy as we used to do in the past.
		if (completion.getDocumentation() != null) {
			if (resolver!=null) {
				item.setData(resolver.resolveLater(completion, doc));
			} else {
				resolveItem(doc, completion, item);
			}
		}
		
		if (completion.isTriggeringNextCompletionRequest()) {
			item.setCommand(new Command("Completion Proposal Request", "editor.action.triggerSuggest"));
		}
		return item;
	}

	private List<ICompletionProposal> filter(Collection<ICompletionProposal> completions) {
		if (filter.isPresent()) {
			List<ICompletionProposal> filtered = new ArrayList<>(completions.size());
			CompletionFilter filterVal = filter.get();
			for (ICompletionProposal proposal : completions) {
				if (filterVal.include(proposal)) {
					filtered.add(proposal);
				}
			}
			return filtered;
		}
		else {
			return new ArrayList<>(completions);
		}
	}

	private static void resolveItem(TextDocument doc, ICompletionProposal completion, CompletionItem item) throws Exception {
		MarkupContent content = new MarkupContent();
		content.setKind(MarkupKind.MARKDOWN);
		content.setValue(toMarkdown(completion.getDocumentation()));
		item.setDocumentation(content);
	}

	private void resolveEdits(TextDocument doc, ICompletionProposal completion, CompletionItem item) {
		AtomicBoolean usedSnippets = new AtomicBoolean();
		Optional<TextEdit> mainEdit = adaptEdits(doc, completion.getTextEdit(), usedSnippets);
		if (mainEdit.isPresent()) {
			item.setTextEdit(Either.forLeft(mainEdit.get()));
			if (server.hasCompletionSnippetSupport()) {
				item.setInsertTextFormat(usedSnippets.get() ? InsertTextFormat.Snippet : InsertTextFormat.PlainText);
			} else {
				item.setInsertTextFormat(InsertTextFormat.PlainText);
			}
		} else {
			item.setInsertText("");
		}

		completion.getAdditionalEdit().ifPresent(edit -> {
			adaptEdits(doc, edit, null).ifPresent(extraEdit -> {
				item.setAdditionalTextEdits(ImmutableList.of(extraEdit));
			});
		});
	}

	private static String toMarkdown(Renderable r) {
		if (r!=null) {
			return r.toMarkdown();
		}
		return null;
	}

	private Optional<TextEdit> adaptEdits(TextDocument doc, DocumentEdits edits, AtomicBoolean usedSnippets) {
		try {
			TextReplace replaceEdit = edits.asReplacement(doc);
			if (usedSnippets != null) {
				usedSnippets.set(edits.hasSnippets());
			}
			if (replaceEdit==null) {
				//The original edit does nothing.
				return Optional.empty();
			} else {
				TextDocument newDoc = doc.copy();
				edits.apply(newDoc);
				TextEdit vscodeEdit = new TextEdit();
				vscodeEdit.setRange(doc.toRange(replaceEdit.start, replaceEdit.end-replaceEdit.start));
				String newText = replaceEdit.newText;
				IRegion selection = edits.getSelection();
				if (selection!=null && usedSnippets != null) {
					//Special handling for the case where cursor is *not* just at the end of the newText
					int cursor = selection.getOffset() + selection.getLength();
					cursor = cursor - replaceEdit.start;
					if (cursor < newText.length() && !edits.hasSnippets()) {
						newText = server.createSnippetBuilder()
								.text(newText.substring(0, cursor))
								.finalTabStop()
								.text(newText.substring(cursor))
								.build()
								.toString();
						usedSnippets.set(true);
					}
				}
				if (isMagicIndentingClient()) {
					newText = vscodeIndentFix(doc, vscodeEdit.getRange().getStart(), replaceEdit.newText);
				}
				vscodeEdit.setNewText(newText);
				return Optional.of(vscodeEdit);
			}
		}  catch (Exception e) {
			log.error("{}", e);
			return Optional.empty();
		}
	}

	/**
	 * When this is true, it means the client does 'magic indents' (basically.. that is only on vscode since the magics aren't part of the LSP spec).
	 */
	private boolean isMagicIndentingClient() {
		return !Boolean.getBoolean("lsp.completions.indentation.enable");
	}

	private static String vscodeIndentFix(TextDocument doc, Position start, String newText) {
		//Vscode applies some magic indent to a multi-line edit text. We do everything ourself so we have adjust for the magic
		// and do some kind of 'inverse magic' here.
		//See here: https://github.com/Microsoft/language-server-protocol/issues/83
		IndentUtil indenter = new IndentUtil(doc);
		try {
			String refIndent = indenter.getReferenceIndent(doc.toOffset(start), doc);
			if (!refIndent.isEmpty()) {
				return  StringUtil.stripIndentation(refIndent, newText);
			}
		} catch (BadLocationException e) {
			log.error("{}", e);
		}
		return newText;
	}

	@Override
	public CompletionItem resolveCompletion(CancelChecker cancelToken, CompletionItem unresolved) {
		resolver.resolveNow(cancelToken, unresolved);
		return unresolved;
	}

	@FunctionalInterface
	public interface CompletionFilter {

		/**
		 *
		 * @param proposal
		 * @return true if proposal should be included from completion list. False
		 *         otherwise
		 */
		boolean include(ICompletionProposal proposal);

	}
}
