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

public class NewGroupWizardNamePage extends WizardPage {

	private Label lblGroupName;
	private Text txtGroupName;
	
	private NewGroupData wizardData;
	
	public NewGroupWizardNamePage(NewGroupData data){
		super("New Group Wizard");
		
		wizardData = data;
	}
	
	@Override
	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NONE);
		container.setLayout(new GridLayout(2, false));
		
		lblGroupName = new Label(container, SWT.NONE);
		lblGroupName.setText("Enter name for the new Group.");
		
		txtGroupName = new Text(container, SWT.BORDER | SWT.SINGLE);
		txtGroupName.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		txtGroupName.addKeyListener(new KeyListener(){
			@Override
			public void keyPressed(KeyEvent e) {}

			@Override
			public void keyReleased(KeyEvent e) {
				if(isGroupNameValid(txtGroupName.getText())){
					wizardData.setGroupName(txtGroupName.getText());
					setPageComplete(true);
				}
				else{
					setPageComplete(false);
				}
			}
		});
		
		setPageComplete(false);
	}
	
	public boolean isGroupNameValid(String newGroupName){
		boolean isValid = true;
		String errorMessage = null;
				
		if(newGroupName.length() == 0){
			errorMessage = "Group name must not have length 0!";
			isValid = false;
		}
		else if(wizardData.getDestProject().containsGroup(newGroupName)){
			errorMessage = "A Group with that name already exists!";
			isValid = false;
		}
		
		setErrorMessage(errorMessage);
		
		return isValid;
	}
}