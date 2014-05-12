package LinGUIne.wizards;

import java.util.Collection;

import javax.inject.Inject;

import org.eclipse.jface.util.SafeRunnable;
import org.eclipse.jface.wizard.Wizard;

import LinGUIne.model.IProjectData;
import LinGUIne.model.ProjectManager;
import LinGUIne.utilities.SafeExporter;

/**
 * Wizard to walk the user through exporting a Result to an external File.
 * 
 * @author Kyle Mullins
 */
public class ExportFileWizard extends Wizard {
	
	@Inject
	private ProjectManager projectMan;
	
	private ExportFileData wizardData;
	private ExportFileWizardExporterPage exporterPage;
	private ExportFileWizardChooseResultPage resultPage;
	private ExportFileWizardChooseFilePage filePage;
	
	public ExportFileWizard(){
		super();
		
		wizardData = new ExportFileData();
	}
	
	@Override
	public void addPages(){
		exporterPage = new ExportFileWizardExporterPage(wizardData);
		resultPage = new ExportFileWizardChooseResultPage(wizardData, projectMan);
		filePage = new ExportFileWizardChooseFilePage(wizardData);
		
		addPage(exporterPage);
		addPage(resultPage);
		addPage(filePage);
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
