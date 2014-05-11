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
 * Launches the MoveGroupWizard to move a folder and all of its contents in the
 * Project Explorer.
 * TODO: MoveGroupWizard
 * 
 * @author Kyle Mullins
 */
public class MoveGroupHandler {

	private static final String TARGET_GROUP_PARAM = "linguine.command."
			+ "moveGroup.parameter.targetGroup";
	
	private static final String PARENT_PROJECT_PARAM = "linguine.command."
			+ "moveGroup.parameter.parentProject";
	
	@Inject
	private ProjectManager projectMan;
	
	@Execute
	public void execute(@Named(TARGET_GROUP_PARAM) String targetGroup,
			@Named(PARENT_PROJECT_PARAM) String parentProject,
			@Named(IServiceConstants.ACTIVE_SHELL) Shell shell) {
		
//		MoveGroupWizard moveWizard = new MoveGroupWizard(projectMan);
		
		Project project = projectMan.getProject(parentProject);
		
		if(project != null){
			ProjectGroup group = project.getGroup(targetGroup);

//			moveWizard.addStartingData(project, group);
		}
		
//		WizardDialog wizardDialog = new WizardDialog(shell, moveWizard);
		
//		int retval = wizardDialog.open();
	}
}
