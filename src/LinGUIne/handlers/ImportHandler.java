package LinGUIne.handlers;

import javax.inject.Inject;
import javax.inject.Named;

import org.eclipse.e4.core.contexts.ContextInjectionFactory;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Shell;

import LinGUIne.model.ProjectManager;
import LinGUIne.wizards.ImportFileWizard;

/**
 * Handler for importing files into a Project in the workspace.
 * 
 * @author Kyle Mullins
 */
public class ImportHandler {
	
	@Inject
	@Optional
	private ProjectManager projectMan;
	
	@Inject
	private MApplication application;
	
	/**
	 * Prompts the user for one or more files to import into a Project and
	 * then copies them into the workspace.
	 * 
	 * @param shell	The currently active Shell.
	 */
	@Execute
	public void execute(@Named(IServiceConstants.ACTIVE_SHELL) Shell shell) {
		
		ImportFileWizard importWizard = new ImportFileWizard();
		WizardDialog wizardDialog = new WizardDialog(shell, importWizard);
		
		ContextInjectionFactory.inject(importWizard, application.getContext());
		
		int retval = wizardDialog.open();
		
		if(retval == WizardDialog.OK){
			//TODO: stuff
		}
		
		/*FileDialog dialog = new FileDialog(shell, SWT.OPEN | SWT.MULTI);
		String chosenFile = dialog.open();
		
		if(chosenFile != null) {
			String sourceDir = new File(chosenFile).getParent();
			String[] chosenFiles = dialog.getFileNames();
			IPath destPath = Platform.getLocation();
			
			//Copy selected files into the workspace
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
		}*/
	}
}
