package LinGUIne.handlers;

import javax.inject.Inject;
import javax.inject.Named;

import org.eclipse.e4.core.contexts.ContextInjectionFactory;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Shell;

import LinGUIne.wizards.ExportFileWizard;

/**
 * Handles exporting Project Data to other file formats.
 * 
 * @author Kyle Mullins
 */
public class ExportFileHandler {
	@Inject
	private MApplication application;
	
	@Execute
	public void execute(@Named(IServiceConstants.ACTIVE_SHELL) Shell shell) {
		ExportFileWizard exportWizard = new ExportFileWizard();
		WizardDialog wizardDialog = new WizardDialog(shell, exportWizard);
		
		ContextInjectionFactory.inject(exportWizard, application.getContext());
		
		wizardDialog.open();
	}
}
