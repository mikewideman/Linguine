 
package LinGUIne.handlers;

import javax.inject.Inject;
import javax.inject.Named;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.swt.widgets.Shell;

import LinGUIne.model.Project;
import LinGUIne.model.ProjectManager;

/**
 * Launches the RenameProjectWizard to rename some Project in the
 * ProjectExplorer.
 * TODO: RenameProjectWizard
 * 
 * @author Kyle Mullins
 */
public class RenameProjectHandler {
	
	private static final String TARGET_PROJECT_PARAM = "linguine.command."
			+ "renameProject.parameter.targetProject";
	
	@Inject
	private ProjectManager projectMan;
	
	@Execute
	public void execute(@Named(TARGET_PROJECT_PARAM) String targetProject,
			@Named(IServiceConstants.ACTIVE_SHELL) Shell shell) {
		
		Project project = projectMan.getProject(targetProject);
	}
}