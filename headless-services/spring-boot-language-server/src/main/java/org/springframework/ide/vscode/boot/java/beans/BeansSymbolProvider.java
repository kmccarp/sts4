/*******************************************************************************
 * Copyright (c) 2017, 2023 Pivotal, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Pivotal, Inc. - initial API and implementation
 *******************************************************************************/
package org.springframework.ide.vscode.boot.java.beans;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Annotation;
import org.eclipse.jdt.core.dom.IAnnotationBinding;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.Modifier;
import org.eclipse.jdt.core.dom.ParameterizedType;
import org.eclipse.jdt.core.dom.StringLiteral;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.lsp4j.Location;
import org.eclipse.lsp4j.SymbolKind;
import org.eclipse.lsp4j.WorkspaceSymbol;
import org.eclipse.lsp4j.jsonrpc.messages.Either;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ide.vscode.boot.java.Annotations;
import org.springframework.ide.vscode.boot.java.handlers.AbstractSymbolProvider;
import org.springframework.ide.vscode.boot.java.handlers.EnhancedSymbolInformation;
import org.springframework.ide.vscode.boot.java.handlers.SymbolAddOnInformation;
import org.springframework.ide.vscode.boot.java.utils.ASTUtils;
import org.springframework.ide.vscode.boot.java.utils.CachedSymbol;
import org.springframework.ide.vscode.boot.java.utils.FunctionUtils;
import org.springframework.ide.vscode.boot.java.utils.SpringIndexerJavaContext;
import org.springframework.ide.vscode.commons.protocol.spring.Bean;
import org.springframework.ide.vscode.commons.protocol.spring.InjectionPoint;
import org.springframework.ide.vscode.commons.util.BadLocationException;
import org.springframework.ide.vscode.commons.util.text.DocumentRegion;
import org.springframework.ide.vscode.commons.util.text.TextDocument;

import com.google.common.collect.ImmutableList;

import reactor.util.function.Tuple2;
import reactor.util.function.Tuple3;
import reactor.util.function.Tuples;

/**
 * @author Martin Lippert
 * @author Kris De Volder
 */
public class BeansSymbolProvider extends AbstractSymbolProvider {
	
	
	private static final Logger log = LoggerFactory.getLogger(BeansSymbolProvider.class);

	private static final String[] NAME_ATTRIBUTES = {"value", "name"};

	@Override
	protected void addSymbolsPass1(Annotation node, ITypeBinding annotationType, Collection<ITypeBinding> metaAnnotations, SpringIndexerJavaContext context, TextDocument doc) {
		if (node == null) return;
		
		ASTNode parent = node.getParent();
		if (parent == null || !(parent instanceof MethodDeclaration)) return;
		
		MethodDeclaration method = (MethodDeclaration) parent;

		if (isMethodAbstract(method)) return;

		boolean isFunction = isFunctionBean(method);
		ITypeBinding beanType = getBeanType(method);
		String markerString = getAnnotations(method);

		for (Tuple2<String, DocumentRegion> nameAndRegion : getBeanNames(node, doc)) {
			try {
				Location location = new Location(doc.getUri(), doc.toRange(nameAndRegion.getT2()));

				EnhancedSymbolInformation enhancedSymbol = new EnhancedSymbolInformation(
						new WorkspaceSymbol(
								beanLabel(isFunction, nameAndRegion.getT1(), beanType.getName(), "@Bean" + markerString),
								SymbolKind.Interface,
								Either.forLeft(location)),
						new SymbolAddOnInformation[] {new BeansSymbolAddOnInformation(nameAndRegion.getT1(), beanType.getQualifiedName())}
				);

				InjectionPoint[] injectionPoints = ASTUtils.findInjectionPoints(method, doc);
				
				Set<String> supertypes = new HashSet<>();
				ASTUtils.findSupertypes(beanType, supertypes);

				Bean beanDefinition = new Bean(nameAndRegion.getT1(), beanType.getQualifiedName(), location, injectionPoints, (String[]) supertypes.toArray(new String[supertypes.size()]));

				context.getGeneratedSymbols().add(new CachedSymbol(context.getDocURI(), context.getLastModified(), enhancedSymbol, beanDefinition));

			} catch (BadLocationException e) {
				log.error("", e);
			}
		}
	}

