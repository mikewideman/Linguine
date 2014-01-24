package LinGUIne.wizards;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;

import LinGUIne.extensions.IFileImporter;
import LinGUIne.model.Project;

public class ImportFileData {
	private IFileImporter chosenImporter;
	private Project chosenProject;
	private ArrayList<File> chosenFiles;
	private boolean createNewProject;
	
	public ImportFileData(){
		chosenImporter = null;
		chosenProject = null;
		chosenFiles = new ArrayList<File>();
		createNewProject = false;
	}
	
	public void setImporter(IFileImporter importer){
		chosenImporter = importer;
	}
	
	public IFileImporter getChosenImporter(){
		return chosenImporter;
	}
	
	public void setProject(Project project){
		chosenProject = project;
	}
	
	public Project getChosenProject(){
		return chosenProject;
	}
	
	public boolean addFile(File file){
		if(chosenFiles.contains(file)){
			return false;
		}
		
		return chosenFiles.add(file);
	}
	
	public boolean removeFile(File file){
		return chosenFiles.remove(file);
	}
	
	public Collection<File> getChosenFiles(){
		return chosenFiles;
	}
	
	public String[] getChosenFileNames(){
		String[] fileNames = new String[chosenFiles.size()];
		
		for(int x = 0; x < chosenFiles.size(); x++){
			fileNames[x] = chosenFiles.get(x).getPath();
		}
		
		return fileNames;
	}
	
	public void setCreateNewProject(boolean option){
		createNewProject = option;
	}
	
	public boolean shouldCreateNewProject(){
		return createNewProject;
	}
	
	public boolean isReadyForFiles(){
		return chosenImporter != null && (chosenProject != null ||
				createNewProject);
	}
	
	public boolean isComplete(){
		return chosenImporter != null && !chosenFiles.isEmpty() &&
				chosenProject != null;
	}
}
