package LinGUIne.wizards;

import LinGUIne.model.Project;
import LinGUIne.model.ProjectGroup;

/**
 * Data object used to carry data through the NewFileWizard.
 * 
 * @author Kyle Mullins
 */
public class NewFileData {
	
	private Project chosenProject;
	private ProjectGroup parentGroup;
	private String newFileName;
	
	/**
	 * Sets the chosen Project.
	 */
	public void setChosenProject(Project chosenProj){
		chosenProject = chosenProj;
	}
	
	/**
	 * Returns the chosen Project.
	 */
	public Project getChosenProject(){
		return chosenProject;
	}
	
	/**
	 * Sets the chosen parent ProjectGroup.
	 */
	public void setParentGroup(ProjectGroup chosenGroup){
		parentGroup = chosenGroup;
	}
	
	/**
	 * Returns the chosen parent ProjectGroup.
	 */
	public ProjectGroup getParentGroup(){
		return parentGroup;
	}
	
	/**
	 * Sets the name of the new File.
	 */
	public void setNewFileName(String fileName){
		newFileName = fileName;
	}
	
	/**
	 * Returns the name of the new File.
	 */
	public String getNewFileName(){
		return newFileName;
	}
	
	/**
	 * Returns whether or not this data object is completely filled.
	 */
	public boolean isComplete(){
		return chosenProject != null && parentGroup != null &&
				newFileName != null;
	}
}
