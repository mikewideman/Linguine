 
package LinGUIne.handlers;

import java.io.IOException;

import javax.inject.Inject;
import javax.inject.Named;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Shell;

import LinGUIne.model.IProjectData;
import LinGUIne.model.Project;
import LinGUIne.model.ProjectGroup;
import LinGUIne.model.ProjectManager;

public class RemoveGroupHandler {
	
	private static final String TARGET_GROUP_PARAM = "linguine.command."
			+ "removeGroup.parameter.targetGroup";
	
	private static final String PARENT_PROJECT_PARAM = "linguine.command."
			+ "removeGroup.parameter.parentProject";
	
	@Inject
	private ProjectManager projectMan;
	
	@Execute
	public void execute(@Named(TARGET_GROUP_PARAM) String targetGroup,
			@Named(PARENT_PROJECT_PARAM) String parentProject,
			@Named(IServiceConstants.ACTIVE_SHELL) Shell shell) {
		
		Project project = projectMan.getProject(parentProject);
		
		if(project != null){
			ProjectGroup group = project.getGroup(targetGroup);

			if(group != null){
				ConfirmWithOptionDialog dialog = new ConfirmWithOptionDialog(shell,
						"Remove Group",
						"Are you sure you want to remove the Group and all of "
						+ "its contents?",
						"Delete Group contents on disk (cannot be undone).");
				
				boolean confirmed = dialog.open() == Window.OK;
				
				if(confirmed){
					try {
						removeGroupAndChildren(group, project,
								dialog.wasOptionChosen());
					}
					catch(IOException e){
						MessageDialog.openError(shell, "Error",
								"Could not delete Group contents on disk.");
					}
				}
			}
		}
	}
	
	private void removeGroupAndChildren(ProjectGroup group, Project parentProj,
			boolean shouldDelete) throws IOException{
		
		//Remove child groups recursively
		for(ProjectGroup childGroup: group.getChildren()){
			removeGroupAndChildren(childGroup, parentProj, shouldDelete);
		}
		
		//Remove child data
		for(IProjectData data: parentProj.getDataInGroup(group)){
			parentProj.removeProjectData(data);
			
			if(shouldDelete){
				data.deleteContentsOnDisk();
			}
		}
		
		if(shouldDelete){
			group.deleteGroupDirectory(parentProj.getProjectDirectory());
		}

		parentProj.removeGroup(group);
	}
}