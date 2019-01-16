/*******************************************************************************
 * Copyright (c) 2017, 2019 Pivotal, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Pivotal, Inc. - initial API and implementation
 *******************************************************************************/
package org.springframework.ide.vscode.boot.java;

import org.springframework.ide.vscode.commons.languageserver.util.Settings;

/**
 * Boot-Java LS settings
 *
 * @author Alex Boyko
 *
 */
public class BootJavaConfig {

	private Settings settings = new Settings(null);

	public boolean isBootHintsEnabled() {
		Boolean enabled = settings.getBoolean("boot-java", "boot-hints", "on");
		return enabled == null || enabled.booleanValue();
	}

	public boolean isSpringXMLSupportEnabled() {
		Boolean enabled = settings.getBoolean("boot-java", "support-spring-xml-config", "on");
		return enabled != null && enabled.booleanValue();
	}

	public boolean isChangeDetectionEnabled() {
		Boolean enabled = settings.getBoolean("boot-java", "change-detection", "on");
		return enabled != null && enabled.booleanValue();
	}

	public void handleConfigurationChange(Settings newConfig) {
		this.settings = newConfig;
	}


}
