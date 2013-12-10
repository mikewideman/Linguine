package LinGUIne.handlers;

import java.io.IOException;
import java.util.TreeMap;

import javax.inject.Named;

import org.eclipse.core.runtime.Platform;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Shell;

import LinGUIne.model.Project;
import LinGUIne.wizards.NewProjectWizard;

/**
 * Handler for creating a new Project.
 * 
 * @author Kyle Mullins
 */
public class NewProjectHandler {
	
	/**
	 * Opens the New Project Wizard then creates a new Project in the
	 * workspace.
	 * 
	 * @param shell	The currently active Shell.
	 */
	@Execute
	public void execute(@Named(IServiceConstants.ACTIVE_SHELL) Shell shell,
			@Named("ProjectSet") TreeMap<String, Project> projectSet) {
		
		NewProjectWizard projectWizard = new NewProjectWizard(projectSet);
		WizardDialog wizardDialog = new WizardDialog(shell, projectWizard);
		
		int retval = wizardDialog.open();
		
		if(retval == WizardDialog.OK) {
			Project newProj = projectWizard.getProject();
			newProj.setParentDirectory(Platform.getLocation());
			
			try {
				newProj.createProjectFiles();
				projectSet.put(newProj.getName().toLowerCase(), newProj);
			}
			catch(IOException ioe) {
				MessageDialog.openError(shell, "Error", "Could not create "
						+ "Project directory: " + ioe.getMessage());
			}
		}
	}
}
