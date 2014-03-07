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
	private NewFileWizardPage newFilePage;
	
	public NewFileWizard(){
		super();
		
		wizardData = new NewFileData();
	}
	
	@Override
	public void addPages(){
		newFilePage = new NewFileWizardPage(wizardData, projectMan);
		
		addPage(newFilePage);
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
			
			eventBroker.post(LinGUIneEvents.UILifeCycle.OPEN_PROJECT_DATA,
					new OpenProjectDataEvent(newTextData));
		}
		catch(IOException e) {
			e.printStackTrace();
		}
		
		return true;
	}
}
