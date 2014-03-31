package LinGUIne.handlers;

import javax.inject.Inject;
import javax.inject.Named;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Shell;

import LinGUIne.model.Project;
import LinGUIne.model.ProjectGroup;
import LinGUIne.model.ProjectManager;
import LinGUIne.wizards.NewGroupWizard;

public class NewGroupHandler {

	@Inject
	private ProjectManager projectMan;
	
	@Execute
	public void execute(@Named("linguine.command.newGroup.parameter."
			+ "destinationProject") String destinationProject, @Named("linguine"
			+ ".command.newGroup.parameter.parentGroup") String
			parentGroup, @Named(IServiceConstants.ACTIVE_SHELL) Shell shell){
		
		Project project = projectMan.getProject(destinationProject);
		
		if(project != null){
			ProjectGroup group = project.getGroup(parentGroup);
			
			if(parentGroup != null){
				NewGroupWizard groupWizard = new NewGroupWizard(projectMan,
						project, group);
				WizardDialog wizardDialog = new WizardDialog(shell, groupWizard);
				
				int retval = wizardDialog.open();
			}
		}
	}
}
