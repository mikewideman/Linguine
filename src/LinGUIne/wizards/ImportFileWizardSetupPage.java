package LinGUIne.wizards;

import java.util.HashMap;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;

import LinGUIne.model.Project;
import LinGUIne.model.ProjectManager;
import LinGUIne.extensions.IFileImporter;
import LinGUIne.extensions.PlaintextImporter;

/**
 * A WizardPage for the ImportFileWizard which prompts the user select an
 * IFileImporter they would like to use and a Project they would like to import
 * Files into.
 * 
 * @author Kyle Mullins
 */
public class ImportFileWizardSetupPage extends WizardPage {

	private Label lblImporters;
	private List lstImporters;
	private Label lblProjects;
	private List lstProjects;
	private Button radExistingProject;
	private Button radNewProject;
	
	private ImportFileData wizardData;
	private ProjectManager projectMan;
	
	
	/**
	 * Creates a new instance of the page with the given ImportFileData and the
	 * given instance of ProjectManager.
	 */
	public ImportFileWizardSetupPage(ImportFileData data, ProjectManager projects){
		super("Import File Wizard");
		setTitle("Import File Wizard");
		setDescription("");//TODO: pick a description
		setControl(lstImporters);
		
		wizardData = data;
		projectMan = projects;
	}
	
	/**
	 * Assembles the UI components for the page and registers
	 * SelectionListeners.
	 */
	@Override
	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		container.setLayout(layout);
		
		Group grpImporter = new Group(container, SWT.NONE);
		grpImporter.setLayout(new GridLayout(1, false));
		grpImporter.setLayoutData(new GridData(GridData.FILL_BOTH));
		grpImporter.setText("Import source");
		
		lblImporters = new Label(grpImporter, SWT.NONE);
		lblImporters.setText("Select the type of file you want to import:");
		
		lstImporters = new List(grpImporter, SWT.BORDER | SWT.V_SCROLL);
		lstImporters.setLayoutData(new GridData(GridData.FILL_BOTH));
		lstImporters.add("Plaintext file");
		
		IConfigurationElement[] configElements = Platform.getExtensionRegistry().
				getConfigurationElementsFor("LinGUIne.extensions.IFileImporter");
		
		final HashMap<String, IConfigurationElement> importerConfigs =
				new HashMap<String, IConfigurationElement>();
		
		for(IConfigurationElement configElement: configElements){
			String fileType = configElement.getAttribute("file_type");
			
			lstImporters.add(fileType);
			importerConfigs.put(fileType, configElement);
		}
		
		lstImporters.addSelectionListener(new SelectionListener(){

			/**
			 * Set which IFileImporter is currently selected.
			 */
			@Override
			public void widgetSelected(SelectionEvent e) {
				String selectedFileType = lstImporters.getSelection()[0];
				IFileImporter importer = null;
				
				if(importerConfigs.containsKey(selectedFileType)){
					IConfigurationElement importerConfig =
							importerConfigs.get(selectedFileType);
					
					try {
						importer = (IFileImporter)importerConfig.
								createExecutableExtension("class");
					}
					catch (CoreException e1) {
						e1.printStackTrace();
					}
				}
				else{
					importer = new PlaintextImporter();
				}
				
				wizardData.setImporter(importer);
				
				checkIfPageComplete();
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {}
		});

		Group grpProject = new Group(container, SWT.NONE);
		grpProject.setLayoutData(new GridData(GridData.FILL_BOTH));
		grpProject.setLayout(new GridLayout(1, false));
		grpProject.setText("Project");
		
		lblProjects = new Label(grpProject, SWT.NONE);
		lblProjects.setText("Select a Project:");
		
		lstProjects = new List(grpProject, SWT.BORDER | SWT.V_SCROLL);
		lstProjects.setLayoutData(new GridData(GridData.FILL_BOTH));
		
		for(Project project: projectMan.getProjects()){
			lstProjects.add(project.getName());
		}
		
		lstProjects.addSelectionListener(new SelectionListener(){

			/**
			 * Set which Project is selected.
			 */
			@Override
			public void widgetSelected(SelectionEvent e) {
				
				Project selected = projectMan.getProject(lstProjects.getSelection()[0]);
				wizardData.setProject(selected);
				
				checkIfPageComplete();
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {}
		});
		
		Composite radioContainer = new Composite(grpProject, SWT.NONE);
		radioContainer.setLayout(new GridLayout(2, true));
		radioContainer.setLayoutData(new GridData(GridData.FILL_BOTH));
		
		radExistingProject = new Button(radioContainer, SWT.RADIO);
		radExistingProject.setText("Use existing Project.");
		radExistingProject.setSelection(true);
		radExistingProject.addSelectionListener(new SelectionListener(){

			/**
			 * Re-enable the Project list.
			 */
			@Override
			public void widgetSelected(SelectionEvent e) {
				lstProjects.setEnabled(true);
				wizardData.setCreateNewProject(false);
				
				checkIfPageComplete();
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {}
		});
		
		radNewProject = new Button(radioContainer, SWT.RADIO);
		radNewProject.setText("Create new Project.");
		radNewProject.addSelectionListener(new SelectionListener(){

			/**
			 * Disable the Project list and set that a new Project should be
			 * created.
			 */
			@Override
			public void widgetSelected(SelectionEvent e) {
				lstProjects.setEnabled(false);
				wizardData.setCreateNewProject(true);
				wizardData.setProject(null);
				
				checkIfPageComplete();
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {}
		});
		
		setControl(container);
		setPageComplete(false);
	}
	
	/**
	 * Sets whether or not the page is complete.
	 */
	private void checkIfPageComplete(){
		if(wizardData.isReadyForFiles()){
			setPageComplete(true);
		}
		else{
			setPageComplete(false);
		}
	}
}
