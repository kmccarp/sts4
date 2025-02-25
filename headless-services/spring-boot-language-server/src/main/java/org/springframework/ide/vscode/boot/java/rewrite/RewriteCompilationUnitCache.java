/*******************************************************************************
 * Copyright (c) 2022, 2023 VMware, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     VMware, Inc. - initial API and implementation
 *******************************************************************************/
package org.springframework.ide.vscode.boot.java.rewrite;

import java.io.ByteArrayInputStream;
import java.net.URI;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CancellationException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.commons.io.IOUtils;
import org.eclipse.lsp4j.TextDocumentIdentifier;
import org.openrewrite.Parser.Input;
import org.openrewrite.Tree;
import org.openrewrite.java.JavaParser;
import org.openrewrite.java.marker.JavaSourceSet;
import org.openrewrite.java.tree.J.CompilationUnit;
import org.openrewrite.java.tree.JavaType;
import org.openrewrite.java.tree.JavaType.FullyQualified;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ide.vscode.boot.java.utils.DocumentContentProvider;
import org.springframework.ide.vscode.boot.java.utils.ServerUtils;
import org.springframework.ide.vscode.commons.java.IClasspathUtil;
import org.springframework.ide.vscode.commons.java.IJavaProject;
import org.springframework.ide.vscode.commons.languageserver.java.JavaProjectFinder;
import org.springframework.ide.vscode.commons.languageserver.java.ProjectObserver;
import org.springframework.ide.vscode.commons.languageserver.util.SimpleLanguageServer;
import org.springframework.ide.vscode.commons.languageserver.util.SimpleTextDocumentService;
import org.springframework.ide.vscode.commons.rewrite.java.ORAstUtils;
import org.springframework.ide.vscode.commons.util.text.TextDocument;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader.InvalidCacheLoadException;
import com.google.common.cache.RemovalListener;
import com.google.common.cache.RemovalNotification;
import com.google.common.util.concurrent.UncheckedExecutionException;

import reactor.core.Disposable;

public class RewriteCompilationUnitCache implements DocumentContentProvider, Disposable {
	
	private static final Logger logger = LoggerFactory.getLogger(RewriteCompilationUnitCache.class);
	
	private static final long CU_ACCESS_EXPIRATION = 1;
//	private JavaProjectFinder projectFinder;
	private ProjectObserver projectObserver;
	
	private final ProjectObserver.Listener projectListener;
	private final SimpleTextDocumentService documentService;

	private final Cache<URI, CompletableFuture<CompilationUnit>> uriToCu;
	private final Cache<URI, Set<URI>> projectToDocs;
	private final Cache<URI, JavaParser> javaParsers;
	
	private final Cache<URI, List<JavaType.FullyQualified>> sourceSetClasspath;
	
	public RewriteCompilationUnitCache(JavaProjectFinder projectFinder, SimpleLanguageServer server, ProjectObserver projectObserver) {
//		this.projectFinder = projectFinder;
		this.projectObserver = projectObserver;
		
		// PT 154618835 - Avoid retaining the CU in the cache as it consumes memory if it hasn't been
		// accessed after some time
		this.uriToCu = CacheBuilder.newBuilder()
				.maximumSize(100)
				.expireAfterAccess(CU_ACCESS_EXPIRATION, TimeUnit.MINUTES)
				.removalListener(new RemovalListener<URI, CompletableFuture<CompilationUnit>>() {

					@Override
					public void onRemoval(RemovalNotification<URI, CompletableFuture<CompilationUnit>> notification) {
						URI uri = notification.getKey();
						CompletableFuture<CompilationUnit> future = notification.getValue();
						
						if (future != null) {
							if (!future.isDone() && !future.isCancelled()) {
								future.cancel(true);
							}
							Optional<IJavaProject> project = projectFinder.find(new TextDocumentIdentifier(uri.toASCIIString()));
							if (project.isPresent()) {

								JavaParser parser = javaParsers.getIfPresent(project.get().getLocationUri());
								if (parser != null) {
//									if (future.isDone()) {
//										try {
//											CompilationUnit cu = future.get();
//											if (cu != null) {
//												parser.resetCUs(List.of(cu));
//												return;
//											}
//										} catch (Throwable t) {
//											logger.error("", t);
//										}
//									}
//									parser.reset(List.of(uri));
									parser.reset();
								}
							}
						}
					}
				})
				.build();
		
		this.projectToDocs = CacheBuilder.newBuilder().build();
		this.javaParsers = CacheBuilder.newBuilder()
				.removalListener(new RemovalListener<URI, JavaParser>() {

					@Override
					public void onRemoval(RemovalNotification<URI, JavaParser> notification) {
						logger.info("CU Cache: invalidate project {}", notification.getKey());
						sourceSetClasspath.invalidate(notification.getKey());
					}
				})
				.build();
		
		this.sourceSetClasspath = CacheBuilder.newBuilder().build();

		this.documentService = server == null ? null : server.getTextDocumentService();

		// IMPORTANT ===> these notifications arrive within the lsp message loop, so reactions to them have to be fast
		// and not be blocked by waiting for anything
		if (this.documentService != null) {
			this.documentService.onDidChangeContent(doc -> invalidateCuForJavaFile(doc.getDocument().getId().getUri()));
			this.documentService.onDidClose(doc -> invalidateCuForJavaFile(doc.getId().getUri()));
		}

//		if (this.projectFinder != null) {
//			for (IJavaProject project : this.projectFinder.all()) {
//				logger.info("CU Cache: initial lookup env creation for project <{}>", project.getElementName());
//				loadJavaParser(project);
//			}
//		}

		this.projectListener = new ProjectObserver.Listener() {
			
			@Override
			public void deleted(IJavaProject project) {
				logger.debug("CU Cache: deleted project {}", project.getElementName());
				invalidateProject(project);
			}
			
			@Override
			public void created(IJavaProject project) {
				logger.debug("CU Cache: created project {}", project.getElementName());
				invalidateProject(project);
//				loadJavaParser(project);
			}
			
			@Override
			public void changed(IJavaProject project) {
				logger.debug("CU Cache: changed project {}", project.getElementName());
				invalidateProject(project);
				// Load the new cache the value right away
//				loadJavaParser(project);
			}
		};

		if (this.projectObserver != null) {
			this.projectObserver.addListener(this.projectListener);
		}
		
		if (server != null) {
			ServerUtils.listenToClassFileChanges(server.getWorkspaceService().getFileObserver(), projectFinder, this::invalidateProject);
		}
		
	}

