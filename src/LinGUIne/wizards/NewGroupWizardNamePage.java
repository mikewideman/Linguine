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

/**
 * Page for the user to choose the name for the new ProjectGroup.
 * 
 * @author Kyle Mullins
 */
public class NewGroupWizardNamePage extends WizardPage {

	private Label lblGroupName;
	private Text txtGroupName;
	
	private NewGroupData wizardData;
	
	public NewGroupWizardNamePage(NewGroupData data){
		super("New Folder Wizard");
		setTitle("New Folder Wizard");
		setDescription("Choose a name for the new Folder.");
		
		wizardData = data;
	}
	
	@Override
	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NONE);
		container.setLayout(new GridLayout(2, false));
		
		lblGroupName = new Label(container, SWT.NONE);
		lblGroupName.setText("Enter name for the new Folder:");
		
		txtGroupName = new Text(container, SWT.BORDER | SWT.SINGLE);
		txtGroupName.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		txtGroupName.addModifyListener(new ModifyListener(){
			@Override
			public void modifyText(ModifyEvent e) {
				if(isGroupNameValid(txtGroupName.getText())){
					wizardData.setGroupName(txtGroupName.getText());
					setPageComplete(true);
				}
				else{
					setPageComplete(false);
				}
			}
		});
		
		setControl(container);
		setPageComplete(false);
	}
	
	/**
	 * Validates the chosen name for the new ProjectGroup.
	 * 
	 * @param newGroupName	The currently chosen name for the new Group.
	 * 
	 * @return	True iff the name is valid, false otherwise.
	 */
	public boolean isGroupNameValid(String newGroupName){
		boolean isValid = true;
		String errorMessage = null;
				
		if(newGroupName.length() == 0){
			errorMessage = "Folder name must not have length 0!";
			isValid = false;
		}
		else if(wizardData.getDestProject().containsGroup(newGroupName)){
			errorMessage = "A Folder with that name already exists!";
			isValid = false;
		}
		
		setErrorMessage(errorMessage);
		
		return isValid;
	}
}
