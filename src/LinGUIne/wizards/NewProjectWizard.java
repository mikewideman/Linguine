package LinGUIne.wizards;

import org.eclipse.jface.wizard.Wizard;

import LinGUIne.model.Project;
import LinGUIne.model.ProjectManager;

public class NewProjectWizard extends Wizard {

	private NewProjectWizardNamePage namePage;
	
	private Project newProject;
	private ProjectManager projectMan;
	
	public NewProjectWizard(ProjectManager projects){
		super();
		newProject = new Project();
		projectMan = projects;
	}
	
	@Override
	public void addPages(){
		namePage = new NewProjectWizardNamePage(newProject, projectMan);
		
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
