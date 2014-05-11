 
package LinGUIne.handlers;

import javax.inject.Inject;
import javax.inject.Named;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.swt.widgets.Shell;

import LinGUIne.model.Project;
import LinGUIne.model.ProjectGroup;
import LinGUIne.model.ProjectManager;

/**
 * Launches the RenameGroupWizard to rename some Group (folder) from some
 * Project in the ProjectExplorer.
 * TODO: RenameGroupWizard
 * 
 * @author Kyle Mullins
 */
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
	}
}