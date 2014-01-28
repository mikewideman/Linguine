package LinGUIne.wizards;

import javax.inject.Inject;

import org.eclipse.core.runtime.SafeRunner;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.Wizard;

import LinGUIne.model.ProjectManager;

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
	 * Determines which page is to be shown next. Launches the NewProjectWizard
	 * if a new Project needs to be created.
	 */
	@Override
	public IWizardPage getNextPage(IWizardPage page){
		if(page == setupPage && wizardData.shouldCreateNewProject()){
			//TODO: launch NewProjectWizard
			//TODO: set Project in wizardData
		}
		
		return super.getNextPage(page);
	}
	
	/**
	 * Runs the import job once the wizard has finished.
	 */
	@Override
	public boolean performFinish() {
		SafeImporter safeImporter = new SafeImporter(getShell(),
				wizardData.getChosenImporter(), wizardData.getChosenFiles(),
				wizardData.getChosenProject());
		
		SafeRunner.run(safeImporter);
		
		return true;
	}
}
