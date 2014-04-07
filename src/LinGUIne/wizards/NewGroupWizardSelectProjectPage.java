package LinGUIne.wizards;

import java.util.TreeSet;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.List;

import LinGUIne.model.Project;
import LinGUIne.model.ProjectGroup;
import LinGUIne.model.ProjectManager;

public class NewGroupWizardSelectProjectPage extends WizardPage {

	private List lstProjects;
	private List lstGroups;
	
	private ProjectManager projectMan;
	private NewGroupData wizardData;
	
	public NewGroupWizardSelectProjectPage(ProjectManager projects,
			NewGroupData data){
		super("New Group Wizard");
		
		projectMan = projects;
		wizardData = data;
	}
	
	@Override
	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NONE);
		container.setLayout(new GridLayout(2, true));
		
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
					
					//TODO: Ensure this works as expected
					if(groupName.contains("/")){
						groupName = groupName.substring(
								groupName.lastIndexOf("/"));
					}
					
					wizardData.setParentGroup(destProject.getGroup(groupName));
					setPageComplete(true);
				}
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {}
		});
		
		setPageComplete(false);
	}
	
	private void populateGroupList(){
		TreeSet<String> sortedGroups = new TreeSet<String>();
		
		for(ProjectGroup group: wizardData.getDestProject().getGroups()){
			sortedGroups.add(group.getGroupPath());
		}
		
		lstGroups.removeAll();
		
		for(String groupPath: sortedGroups){
			lstGroups.add(groupPath);
		}
	}
}
