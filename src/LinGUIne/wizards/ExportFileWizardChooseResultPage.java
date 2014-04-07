package LinGUIne.wizards;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.widgets.Composite;

import LinGUIne.model.ProjectManager;

public class ExportFileWizardChooseResultPage extends WizardPage {

	private ExportFileData wizardData;
	private ProjectManager projectMan;
	
	public ExportFileWizardChooseResultPage(ExportFileData data,
			ProjectManager projects){
		super("");
		
		wizardData = data;
		projectMan = projects;
	}
	
	@Override
	public void createControl(Composite parent) {

	}
}
