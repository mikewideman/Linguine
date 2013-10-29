 
package LinGUIne.handlers;

import javax.inject.Named;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Shell;

import LinGUIne.wizards.AnalysisWizard;

/**
 * Handler for starting the Analysis Wizard.
 * 
 * @author Matthew Talbot
 */

public class AnalysisHandler {
	
	/**
	 * Prompts the user through a series of wizard windows to set any
	 * necessary user settings prior to running an analysis
	 * 
	 * @param shell	The currently active Shell.
	 */
	
	@Execute
	public void execute(@Named(IServiceConstants.ACTIVE_SHELL) Shell shell) {
		WizardDialog wizardDialog = new WizardDialog(shell, new AnalysisWizard());
		wizardDialog.open();
	}
}