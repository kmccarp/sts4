/*******************************************************************************
 * Copyright (c) 2020, 2023 Pivotal, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Pivotal, Inc. - initial API and implementation
 *******************************************************************************/
package org.springframework.ide.vscode.boot.validation.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.ide.vscode.boot.bootiful.BootLanguageServerTest;
import org.springframework.ide.vscode.boot.bootiful.HoverTestConf;
import org.springframework.ide.vscode.boot.validation.generations.SpringIoProjectsProvider;
import org.springframework.ide.vscode.boot.validation.generations.SpringProjectsProvider;
import org.springframework.ide.vscode.boot.validation.generations.json.Generation;
import org.springframework.ide.vscode.boot.validation.generations.json.Link;
import org.springframework.ide.vscode.boot.validation.generations.json.ResolvedSpringProject;
import org.springframework.ide.vscode.boot.validation.generations.json.SpringProject;
import org.springframework.ide.vscode.commons.java.SpringProjectUtil;
import org.springframework.ide.vscode.commons.java.Version;
import org.springframework.ide.vscode.project.harness.BootLanguageServerHarness;
import org.springframework.ide.vscode.project.harness.ProjectsHarness;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@BootLanguageServerTest
@Import(HoverTestConf.class)
public class ProjectGenerationsValidationTest {
	
	@Autowired private BootLanguageServerHarness harness;

	private ProjectsHarness projects = ProjectsHarness.INSTANCE;


	@BeforeEach
	public void setup() throws Exception {
		harness.useProject(projects.mavenProject("empty-boot-1.3.0-app"));
		harness.intialize(null);
	}


    @Test
    void testProjectsInfoFromSpringIo() throws Exception {
        SpringProjectsProvider cache = new SpringIoProjectsProvider("https://api.spring.io/projects");

        SpringProject project = cache.getProject("spring-boot");
        assertNotNull(project);
        assertEquals("Spring Boot", project.getName());
        assertEquals("spring-boot", project.getSlug());
        Link generationsUrl = project.get_links().getGenerations();
        assertNotNull(generationsUrl);
        assertEquals("https://api.spring.io/projects/spring-boot/generations", generationsUrl.getHref());

        project = cache.getProject("spring-integration");
        assertNotNull(project);
        assertEquals("Spring Integration", project.getName());
        assertEquals("spring-integration", project.getSlug());
        generationsUrl = project.get_links().getGenerations();
        assertNotNull(generationsUrl);
        assertEquals("https://api.spring.io/projects/spring-integration/generations", generationsUrl.getHref());

        // Enable when generations is  actually available  from  spring.io
        ResolvedSpringProject resolvedProject = cache.getProject("spring-boot");
        assertNotNull(resolvedProject);
		List<Generation> generations = resolvedProject.getGenerations();
		assertNotNull(generations);
		assertTrue(generations.size() > 5);
    }

    @Test
    void testGenerationsFromSample() throws Exception {

        SampleProjectsProvider provider = new SampleProjectsProvider();

        ResolvedSpringProject project = provider.getProject("spring-boot");
        assertNotNull(project);

        List<Generation> genList = project.getGenerations();

        assertNotNull(genList);
        assertTrue(genList.size() > 0);

        Generation generation = genList.get(0);
        assertEquals("1.3.x", generation.getName());
        assertEquals("2019-01-01", generation.getInitialReleaseDate());
        assertEquals("2020-01-01", generation.getOssSupportEndDate());
        assertEquals("2021-01-01", generation.getCommercialSupportEndDate());
    }

    @Test
    void testDependencyVersionCalculation() throws Exception {
        Version version = SpringProjectUtil.getDependencyVersion("spring-boot-1.2.3.jar", "spring-boot");
        assertEquals(1, version.getMajor(), 1);
        assertEquals(2, version.getMinor(), 2);
        assertEquals(3, version.getPatch());
        assertNull(version.getQualifier());

        version = SpringProjectUtil.getDependencyVersion("spring-boot-1.2.3-RELEASE.jar", "spring-boot");
        assertEquals(version.getMajor(), 1);
        assertEquals(version.getMinor(), 2);
        assertEquals(version.getPatch(), 3);
        assertEquals(version.getQualifier(), "RELEASE");

        version = SpringProjectUtil.getDependencyVersion("spring-boot-1.2.3.RELEASE.jar", "spring-boot");
        assertEquals(1, version.getMajor(), 1);
        assertEquals(2, version.getMinor(), 2);
        assertEquals(3, version.getPatch());
        assertEquals("RELEASE", version.getQualifier());

        version = SpringProjectUtil.getDependencyVersion("spring-boot-1.2.3.BUILD-SNAPSHOT.jar", "spring-boot");
        assertEquals(1, version.getMajor(), 1);
        assertEquals(2, version.getMinor(), 2);
        assertEquals(3, version.getPatch());
        assertEquals("BUILD-SNAPSHOT", version.getQualifier());

        version = SpringProjectUtil.getDependencyVersion("spring-boot-actuator-1.2.3.BUILD-SNAPSHOT.jar", "spring-boot");
        assertNull(version);
    }

    @Test
    void testVersionCalculation1() throws Exception {
        Version version = SpringProjectUtil.getVersion("2.7.5");
        assertEquals(2, version.getMajor());
        assertEquals(7, version.getMinor());
        assertEquals(5, version.getPatch());
        assertNull(version.getQualifier());

        version = SpringProjectUtil.getVersion("3.0.0-SNAPSHOT");
        assertEquals(3, version.getMajor());
        assertEquals(0, version.getMinor());
        assertEquals(0, version.getPatch());
        assertEquals(version.getQualifier(), "SNAPSHOT");


        version = SpringProjectUtil.getVersion("2.6.14-RC2");
        assertEquals(2, version.getMajor());
        assertEquals(6, version.getMinor());
        assertEquals(14, version.getPatch());
        assertEquals(version.getQualifier(), "RC2");
    }

    @Test
    void testVersionCalculation2() throws Exception {
        Version version = SpringProjectUtil.getVersion("2.7");
        assertEquals(2, version.getMajor());
        assertEquals(7, version.getMinor());
        assertEquals(0, version.getPatch());
        assertNull(version.getQualifier());

        version = SpringProjectUtil.getVersion("2");
        assertEquals(2, version.getMajor());
        assertEquals(0, version.getMinor());
        assertEquals(0, version.getPatch());
        assertNull(version.getQualifier());
    }
}
