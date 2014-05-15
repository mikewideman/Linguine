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

/**
 * Wizard for Twitter data import, for use with Twitter Data Plugin
 * Creates the wizard form which will be used to create the twitter query
 * 
 * @author Peter Maresca
 *
 */
public class TwitDataWizard extends Wizard {
	
	ImportFileData wizardData;
	TwitDataWizardChooseSearchPage wizardSearchPage;

	/**
	 * Constructor for the wizard
	 * @param wData - Import file data the wizard is getting handed from Import wizard
	 */
	public TwitDataWizard(ImportFileData wData){
		super();
		wizardData = wData;
		
	}
	
	/**
	 * Adds the Search Page to the wizard
	 */
	@Override
	public void addPages() {
		wizardSearchPage = new TwitDataWizardChooseSearchPage(wizardData);
		addPage(wizardSearchPage);
	}
	
	/**
	 * Creates a new file with the project information from the import data. 
	 */
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
