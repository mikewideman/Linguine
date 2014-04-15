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
	
	public void setChosenProject(Project chosenProj){
		chosenProject = chosenProj;
	}
	
	public Project getChosenProject(){
		return chosenProject;
	}
	
	public void setParentGroup(ProjectGroup chosenGroup){
		parentGroup = chosenGroup;
	}
	
	public ProjectGroup getParentGroup(){
		return parentGroup;
	}
	
	public void setNewFileName(String fileName){
		newFileName = fileName;
	}
	
	public String getNewFileName(){
		return newFileName;
	}
	
	public boolean isComplete(){
		return chosenProject != null && parentGroup != null &&
				newFileName != null;
	}
}
