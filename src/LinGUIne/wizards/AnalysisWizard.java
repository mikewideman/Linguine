package LinGUIne.wizards;

import javax.inject.Inject;

import org.eclipse.core.runtime.SafeRunner;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.Wizard;

import LinGUIne.model.ProjectManager;
import LinGUIne.model.SoftwareModuleManager;

/**
 * Analysis Wizard object; pieces the Wizard Pages together into
 * a comprehensive Wizard Dialog
 * 
 * @author Matthew Talbot
 */

public class AnalysisWizard extends Wizard {

	private AnalysisWizardSelectFilePage selectFilePage;
	private AnalysisWizardSelectAnalysisPage selectAnalysisPage;
	private AnalysisWizardSelectSettingsPage settingsPage;

	@Inject
	private ProjectManager projectMan;
	@Inject
	private SoftwareModuleManager softwareModuleMan;
	private AnalysisData wizardData;

	public AnalysisWizard() {
		super();
		setNeedsProgressMonitor(true);

		wizardData = new AnalysisData();
	}

	/**
	 * Automagically sets the Wizard pages in order
	 * 
	 */
	@Override
	public void addPages() {
		selectFilePage = new AnalysisWizardSelectFilePage(wizardData, projectMan);
		selectAnalysisPage = new AnalysisWizardSelectAnalysisPage(wizardData,
				softwareModuleMan);
		//    settingsPage = new AnalysisWizardSelectSettingsPage();

		addPage(selectFilePage);
		addPage(selectAnalysisPage);
		addPage(settingsPage);
	}

	@Override
	public IWizardPage getNextPage(IWizardPage page){
		if(page == selectAnalysisPage){
			//TODO: Show page provided by Analysis plug-in if any
		}

		return super.getNextPage(page);
	}

	/**
	 * If there are any actions to be performed when the Wizard closes, they would go in this method.
	 * Pending actual content before we can fill this part out.
	 * 
	 */
	@Override
	public boolean performFinish() {
		SafeAnalysis safeAnalysis = new SafeAnalysis(getShell(),
				wizardData.getChosenAnalysis(), wizardData.getChosenProjectData(),
				wizardData.getChosenProject());

		SafeRunner.run(safeAnalysis);
		
		return true;
	}
}