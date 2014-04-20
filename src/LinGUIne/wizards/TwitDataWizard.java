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
import LinGUIne.model.TextData;
import LinGUIne.model.Project.Subdirectory;

public class TwitDataWizard extends Wizard {
	
	ImportFileData wizardData;
	TwitDataWizardChooseSearchPage wizardSearchPage;

	
	public TwitDataWizard(ImportFileData wData){
		super();
		wizardData = wData;
		
	}
	
	@Override
	public void addPages() {
		wizardSearchPage = new TwitDataWizardChooseSearchPage(wizardData);
		addPage(wizardSearchPage);
	}
	
	@Override
	public boolean performFinish() {
		Project chosenProject = wizardData.getChosenProject();
		File newFile = chosenProject.getSubdirectory(Subdirectory.Data).append(
				wizardData.getInternetSourceFileName()).toFile();

		try {
			Files.createFile(newFile.toPath());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		wizardData.addFile(newFile);
		
		return true;
	}

}
