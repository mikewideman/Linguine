package LinGUIne.wizards;

import java.util.Collection;

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
 * Page for the user to select which ProjectData's Annotations they would like
 * to export.
 * 
 * @author Kyle Mullins
 */
public class ExportAnnotationWizardChooseAnnotationPage extends WizardPage {

	private Label lblProjects;
	private List lstProjects;
	private Label lblData;
	private List lstData;
	
	private ExportAnnotationData wizardData;
	private ProjectManager projectMan;
	
	public ExportAnnotationWizardChooseAnnotationPage(ExportAnnotationData data,
			ProjectManager projects){
		super("Export Annotation Wizard");
		setTitle("Export Annotation Wizard");
		setMessage("Select the Data for which you wish to export the Annotations.");
		
		wizardData = data;
		projectMan = projects;
	}
	
	@Override
	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NONE);
	    container.setLayout(new GridLayout(2, false));
	    
	    Group grpProjects = new Group(container, SWT.NONE);
	    grpProjects.setLayout(new GridLayout(1, false));
	    grpProjects.setLayoutData(new GridData(GridData.FILL_BOTH));
	    grpProjects.setText("Project");
	    
	    lblProjects = new Label(grpProjects, SWT.NONE);
	    lblProjects.setText("Select from which Project to export the Annotations:");

	    lstProjects = new List(grpProjects, SWT.BORDER | SWT.V_SCROLL);
	    lstProjects.setLayoutData(new GridData(GridData.FILL_BOTH));
	    
	    for(Project project: projectMan.getProjects()){
	    	lstProjects.add(project.getName());
	    }
	    
	    lstProjects.addSelectionListener(new SelectionListener(){

	    	/**
	    	 * Sets which Project is currently selected and populates the 
	    	 * List of compatible Annotated Data in the Project.
	    	 */
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(lstProjects.getSelectionCount() != 0){
					Project selectedProject = projectMan.getProject(lstProjects.getSelection()[0]);
					wizardData.setProject(selectedProject);
					wizardData.setAnnotatedData(null);
					
					updateFileList();
					lstData.setEnabled(true);
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
	    
	    lblData = new Label(grpFiles, SWT.NONE);
	    lblData.setText("Select the Data for which you wish to export Annotations:");
	    
	    lstData = new List(grpFiles, SWT.BORDER | SWT.V_SCROLL);
	    lstData.setLayoutData(new GridData(GridData.FILL_BOTH));
	    lstData.setEnabled(false);
	    lstData.addSelectionListener(new SelectionListener(){

	    	/**
	    	 * Sets which Annotated Data is currently selected.
	    	 */
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(lstData.getSelectionCount() != 0){
					IProjectData selectedResult = wizardData.getChosenProject().
							getProjectData(lstData.getSelection()[0]);

					wizardData.setAnnotatedData(selectedResult);
					checkIfPageComplete();
				}
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {}
	    });
	    
	    setControl(container);
	    setPageComplete(false);
	}
	
	/**
	 * Updates the ProjectData list.
	 */
	private void updateFileList(){
		lstData.deselectAll();
		lstData.removeAll();
		
		Collection<Class<? extends IProjectData>> supportedTypes =
				wizardData.getChosenExporter().getSupportedSourceDataTypes();
		
		for(IProjectData projData: wizardData.getChosenProject().
				getOriginalData()){
			
			if(wizardData.getChosenProject().isAnnotated(projData) &&
					supportedTypes.contains(projData.getClass())){
				
				lstData.add(projData.getName());
			}
		}
		
		lstData.update();
	}
	
	/**
	 * Sets the page as complete if a Project and ProjectData have both been
	 * chosen.
	 */
	private void checkIfPageComplete(){
		setPageComplete(wizardData.getChosenProject() != null &&
				wizardData.getChosenAnnotatedData() != null);
	}
}
