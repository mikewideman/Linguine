package LinGUIne.wizards;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.program.Program;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Link;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;

import LinGUIne.utilities.FileUtils;

/**
 * A wizard page for populating the fields necessary to execute a twitter query with the Twitter
 * plugin. 
 * @author Pete
 *
 */
public class TwitDataWizardChooseSearchPage extends WizardPage {
	
	
	private ImportFileData wizardData;
	private Composite container;
	private Text txtSearch;
	private Text txtName;
	private Text txtCount;
	private Text consumerKey;
	private Text consumerSecret;
	private Text accessTok;
	private Text accessTokSecret;
	
	private final static String QUERY_TOO_SHORT = "Invalid Query Input";
	
	/**
	 * Constructs the wizard page, sets title as well as populates the internetSourceDetails array
	 * of the wizard data.
	 * @param wData the project data we are performing an import on
	 */
	public TwitDataWizardChooseSearchPage(ImportFileData wData){
		super("TwitDataWizard");
		setTitle("Twitter Data Wizard");
		setControl(container);
		wizardData = wData;
		//So there's no index exceptions in listeners or plugin
		wizardData.getInternetSourceDetails()
			.addAll(Arrays.asList("","","","","",""));
		
		
	}
	
	/**
	 * Creates the Twitter import query field forms.
	 */
	@Override
	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		container.setLayout(layout);
		
		Label lblQuery = new Label(container, SWT.NONE);
		lblQuery.setText("Search Twitter for project Data! Search by @Username"
				+ " or #hashtag.");
		
		txtSearch = new Text(container, SWT.BORDER | SWT.SINGLE);
		txtSearch.setText("");
		txtSearch.addKeyListener(new KeyListener(){

			@Override
			public void keyPressed(KeyEvent e) {}

			@Override
			public void keyReleased(KeyEvent e) {
				if(isValidQueryInput()){
						wizardData.getInternetSourceDetails()
						.set(0, txtSearch.getText());
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
		
		Label lblCount = new Label(container, SWT.NONE);
		lblCount.setText("Enter the number of tweets(maximum of 100) you would "
				+ "like to receive.");
		txtCount = new Text(container, SWT.BORDER | SWT.SINGLE);
		txtCount.setLayoutData(grid);
		txtCount.addModifyListener(new ModifyListener(){

			@Override
			public void modifyText(ModifyEvent e) {
					

					wizardData.getInternetSourceDetails().set(1, txtCount.getText());
				
			}
			
		});
		
		txtCount.setText("25");
		
		Label instructions = new Label(container, SWT.NONE);
		instructions.setText("Please reference the Twitter Import User Guide to"
				+ " help generate your credentials.");
		GridData instructionsData = new GridData();
		instructionsData.horizontalSpan = 2;
		instructions.setLayoutData(instructionsData);
		
		Label lblConKey = new Label(container, SWT.NONE);
		lblConKey.setText("Enter Consumer Key");
		consumerKey = new Text(container, SWT.BORDER | SWT.SINGLE);
		consumerKey.setText("");
		consumerKey.setLayoutData(grid);
		consumerKey.addModifyListener(new ModifyListener(){

			@Override
			public void modifyText(ModifyEvent e) {
				wizardData.getInternetSourceDetails().set(2, consumerKey.getText());
				
			}
			
		});
		Label lblConSecKey = new Label(container, SWT.NONE);
		lblConSecKey.setText("Enter Consumer Secret Key");
		consumerSecret = new Text(container, SWT.BORDER | SWT.SINGLE);
		consumerSecret.setText("");
		consumerSecret.setLayoutData(grid);
		consumerSecret.addModifyListener(new ModifyListener(){

			@Override
			public void modifyText(ModifyEvent e) {
				wizardData.getInternetSourceDetails().set(3, consumerSecret.getText());
				
			}
			
		});
		Label lblAccessTok = new Label(container, SWT.NONE);
		lblAccessTok.setText("Enter Access Token");
		accessTok = new Text(container, SWT.BORDER | SWT.SINGLE);
		accessTok.setText("");
		accessTok.setLayoutData(grid);
		accessTok.addModifyListener(new ModifyListener(){

			@Override
			public void modifyText(ModifyEvent e) {
				wizardData.getInternetSourceDetails().set(4, accessTok.getText());
				
			}
			
		});
		Label lblAccessTokSecret = new Label(container, SWT.NONE);
		lblAccessTokSecret.setText("Enter Access Token Secret");
		accessTokSecret = new Text(container, SWT.BORDER | SWT.SINGLE);
		accessTokSecret.setText("");
		accessTokSecret.setLayoutData(grid);
		accessTokSecret.addModifyListener(new ModifyListener(){

			@Override
			public void modifyText(ModifyEvent e) {
				wizardData.getInternetSourceDetails().set(5, accessTokSecret.getText());
				
			}
			
		});
		setControl(container);
		setPageComplete(isPageComplete());
	}
	/**
	 * Checks to see that what is put in the search text field is valid
	 * @return false if the text is empty or of length one, returns true otherwise
	 */
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
	/**
	 * Checks that the file name chosen for the import doesn't already exist in the project
	 * @return True if the chosen name is valid, false otherwise
	 */
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
	
	/**
	 * Returns whether the wizard page is complete
	 */
	public boolean isPageComplete(){
		if(isValidQueryInput() && isValidName()){
			return true;
		}
		else{
			return false;
		}
	}
	/**
	 * Determines whether the oAuth credentials are filled out
	 * @return false if any of the oAuth fields are missing, true if all fields are filled.
	 */
	public boolean isCredentialFilled(){
		if(consumerKey.getText().isEmpty() || consumerSecret.getText().isEmpty() || accessTok.getText().isEmpty() 
				|| accessTokSecret.getText().isEmpty()){
			
			return false;
		}
		else{
			return true;
		}
	}
	

}
