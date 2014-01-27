package LinGUIne.model;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.TreeMap;

import javax.annotation.PreDestroy;
import javax.inject.Inject;

import org.eclipse.core.runtime.IPath;
import org.eclipse.e4.core.services.events.IEventBroker;

import LinGUIne.model.Project.ProjectListener;
import LinGUIne.utilities.FileUtils;

/**
 * Encapsulates all Projects within some workspace and controls access to them.
 * 
 * @author Kyle Mullins
 */
public class ProjectManager {

	/**
	 * Event strings used to subscribe to project-related events.
	 */
	public static final String ALL_PROJECT_EVENTS = "Project/*";
	public static final String PROJECT_ADDED = "Project/Added";
	public static final String PROJECT_REMOVED = "Project/Removed";
	public static final String PROJECT_MODIFIED = "Project/Modified";
	
	@Inject
	private IEventBroker eventBroker;
	
	private TreeMap<String, Project> projectSet;
	private IPath workspace;
	
	private ProjectListener projListener;
	
	/**
	 * Creates a new ProjectManager for the given workspace.
	 */
	public ProjectManager(IPath workspacePath){
		workspace = workspacePath;
		projectSet = new TreeMap<String, Project>();
		
		projListener = new ProjectListener() {
			
			@Override
			public void notify(Project modifiedProj) {
				postEvent(PROJECT_MODIFIED);
			}
		};
	}
	
	/**
	 * Called just before the ProjectManager is destroyed to remove Project
	 * listeners and force updates of all Project files.
	 */
	@PreDestroy
	public void preDestroy(){
		for(Project proj: projectSet.values()){
			proj.removeListener(projListener);
			
			try{
				proj.updateProjectFile();
			}
			catch(IOException ioe){
				ioe.printStackTrace();
			}
		}
	}
	
	/**
	 * Returns the path to the workspace for this ProjectManager.
	 */
	public IPath getWorkspace(){
		return workspace;
	}
	
	/**
	 * Returns whether or not a Project of the given name exists within this
	 * ProjectManager.
	 * Note: Project names are case insensitive.
	 */
	public boolean containsProject(String projectName){
		return projectSet.containsKey(projectName.toLowerCase());
	}
	
	/**
	 * Adds the given Project to the ProjectManager if it is complete.
	 * Note: Projects added are assumed to have been created within the file
	 * system first.
	 * 
	 * @param newProject	The Project to be added.
	 * 
	 * @return	True iff the Project was added successfully, false otherwise.
	 */
	public boolean addProject(Project newProject){
		if(newProject.getName() == null){
			return false;
		}
		else if(!containsProject(newProject.getName())){
			projectSet.put(newProject.getName().toLowerCase(), newProject);
			newProject.addListener(projListener);
			postEvent(PROJECT_ADDED);
			
			return true;
		}
		
		return false;
	}
	
	/**
	 * Returns the Project with the given name if it exists within this
	 * ProjectManager, null otherwise.
	 */
	public Project getProject(String projectName){
		if(containsProject(projectName)){
			return projectSet.get(projectName.toLowerCase());
		}
		
		return null;
	}
	
	/**
	 * Returns a collection of all the Projects managed by this instance.
	 */
	public Collection<Project> getProjects(){
		return projectSet.values();
	}
	
	/**
	 * Loads all of the Projects located in this ProjectManager's workspace
	 * and adds them.
	 */
	public void loadProjects(){
		for(File dir: workspace.toFile().listFiles()){
			if(dir.isDirectory()){
				for(String filename: dir.list()){
					if(filename.equals(Project.PROJECT_FILE)){
						Project project = Project.createFromFile(
								FileUtils.appendPath(dir, filename));
						
						project.setParentDirectory(workspace);
						addProject(project);
					}
				}
			}
		}
	}
	
	private void postEvent(String eventStr) {
		if(eventBroker != null){
			eventBroker.post(eventStr, this);
		}
	}
}
