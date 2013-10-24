package LinGUIne.handlers;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import javax.inject.Named;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Platform;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;

public class ImportHandler {
	@Execute
	public void execute(@Named(IServiceConstants.ACTIVE_SHELL) Shell shell) {
		FileDialog dialog = new FileDialog(shell, SWT.OPEN | SWT.MULTI);
		String chosenFile = dialog.open();
		
		if(chosenFile != null) {
			String sourceDir = new File(chosenFile).getParent();
			String[] chosenFiles = dialog.getFileNames();
			IPath destPath = Platform.getLocation();
			
			for(String file: chosenFiles) {
				Path sourceFile = new File(sourceDir + "\\" + file).toPath();
				Path destFile = destPath.append(file).toFile().toPath();
				
				try {
					Files.copy(sourceFile, destFile);
				}
				catch(IOException ioe) {
					MessageDialog.openError(shell, "Error", "Could not copy " +
							"files to Project workspace: " + ioe.getMessage());
					
					return;
				}
			}
		}
	}
}
