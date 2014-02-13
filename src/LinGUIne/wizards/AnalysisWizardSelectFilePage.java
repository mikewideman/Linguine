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

import LinGUIne.model.IProjectData;
import LinGUIne.model.Project;
import LinGUIne.model.ProjectManager;

/**
 * Analysis Page object; this is the GUI components
 * for the first step in the Analysis Wizard. The user
 * will select files that he or she wishes to run the 
 * analyses on.
 * 
 * @author Matthew Talbot
 * @author Kyle Mullins
 */

public class AnalysisWizardSelectFilePage extends WizardPage{

	private Label lblProjects;
	private List lstProjects;
	private Label lblFiles;
	private List lstFiles;
	
	private ProjectManager projectMan;
	private AnalysisData wizardData;

	/**
	 * Straightforward constructor. Sets the title and the
	 * "directions" for the first page.
	 */
	public AnalysisWizardSelectFilePage(AnalysisData data,
			ProjectManager projects){
		
		super("Analysis Wizard");
		setTitle("Analysis Wizard - Step 1");
		setDescription("Select the file(s) you wish to analyze.");
		
		wizardData = data;
		projectMan = projects;
	}
	
	/**
	 * Actually set the content of the first page.
	 * Actual implementation of this pending content.
	 * 
	 * @param parent - the current content pane
	 */
	@Override
	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NONE);
	    GridLayout layout = new GridLayout();
	    layout.numColumns = 2;
	    container.setLayout(layout);
	    
	    Group grpProjects = new Group(container, SWT.NONE);
	    grpProjects.setLayout(new GridLayout(1, false));
	    grpProjects.setLayoutData(new GridData(GridData.FILL_BOTH));
	    grpProjects.setText("Project");
	    
	    lblProjects = new Label(grpProjects, SWT.NONE);
	    lblProjects.setText("Select from which Project to select files:");

	    lstProjects = new List(grpProjects, SWT.BORDER | SWT.V_SCROLL);
	    lstProjects.setLayoutData(new GridData(GridData.FILL_BOTH));
	    
	    for(Project project: projectMan.getProjects()){
	    	lstProjects.add(project.getName());
	    }
	    
	    lstProjects.addSelectionListener(new SelectionListener(){

	    	/**
	    	 * Sets which Project is currently selected and populates the 
	    	 * List of Project Data in the Project.
	    	 */
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(lstProjects.getSelectionCount() > 0){
					Project selectedProject = projectMan.getProject(lstProjects.getSelection()[0]);
					wizardData.setChosenProject(selectedProject);
					wizardData.setChosenProjectData(new LinkedList<IProjectData>());
					
					updateFileList();
					lstFiles.setEnabled(true);
					checkIfPageComplete();
				}
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {}
	    	
	    });
	    
	    Group grpFiles = new Group(container, SWT.NONE);
	    grpFiles.setLayout(new GridLayout(1, false));
	    grpFiles.setLayoutData(new GridData(GridData.FILL_BOTH));
	    grpFiles.setText("Files");
	    
	    lblFiles = new Label(grpFiles, SWT.NONE);
	    lblFiles.setText("Select the Files on which to run the Analysis:");
	    
	    lstFiles = new List(grpFiles, SWT.BORDER | SWT.V_SCROLL | SWT.MULTI);
	    lstFiles.setLayoutData(new GridData(GridData.FILL_BOTH));
	    lstFiles.setEnabled(false);
	    lstFiles.addSelectionListener(new SelectionListener(){

	    	/**
	    	 * Sets which Project Data are currently selected.
	    	 */
			@Override
			public void widgetSelected(SelectionEvent e) {
				LinkedList<IProjectData> selectedProjectData =
						new LinkedList<IProjectData>();
				
				for(String dataName: lstFiles.getSelection()){
					selectedProjectData.add(wizardData.getChosenProject().
							getProjectData(dataName));
				}
				
				wizardData.setChosenProjectData(selectedProjectData);
				checkIfPageComplete();
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {}
	    });
	    
	    // Required to avoid an error in the system
	    setControl(container);
	    setPageComplete(false);
	}
	
	/**
	 * Updates the contents of lstFiles.
	 */
	private void updateFileList(){
		lstFiles.deselectAll();
		lstFiles.removeAll();
		
		for(IProjectData projData: wizardData.getChosenProject().getOriginalData()){
			lstFiles.add(projData.getName());
		}
		
		lstFiles.update();
	}
	
	private void checkIfPageComplete(){
		if(wizardData.getChosenProject() != null &&
				!wizardData.getChosenProjectData().isEmpty()){
			
			setPageComplete(true);
		}
		else{
			setPageComplete(false);
		}
	}
}
