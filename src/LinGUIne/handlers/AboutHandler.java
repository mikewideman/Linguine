package LinGUIne.handlers;

import javax.inject.Inject;
import javax.inject.Named;

import org.eclipse.e4.core.contexts.ContextInjectionFactory;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Shell;

import LinGUIne.wizards.AboutWizard;

/**
 * Handler for using the About command.
 * 
 * @author Matthew Talbot
 */

public class AboutHandler {
	
	@Inject
	private MApplication application;
	
	/**
	 * Opens the About Wizard.
	 */
	@Execute
	public void execute(@Named(IServiceConstants.ACTIVE_SHELL) Shell shell) {
		AboutWizard aboutWizard = new AboutWizard();
		WizardDialog wizardDialog = new WizardDialog(shell, aboutWizard);
		ContextInjectionFactory.inject(aboutWizard, application.getContext());
		int retval = wizardDialog.open();
		if(retval == WizardDialog.OK){
		}
	}
		
}
