package LinGUIne.wizards;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import javax.inject.Inject;

import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.jface.wizard.Wizard;

import LinGUIne.events.LinGUIneEvents;
import LinGUIne.events.OpenProjectDataEvent;
import LinGUIne.model.Project;
import LinGUIne.model.Project.Subdirectory;
import LinGUIne.model.ProjectGroup;
import LinGUIne.model.ProjectManager;
import LinGUIne.model.TextData;

/**
 * Wizard for creating a new text File and adding it to a Project.
 * 
 * @author Kyle Mullins
 */
public class NewFileWizard extends Wizard {

	@Inject
	private ProjectManager projectMan;
	
	@Inject
	private IEventBroker eventBroker;
	
	private NewFileData wizardData;
	private NewFileWizardSelectProjectPage selectProjectPage;
	private NewFileWizardNamePage namePage;
	
	public NewFileWizard(){
		super();
		
		wizardData = new NewFileData();
	}
	
	public void addStartingData(Project project, ProjectGroup group){		
		wizardData.setChosenProject(project);
		wizardData.setParentGroup(group);
	}
	
	@Override
	public void addPages(){
		selectProjectPage = new NewFileWizardSelectProjectPage(wizardData,
				projectMan);
		namePage = new NewFileWizardNamePage(wizardData);
		
		//Only add the first page if the Project and Group haven't been chosen
		if(wizardData.getChosenProject() == null ||
				wizardData.getParentGroup() == null){

			addPage(selectProjectPage);
		}
		
		addPage(namePage);
	}
	
	@Override
	public boolean performFinish() {

		Project chosenProject = wizardData.getChosenProject();
		File newFile = chosenProject.getSubdirectory(Subdirectory.Data).append(
				wizardData.getNewFileName()).toFile();
		TextData newTextData = new TextData(newFile);
		
		try {
			Files.createFile(newFile.toPath());
			chosenProject.addProjectData(newTextData);
			chosenProject.addDataToGroup(newTextData, wizardData.getParentGroup());
			
			eventBroker.post(LinGUIneEvents.UILifeCycle.OPEN_PROJECT_DATA,
					new OpenProjectDataEvent(newTextData, chosenProject));
		}
		catch(IOException e) {
			e.printStackTrace();
		}
		
		return true;
	}
}
