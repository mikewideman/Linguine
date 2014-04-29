package LinGUIne.wizards;

import java.io.File;

import LinGUIne.extensions.IAnnotationExporter;
import LinGUIne.model.IProjectData;
import LinGUIne.model.Project;

/**
 * Data object used to carry data between pages in the ExportFileWizard.
 * 
 * @author Kyle Mullins
 */
public class ExportAnnotationData {
	private IAnnotationExporter chosenExporter;
	private Project chosenProject;
	private IProjectData chosenAnnotatedData;
	private File chosenDestFile;
	
	/**
	 * Creates a default empty instance.
	 */
	public ExportAnnotationData(){
		chosenExporter = null;
		chosenProject = null;
		chosenAnnotatedData = null;
		chosenDestFile = null;
	}
	
	/**
	 * Sets the IAnnotationExporter that was chosen.
	 * 
	 * @param exporter	An instance of the chosen IAnnotationExporter.
	 */
	public void setExporter(IAnnotationExporter exporter){
		chosenExporter = exporter;
	}
	
	/**
	 * Returns the chosen IAnnotationExporter instance.
	 */
	public IAnnotationExporter getChosenExporter(){
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
	 * Sets the Annotated Data chosen to be exported.
	 * 
	 * @param result	the chosen Annotated Data.
	 */
	public void setAnnotatedData(IProjectData result){
		chosenAnnotatedData = result;
	}
	
	/**
	 * Returns the Annotated Data chosen to be exported.
	 */
	public IProjectData getChosenAnnotatedData(){
		return chosenAnnotatedData;
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
	 * which Annotated Data to export.
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
	 * 			has been chosen, and Annotated Data has been chosen.
	 */
	public boolean isComplete(){
		return isReadyForResult() && chosenAnnotatedData != null;
	}
}
