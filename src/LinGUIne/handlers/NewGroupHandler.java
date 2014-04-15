package LinGUIne.handlers;

import javax.inject.Inject;
import javax.inject.Named;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Shell;

import LinGUIne.model.Project;
import LinGUIne.model.ProjectGroup;
import LinGUIne.model.ProjectManager;
import LinGUIne.wizards.NewGroupWizard;

public class NewGroupHandler {

	private static final String DEST_PROJECT_PARAM = "linguine.command.newGroup"
			+ ".parameter.destProject";
	
	private static final String PARENT_GROUP_PARAM = "linguine.command.newGroup"
			+ ".parameter.parentGroup";
	
	@Inject
	private ProjectManager projectMan;
	
	@Execute
	public void execute(@Optional @Named(DEST_PROJECT_PARAM) String
			destProject, @Optional @Named(PARENT_GROUP_PARAM) String
			parentGroup, @Named(IServiceConstants.ACTIVE_SHELL) Shell shell){
		
		NewGroupWizard groupWizard = new NewGroupWizard(projectMan);

		Project project = destProject == null ? null :
			projectMan.getProject(destProject);
		
		if(project != null){
			ProjectGroup group = project.getGroup(parentGroup);
			
			groupWizard.addStartingData(project, group);
		}
		
		WizardDialog wizardDialog = new WizardDialog(shell, groupWizard);
		
		int retval = wizardDialog.open();
	}
}
