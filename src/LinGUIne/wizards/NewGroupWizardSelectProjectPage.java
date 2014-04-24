package LinGUIne.wizards;

import java.util.TreeSet;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;

import LinGUIne.model.Project;
import LinGUIne.model.ProjectGroup;
import LinGUIne.model.ProjectManager;
import LinGUIne.model.RootProjectGroup;

public class NewGroupWizardSelectProjectPage extends WizardPage {

	private List lstProjects;
	private List lstGroups;
	
	private ProjectManager projectMan;
	private NewGroupData wizardData;
	
	public NewGroupWizardSelectProjectPage(ProjectManager projects,
			NewGroupData data){
		super("New Folder Wizard");
		setTitle("New Folder Wizard");
		setDescription("Select where the new Folder should go.");
		
		projectMan = projects;
		wizardData = data;
	}
	
	@Override
	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NONE);
		container.setLayout(new GridLayout(2, true));
		
		Label lblProjects = new Label(container, SWT.NONE);
		lblProjects.setText("Select the Project to put the Folder in:");
		
		Label lblGroups = new Label(container, SWT.NONE);
		lblGroups.setText("Select the new Folder's parent Folder:");
		
		lstProjects = new List(container, SWT.BORDER | SWT.V_SCROLL | 
				SWT.H_SCROLL);
		lstProjects.setLayoutData(new GridData(GridData.FILL_BOTH));
		lstProjects.addSelectionListener(new SelectionListener(){
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(lstProjects.getSelectionCount() != 0){
					String projName = lstProjects.getSelection()[0];
					
					wizardData.setDestProject(projectMan.getProject(projName));
					populateGroupList();
					setPageComplete(false);
				}
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {}
		});
		
		for(Project proj: projectMan.getProjects()){
			lstProjects.add(proj.getName());
		}
		
		lstGroups = new List(container, SWT.BORDER | SWT.V_SCROLL |
				SWT.H_SCROLL);
		lstGroups.setLayoutData(new GridData(GridData.FILL_BOTH));
		lstGroups.addSelectionListener(new SelectionListener(){
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(lstGroups.getSelectionCount() != 0){
					Project destProject = wizardData.getDestProject();
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
		if(wizardData.getDestProject() != null){
			lstProjects.setSelection(
					new String[]{wizardData.getDestProject().getName()});
			populateGroupList();
		}
	}
	
	private void populateGroupList(){
		TreeSet<String> sortedGroups = new TreeSet<String>();
		
		for(ProjectGroup group: wizardData.getDestProject().getGroups()){
			if(group instanceof RootProjectGroup){
				if(((RootProjectGroup)group).isHidden()){
					continue;
				}
			}
			
			sortedGroups.add(group.getDisplayGroupPath());
		}
		
		lstGroups.removeAll();
		
		for(String groupPath: sortedGroups){
			lstGroups.add(groupPath);
		}
	}
}
