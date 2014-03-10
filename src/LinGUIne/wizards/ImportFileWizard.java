package LinGUIne.wizards;

import java.io.IOException;

import javax.inject.Inject;

import org.eclipse.core.runtime.SafeRunner;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.jface.wizard.WizardDialog;

import LinGUIne.model.Project;
import LinGUIne.model.ProjectManager;
import LinGUIne.utilities.SafeImporter;

/**
 * Wizard for importing one or more Files into a Project.
 * 
 * @author Kyle Mullins
 */
public class ImportFileWizard extends Wizard {

	@Inject
	private ProjectManager projectMan;
	
	private ImportFileData wizardData;
	private ImportFileWizardSetupPage setupPage;
	private ImportFileWizardChooseFilesPage chooseFilesPage;
	
	/**
	 * Creates a new ImportFileWizard.
	 */
	public ImportFileWizard(){
		super();
		
		wizardData = new ImportFileData();
	}
	
	/**
	 * Sets up the pages in the wizard.
	 */
	@Override
	public void addPages(){
		setupPage = new ImportFileWizardSetupPage(wizardData, projectMan);
		chooseFilesPage = new ImportFileWizardChooseFilesPage(wizardData);
		
		addPage(setupPage);
		addPage(chooseFilesPage);
	}
	
	/**
	 * Runs the import job once the wizard has finished.
	 */
	@Override
	public boolean performFinish() {
		if(wizardData.shouldCreateNewProject()){
			//Launch NewProjectWizard and capture the created Project
			NewProjectWizard projectWizard = new NewProjectWizard(projectMan);
			WizardDialog wizardDialog = new WizardDialog(getShell(), projectWizard);
			
			int retval = wizardDialog.open();
			
			if(retval == WizardDialog.OK) {
				Project newProj = projectWizard.getProject();
				newProj.setParentDirectory(projectMan.getWorkspace());
				
				try {
					newProj.createProjectFiles();
					projectMan.addProject(newProj);
					wizardData.setProject(newProj);
				}
				catch(IOException ioe) {
					MessageDialog.openError(getShell(), "Error", "Could not create "
							+ "Project directory: " + ioe.getMessage());
				}
			}
		}
		
		if(wizardData.getChosenProject() != null){
			SafeImporter safeImporter = new SafeImporter(getShell(),
					wizardData.getChosenImporter(), wizardData.getChosenFiles(),
					wizardData.getChosenProject());
			
			SafeRunner.run(safeImporter);
			
			return true;
		}
		
		return false;
	}
}
