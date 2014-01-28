package LinGUIne.handlers;

import javax.inject.Inject;
import javax.inject.Named;

import org.eclipse.e4.core.contexts.ContextInjectionFactory;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Shell;

import LinGUIne.model.ProjectManager;
import LinGUIne.wizards.ImportFileWizard;

/**
 * Handler for importing one or more Files into a Project in the workspace.
 * 
 * @author Kyle Mullins
 */
public class ImportHandler {
	
	@Inject
	@Optional
	private ProjectManager projectMan;
	
	@Inject
	private MApplication application;
	
	/**
	 * Opens the ImportFileWizard.
	 * 
	 * @param shell	The currently active Shell.
	 */
	@Execute
	public void execute(@Named(IServiceConstants.ACTIVE_SHELL) Shell shell) {
		
		ImportFileWizard importWizard = new ImportFileWizard();
		WizardDialog wizardDialog = new WizardDialog(shell, importWizard);
		
		ContextInjectionFactory.inject(importWizard, application.getContext());
		
		int retval = wizardDialog.open();
		
		if(retval == WizardDialog.OK){
			//TODO: stuff?
		}
	}
}
