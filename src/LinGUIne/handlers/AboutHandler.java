/*******************************************************************************
 * Copyright (c) 2010 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package LinGUIne.handlers;

import javax.inject.Named;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;

import LinGUIne.wizards.AboutDialog;

public class AboutHandler {
	@Execute
	public void execute(@Named(IServiceConstants.ACTIVE_SHELL) Shell shell) {
		MessageDialog.openInformation(shell, "About", "Eclipse 4 Application example.");
		BundleContext ctx = FrameworkUtil.getBundle(LinGUIne.utilities.Activator.class).getBundleContext();
		Bundle[] bundles = ctx.getBundles();
		for(int i = 0; i < bundles.length; i++){
			System.out.println(bundles[i].toString());
		}
		AboutDialog dia = new AboutDialog(shell);
		dia.open();
	}
}
