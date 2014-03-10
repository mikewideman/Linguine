package LinGUIne.events;

import LinGUIne.model.Project;
import LinGUIne.model.ProjectManager;

public class ProjectEvent {
	
	private ProjectManager projectManager;
	private Project affectedProject;
	
	public ProjectEvent(ProjectManager projectMan, Project project){
		projectManager = projectMan;
		affectedProject = project;
	}

	public ProjectManager getProjectManager() {
		return projectManager;
	}

	public Project getAffectedProject() {
		return affectedProject;
	}
}
