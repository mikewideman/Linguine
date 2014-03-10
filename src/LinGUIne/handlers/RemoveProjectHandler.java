 
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

public class RemoveProjectHandler {
	
	@Inject
	private ProjectManager projectMan;
	
	@Execute
	public void execute(@Named("linguine.command.removeProject.parameter."
			+ "projectForRemoval") String projectForRemoval,
			@Named(IServiceConstants.ACTIVE_SHELL) Shell shell) {
		
		Project project = projectMan.getProject(projectForRemoval);
		
		if(project != null){
			//TODO: Also ask user if they want to delete Project contents on disk
//			boolean confirmed = MessageDialog.openConfirm(shell, "Remove Project",
//					"Are you sure you want to remove the Project?");
			
			ConfirmWithOptionDialog dialog = new ConfirmWithOptionDialog(shell,
					"Remove Project",
					"Are you sure you want to remove the Project?",
					"Delete Project contents on disk (cannot be undone).");
			
			boolean confirmed = dialog.open() == Window.OK;
			System.out.println(dialog.wasOptionChosen());
			
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