package LinGUIne.wizards;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

public class TwitDataWizardChooseSearchPage extends WizardPage {
	
	private ImportFileData wizardData;
	private Composite container;
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
		
		Label lblName = new Label(container, SWT.NONE);
		lblName.setText("Search Twitter for project Data! Search by @Username, #hashtag, ");
		
		txtName = new Text(container, SWT.BORDER | SWT.SINGLE);
		txtName.setText("");
		txtName.addKeyListener(new KeyListener(){

			@Override
			public void keyPressed(KeyEvent e) {}

			@Override
			public void keyReleased(KeyEvent e) {
				if(isValidInput(txtName.getText())){
					setPageComplete(true);
				}
				else{
					setPageComplete(false);
				}
			}
		});
		
		GridData grid = new GridData(GridData.FILL_HORIZONTAL);
		txtName.setLayoutData(grid);
		
		setControl(container);
		setPageComplete(false);
		
	}
	
	private boolean isValidInput(String name){
		boolean isValid = true;
		String errorMessage = null;
		//Searching for a user
		if(name.length() == 1){
			errorMessage = QUERY_TOO_SHORT;
			isValid = false;
		}
		
		setErrorMessage(errorMessage);
		
		return isValid;
	}

}
