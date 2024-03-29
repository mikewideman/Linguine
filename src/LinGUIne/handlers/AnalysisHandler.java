 
package LinGUIne.handlers;

import javax.inject.Inject;
import javax.inject.Named;

import org.eclipse.e4.core.contexts.ContextInjectionFactory;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.model.application.MApplication;
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
	
	@Inject
	private MApplication application;
	
	/**
	 * Prompts the user through a series of wizard windows to set any
	 * necessary user settings prior to running an analysis
	 * 
	 * @param shell	The currently active Shell.
	 */
	
	@Execute
	public void execute(@Named(IServiceConstants.ACTIVE_SHELL) Shell shell) {
		AnalysisWizard analysisWizard = new AnalysisWizard();
		WizardDialog wizardDialog = new WizardDialog(shell, analysisWizard);
		
		ContextInjectionFactory.inject(analysisWizard, application.getContext());
		
		int retval = wizardDialog.open();
		
		if(retval == WizardDialog.OK){
			//TODO: stuff?
		}
	}
}