	public void dispose() {
		if (this.projectObserver != null) {
			this.projectObserver.removeListener(this.projectListener);
		}
	}
	
	private JavaParser loadJavaParser(IJavaProject project) {
		try {
			return javaParsers.get(project.getLocationUri(), () -> ORAstUtils.createJavaParser(project));
		} catch (ExecutionException e) {
			logger.error("{}", e);
			return null;
		}
	}
	
	private void invalidateCuForJavaFile(String uriStr) {
		URI uri = URI.create(uriStr);
		uriToCu.invalidate(uri);
	}
	
	private void invalidateProject(IJavaProject project) {
		Set<URI> docUris = projectToDocs.getIfPresent(project.getLocationUri());
		if (docUris != null) {
			uriToCu.invalidateAll(docUris);
			projectToDocs.invalidate(project.getLocationUri());
		}
		javaParsers.invalidate(project.getLocationUri());
	}

	@Override
	public String fetchContent(URI uri) throws Exception {
		if (documentService != null) {
			TextDocument document = documentService.getLatestSnapshot(uri.toASCIIString());
			if (document != null) {
				return document.get();
			}
		}
		return IOUtils.toString(uri);
	}
	
	public CompilationUnit getCU(IJavaProject project, URI uri) {
		try {
			if (project != null) {
					try {
						return uriToCu.get(uri, () -> {
							CompletableFuture<CompilationUnit> future = CompletableFuture.supplyAsync(() -> {
								try {
									return doParse(project, uri);
								} catch (Exception e) {
									return null;
								}
							});
							return future;
						}).get();
					} catch (UncheckedExecutionException e1) {
						// ignore errors from rewrite parser. There could be many parser exceptions due to
						// user incrementally typing code's text
						return null;
					} catch (InvalidCacheLoadException | CancellationException e) {
						// ignore
					} catch (Exception e) {
						logger.error("", e);
						return null;
					}
				}
		} catch (Exception e) {
			logger.error("Failed to parse {}", uri, e);
		}
		return null;
	}
	
	private CompilationUnit doParse(IJavaProject project, URI uri) throws Exception {
		boolean newParser = javaParsers.getIfPresent(project.getLocationUri()) == null;
		JavaParser javaParser = null;
		try {
			logger.debug("Parsing CU {}", uri);
			javaParser = loadJavaParser(project);
			
			Path sourcePath = Paths.get(uri);
			
			Input input = new Input(sourcePath, () -> {
				try {
					return new ByteArrayInputStream(fetchContent(uri).getBytes());
				} catch (Exception e) {
					throw new IllegalStateException("Unexpected error fetching document content");
				}
			});
			List<CompilationUnit> cus = ORAstUtils.parseInputs(javaParser, List.of(input), null);
			CompilationUnit cu = cus.get(0);
			
			// Manually add source set
			JavaSourceSet sourceSet = createSourceSet(project, ORAstUtils.getSourceSetName(project, sourcePath));
			cu = cu.withMarkers(cu.getMarkers().computeByType(sourceSet, (original, updated) -> updated));
			
			if (cu != null) {
				projectToDocs.get(project.getLocationUri(), () -> new HashSet<>()).add(uri);
				return cu;
			} else {
				throw new IllegalStateException("Failed to parse Java source");
			}
		} catch (Exception e) {
			if (newParser) {
				javaParsers.invalidate(project);
			}
			throw e;
		} finally {
			if (javaParser != null) {
				javaParser.reset(Collections.emptyList());
			}
		}
	}
	
	private JavaSourceSet createSourceSet(IJavaProject project, String name) {
		List<FullyQualified> fqNames;
		try {
			fqNames = sourceSetClasspath.get(project.getLocationUri(), () -> {
				List<Path> classpath = IClasspathUtil.getAllBinaryRoots(project.getClasspath()).stream().map(f -> f.toPath()).collect(Collectors.toList());
				return JavaSourceSet.build("", classpath, null, false).getClasspath();
			});
		} catch (ExecutionException e) {
			logger.error("", e);
			fqNames = Collections.emptyList();
		}
		return new JavaSourceSet(Tree.randomId(), name, fqNames);
	}
	
	/**
	 * Does not need to be via callback - kept the same in order to keep the same API to replace JDT with Rewrite in distant future
	 */
	public <T> T withCompilationUnit(IJavaProject project, URI uri, Function<CompilationUnit, T> requestor) {
		logger.info("CU Cache: work item submitted for doc {}", uri.toASCIIString());
		CompilationUnit cu = getCU(project, uri);
		if (cu != null) {
			try {
				logger.info("CU Cache: start work on AST for {}", uri.toASCIIString());
				return requestor.apply(cu);
			} catch (CancellationException e) {
				throw e;
			} catch (Exception e) {
				logger.error("", e);
			} finally {
				logger.info("CU Cache: end work on AST for {}", uri.toASCIIString());
			}
		}
		return requestor.apply(null);
	}
	
}
