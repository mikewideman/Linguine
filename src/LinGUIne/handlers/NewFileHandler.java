 
package LinGUIne.handlers;

import javax.inject.Inject;
import javax.inject.Named;

import org.eclipse.e4.core.contexts.ContextInjectionFactory;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Shell;

import LinGUIne.wizards.NewFileWizard;

public class NewFileHandler {
	
	@Inject
	private MApplication application;
	
	@Execute
	public void execute(@Named(IServiceConstants.ACTIVE_SHELL) Shell shell) {
		NewFileWizard newFileWizard = new NewFileWizard();
		WizardDialog wizardDialog = new WizardDialog(shell, newFileWizard);
		
		ContextInjectionFactory.inject(newFileWizard, application.getContext());

		int retval = wizardDialog.open();
		
		if(retval == WizardDialog.OK){
			//TODO: stuff?
		}
	}
}