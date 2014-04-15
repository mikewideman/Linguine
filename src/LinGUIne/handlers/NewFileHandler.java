 
package LinGUIne.handlers;

import javax.inject.Inject;
import javax.inject.Named;

import org.eclipse.e4.core.contexts.ContextInjectionFactory;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Shell;

import LinGUIne.model.Project;
import LinGUIne.model.ProjectGroup;
import LinGUIne.model.ProjectManager;
import LinGUIne.wizards.NewFileWizard;

public class NewFileHandler {
	
	private static final String DEST_PROJECT_PARAM = "linguine.command.newFile"
			+ ".parameter.destProject";
	
	private static final String PARENT_GROUP_PARAM = "linguine.command.newFile"
			+ ".parameter.parentGroup";
	
	@Inject
	private MApplication application;
	
	@Inject
	private ProjectManager projectMan;
	
	@Execute
	public void execute(@Optional @Named(DEST_PROJECT_PARAM) String
			destProject, @Optional @Named(PARENT_GROUP_PARAM) String
			parentGroup, @Named(IServiceConstants.ACTIVE_SHELL) Shell shell) {
		
		NewFileWizard newFileWizard = new NewFileWizard();
		
		Project project = destProject == null ? null :
			projectMan.getProject(destProject);
		
		if(project != null){
			ProjectGroup group = project.getGroup(parentGroup);
			
			newFileWizard.addStartingData(project, group);
		}
		
		WizardDialog wizardDialog = new WizardDialog(shell, newFileWizard);
		
		ContextInjectionFactory.inject(newFileWizard, application.getContext());

		int retval = wizardDialog.open();
	}
}