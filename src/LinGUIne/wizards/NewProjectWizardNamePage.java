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
import LinGUIne.model.ProjectManager;

/**
 * A WizardPage for the NewProjectWizard which prompts the user to input a name
 * for their new Project.
 * 
 * @author Kyle Mullins
 */
public class NewProjectWizardNamePage extends WizardPage {

	private Text txtName;
	private Label lblName;
	private Label lblError;
	
	private Project newProject;
	private ProjectManager projectMan;
	
	/**
	 * Creates a new instance of the page with the given empty Project object,
	 * and the given instance of ProjectManager.
	 * 
	 * @param newProj
	 * @param projects
	 */
	public NewProjectWizardNamePage(Project newProj, ProjectManager projects){
		super("New Project Wizard");
		setTitle("New Project Wizard");
		setDescription("Choose a name for the new project.");
		setControl(txtName);
		
		newProject = newProj;
		projectMan = projects;
	}
	
	/**
	 * Assembles the UI components for the page and registers a KeyListener.
	 */
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

	/**
	 * Returns whether or not the given name is a valid Project name. An empty
	 * name is invalid and Projects cannot have duplicate names.
	 * 
	 * @param name	The prospective Project name the user has entered.
	 * 
	 * @return	True iff the given name is valid, false otherwise.
	 */
	private boolean isProjectNameValid(String name){
		String errorText = "";
		boolean isValid = true;
		
		if(name.length() == 0){
			errorText = "Project name must not have length 0!";
			isValid = false;
		}
		else if(projectMan.containsProject(name)){
			errorText = "A Project with that name already exists!";
			isValid = false;
		}
		
		lblError.setText(errorText);
		lblError.getParent().layout();
		
		return isValid;
	}
}
