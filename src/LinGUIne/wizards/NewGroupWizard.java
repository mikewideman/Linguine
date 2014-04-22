package LinGUIne.wizards;

import org.eclipse.jface.wizard.Wizard;

import LinGUIne.model.Project;
import LinGUIne.model.ProjectGroup;
import LinGUIne.model.ProjectManager;

public class NewGroupWizard extends Wizard {

	private ProjectManager projectMan;
	private NewGroupData wizardData;
	
	private NewGroupWizardSelectProjectPage selectProjectPage;
	private NewGroupWizardNamePage namePage;
	
	public NewGroupWizard(ProjectManager projects, Project project,
			ProjectGroup group){
		super();
		
		projectMan = projects;
		wizardData = new NewGroupData();
		wizardData.setDestProject(project);
		wizardData.setParentGroup(group);
	}
	
	@Override
	public void addPages(){
		selectProjectPage = new NewGroupWizardSelectProjectPage(projectMan,
				wizardData);
		namePage = new NewGroupWizardNamePage(wizardData);

		//Only add the first page if the Project and Group haven't been chosen
		if(wizardData.getDestProject() != null &&
				wizardData.getParentGroup() != null){

			addPage(selectProjectPage);
		}

		addPage(namePage);
	}
	
	@Override
	public boolean performFinish() {
		ProjectGroup newGroup = new ProjectGroup(wizardData.getGroupName(),
				wizardData.getParentGroup());
		
		wizardData.getDestProject().addGroup(newGroup);
		
		return true;
	}
}