package LinGUIne.utilities;

import java.io.File;
import java.util.Collection;
import java.util.List;

import org.eclipse.core.runtime.ISafeRunnable;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Shell;

import LinGUIne.extensions.IFileImporter;
import LinGUIne.model.IProjectData;
import LinGUIne.model.IProjectDataContents;
import LinGUIne.model.Project;
import LinGUIne.model.TextData;

/**
 * Runnable which wraps the execution of an IFileImporter in a safe fashion.
 * 
 * @author Kyle Mullins
 */
public class SafeImporter implements ISafeRunnable {
	private Shell shell;
	private IFileImporter fileImporter;
	private Collection<File> sourceFiles;
	private Project destProject;
	private List<String> searchDetails; //For importers that don't use local file system

	/**
	 * Creates a new SafeImporter using the given importer, importing the given
	 * files into the given Project.
	 * 
	 * @param theShell	The current Shell; used to display error dialogs.
	 * @param importer	The IFileImporter to be used for the import job.
	 * @param sources	The File(s) to be imported at this time.
	 * @param proj		The Project into which the File(s) are to be imported.
	 */
	public SafeImporter(Shell theShell, IFileImporter importer,
			Collection<File> sources, Project proj, List<String> details) {
		
		shell = theShell;
		fileImporter = importer;
		sourceFiles = sources;
		destProject = proj;
		searchDetails = details;
	}

	/**
	 * Raises an error dialog in the event of an exception during execution.
	 */
	@Override
	public void handleException(Throwable exception) {
		MessageDialog.open(SWT.OK, shell, "Error",
				"An error occurred while importing file.", SWT.NONE);
	}

	/**
	 * Imports the sourceFiles into destProject using fileImporter.
	 */
	@Override
	public void run() throws Exception {
		for(File sourceFile: sourceFiles){
			
			IProjectDataContents importedData =
					fileImporter.importFile(sourceFile, searchDetails);
			
			File newFile = destProject.getSubdirectory(Project.Subdirectory.Data).
					append(sourceFile.getName()).toFile();
			
			Class<? extends IProjectData> clazz =
					importedData.getAssociatedDataType();
			IProjectData newProjData = (IProjectData)clazz.getDeclaredConstructor(
					File.class).newInstance(newFile);
			
			if(newProjData.updateContents(importedData)){				
				destProject.addProjectData(newProjData);
			}
			else{
				throw new Exception("Could not write data to workspace file: " +
						newProjData.getFile());
			}
		}
	}
}