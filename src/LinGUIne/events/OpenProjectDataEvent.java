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
	
	public OpenProjectDataEvent(IProjectData data, Project project){
		projectData = data;
		parentProject = project;
	}
	
	public IProjectData getProjectData() {
		return projectData;
	}

	public Project getParentProject() {
		return parentProject;
	}

	public void setParentProject(Project project) {
		parentProject = project;
	}
}
