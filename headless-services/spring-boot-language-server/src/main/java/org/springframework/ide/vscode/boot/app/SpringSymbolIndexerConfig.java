/*******************************************************************************
 * Copyright (c) 2019 Pivotal, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Pivotal, Inc. - initial API and implementation
 *******************************************************************************/
package org.springframework.ide.vscode.boot.app;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.ide.vscode.boot.index.SpringMetamodelIndex;
import org.springframework.ide.vscode.boot.java.Annotations;
import org.springframework.ide.vscode.boot.java.annotations.AnnotationHierarchyAwareLookup;
import org.springframework.ide.vscode.boot.java.beans.BeansSymbolProvider;
import org.springframework.ide.vscode.boot.java.beans.ComponentSymbolProvider;
import org.springframework.ide.vscode.boot.java.data.DataRepositorySymbolProvider;
import org.springframework.ide.vscode.boot.java.handlers.SymbolProvider;
import org.springframework.ide.vscode.boot.java.requestmapping.RequestMappingSymbolProvider;
import org.springframework.ide.vscode.boot.java.requestmapping.WebfluxRouterSymbolProvider;
import org.springframework.ide.vscode.boot.java.utils.RestrictedDefaultSymbolProvider;
import org.springframework.ide.vscode.boot.java.utils.SymbolCache;

@Configuration(proxyBeanMethods = false)
public class SpringSymbolIndexerConfig {

	@Bean
	AnnotationHierarchyAwareLookup<SymbolProvider> symbolProviders(SymbolCache cache) {
		AnnotationHierarchyAwareLookup<SymbolProvider> providers = new AnnotationHierarchyAwareLookup<>();

		RequestMappingSymbolProvider requestMappingSymbolProvider = new RequestMappingSymbolProvider();
		BeansSymbolProvider beansSymbolProvider = new BeansSymbolProvider();
		ComponentSymbolProvider componentSymbolProvider = new ComponentSymbolProvider();
		RestrictedDefaultSymbolProvider restrictedDefaultSymbolProvider = new RestrictedDefaultSymbolProvider();
		DataRepositorySymbolProvider dataRepositorySymbolProvider = new DataRepositorySymbolProvider();
		WebfluxRouterSymbolProvider webfluxRouterSymbolProvider = new WebfluxRouterSymbolProvider();

		providers.put(Annotations.SPRING_REQUEST_MAPPING, requestMappingSymbolProvider);
		providers.put(Annotations.SPRING_GET_MAPPING, requestMappingSymbolProvider);
		providers.put(Annotations.SPRING_POST_MAPPING, requestMappingSymbolProvider);
		providers.put(Annotations.SPRING_PUT_MAPPING, requestMappingSymbolProvider);
		providers.put(Annotations.SPRING_DELETE_MAPPING, requestMappingSymbolProvider);
		providers.put(Annotations.SPRING_PATCH_MAPPING, requestMappingSymbolProvider);

		providers.put(Annotations.BEAN, beansSymbolProvider);
		providers.put(Annotations.COMPONENT, componentSymbolProvider);

		providers.put(Annotations.PROFILE, restrictedDefaultSymbolProvider);

		providers.put(Annotations.CONDITIONAL, restrictedDefaultSymbolProvider);
		providers.put(Annotations.CONDITIONAL_ON_BEAN, restrictedDefaultSymbolProvider);
		providers.put(Annotations.CONDITIONAL_ON_MISSING_BEAN, restrictedDefaultSymbolProvider);
		providers.put(Annotations.CONDITIONAL_ON_PROPERTY, restrictedDefaultSymbolProvider);
		providers.put(Annotations.CONDITIONAL_ON_RESOURCE, restrictedDefaultSymbolProvider);
		providers.put(Annotations.CONDITIONAL_ON_CLASS, restrictedDefaultSymbolProvider);
		providers.put(Annotations.CONDITIONAL_ON_MISSING_CLASS, restrictedDefaultSymbolProvider);
		providers.put(Annotations.CONDITIONAL_ON_CLOUD_PLATFORM, restrictedDefaultSymbolProvider);
		providers.put(Annotations.CONDITIONAL_ON_WEB_APPLICATION, restrictedDefaultSymbolProvider);
		providers.put(Annotations.CONDITIONAL_ON_NOT_WEB_APPLICATION, restrictedDefaultSymbolProvider);
		providers.put(Annotations.CONDITIONAL_ON_ENABLED_INFO_CONTRIBUTOR, restrictedDefaultSymbolProvider);
		providers.put(Annotations.CONDITIONAL_ON_ENABLED_RESOURCE_CHAIN, restrictedDefaultSymbolProvider);
		providers.put(Annotations.CONDITIONAL_ON_ENABLED_ENDPOINT, restrictedDefaultSymbolProvider);
		providers.put(Annotations.CONDITIONAL_ON_ENABLED_HEALTH_INDICATOR, restrictedDefaultSymbolProvider);
		providers.put(Annotations.CONDITIONAL_ON_EXPRESSION, restrictedDefaultSymbolProvider);
		providers.put(Annotations.CONDITIONAL_ON_JAVA, restrictedDefaultSymbolProvider);
		providers.put(Annotations.CONDITIONAL_ON_JNDI, restrictedDefaultSymbolProvider);
		providers.put(Annotations.CONDITIONAL_ON_SINGLE_CANDIDATE, restrictedDefaultSymbolProvider);

		providers.put(Annotations.REPOSITORY, dataRepositorySymbolProvider);
		providers.put("", webfluxRouterSymbolProvider);

		return providers;
	}
}
