package LinGUIne.wizards;

import java.io.File;
import java.util.Collection;
import java.util.LinkedList;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;

import LinGUIne.extensions.IVisualization;
import LinGUIne.model.KeyValueResult;
import LinGUIne.model.Result;
import LinGUIne.model.VisualizationPluginManager;

/**
 * This page is used to select a visualization from the visualization plugins
 * currently loaded into the application. Visualizations will only be listed if
 * they meet the user's selected result types.
 * 
 * @author Peter Dimou
 */
public class VisualizationWizardSelectVisualizationPage extends WizardPage {

	private VisualizationPluginManager visualizationPluginMan;
	private VisualizationData wizardData;

	private Label lblVisualizations;
	private List lstVisualizations;
	private Label lblDescription;

	/**
	 * Constructs the page with the correct title and description. This is
	 * currently the second page of the VisualizationWizard and the description
	 * reflects that.
	 * 
	 * @param data
	 *            The data to be carried to/from each page in the wizard.
	 * @param visualizationPluginMan
	 *            Where to get the listing of all visualizations currently in
	 *            the application.
	 */
	public VisualizationWizardSelectVisualizationPage(
			VisualizationData wizardData,
			VisualizationPluginManager visualizationPluginMan) {
		super("Visualization Wizard");
		setTitle("Visualization Wizard - Step 2");
		setDescription("Select the Visualization you wish to run.");

		this.wizardData = wizardData;
		this.visualizationPluginMan = visualizationPluginMan;
	}

	/**
	 * Populates the wizard page with content.
	 * 
	 * @param parent
	 *            The current content pane
	 */
	@Override
	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout(2, true);
		container.setLayout(layout);

		Group grpVisualizations = new Group(container, SWT.NONE);
		grpVisualizations.setLayout(new GridLayout(1, false));
		grpVisualizations.setLayoutData(new GridData(GridData.FILL_BOTH));
		grpVisualizations.setText("Visualizations");

		lblVisualizations = new Label(grpVisualizations, SWT.NONE);
		lblVisualizations.setText("Select a Visualization for your data:");

		lstVisualizations = new List(grpVisualizations, SWT.BORDER
				| SWT.V_SCROLL);
		lstVisualizations.setLayoutData(new GridData(GridData.FILL_BOTH));

		// TODO: FOR DEMONSTRATION PURPOSES ONLY! Remove in the final version!
		Collection<Result> testResults = new LinkedList<Result>();
		KeyValueResult testKVResult = new KeyValueResult(new File(""));
		testResults.add(testKVResult);
		wizardData.setChosenResults(testResults);

		// Populate the list of visualizations based on result types
		for (IVisualization visualization : visualizationPluginMan
				.getVisualizationsBySupportedResultTypeSet(wizardData
						.getChosenResultTypes())) {
			lstVisualizations.add(visualization.getName());
		}

		// Add the listener to change the description label upon click of a
		// visualization
		lstVisualizations.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				if (lstVisualizations.getSelectionCount() > 0) {
					String selectedVisualizationName = lstVisualizations
							.getSelection()[0];
					IVisualization visualization = visualizationPluginMan
							.getVisualizationByName(selectedVisualizationName);
					String description = visualizationPluginMan
							.getVisualizationDescriptionByName(visualization
									.getName());

					wizardData.setChosenVisualization(visualization);
					lblDescription.setText(description);
					lblDescription.update();
				}
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
			}
		});

		// Create the group that displays a visualization's description.
		Group grpDescription = new Group(container, SWT.None);
		grpDescription.setLayout(new GridLayout(1, false));
		grpDescription.setLayoutData(new GridData(GridData.FILL_BOTH));
		grpDescription.setText("Description");

		lblDescription = new Label(grpDescription, SWT.NONE);
		lblDescription.setLayoutData(new GridData(GridData.FILL_BOTH));

		setControl(container);
		setPageComplete(false);
	}
}
