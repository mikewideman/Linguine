package LinGUIne.wizards;

import org.eclipse.jface.wizard.Wizard;

import LinGUIne.model.Project;

public class NewProjectWizard extends Wizard {

	private NewProjectWizardNamePage namePage;
	
	private Project newProject;
	
	public NewProjectWizard(){
		super();
		newProject = new Project();
	}
	
	@Override
	public void addPages(){
		namePage = new NewProjectWizardNamePage(newProject);
		
		addPage(namePage);
	}
	
	@Override
	public boolean performFinish() {
		return true;
	}

	public Project getProject(){
		return newProject;
	}
}
