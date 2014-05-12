package LinGUIne.wizards;

import java.util.TreeSet;

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

import LinGUIne.model.Project;
import LinGUIne.model.ProjectGroup;
import LinGUIne.model.ProjectManager;

/**
 * Page for the user to select into which Project they want to put the new File.
 * 
 * @author Kyle Mullins
 */
public class NewFileWizardSelectProjectPage extends WizardPage {

	private ProjectManager projectMan;
	private NewFileData wizardData;
	
	private List lstProjects;
	private Label lblProjects;
	private List lstGroups;
	private Label lblGroups;
	
	public NewFileWizardSelectProjectPage(NewFileData data,
			ProjectManager projects) {
		
		super("New File Wizard");
		setTitle("New File Wizard");
		setDescription("Select where the new File should go.");
		
		wizardData = data;
		projectMan = projects;
	}

	@Override
	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NONE);
		container.setLayout(new GridLayout(2, true));
		
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
					Project selected = projectMan.getProject(lstProjects.
							getSelection()[0]);
					wizardData.setChosenProject(selected);
					
					populateGroupList();
					setPageComplete(false);
				}
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {}
		});
		
		Group grpGroup = new Group(container, SWT.NONE);
		grpGroup.setLayoutData(new GridData(GridData.FILL_BOTH));
		grpGroup.setLayout(new GridLayout(1, false));
		grpGroup.setText("Group");
		
		lblGroups = new Label(grpGroup, SWT.NONE);
		lblGroups.setText("Select a Group for the File to go into:");
		
		lstGroups = new List(grpGroup, SWT.BORDER | SWT.V_SCROLL |
				SWT.H_SCROLL);
		lstGroups.setLayoutData(new GridData(GridData.FILL_BOTH));
		lstGroups.addSelectionListener(new SelectionListener(){
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(lstGroups.getSelectionCount() != 0){
					Project destProject = wizardData.getChosenProject();
					String groupName = lstGroups.getSelection()[0];
					
					if(groupName.contains("/")){
						groupName = groupName.substring(
								groupName.lastIndexOf("/") + 1);
					}
					
					wizardData.setParentGroup(destProject.getGroup(groupName));
					setPageComplete(true);
				}
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {}
		});
		
		setControl(container);
		setPageComplete(false);
		
		//If there is a Project already selected (in wizardData), select it
		if(wizardData.getChosenProject() != null){
			lstProjects.setSelection(
					new String[]{wizardData.getChosenProject().getName()});
			populateGroupList();
		}
	}
	
	/**
	 * Updates the list of ProjectGroups.
	 */
	private void populateGroupList(){
		TreeSet<String> sortedGroups = new TreeSet<String>();
		ProjectGroup projDataGroup = wizardData.getChosenProject().
				getGroup("Project Data");
		
		sortedGroups.add(projDataGroup.getDisplayGroupPath());
		
		addChildGroups(projDataGroup, sortedGroups);
		
		lstGroups.removeAll();
		
		for(String groupPath: sortedGroups){
			lstGroups.add(groupPath);
		}
	}
	
	/**
	 * Recursively adds child Groups to the list of ProjectGroups.
	 */
	private void addChildGroups(ProjectGroup parentGroup,
			TreeSet<String> sortedGroups){
		
		for(ProjectGroup group: parentGroup.getChildren()){
			sortedGroups.add(group.getDisplayGroupPath());
			
			addChildGroups(group, sortedGroups);
		}
	}
}
