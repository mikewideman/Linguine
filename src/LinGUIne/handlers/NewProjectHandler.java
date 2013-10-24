package LinGUIne.handlers;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import javax.inject.Named;

import org.eclipse.core.runtime.Platform;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;

public class NewProjectHandler {
	@Execute
	public void execute(@Named(IServiceConstants.ACTIVE_SHELL) Shell shell) {
		InputDialog projectNamePrompt = new InputDialog(shell, "New Project",
				"Input the Project name:", null, null);
		
		int retval = projectNamePrompt.open();
		
		if(retval == InputDialog.OK) {
			String projectName = projectNamePrompt.getValue();
			Path dir = Platform.getLocation().append(projectName).toFile().toPath();
			
			try {
				Files.createDirectory(dir);
			}
			catch(IOException ioe) {
				MessageDialog.openError(shell, "Error", "Could not create "
						+ "Project directory: " + ioe.getMessage());
			}
		}
	}
}
