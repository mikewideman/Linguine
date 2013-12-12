package LinGUIne.wizards;

import org.eclipse.jface.wizard.Wizard;

import LinGUIne.model.Project;
import LinGUIne.model.ProjectManager;

/**
 * Wizard for creating a new Project.
 * 
 * @author Kyle Mullins
 */
public class NewProjectWizard extends Wizard {

	private NewProjectWizardNamePage namePage;
	
	private Project newProject;
	private ProjectManager projectMan;
	
	/**
	 * Creates a new NewProjectWizard referencing the given ProjectManager to
	 * obtain information about existing Projects.
	 * 
	 * @param projects	The ProjectManager instance.
	 */
	public NewProjectWizard(ProjectManager projects){
		super();
		newProject = new Project();
		projectMan = projects;
	}
	
	/**
	 * Sets up the pages in the wizard.
	 */
	@Override
	public void addPages(){
		namePage = new NewProjectWizardNamePage(newProject, projectMan);
		
		addPage(namePage);
	}
	
	@Override
	public boolean performFinish() {
		return true;
	}

	/**
	 * Returns the new Project object that was created by the wizard.
	 */
	public Project getProject(){
		return newProject;
	}
}
