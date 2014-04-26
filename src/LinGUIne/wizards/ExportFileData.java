package LinGUIne.wizards;

import java.io.File;

import LinGUIne.extensions.IFileExporter;
import LinGUIne.model.Project;
import LinGUIne.model.Result;

/**
 * Data object used to carry data between pages in the ExportFileWizard.
 * 
 * @author Kyle Mullins
 */
public class ExportFileData {
	private IFileExporter chosenExporter;
	private Project chosenProject;
	private Result chosenResult;
	private File chosenDestFile;
	
	/**
	 * Creates a default empty instance.
	 */
	public ExportFileData(){
		chosenExporter = null;
		chosenProject = null;
		chosenResult = null;
		chosenDestFile = null;
	}
	
	/**
	 * Sets the IFileExporter that was chosen.
	 * 
	 * @param exporter	An instance of the chosen IFileExporter.
	 */
	public void setExporter(IFileExporter exporter){
		chosenExporter = exporter;
	}
	
	/**
	 * Returns the chosen IFileExporter instance.
	 */
	public IFileExporter getChosenExporter(){
		return chosenExporter;
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
	 * Sets the Result chosen to be exported.
	 * 
	 * @param result	the chosen Result.
	 */
	public void setResult(Result result){
		chosenResult = result;
	}
	
	/**
	 * Returns the Result chosen to be exported.
	 */
	public Result getChosenResult(){
		return chosenResult;
	}

	/**
	 * Sets the chosen destination File to which the Result is to be exported.
	 * 
	 * @param file	The File to which the Result should be exported.
	 */
	public void setDestFile(File file){
		chosenDestFile = file;
	}
	
	/**
	 * Returns the File to which the Result should be exported.
	 */
	public File getDestFile(){
		return chosenDestFile;
	}
	
	/**
	 * Returns whether or not there is enough data in this instance to select
	 * which Result to export.
	 * 
	 * @return	True iff an IFileExporter has been chosen and a destination File
	 * 			has been chosen.
	 */
	public boolean isReadyForResult(){
		return chosenExporter != null && chosenDestFile != null;
	}
	
	/**
	 * Returns whether or not there is enough data in this instance to actually
	 * export Files.
	 * 
	 * @return	True iff an IFileExporter has been chosen, a destination File
	 * 			has been chosen, and a Result has been chosen.
	 */
	public boolean isComplete(){
		return isReadyForResult() && chosenResult != null;
	}
}