	@Override
	protected void addSymbolsPass1(TypeDeclaration typeDeclaration, SpringIndexerJavaContext context, TextDocument doc) {
		// this checks function beans that are defined as implementations of Function interfaces
		Tuple3<String, ITypeBinding, DocumentRegion> functionBean = FunctionUtils.getFunctionBean(typeDeclaration, doc);
		if (functionBean != null) {
			try {
				WorkspaceSymbol symbol = new WorkspaceSymbol(
						beanLabel(true, functionBean.getT1(), functionBean.getT2().getName(), null),
						SymbolKind.Interface,
						Either.forLeft(new Location(doc.getUri(), doc.toRange(functionBean.getT3()))));

				context.getGeneratedSymbols().add(new CachedSymbol(context.getDocURI(), context.getLastModified(),
						new EnhancedSymbolInformation(symbol, new SymbolAddOnInformation[] {new BeansSymbolAddOnInformation(functionBean.getT1(), functionBean.getT2().getQualifiedName())}), null));

			} catch (BadLocationException e) {
				log.error("", e);
			}
		}
	}

	protected Collection<Tuple2<String, DocumentRegion>> getBeanNames(Annotation node, TextDocument doc) {
		Collection<StringLiteral> beanNameNodes = getBeanNameLiterals(node);

		if (beanNameNodes != null && !beanNameNodes.isEmpty()) {
			ImmutableList.Builder<Tuple2<String,DocumentRegion>> namesAndRegions = ImmutableList.builder();
			for (StringLiteral nameNode : beanNameNodes) {
				String name = ASTUtils.getLiteralValue(nameNode);
				namesAndRegions.add(Tuples.of(name, ASTUtils.stringRegion(doc, nameNode)));
			}
			return namesAndRegions.build();
		}
		else {
			ASTNode parent = node.getParent();
			if (parent instanceof MethodDeclaration) {
				MethodDeclaration method = (MethodDeclaration) parent;
				return ImmutableList.of(Tuples.of(
						method.getName().toString(),
						ASTUtils.nameRegion(doc, node)
				));
			}
			return ImmutableList.of();
		}
	}

	public static String beanLabel(boolean isFunctionBean, String beanName, String beanType, String markerString) {
		StringBuilder symbolLabel = new StringBuilder();
		symbolLabel.append('@');
		symbolLabel.append(isFunctionBean ? '>' : '+');
		symbolLabel.append(' ');
		symbolLabel.append('\'');
		symbolLabel.append(beanName);
		symbolLabel.append('\'');

		markerString = markerString != null && markerString.length() > 0 ? " (" + markerString + ") " : " ";
		symbolLabel.append(markerString);

		symbolLabel.append(beanType);
		return symbolLabel.toString();
	}

	protected Collection<StringLiteral> getBeanNameLiterals(Annotation node) {
		ImmutableList.Builder<StringLiteral> literals = ImmutableList.builder();
		for (String attrib : NAME_ATTRIBUTES) {
			ASTUtils.getAttribute(node, attrib).ifPresent((valueExp) -> {
				literals.addAll(ASTUtils.getExpressionValueAsListOfLiterals(valueExp));
			});
		}
		return literals.build();
	}

	protected ITypeBinding getBeanType(MethodDeclaration method) {
		return method.getReturnType2().resolveBinding();
	}

	private boolean isFunctionBean(MethodDeclaration method) {
		String returnType = null;

		if (method.getReturnType2().isParameterizedType()) {
			ParameterizedType paramType = (ParameterizedType) method.getReturnType2();
			Type type = paramType.getType();
			ITypeBinding typeBinding = type.resolveBinding();
			returnType = typeBinding.getBinaryName();
		}
		else {
			returnType = method.getReturnType2().resolveBinding().getQualifiedName();
		}

		return FunctionUtils.FUNCTION_FUNCTION_TYPE.equals(returnType) || FunctionUtils.FUNCTION_CONSUMER_TYPE.equals(returnType)
				|| FunctionUtils.FUNCTION_SUPPLIER_TYPE.equals(returnType);
	}

	private String getAnnotations(MethodDeclaration method) {
		StringBuilder result = new StringBuilder();

		List<?> modifiers = method.modifiers();
		for (Object modifier : modifiers) {
			if (modifier instanceof Annotation) {
				Annotation annotation = (Annotation) modifier;
				IAnnotationBinding annotationBinding = annotation.resolveAnnotationBinding();
				String type = annotationBinding.getAnnotationType().getBinaryName();

				if (type != null && !Annotations.BEAN.equals(type)) {
					result.append(' ');
					result.append(annotation.toString());
				}
			}
		}
		return result.toString();
	}

	private boolean isMethodAbstract(MethodDeclaration method) {
		List<?> modifiers = method.modifiers();
		for (Object modifier : modifiers) {
			if (modifier instanceof Modifier && ((Modifier) modifier).isAbstract()) {
				return true;
			}
		}
		return false;
	}

}
