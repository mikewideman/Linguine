 
package LinGUIne.handlers;

import javax.inject.Inject;
import javax.inject.Named;

import org.eclipse.e4.core.contexts.ContextInjectionFactory;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Shell;

import LinGUIne.wizards.ExportAnnotationWizard;

public class ExportAnnotationHandler {
	
	@Inject
	private MApplication application;
	
	@Execute
	public void execute(@Named(IServiceConstants.ACTIVE_SHELL) Shell shell) {
		ExportAnnotationWizard exportWizard = new ExportAnnotationWizard();
		WizardDialog wizardDialog = new WizardDialog(shell, exportWizard);
		
		ContextInjectionFactory.inject(exportWizard, application.getContext());
		
		wizardDialog.open();
	}
}