 
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
import LinGUIne.model.ProjectGroup;
import LinGUIne.model.ProjectManager;

public class RenameGroupHandler {
	
	private static final String TARGET_GROUP_PARAM = "linguine.command."
			+ "renameGroup.parameter.targetGroup";
	
	private static final String PARENT_PROJECT_PARAM = "linguine.command."
			+ "renameGroup.parameter.parentProject";
	
	@Inject
	private ProjectManager projectMan;
	
	@Execute
	public void execute(@Named(TARGET_GROUP_PARAM) String targetGroup,
			@Named(PARENT_PROJECT_PARAM) String parentProject,
			@Named(IServiceConstants.ACTIVE_SHELL) Shell shell) {
		
		Project project = projectMan.getProject(parentProject);
		ProjectGroup group = project.getGroup(targetGroup);
		
		if(project != null){
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