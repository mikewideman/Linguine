package LinGUIne.wizards;

import java.util.TreeMap;

import org.eclipse.jface.wizard.Wizard;

import LinGUIne.model.Project;

public class NewProjectWizard extends Wizard {

	private NewProjectWizardNamePage namePage;
	
	private Project newProject;
	private TreeMap<String, Project> projectSet;
	
	public NewProjectWizard(TreeMap<String, Project> projects){
		super();
		newProject = new Project();
		projectSet = projects;
	}
	
	@Override
	public void addPages(){
		namePage = new NewProjectWizardNamePage(newProject, projectSet);
		
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
