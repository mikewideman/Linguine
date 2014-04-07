package LinGUIne.wizards;

import org.eclipse.jface.wizard.Wizard;

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
		// TODO Auto-generated method stub
		return true;
	}

}
