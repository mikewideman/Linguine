package LinGUIne.wizards;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import LinGUIne.utilities.FileUtils;

public class TwitDataWizardChooseSearchPage extends WizardPage {
	
	private ImportFileData wizardData;
	private Composite container;
	private Text txtSearch;
	private Text txtName;
	
	private final static String QUERY_TOO_SHORT = "Invalid Query Input";
	
	public TwitDataWizardChooseSearchPage(ImportFileData wData){
		super("TwitDataWizard");
		setTitle("Twitter Data Wizard");
		setControl(container);
		wizardData = wData;
		
		
	}

	@Override
	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		container.setLayout(layout);
		
		Label lblQuery = new Label(container, SWT.NONE);
		lblQuery.setText("Search Twitter for project Data! Search by @Username or #hashtag.");
		
		txtSearch = new Text(container, SWT.BORDER | SWT.SINGLE);
		txtSearch.setText("");
		txtSearch.addKeyListener(new KeyListener(){

			@Override
			public void keyPressed(KeyEvent e) {}

			@Override
			public void keyReleased(KeyEvent e) {
				if(isValidQueryInput()){
					wizardData.setInternetSourceQuery(txtSearch.getText());
				}
				setPageComplete(isPageComplete());
			}
		});
		
		GridData grid = new GridData(GridData.FILL_HORIZONTAL);
		txtSearch.setLayoutData(grid);
		
		Label lblName = new Label(container, SWT.NONE);
		lblName.setText("Enter a file name for your plaintext Twitter data.");
		txtName = new Text(container, SWT.BORDER | SWT.SINGLE);
		txtName.setText("");
		txtName.setLayoutData(grid);
		txtName.addModifyListener(new ModifyListener(){

			@Override
			public void modifyText(ModifyEvent e) {
				if(isValidName()){
					wizardData.setInternetSourceFileName(txtName.getText());
				}
				setPageComplete(isPageComplete());
				
			}
			
		});
		
		
		setControl(container);
		setPageComplete(isPageComplete());
		
	}
	
	private boolean isValidQueryInput(){
		boolean isValid = true;
		String errorMessage = null;
		if(txtSearch.getText().isEmpty()){return false;}
		//Searching for a user
		if(txtSearch.getText().length() == 1){
			errorMessage = QUERY_TOO_SHORT;
			isValid = false;
		}
		
		setErrorMessage(errorMessage);
		
		return isValid;
	}
	
	private boolean isValidName(){
		String errorMessage = null;
		boolean isValid = false;
		
		
		if(wizardData.isReadyForFiles() && !txtName.getText().isEmpty()){
			String fileName = txtName.getText();
			if(!wizardData.getChosenProject().containsProjectData(fileName)){
				if(FileUtils.isValidFileName(fileName)){
					isValid = true;
				}
				else{
					errorMessage = "The file name is invalid!";
					isValid = false;
				}
			}
			else{
				errorMessage = "A file with that name already exists!";
				isValid = false;
			}
		}

		setErrorMessage(errorMessage);
		return isValid;
	}
	
	
	public boolean isPageComplete(){
		if(isValidQueryInput() && isValidName()){
			return true;
		}
		else{
			return false;
		}
	}
}
