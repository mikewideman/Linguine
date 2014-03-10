package LinGUIne.handlers;

import java.io.IOException;

import javax.inject.Inject;
import javax.inject.Named;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Shell;

import LinGUIne.model.Project;
import LinGUIne.model.ProjectManager;
import LinGUIne.wizards.NewProjectWizard;

/**
 * Handler for creating a new Project.
 * 
 * @author Kyle Mullins
 */
public class NewProjectHandler {
	
	@Inject
	@Optional
	private ProjectManager projectMan;
	
	/**
	 * Opens the NewProjectWizard then creates a new Project in the
	 * workspace.
	 * 
	 * @param shell	The currently active Shell.
	 */
	@Execute
	public IStatus execute(@Named(IServiceConstants.ACTIVE_SHELL) Shell shell) {
		
		NewProjectWizard projectWizard = new NewProjectWizard(projectMan);
		WizardDialog wizardDialog = new WizardDialog(shell, projectWizard);
		
		int retval = wizardDialog.open();
		
		if(retval == WizardDialog.OK) {
			Project newProj = projectWizard.getProject();
			newProj.setParentDirectory(projectMan.getWorkspace());
			
			try {
				newProj.createProjectFiles();
				projectMan.addProject(newProj);
				
				return Status.OK_STATUS;
			}
			catch(IOException ioe) {
				MessageDialog.openError(shell, "Error", "Could not create "
						+ "Project directory: " + ioe.getMessage());
			}
		}
		
		return Status.CANCEL_STATUS;
	}
}
