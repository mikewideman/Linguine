package LinGUIne.wizards;

import javax.inject.Inject;

import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.jface.wizard.Wizard;

import LinGUIne.model.ProjectManager;
import LinGUIne.model.VisualizationPluginManager;

/**
 * @author Peter Dimou
 */
public class VisualizationWizard extends Wizard {

	private VisualizationWizardSelectFilePage selectFilePage;
	private VisualizationWizardSelectVisualizationPage selectVisualizationPage;

	@Inject
	private ProjectManager projectMan;
	@Inject
	private VisualizationPluginManager visualizationPluginMan;
	private VisualizationData wizardData;

	public VisualizationWizard() {
		super();
		setNeedsProgressMonitor(true);

		wizardData = new VisualizationData();
	}

	public void addPages() {
		selectFilePage = new VisualizationWizardSelectFilePage(wizardData,
				projectMan);
		selectVisualizationPage = new VisualizationWizardSelectVisualizationPage(
				wizardData, visualizationPluginMan);
		
		//addPage(selectFilePage);
		addPage(selectVisualizationPage);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.wizard.Wizard#performFinish()
	 */
	@Override
	public boolean performFinish() {
		// TODO Auto-generated method stub
		return false;
	}

}
