package LinGUIne.handlers;

import javax.inject.Inject;
import javax.inject.Named;

import org.eclipse.e4.core.contexts.ContextInjectionFactory;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Shell;

import LinGUIne.wizards.VisualizationWizard;

/**
 * Handler for starting the Visualization Wizard.
 * 
 * @author Peter Dimou
 */
public class VisualizationHandler {
	
	@Inject
	private MApplication application;
	
	/**
	 * Prompts the user through a series of wizard windows to set any
	 * necessary user settings prior to running a visualization
	 * 
	 * @param shell	The currently active Shell.
	 */
	
	@Execute
	public void execute(@Named(IServiceConstants.ACTIVE_SHELL) Shell shell) {
		VisualizationWizard visualizationWizard = new VisualizationWizard();
		WizardDialog wizardDialog = new WizardDialog(shell, visualizationWizard);
		
		ContextInjectionFactory.inject(visualizationWizard, application.getContext());
		
		int retval = wizardDialog.open();
		
		if(retval == WizardDialog.OK){
			//TODO: stuff?
		}
	}
}