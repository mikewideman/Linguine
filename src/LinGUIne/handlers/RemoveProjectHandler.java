 
package LinGUIne.handlers;

import java.io.IOException;

import javax.inject.Inject;
import javax.inject.Named;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Shell;

import LinGUIne.model.Project;
import LinGUIne.model.ProjectManager;

/**
 * Removes a Project and all of its contents from the ProjectExplorer after
 * prompting the user for confirmation.
 * 
 * @author Kyle Mullins
 */
public class RemoveProjectHandler {
	
	private static final String TARGET_PROJECT_PARAM = "linguine.command."
			+ "removeProject.parameter.targetProject";
	
	@Inject
	private ProjectManager projectMan;
	
	@Execute
	public void execute(@Named(TARGET_PROJECT_PARAM) String targetProject,
			@Named(IServiceConstants.ACTIVE_SHELL) Shell shell) {
		
		Project project = projectMan.getProject(targetProject);
		
		if(project != null){
			ConfirmWithOptionDialog dialog = new ConfirmWithOptionDialog(shell,
					"Remove Project",
					"Are you sure you want to remove the Project?",
					"Delete Project contents on disk (cannot be undone).");
			
			boolean confirmed = dialog.open() == Window.OK;
			
			if(confirmed){
				try {
					projectMan.removeProject(project, dialog.wasOptionChosen());
				}
				catch(IOException e){
					MessageDialog.openError(shell, "Error",
							"Could not delete Project contents on disk.");
				}
			}
		}
	}
}