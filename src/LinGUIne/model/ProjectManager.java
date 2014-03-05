package LinGUIne.model;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeMap;

import javax.annotation.PreDestroy;
import javax.inject.Inject;

import org.eclipse.core.runtime.IPath;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.e4.ui.model.application.MApplication;

import LinGUIne.events.LinGUIneEvents;
import LinGUIne.events.ProjectEvent;
import LinGUIne.model.Project.ProjectListener;
import LinGUIne.utilities.FileUtils;

/**
 * Encapsulates all Projects within some workspace and controls access to them.
 * 
 * @author Kyle Mullins
 */
public class ProjectManager {

	private static final String PROJECT_LIST_KEY = "ProjectManager_ProjectList";
	
	@Inject
	private IEventBroker eventBroker;
	
	private MApplication application;
	private TreeMap<String, Project> projectSet;
	private IPath workspace;
	
	private ProjectListener projListener;
	
	/**
	 * Creates a new ProjectManager for the given workspace.
	 */
	public ProjectManager(IPath workspacePath, MApplication app){
		workspace = workspacePath;
		application = app;
		projectSet = new TreeMap<String, Project>();
		
		projListener = new ProjectListener() {
			
			@Override
			public void notify(Project modifiedProj) {
				postEvent(LinGUIneEvents.Project.MODIFIED, modifiedProj);
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
			postEvent(LinGUIneEvents.Project.ADDED, newProject);
			updateProjectFilesInPersistedState();
			
			return true;
		}
		
		return false;
	}
	
	/**
	 * Removes the given Project from this ProjectManager if it exists.
	 * 
	 * @param proj	The Project to be removed.
	 * 
	 * @return	True iff the Project was removed, false if it doesn't exist.
	 */
	public boolean removeProject(Project proj){
		if(containsProject(proj.getName())){
			projectSet.remove(proj.getName().toLowerCase());
			proj.removeListener(projListener);
			postEvent(LinGUIneEvents.Project.REMOVED, proj);
			updateProjectFilesInPersistedState();
			
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
		List<File> projectFiles = getProjectFilesFromPersistedState();
		
		for(File projectFile: projectFiles){
			Project newProject = Project.createFromFile(projectFile);
			newProject.setParentDirectory(workspace);
			addProject(newProject);
		}
		
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
	
	/**
	 * Gets and parses the list of Project Files from persisted state and
	 * returns it.
	 */
	private List<File> getProjectFilesFromPersistedState(){
		//TODO: Fix loading of persisted state
		
		LinkedList<File> projectFiles = new LinkedList<File>();
		String projectList = application.getPersistedState().get(
				PROJECT_LIST_KEY);
		
		if(projectList != null){
			for(String projectFilePath: projectList.split(";")){
				if(!projectFilePath.isEmpty()){
					File projectFile = new File(projectFilePath);
					
					projectFiles.add(projectFile);
				}
			}
		}
		
		return projectFiles;
	}
	
	/**
	 * Updates the list of Project Files in persisted state.
	 */
	private void updateProjectFilesInPersistedState(){
		String projectList = "";
		
		for(Project project: projectSet.values()){
			projectList += project.getProjectFile().toString() + ";";
		}
		
		application.getPersistedState().put(PROJECT_LIST_KEY, projectList);
	}
	
	private void postEvent(String eventStr, Project affectedProject) {
		if(eventBroker != null){
			eventBroker.post(eventStr, new ProjectEvent(this, affectedProject));
		}
	}
}
