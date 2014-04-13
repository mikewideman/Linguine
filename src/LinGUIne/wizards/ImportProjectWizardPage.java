package LinGUIne.wizards;

import java.io.File;
import java.util.Collection;
import java.util.LinkedList;

import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;

import LinGUIne.model.Project;
import LinGUIne.model.ProjectManager;

public class ImportProjectWizardPage extends WizardPage {

	private ProjectManager projectMan;
	
	private Text txtProjectPath;
	private TableViewer tblProjectList;
	private Button chkSearchForNested;
	
	private File selectedDir;
	private LinkedList<Project> projectsToImport;
	
	public ImportProjectWizardPage(ProjectManager projects){
		super("Import Project Wizard");
		setTitle("Import Project Wizard");
		setDescription("Select an existing Project to import into the workspace.");
		
		projectMan = projects;
		projectsToImport = new LinkedList<Project>();
	}
	
	@Override
	public void createControl(Composite parent) {
		final Composite container = new Composite(parent, SWT.NONE);
		container.setLayout(new GridLayout(1, false));
		
		Label lblChooseProject = new Label(container, SWT.NONE);
		lblChooseProject.setText("Choose an existing Project to import into" +
				" the workspace.");
		
		Composite pathContainer = new Composite(container, SWT.NONE);
		pathContainer.setLayout(new GridLayout(2, false));
		pathContainer.setLayoutData(new GridData(GridData.FILL_BOTH));
		
		txtProjectPath = new Text(pathContainer, SWT.BORDER | SWT.SINGLE);
		txtProjectPath.setText("");
		txtProjectPath.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		txtProjectPath.addModifyListener(new ModifyListener(){
			@Override
			public void modifyText(ModifyEvent e) {
				File enteredPath = new File(txtProjectPath.getText());
				
				if(enteredPath.exists() && enteredPath.isDirectory()){
					selectedDir = enteredPath;
					updateProjectList(chkSearchForNested.getSelection());
				}
				else{
					selectedDir = null;
				}
			}
		});
		
		Button btnBrowse = new Button(pathContainer, SWT.NONE);
		btnBrowse.setText("Browse...");
		btnBrowse.addSelectionListener(new SelectionListener(){
			@Override
			public void widgetSelected(SelectionEvent e) {
				DirectoryDialog dirDialog = new DirectoryDialog(container.getShell(),
						SWT.NONE);
				
				txtProjectPath.setText(dirDialog.open());
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {}
		});
		
		Label lblProjectsInDir = new Label(container, SWT.NONE);
		lblProjectsInDir.setText("Projects:");
		
		tblProjectList = new TableViewer(container, SWT.BORDER | SWT.CHECK |
				SWT.V_SCROLL);
		tblProjectList.getTable().setLayoutData(new GridData(GridData.FILL_BOTH));
		tblProjectList.getTable().addSelectionListener(new SelectionListener(){
			@Override
			public void widgetSelected(SelectionEvent event) {
				if(event.detail == SWT.CHECK){
					TableItem item = (TableItem)event.item;
					
					if(item.getChecked()){
						projectsToImport.add((Project)item.getData());
					}
					else{
						projectsToImport.remove((Project)item.getData());
					}
					
					setPageComplete(!projectsToImport.isEmpty());
				}
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {}
		});
		
		chkSearchForNested = new Button(container, SWT.CHECK);
		chkSearchForNested.setText("Search for nested Projects");
		chkSearchForNested.addSelectionListener(new SelectionListener(){
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(selectedDir != null){
					updateProjectList(chkSearchForNested.getSelection());
				}
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {}
		});
		
		setControl(container);
		setPageComplete(false);
	}
	
	public Collection<Project> getProjectsToImport(){
		return projectsToImport;
	}
	
	private void updateProjectList(boolean searchForNested){
		LinkedList<File> projectFiles = new LinkedList<File>();
		
		findProjectFiles(selectedDir, searchForNested, projectFiles);
		tblProjectList.getTable().removeAll();
		
		for(File projectFile: projectFiles){
			
			boolean alreadyInWorkspace = false;
			
			for(Project proj: projectMan.getProjects()){
				if(projectFile.equals(proj.getProjectFile().toFile())){
					alreadyInWorkspace = true;
					break;
				}
			}
			
			if(!alreadyInWorkspace){
				Project foundProject = Project.createFromFile(projectFile);
				
				tblProjectList.add(foundProject);
			}
		}
	}
	
	private void findProjectFiles(File dir, boolean searchForNested,
			Collection<File> projectFiles){
		
		if(dir != null){
			for(File file: dir.listFiles()){
				if(file.isDirectory() && searchForNested){
					findProjectFiles(file, searchForNested, projectFiles);
				}
				else if(file.getName().equals("linguine.project")){
					projectFiles.add(file);
				}
			}
		}
	}
}
