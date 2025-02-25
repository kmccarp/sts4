/*******************************************************************************
 * Copyright (c) 2023 VMware, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     VMware, Inc. - initial API and implementation
 *******************************************************************************/
package org.springframework.ide.vscode.boot.java.handlers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ide.vscode.boot.app.BootJavaConfig;
import org.springframework.ide.vscode.boot.common.IJavaProjectReconcileEngine;
import org.springframework.ide.vscode.boot.common.ProjectReconcileScheduler;
import org.springframework.ide.vscode.boot.java.rewrite.RewriteRecipeRepository;
import org.springframework.ide.vscode.boot.java.utils.ServerUtils;
import org.springframework.ide.vscode.commons.java.IJavaProject;
import org.springframework.ide.vscode.commons.languageserver.java.JavaProjectFinder;
import org.springframework.ide.vscode.commons.languageserver.java.ProjectObserver;
import org.springframework.ide.vscode.commons.languageserver.util.SimpleLanguageServer;

public class BootJavaProjectReconcilerScheduler extends ProjectReconcileScheduler {
	
	private static final Logger log = LoggerFactory.getLogger(BootJavaProjectReconcilerScheduler.class);

	private ProjectObserver projectObserver;
	private BootJavaConfig config;
	private RewriteRecipeRepository recipesRepo;

	public BootJavaProjectReconcilerScheduler(IJavaProjectReconcileEngine reconciler, ProjectObserver projectObserver,
			BootJavaConfig config, RewriteRecipeRepository recipesRepo, JavaProjectFinder projectFinder, SimpleLanguageServer server) {
		super(server, reconciler, projectFinder);
		this.projectObserver = projectObserver;
		this.config = config;
		this.recipesRepo = recipesRepo;
	}

	@Override
	protected void init() {
		log.info("Starting project reconciler for Java sources");
		super.init();
		if (recipesRepo != null) {
			recipesRepo.onRecipesLoaded(v -> {
				// Recipes will start loading only after config has been received. Therefore safe to start listening to config changes now
				// and launch initial project reconcile since both config and recipes are present
				startListeningToPerformReconcile();			
				scheduleValidationForAllProjects();
				log.info("Started project reconciler for Java sources");
			});
		} else {
			startListeningToPerformReconcile();			
			scheduleValidationForAllProjects();
			log.info("Started project reconciler for Java sources");
		}
	}

	private void startListeningToPerformReconcile() {
		config.addListener(evt -> scheduleValidationForAllProjects());
		
		projectObserver.addListener(new ProjectObserver.Listener() {
			
			@Override
			public void deleted(IJavaProject project) {
				unscheduleValidation(project);
				clear(project, true);
			}
			
			@Override
			public void created(IJavaProject project) {
				scheduleValidation(project);
			}
			
			@Override
			public void changed(IJavaProject project) {
				scheduleValidation(project);
			}
		});
		
		ServerUtils.listenToClassFileChanges(getServer().getWorkspaceService().getFileObserver(), getProjectFinder(), this::scheduleValidation);
		
		// TODO: index update even happens on every file save. Very expensive to blindly reconcile all projects.
		// Need to figure out a check if spring index has any changes 
//		springIndexer.onUpdate(v -> reconcile());
	}

}
