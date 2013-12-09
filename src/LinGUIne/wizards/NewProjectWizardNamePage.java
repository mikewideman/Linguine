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

import LinGUIne.model.Project;

public class NewProjectWizardNamePage extends WizardPage {

	private Text txtName;
	private Label lblName;
	private Label lblError;
	
	private Project newProject;
	
	public NewProjectWizardNamePage(Project newProj){
		super("New Project Wizard");
		setTitle("New Project Wizard");
		setDescription("Choose a name for the new project.");
		setControl(txtName);
		
		newProject = newProj;
	}
	
	@Override
	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		container.setLayout(layout);
		
		lblName = new Label(container, SWT.NONE);
		lblName.setText("Enter a name for the new project.");
		
		txtName = new Text(container, SWT.BORDER | SWT.SINGLE);
		txtName.setText("");
		txtName.addKeyListener(new KeyListener(){

			@Override
			public void keyPressed(KeyEvent e) {}

			@Override
			public void keyReleased(KeyEvent e) {
				if(isProjectNameValid(txtName.getText())){
					setPageComplete(true);
					newProject.setName(txtName.getText());
				}
				else{
					setPageComplete(false);
				}
			}
		});
		
		new Label(container, SWT.NONE); //Empty grid cell
		
		lblError = new Label(container, SWT.NONE);
		lblError.setText("");
		lblError.setForeground(new org.eclipse.swt.graphics.Color(null, 255, 0, 0));
		
		GridData grid = new GridData(GridData.FILL_HORIZONTAL);
		txtName.setLayoutData(grid);
		
		setControl(container);
		setPageComplete(false);
	}

	private boolean isProjectNameValid(String name){
		String errorText = "";
		boolean isValid = true;
		
		if(name.length() == 0){
			errorText = "Project name must not have length 0!";
			isValid = false;
		}
		
		lblError.setText(errorText);
		lblError.getParent().layout();
		
		return isValid;
	}
}
