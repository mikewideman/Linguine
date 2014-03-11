package LinGUIne.wizards;

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

import LinGUIne.model.Project;
import LinGUIne.model.ProjectManager;
import LinGUIne.model.Result;

/**
 * Currently the first page in the wizard, it allows the user to select multiple
 * results from a single project to run a visualization.
 * 
 * @author Peter Dimou
 */
public class VisualizationWizardSelectResultPage extends WizardPage {

	private Label lblProjects;
	private List lstProjects;
	private Label lblResults;
	private List lstResults;

	private ProjectManager projectMan;
	private VisualizationData wizardData;

	/**
	 * Constructs the page with the correct title and description. This is
	 * currently the first page of the VisualizationWizard and the description
	 * reflects that.
	 * 
	 * @param data
	 *            The data to be carried to/from each page in the wizard.
	 * @param projects
	 *            Where to get the listing of all projects currently in the
	 *            application.
	 */
	public VisualizationWizardSelectResultPage(VisualizationData data,
			ProjectManager projects) {

		super("Visualization Wizard");
		setTitle("Visualization Wizard - Step 1");
		setDescription("Select the results you wish to visualize.");

		wizardData = data;
		projectMan = projects;
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

		Group grpProjects = new Group(container, SWT.NONE);
		grpProjects.setLayout(new GridLayout(1, false));
		grpProjects.setLayoutData(new GridData(GridData.FILL_BOTH));
		grpProjects.setText("Project");

		lblProjects = new Label(grpProjects, SWT.NONE);
		lblProjects.setText("Select from which Project to select results:");

		lstProjects = new List(grpProjects, SWT.BORDER | SWT.V_SCROLL);
		lstProjects.setLayoutData(new GridData(GridData.FILL_BOTH));

		for (Project project : projectMan.getProjects()) {
			lstProjects.add(project.getName());
		}

		lstProjects.addSelectionListener(new SelectionListener() {

			/**
			 * Sets which Project is currently selected and populates the List
			 * of Results in the Project.
			 */
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (lstProjects.getSelectionCount() > 0) {
					Project selectedProject = projectMan.getProject(lstProjects
							.getSelection()[0]);
					wizardData.setChosenProject(selectedProject);
					wizardData.setChosenResults(new LinkedList<Result>());

					updateResultList();
					lstResults.setEnabled(true);
					checkIfPageComplete();
				}
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
			}

		});

		// Set up the list of results
		Group grpResults = new Group(container, SWT.NONE);
		grpResults.setLayout(new GridLayout(1, false));
		grpResults.setLayoutData(new GridData(GridData.FILL_BOTH));
		grpResults.setText("Results");

		lblResults = new Label(grpResults, SWT.NONE);
		lblResults
				.setText("Select the results on which to run the visualization:");

		lstResults = new List(grpResults, SWT.BORDER | SWT.V_SCROLL | SWT.MULTI);
		lstResults.setLayoutData(new GridData(GridData.FILL_BOTH));
		lstResults.setEnabled(false);
		lstResults.addSelectionListener(new SelectionListener() {

			/**
			 * Sets which results are currently selected.
			 */
			@Override
			public void widgetSelected(SelectionEvent e) {
				LinkedList<Result> selectedResults = new LinkedList<Result>();

				for (String resultName : lstResults.getSelection()) {
					selectedResults.add((Result) wizardData.getChosenProject()
							.getProjectData(resultName));
				}

				wizardData.setChosenResults(selectedResults);
				checkIfPageComplete();
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
			}
		});

		// Required to avoid an error in the system
		setControl(container);
		setPageComplete(false);
	}

	/**
	 * Updates the list of results displayed to the user.
	 */
	private void updateResultList() {
		lstResults.deselectAll();
		lstResults.removeAll();

		for (Result result : wizardData.getChosenProject().getResults()) {
			lstResults.add(result.getName());
		}

		lstResults.update();
	}

	/**
	 * This page is complete iff there is a chosen project and at least one
	 * result
	 */
	private void checkIfPageComplete() {
		if (wizardData.getChosenProject() != null
				&& !wizardData.getChosenResults().isEmpty()) {

			setPageComplete(true);
		} else {
			setPageComplete(false);
		}
	}
}
