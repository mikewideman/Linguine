package LinGUIne.wizards;

import javax.inject.Inject;

import org.eclipse.core.runtime.SafeRunner;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.jface.wizard.Wizard;

import LinGUIne.model.ProjectManager;
import LinGUIne.model.VisualizationPluginManager;
import LinGUIne.utilities.SafeVisualization;

/**
 * This wizard is used to generate visualizations. Visualization plugins can
 * provide their own wizards which will be called from within this wizard. A
 * user can select a project and several results from a project, which will
 * determine which visualizations are available to them.
 * 
 * @author Peter Dimou
 */
public class VisualizationWizard extends Wizard {

	private VisualizationWizardSelectResultPage selectFilePage;
	private VisualizationWizardSelectVisualizationPage selectVisualizationPage;
	private VisualizationData wizardData;

	@Inject
	private ProjectManager projectMan;
	
	@Inject
	private VisualizationPluginManager visualizationPluginMan;
	
	@Inject
	private IEventBroker eventBroker;

	/**
	 * Constructs the VisualizationWizard with new data that is to be carried
	 * to/from this wizard's pages.
	 */
	public VisualizationWizard() {
		super();
		setNeedsProgressMonitor(true);

		wizardData = new VisualizationData();
	}

	/**
	 * Creates the required pages that belong in the wizard and then adds them
	 * in the correct order.
	 */
	public void addPages() {
		selectFilePage = new VisualizationWizardSelectResultPage(wizardData,
				projectMan);
		selectVisualizationPage = new VisualizationWizardSelectVisualizationPage(
				wizardData, visualizationPluginMan);

		addPage(selectFilePage);
		addPage(selectVisualizationPage);
	}

	/**
	 * Finishes the visualization wizard by running the chosen visualization in
	 * a safe manner.
	 */
	@Override
	public boolean performFinish() {
		SafeVisualization safeVisualization = new SafeVisualization(getShell(),
				wizardData.getChosenVisualization(),
				wizardData.getChosenResults(), wizardData.getChosenProject(),
				eventBroker);

		SafeRunner.run(safeVisualization);
		return true;
	}

}
