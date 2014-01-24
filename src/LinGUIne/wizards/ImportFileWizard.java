package LinGUIne.wizards;

import java.util.ArrayList;

import javax.inject.Inject;

import org.eclipse.core.runtime.ISafeRunnable;
import org.eclipse.core.runtime.SafeRunner;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.swt.widgets.Shell;

import LinGUIne.model.ProjectManager;

/**
 * Wizard for creating a new Project.
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
	 * Creates a new NewProjectWizard referencing the given ProjectManager to
	 * obtain information about existing Projects.
	 */
	public ImportFileWizard(){
		super();
	}
	
	/**
	 * Sets up the pages in the wizard.
	 */
	@Override
	public void addPages(){
		wizardData = new ImportFileData();
		
		setupPage = new ImportFileWizardSetupPage(wizardData, projectMan);
		chooseFilesPage = new ImportFileWizardChooseFilesPage(wizardData);
		
		addPage(setupPage);
		addPage(chooseFilesPage);
	}
	
	@Override
	public IWizardPage getNextPage(IWizardPage page){
		if(page == setupPage && wizardData.shouldCreateNewProject()){
			//TODO: launch NewProjectWizard
			//TODO: set Project in wizardData
		}
		
		return super.getNextPage(page);
	}
	
	@Override
	public boolean performFinish() {
		SafeImporter safeImporter = new SafeImporter(getShell(),
				wizardData.getChosenImporter(), wizardData.getChosenFiles(),
				wizardData.getChosenProject());
		
		SafeRunner.run(safeImporter);
		
		return true;
	}
}
