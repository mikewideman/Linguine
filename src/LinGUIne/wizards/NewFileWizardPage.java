package LinGUIne.wizards;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Text;

import LinGUIne.model.Project;
import LinGUIne.model.ProjectManager;
import LinGUIne.utilities.FileUtils;

public class NewFileWizardPage extends WizardPage {

	private ProjectManager projectMan;
	private NewFileData wizardData;
	
	private List lstProjects;
	private Label lblProjects;
	private Text txtFileName;
	private Label lblFileName;
	
	protected NewFileWizardPage(NewFileData data, ProjectManager projects) {
		super("New File Wizard");
		setTitle("New File Wizard");
		setDescription("Create a new File.");
		
		wizardData = data;
		projectMan = projects;
	}

	@Override
	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		container.setLayout(layout);
		
		Group grpProject = new Group(container, SWT.NONE);
		grpProject.setLayoutData(new GridData(GridData.FILL_BOTH));
		grpProject.setLayout(new GridLayout(1, false));
		grpProject.setText("Project");
		
		lblProjects = new Label(grpProject, SWT.NONE);
		lblProjects.setText("Select a Project for the File to go into:");
		
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
				if(lstProjects.getSelectionCount() != 0){
					Project selected = projectMan.getProject(lstProjects.getSelection()[0]);
					wizardData.setChosenProject(selected);
					
					checkIfPageComplete();
				}
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {}
		});
		
		Group grpFileName = new Group(container, SWT.NONE);
		grpFileName.setLayoutData(new GridData(GridData.FILL_BOTH));
		grpFileName.setLayout(new GridLayout(1, false));
		grpFileName.setText("File Name");
		
		lblFileName = new Label(grpFileName, SWT.NONE);
		lblFileName.setText("Enter a name for the new File:");
		
		txtFileName = new Text(grpFileName, SWT.BORDER);
		txtFileName.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		txtFileName.addModifyListener(new ModifyListener(){
			@Override
			public void modifyText(ModifyEvent e) {
				wizardData.setNewFileName(txtFileName.getText());
				
				checkIfPageComplete();
			}
		});
		
		setControl(lstProjects);
		setPageComplete(false);
	}
	
	private void checkIfPageComplete(){
		String errorMessage = null;
		boolean pageComplete = false;
		
		if(wizardData.isComplete()){
			String fileName = wizardData.getNewFileName();
			
			//Add a default extension if the file name doesn't already have one
			if(fileName.lastIndexOf(".") == -1){
				fileName += ".txt";
				wizardData.setNewFileName(fileName);
			}
			
			if(!wizardData.getChosenProject().containsProjectData(fileName)){
				if(FileUtils.isValidFileName(fileName)){
					pageComplete = true;
				}
				else{
					errorMessage = "The file name is invalid!";
				}
			}
			else{
				errorMessage = "A file with that name already exists!";
			}
		}

		setErrorMessage(errorMessage);
		setPageComplete(pageComplete);
	}
}
