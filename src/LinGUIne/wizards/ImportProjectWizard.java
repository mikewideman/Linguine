package LinGUIne.wizards;

import javax.inject.Inject;

import org.eclipse.jface.wizard.Wizard;

import LinGUIne.model.Project;
import LinGUIne.model.ProjectManager;

public class ImportProjectWizard extends Wizard {

	@Inject
	private ProjectManager projectMan;
	
	private ImportProjectWizardPage wizardPage;
	
	@Override
	public void addPages(){
		wizardPage = new ImportProjectWizardPage(projectMan);
		
		addPage(wizardPage);
	}
	
	@Override
	public boolean performFinish() {
		for(Project projToImport: wizardPage.getProjectsToImport()){
			if(!projectMan.addProject(projToImport)){
				return false;
			}
		}
		
		return true;
	}
}
