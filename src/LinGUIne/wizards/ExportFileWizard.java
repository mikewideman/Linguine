package LinGUIne.wizards;

import java.util.Collection;

import javax.inject.Inject;

import org.eclipse.jface.util.SafeRunnable;
import org.eclipse.jface.wizard.Wizard;

import LinGUIne.model.IProjectData;
import LinGUIne.model.ProjectManager;
import LinGUIne.utilities.SafeExporter;

public class ExportFileWizard extends Wizard {
	
	@Inject
	private ProjectManager projectMan;
	
	private ExportFileData wizardData;
	private ExportFileWizardSetupPage setupPage;
	private ExportFileWizardChooseResultPage resultPage;
	
	public ExportFileWizard(){
		super();
		
		wizardData = new ExportFileData();
	}
	
	@Override
	public void addPages(){
		setupPage = new ExportFileWizardSetupPage(wizardData);
		resultPage = new ExportFileWizardChooseResultPage(wizardData, projectMan);
		
		addPage(setupPage);
		addPage(resultPage);
	}
	
	@Override
	public boolean performFinish() {
		Collection<IProjectData> associatedData = wizardData.getChosenProject().
				getDataForResult(wizardData.getChosenResult());
		
		SafeExporter safeExporter = new SafeExporter(getShell(),
				wizardData.getChosenExporter(), wizardData.getChosenResult(),
				associatedData, wizardData.getDestFile());
		
		SafeRunnable.run(safeExporter);
		
		return true;
	}
}
