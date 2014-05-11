 
package LinGUIne.handlers;

import javax.inject.Inject;
import javax.inject.Named;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.swt.widgets.Shell;

import LinGUIne.model.IProjectData;
import LinGUIne.model.Project;
import LinGUIne.model.ProjectManager;

/**
 * Launches the RenameProjectDataWizard to rename a file in some Project in the
 * ProjectExplorer.
 * TODO: RenameProjectDataWizard
 * 
 * @author Kyle Mullins
 */
public class RenameProjectDataHandler {
	
	private static final String TARGET_DATA_PARAM = "linguine.command."
			+ "renameProjectData.parameter.targetProjectData";
	
	private static final String PARENT_PROJECT_PARAM = "linguine.command."
			+ "renameProjectData.parameter.parentProject";
	
	@Inject
	private ProjectManager projectMan;
	
	@Execute
	public void execute(@Named(TARGET_DATA_PARAM) String targetProjectData,
			@Named(PARENT_PROJECT_PARAM) String parentProject,
			@Named(IServiceConstants.ACTIVE_SHELL) Shell shell) {
		
		Project project = projectMan.getProject(parentProject);
		IProjectData projData = project.getProjectData(targetProjectData);
	}
}