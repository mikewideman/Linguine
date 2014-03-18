package LinGUIne.wizards;

import javax.inject.Inject;

import org.eclipse.core.runtime.SafeRunner;
import org.eclipse.e4.core.contexts.ContextInjectionFactory;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.jface.wizard.WizardDialog;

import LinGUIne.extensions.IVisualization;
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

	private VisualizationWizardSelectResultPage selectResultPage;
	private VisualizationWizardSelectVisualizationPage selectVisualizationPage;

	@Inject
	private ProjectManager projectMan;
	@Inject
	private VisualizationPluginManager visualizationPluginMan;
	@Inject
	private MApplication application;
	private VisualizationData wizardData;

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
		selectResultPage = new VisualizationWizardSelectResultPage(wizardData,
				projectMan);
		selectVisualizationPage = new VisualizationWizardSelectVisualizationPage(
				wizardData, visualizationPluginMan);

		// addPage(selectResultPage);
		addPage(selectVisualizationPage);
	}

	/**
	 * Finishes the visualization wizard by running the chosen visualization in
	 * a safe manner.
	 */
	@Override
	public boolean performFinish() {
		IVisualization visualization = wizardData.getChosenVisualization();

		if (visualization.hasWizard()) {
			// Launch the visualization's wizard
			LinGUIne.extensions.VisualizationWizard visWiz = visualization
					.getWizard();
			WizardDialog wizardDialog = new WizardDialog(getShell(), visWiz);

			int retVal = wizardDialog.open();

			if (retVal == WizardDialog.OK) {
				// TODO: TBD what should be here. If a visualization wizard
				// fails, perhaps default values should be used?
			}
		}

		SafeVisualization safeVisualization = new SafeVisualization(getShell(),
				wizardData.getChosenVisualization(),
				wizardData.getChosenResults());

		// Set the runnable up for injection so it can post events using
		// IEventBroker
		ContextInjectionFactory.inject(safeVisualization,
				application.getContext());

		SafeRunner.run(safeVisualization);
		return true;
	}

}
