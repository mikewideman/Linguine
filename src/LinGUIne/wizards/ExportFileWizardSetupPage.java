package LinGUIne.wizards;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.widgets.Composite;

public class ExportFileWizardSetupPage extends WizardPage {

	private ExportFileData wizardData;
	
	public ExportFileWizardSetupPage(ExportFileData data){
		super("");
		
		wizardData = data;
	}
	
	@Override
	public void createControl(Composite parent) {

	}
}
