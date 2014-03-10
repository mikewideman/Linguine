package LinGUIne.wizards;

import LinGUIne.model.Project;

/**
 * Data object used to carry data through the NewFileWizard.
 * 
 * @author Kyle Mullins
 */
public class NewFileData {
	
	private Project chosenProject;
	private String newFileName;
	
	public void setChosenProject(Project chosenProj){
		chosenProject = chosenProj;
	}
	
	public Project getChosenProject(){
		return chosenProject;
	}
	
	public void setNewFileName(String fileName){
		newFileName = fileName;
	}
	
	public String getNewFileName(){
		return newFileName;
	}
	
	public boolean isComplete(){
		return chosenProject != null && newFileName != null &&
				!newFileName.isEmpty();
	}
}
