package LinGUIne.parts.advanced;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

import LinGUIne.model.IProjectData;
import LinGUIne.model.Project;
import LinGUIne.model.Result;

/**
 * Container for the currently selected elements in the ProjectExplorer.
 * 
 * @author Kyle Mullins
 */
public class ProjectExplorerSelection {

	private List<String> selectedProjects;
	private HashMap<Integer, HashSet<String>> selectedOriginalData;
	private HashMap<Integer, HashSet<String>> selectedResults;
	
	/**
	 * Creates a new empty ProjectExplorerSelection.
	 */
	public ProjectExplorerSelection(){
		selectedProjects = new LinkedList<String>();
		selectedOriginalData = new HashMap<Integer, HashSet<String>>();
		selectedResults = new HashMap<Integer, HashSet<String>>();
	}
	
	/**
	 * Returns whether or not the ProjectExplorerSelection is empty.
	 */
	public boolean isEmpty(){
		return selectedProjects.isEmpty();
	}
	
	/**
	 * Returns a list of the names of Projects with current selections.
	 */
	public Collection<String> getSelectedProjects(){
		return selectedProjects;
	}
	
	/**
	 * Returns a collection of names of selected non-Result ProjectData which
	 * reside in the given Project.
	 * 
	 * @param projectName	The name of the Project for which selected original
	 * 						ProjectData should be returned.
	 * 
	 * @return	Collection of ProjectData names or an empty list if there are
	 * 			no data selected for the given Project name.
	 */
	public Collection<String> getSelectedOriginalData(String projectName){
		int projectId = selectedProjects.indexOf(projectName);
		
		if(projectId >= 0){
			return selectedOriginalData.get(projectId);
		}
	
		return new LinkedList<String>();
	}
	
	/**
	 * Returns a collection of names of selected Results which reside in the
	 * given Project.
	 * 
	 * @param projectName	The name of the Project for which the selected
	 * 						Results should be returned.
	 * 
	 * @return	Collection of Result names or an empty list if there are no data
	 * 			selected for the given Project name.
	 */
	public Collection<String> getSelectedResults(String projectName){
		int projectId = selectedProjects.indexOf(projectName);
		
		if(projectId >= 0){
			return selectedResults.get(projectId);
		}
	
		return new LinkedList<String>();
	}
	
	/**
	 * Adds all ProjectData from the given Project to this
	 * ProjectExplorerSelection.
	 */
	public void addToSelection(Project selectedProject){
		List<IProjectData> selectedProjectData = new LinkedList<IProjectData>();
		
		selectedProjectData.addAll(selectedProject.getOriginalData());
		selectedProjectData.addAll(selectedProject.getResults());
		
		addToSelection(selectedProject, selectedProjectData);
	}
	
	/**
	 * Adds the given ProjectData to this ProjectExplorerSelection.
	 * Note: duplicates will not be added.
	 * 
	 * @param parentProject			The Project to which all of the given
	 * 								ProjectData belongs.
	 * @param selectedProjectData	The ProjectData that is selected and resides
	 * 								within the given Project.
	 */
	public void addToSelection(Project parentProject,
			List<IProjectData> selectedProjectData){
		int projectId;
		
		if(!selectedProjects.contains(parentProject.getName())){
			projectId = selectedProjects.size();
			
			selectedProjects.add(parentProject.getName());
			selectedOriginalData.put(projectId, new HashSet<String>());
			selectedResults.put(projectId, new HashSet<String>());
		}
		else{
			projectId = selectedProjects.indexOf(parentProject.getName());
		}
		
		HashSet<String> originalDataSet = selectedOriginalData.get(projectId);
		HashSet<String> resultSet = selectedResults.get(projectId);
		
		for(IProjectData data: selectedProjectData){
			if(data instanceof Result){
				resultSet.add(data.getName());
			}
			else{
				originalDataSet.add(data.getName());
			}
		}
	}
	
	/**
	 * Clears the current ProjectExplorerSelection such that it is now empty.
	 * Note: this does not affect what is currently selected in the
	 * ProjectExplorer.
	 */
	public void clearSelection(){
		selectedProjects.clear();
		selectedOriginalData.clear();
		selectedResults.clear();
	}
}
