package LinGUIne.events;

import LinGUIne.model.IProjectData;
import LinGUIne.model.Project;

/**
 * Container for data related to ProjectData open events.
 * 
 * @author Kyle Mullins
 */
public class OpenProjectDataEvent {
	
	private IProjectData projectData;
	private Project parentProject;
	
	/**
	 * Creates a new event to open the given Project Data from the given
	 * Project.
	 * 
	 * @param data		The Project Data to be opened.
	 * @param project	The Project to which the Project Data belongs.
	 */
	public OpenProjectDataEvent(IProjectData data, Project project){
		projectData = data;
		parentProject = project;
	}
	
	/**
	 * Returns the Project Data to be opened.
	 */
	public IProjectData getProjectData() {
		return projectData;
	}

	/**
	 * Returns the parent Project.
	 */
	public Project getParentProject() {
		return parentProject;
	}

	/**
	 * Sets the parent Project to the given one.
	 * 
	 * @param project	The Project to which the Project Data belongs.
	 */
	public void setParentProject(Project project) {
		parentProject = project;
	}
}
