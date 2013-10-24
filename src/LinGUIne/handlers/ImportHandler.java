package LinGUIne.handlers;

import java.awt.Dialog;

import javax.inject.Named;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;

public class ImportHandler {
	@Execute
	public void execute(@Named(IServiceConstants.ACTIVE_SHELL) Shell shell) {
		FileDialog dialog = new FileDialog(shell, SWT.OPEN | SWT.MULTI);
		String chosenFile = dialog.open();
		
		if(chosenFile != null) {
			String[] chosenFiles = dialog.getFileNames();
			System.out.println(chosenFiles);
			
		}
	}
}
