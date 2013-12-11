package LinGUIne.model;

import java.io.File;
import java.util.Collection;
import java.util.TreeMap;

import org.eclipse.core.runtime.IPath;

public class ProjectManager {

	private TreeMap<String, Project> projectSet;
	private IPath workspace;
	
	public ProjectManager(IPath workspacePath){
		workspace = workspacePath;
		projectSet = new TreeMap<String, Project>();
	}
	
	public IPath getWorkspace(){
		return workspace;
	}
	
	public boolean containsProject(String projectName){
		return projectSet.containsKey(projectName.toLowerCase());
	}
	
	public void addProject(Project newProject){
		projectSet.put(newProject.getName().toLowerCase(), newProject);
	}
	
	public Project getProject(String projectName){
		if(containsProject(projectName)){
			return projectSet.get(projectName.toLowerCase());
		}
		
		return null;
	}
	
	public Collection<Project> getProjects(){
		return projectSet.values();
	}
	
	public void loadProjects(){
		for(File dir: workspace.toFile().listFiles()){
			if(dir.isDirectory()){
				for(String filename: dir.list()){
					if(filename.equals(Project.PROJECT_FILE)){
						String projectName = dir.getName(); //TODO: change this to read from project file
						Project project = new Project();
						
						project.setName(projectName);
						project.setParentDirectory(workspace);
						
						projectSet.put(projectName, project);
					}
				}
			}
		}
	}
}
