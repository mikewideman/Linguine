package LinGUIne.wizards;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;

import LinGUIne.extensions.IFileImporter;
import LinGUIne.model.Project;

/**
 * Data object used to carry data between pages in the ImportFileWizard.
 * 
 * @author Kyle Mullins
 */
public class ImportFileData {
	private IFileImporter chosenImporter;
	private Project chosenProject;
	private ArrayList<File> chosenFiles;
	private boolean createNewProject;
	private boolean internetSource;
	private String internetSourceFileName;//Used when importing non-local files into project
	private String internetSourceQuery; //Used when importing non-local files into project
	
	/**
	 * Creates a default empty instance.
	 */
	public ImportFileData(){
		chosenImporter = null;
		chosenProject = null;
		chosenFiles = new ArrayList<File>();
		createNewProject = false;
		internetSource = false;
	}
	
	/**
	 * Sets the IFileImporter that was chosen.
	 * 
	 * @param importer	An instance of the chosen IFileImporter.
	 */
	public void setImporter(IFileImporter importer){
		chosenImporter = importer;
	}
	
	/**
	 * Returns the chosen IFileImporter instance.
	 */
	public IFileImporter getChosenImporter(){
		return chosenImporter;
	}
	
	/**
	 * Sets the Project instance that was chosen.
	 * 
	 * @param project	The chosen Project instance.
	 */
	public void setProject(Project project){
		chosenProject = project;
	}
	
	/**
	 * Returns the chosen Project.
	 */
	public Project getChosenProject(){
		return chosenProject;
	}

	/**
	 * Adds the given File to the Collection of Files to be imported.
	 * 
	 * @param file	The File to be imported.
	 * 
	 * @return	True iff the File was added to the collection, false otherwise.
	 */
	public boolean addFile(File file){
		if(chosenFiles.contains(file)){
			return false;
		}
		
		return chosenFiles.add(file);
	}
	
	/**
	 * Removes the given File from the Collection of Files to be imported.
	 * 
	 * @param file	The File which should no longer be imported.
	 * 
	 * @return	True iff the File was removed from the collection, false
	 * 			otherwise.
	 */
	public boolean removeFile(File file){
		return chosenFiles.remove(file);
	}
	
	/**
	 * Returns the Collection of the Files to be imported.
	 */
	public Collection<File> getChosenFiles(){
		return chosenFiles;
	}
	
	/**
	 * Returns an array of the paths to each File in the Collection of Files to
	 * be imported as a String.
	 */
	public String[] getChosenFileNames(){
		String[] fileNames = new String[chosenFiles.size()];
		
		for(int x = 0; x < chosenFiles.size(); x++){
			fileNames[x] = chosenFiles.get(x).getPath();
		}
		
		return fileNames;
	}
	
	/**
	 * Marks whether a new Project is to be created for the Files to be
	 * imported into.
	 * 
	 * @param option	True if a new Project is to be created, false otherwise.
	 */
	public void setCreateNewProject(boolean option){
		createNewProject = option;
	}
	
	/**
	 * Returns whether or not a new Project is to be created.
	 */
	public boolean shouldCreateNewProject(){
		return createNewProject;
	}
	
	/**
	 * Returns whether or not there is enough data in this instance to select
	 * which Files to import.
	 * 
	 * @return	True iff an IFileImporter has been chosen and either a new
	 * 			Project is to be created or an existing Project has been
	 * 			chosen, false otherwise.
	 */
	public boolean isReadyForFiles(){
		return chosenImporter != null && (chosenProject != null ||
				createNewProject);
	}
	
	/**
	 * Returns whether or not there is enough data in this instance to actually
	 * import Files.
	 * 
	 * @return	True iff an IFileImporter has been chosen, at least one File
	 * 			has been chosen, and a Project has been chosen.
	 */
	public boolean isComplete(){
//		return chosenImporter != null && !chosenFiles.isEmpty() &&
//				chosenProject != null;
		return isReadyForFiles() && !chosenFiles.isEmpty();
	}
	
	/**
	 * Determines whether or not we are looking to import a concrete file on the disk or must pull 
	 * the data from a non-local source
	 * @return True if the file is not local, false otherwise.
	 */
	public boolean isInternetSource(){
		return internetSource;
	}
	
	public void setInternetSource(boolean value){
		internetSource = value;
	}
	
	public void setInternetSourceFileName(String name){
		internetSourceFileName = name;
	}
	
	public String getInternetSourceFileName(){
		return internetSourceFileName;
	}
	
	public void setInternetSourceQuery(String query){
		internetSourceQuery = query;
	}
	
	public String getInterneSourceQuery(){
		return internetSourceQuery;
	}
}
