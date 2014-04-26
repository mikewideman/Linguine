package LinGUIne.wizards;

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

import LinGUIne.model.IProjectData;
import LinGUIne.model.Project;
import LinGUIne.model.ProjectManager;
import LinGUIne.model.Result;

public class ExportFileWizardChooseResultPage extends WizardPage {

	private Label lblProjects;
	private List lstProjects;
	private Label lblResults;
	private List lstResults;
	
	private ExportFileData wizardData;
	private ProjectManager projectMan;
	
	public ExportFileWizardChooseResultPage(ExportFileData data,
			ProjectManager projects){
		super("Export Result Wizard");
		setTitle("Export Result Wizard");
		setMessage("Select the Result you wish to export.");
		
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
	    lblProjects.setText("Select from which Project to export the Result:");

	    lstProjects = new List(grpProjects, SWT.BORDER | SWT.V_SCROLL);
	    lstProjects.setLayoutData(new GridData(GridData.FILL_BOTH));
	    
	    for(Project project: projectMan.getProjects()){
	    	lstProjects.add(project.getName());
	    }
	    
	    lstProjects.addSelectionListener(new SelectionListener(){

	    	/**
	    	 * Sets which Project is currently selected and populates the 
	    	 * List of compatible Results in the Project.
	    	 */
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(lstProjects.getSelectionCount() != 0){
					Project selectedProject = projectMan.getProject(lstProjects.getSelection()[0]);
					wizardData.setProject(selectedProject);
					wizardData.setResult(null);
					
					updateFileList();
					lstResults.setEnabled(true);
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
	    
	    lblResults = new Label(grpFiles, SWT.NONE);
	    lblResults.setText("Select the Result you wish to export:");
	    
	    lstResults = new List(grpFiles, SWT.BORDER | SWT.V_SCROLL);
	    lstResults.setLayoutData(new GridData(GridData.FILL_BOTH));
	    lstResults.setEnabled(false);
	    lstResults.addSelectionListener(new SelectionListener(){

	    	/**
	    	 * Sets which Result is currently selected.
	    	 */
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(lstResults.getSelectionCount() != 0){
					Result selectedResult = (Result)wizardData.getChosenProject().
							getProjectData(lstResults.getSelection()[0]);

					wizardData.setResult(selectedResult);
					checkIfPageComplete();
				}
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {}
	    });
	    
	    setControl(container);
	    setPageComplete(false);
	}
	
	private void updateFileList(){
		lstResults.deselectAll();
		lstResults.removeAll();
		
		Collection<Class<? extends Result>> supportedTypes =
				wizardData.getChosenExporter().getSupportedSourceDataTypes();
		
		for(Result result: wizardData.getChosenProject().getResults()){
			if(supportedTypes.contains(result.getClass())){
				lstResults.add(result.getName());
			}
		}
		
		lstResults.update();
	}
	
	private void checkIfPageComplete(){
		setPageComplete(wizardData.getChosenProject() != null &&
				wizardData.getChosenResult() != null);
	}
}
