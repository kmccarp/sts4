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
package org.springframework.ide.vscode.boot.test;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import java.io.File;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.ide.vscode.boot.bootiful.BootLanguageServerTest;
import org.springframework.ide.vscode.boot.bootiful.SymbolProviderTestConf;
import org.springframework.ide.vscode.boot.metadata.DefaultSpringPropertyIndexProvider;
import org.springframework.ide.vscode.commons.languageserver.ProgressService;
import org.springframework.ide.vscode.commons.maven.MavenCore;
import org.springframework.ide.vscode.commons.util.text.LanguageId;
import org.springframework.ide.vscode.commons.util.text.TextDocument;
import org.springframework.ide.vscode.languageserver.testharness.LanguageServerHarness;
import org.springframework.ide.vscode.project.harness.ProjectsHarness;
import org.springframework.test.context.junit.jupiter.SpringExtension;

/**
 * Tests for Boot properties index
 *
 * @author Alex Boyko
 *
 */
@ExtendWith(SpringExtension.class)
@BootLanguageServerTest
@Import(SymbolProviderTestConf.class)
public class SpringPropertiesIndexTest {

	@Autowired
	private LanguageServerHarness harness;

	@Autowired
	private DefaultSpringPropertyIndexProvider propertyIndexProvider;

    @Test
    void testPropertiesIndexRefreshOnProjectChange() throws Exception {
        harness.intialize(new File(ProjectsHarness.class.getResource("/test-projects/boot-1.2.0-properties-live-metadta/").toURI()));

        File directory = new File(ProjectsHarness.class.getResource("/test-projects/boot-1.2.0-properties-live-metadta/").toURI());

        File javaFile = new File(directory, "/src/main/java/demo/Application.java");

        TextDocument doc = new TextDocument(javaFile.toURI().toASCIIString(), LanguageId.JAVA);

        // Not cached yet, hence progress service invoked
        ProgressService progressService = mock(ProgressService.class);
        propertyIndexProvider.setProgressService(progressService);
        propertyIndexProvider.getIndex(doc);
        verify(progressService, atLeastOnce()).progressBegin(any(), any(), any());

        // Should be cached now, so progress service should not be touched
        progressService = mock(ProgressService.class);
        propertyIndexProvider.setProgressService(progressService);
        propertyIndexProvider.getIndex(doc);
        verify(progressService, never()).progressBegin(any(), any(), any());

        // Change POM file for the project
        harness.changeFile(new File(directory, MavenCore.POM_XML).toURI().toASCIIString());

        // POM has changed, hence project needs to be reloaded, cached value is cleared
        progressService = mock(ProgressService.class);
        propertyIndexProvider.setProgressService(progressService);
        propertyIndexProvider.getIndex(doc);
        verify(progressService, atLeastOnce()).progressBegin(any(), any(), any());
    }

}
