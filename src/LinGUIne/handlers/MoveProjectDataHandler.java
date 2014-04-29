package LinGUIne.handlers;

import javax.inject.Inject;
import javax.inject.Named;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.swt.widgets.Shell;

import LinGUIne.model.IProjectData;
import LinGUIne.model.Project;
import LinGUIne.model.ProjectManager;

public class MoveProjectDataHandler {

	private static final String TARGET_DATA_PARAM = "linguine.command."
			+ "moveProjectData.parameter.targetData";
	
	private static final String PARENT_PROJECT_PARAM = "linguine.command."
			+ "moveProjectData.parameter.parentProject";
	
	@Inject
	private ProjectManager projectMan;
	
	@Execute
	public void execute(@Named(TARGET_DATA_PARAM) String targetData,
			@Named(PARENT_PROJECT_PARAM) String parentProject,
			@Named(IServiceConstants.ACTIVE_SHELL) Shell shell) {
		
//		MoveGroupWizard moveWizard = new MoveGroupWizard(projectMan);
		
		Project project = projectMan.getProject(parentProject);
		
		if(project != null){
			IProjectData projData = project.getProjectData(targetData);

//			moveWizard.addStartingData(project, projData);
		}
		
//		WizardDialog wizardDialog = new WizardDialog(shell, moveWizard);
		
//		int retval = wizardDialog.open();
	}
}
