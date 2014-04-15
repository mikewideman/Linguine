package LinGUIne.wizards;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import LinGUIne.utilities.FileUtils;

public class NewFileWizardNamePage extends WizardPage {

	private Label lblFileName;
	private Text txtFileName;
	
	private NewFileData wizardData;
	
	public NewFileWizardNamePage(NewFileData data) {
		super("New File Wizard");
		setTitle("New File Wizard");
		setDescription("Choose a name for the new File.");
		
		wizardData = data;
	}
	
	@Override
	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NONE);
		container.setLayout(new GridLayout(2, false));
		
		lblFileName = new Label(container, SWT.NONE);
		lblFileName.setText("Enter name for the new File:");
		
		txtFileName = new Text(container, SWT.BORDER | SWT.SINGLE);
		txtFileName.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		txtFileName.addModifyListener(new ModifyListener(){
			@Override
			public void modifyText(ModifyEvent e) {
				wizardData.setNewFileName(txtFileName.getText());
				
				checkIfPageComplete();
			}
		});
		
		setControl(container);
		setPageComplete(false);
	}
	
	private void checkIfPageComplete(){
		String errorMessage = null;
		boolean pageComplete = false;
		
		if(wizardData.isComplete()){
			String fileName = wizardData.getNewFileName();
			
			if(!fileName.isEmpty()){
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
			else{
				errorMessage = "File name must not have length 0!";
			}
		}

		setErrorMessage(errorMessage);
		setPageComplete(pageComplete);
	}
}
