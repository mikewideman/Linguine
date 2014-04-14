 
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
import LinGUIne.model.ProjectManager;
import LinGUIne.model.Result;

public class RemoveProjectDataHandler {
	
	private static final String TARGET_DATA_PARAM = "linguine.command."
			+ "removeProjectData.parameter.targetProjectData";
	
	private static final String PARENT_PROJECT_PARAM = "linguine.command."
			+ "removeProjectData.parameter.parentProject";
	
	@Inject
	private ProjectManager projectMan;
	
	@Execute
	public void execute(@Named(TARGET_DATA_PARAM) String targetProjectData,
			@Named(PARENT_PROJECT_PARAM) String parentProject,
			@Named(IServiceConstants.ACTIVE_SHELL) Shell shell) {
		
		Project project = projectMan.getProject(parentProject);
		
		if(project != null){
			IProjectData projData = project.getProjectData(targetProjectData);

			if(projData != null){
				ConfirmWithOptionDialog dialog = new ConfirmWithOptionDialog(shell,
						"Remove File",
						"Are you sure you want to remove the File?\n" +
						"This may invalidate Results generated using this File.",
						"Delete File contents on disk (cannot be undone).");
				
				boolean confirmed = dialog.open() == Window.OK;
				
				if(confirmed){
					try {
						if(projData instanceof Result){
							project.removeResult((Result)projData);
						}
						else{
							project.removeProjectData(projData);
						}
						
						if(dialog.wasOptionChosen()){
							projData.deleteContentsOnDisk();
						}
					}
					catch(IOException e){
						MessageDialog.openError(shell, "Error",
								"Could not delete File contents on disk.");
					}
				}
			}
		}
	}
}