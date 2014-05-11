package LinGUIne.events;

import LinGUIne.model.Project;
import LinGUIne.model.ProjectManager;

/**
 * Container for data related to Project modified events.
 * 
 * @author Kyle Mullins
 */
public class ProjectEvent {
	
	private ProjectManager projectManager;
	private Project affectedProject;
	
	/**
	 * Creates a new event that the given Project was modified.
	 * 
	 * @param projectMan	The ProjectManager containing the modified Project.
	 * @param project		The modified Project.
	 */
	public ProjectEvent(ProjectManager projectMan, Project project){
		projectManager = projectMan;
		affectedProject = project;
	}

	/**
	 * Returns the ProjectManager.
	 */
	public ProjectManager getProjectManager() {
		return projectManager;
	}

	/**
	 * Returns the affected Project.
	 */
	public Project getAffectedProject() {
		return affectedProject;
	}
}
