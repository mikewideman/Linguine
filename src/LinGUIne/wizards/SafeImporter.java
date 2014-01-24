package LinGUIne.wizards;

import java.io.File;
import java.util.Collection;

import org.eclipse.core.runtime.ISafeRunnable;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Shell;

import LinGUIne.extensions.IFileImporter;
import LinGUIne.model.IProjectData;
import LinGUIne.model.IProjectDataContents;
import LinGUIne.model.Project;
import LinGUIne.model.TextData;

public class SafeImporter implements ISafeRunnable {
	private Shell shell;
	private IFileImporter fileImporter;
	private Collection<File> sourceFiles;
	private Project destProject;

	public SafeImporter(Shell theShell, IFileImporter importer,
			Collection<File> sources, Project proj) {
		
		shell = theShell;
		fileImporter = importer;
		sourceFiles = sources;
		destProject = proj;
	}

	@Override
	public void handleException(Throwable exception) {
		MessageDialog.open(SWT.OK, shell, "Error",
				"An error occurred while importing file.", SWT.NONE);
	}

	@Override
	public void run() throws Exception {
		for(File sourceFile: sourceFiles){
			
			IProjectDataContents importedData =
					fileImporter.importFile(sourceFile);
			
			File newFile = destProject.getProjectDirectory().append(
					Project.DATA_SUBDIR).append(sourceFile.getName()).toFile();
			
			//TODO: get the proper IProjectData type from somewhere
			IProjectData newProjData = new TextData(newFile);
			
			newProjData.updateContents(importedData);
			destProject.addProjectData(newProjData);
		}
	}
}