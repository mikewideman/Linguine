package LinGUIne.parts.basic;

import java.util.LinkedList;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;

import org.eclipse.core.runtime.Platform;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.di.Focus;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Text;


import LinGUIne.model.IProjectData;
import LinGUIne.model.Project;
import LinGUIne.model.ProjectManager;

public class BasicMainPart {
	
	private CTabFolder tabFolder;
	private Text txtName;
	private ProjectManager basicProjectMan;
	private Project newProject;
	
	private CTabItem tab1;
	private CTabItem tab2;
	private CTabItem tab3;
	private CTabItem tab4;
	

	

	
	@PostConstruct
	public void createComposite(Composite parent){
		basicProjectMan = new ProjectManager(Platform.getLocation());
		basicProjectMan.loadProjects();
		tabFolder = new CTabFolder(parent,SWT.NONE);
		tabFolder.setTabHeight(10);
//		CTabItem newTab = new CTabItem(tabFolder,SWT.NONE);
//		newTab.setText("Select A Project");
//		tabFolder.setSelection(newTab);
//		StyledText textArea = new StyledText(tabFolder,SWT.V_SCROLL|SWT.H_SCROLL);
//		newTab.setControl(textArea);
		
//	    Button button = new Button(tabFolder, SWT.BOTTOM);
//	    button.setSize(300, 400);
//	    button.setText("This is a button.");
	    newProject = new Project();
	    CTabItem tabItem1 = new CTabItem(tabFolder, SWT.NONE);
	    tabItem1.setText("Select a Project ->");
	    Composite tabComp = new Composite(tabFolder,SWT.CHECK);
	    GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		layout.marginTop = 15;
		tabComp.setLayout(layout);
	    Label projLabel = new Label(tabComp, SWT.NONE);
	    projLabel.setText("Enter name for New Project");
		txtName = new Text(tabComp, SWT.BORDER | SWT.SINGLE);
		txtName.setText("");
		Label existingProjLabel = new Label(tabComp, SWT.NONE);
		existingProjLabel.setText("Select from list of existing projects");
		
		
		final Button nButton = new Button(tabComp, SWT.PUSH);
	    nButton.setText("Next");
	    GridData buttonData = new GridData(SWT.CENTER, SWT.BOTTOM, false, false, 1, 1);
	    buttonData.verticalIndent = 50;
	    nButton.setLayoutData(new GridData(SWT.RIGHT, SWT.BOTTOM, false, false, 1, 1));
	    nButton.addSelectionListener(new SelectionAdapter() {
	        @Override
	        public void widgetSelected(SelectionEvent e) {
	            openSelectFileTab();
	        }
	    });
		txtName.addKeyListener(new KeyListener(){

			@Override
			public void keyPressed(KeyEvent e) {}

			@Override
			public void keyReleased(KeyEvent e) {
				if(isProjectNameValid(txtName.getText())){
					newProject.setName(txtName.getText());
				}
				else{
					nButton.setGrayed(false);
				}
			}
		});
		GridData grid = new GridData(800, 25);
		txtName.setLayoutData(grid);
	    tabItem1.setControl(tabComp);
	    tabFolder.setSelection(tabItem1);
		
		

	}
	
	
	@Focus
	public void onFocus(){
		
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
		boolean isValid = true;
				
		if(name.length() == 0){
			isValid = false;
		}
		else if(basicProjectMan.containsProject(name)){
			isValid = false;
		}
		
		
		return isValid;
	}
	
	public void openSelectFileTab(){
		if(tab2 == null){
			Label lblFiles;
			final List lstFiles;
			CTabItem selectFileTab = new CTabItem(tabFolder, SWT.NONE);
			selectFileTab.setText("Select a File ->");
			Composite container = new Composite(tabFolder, SWT.NONE);
			 GridLayout layout = new GridLayout();
			    layout.numColumns = 2;
			    layout.marginHeight = 15;
			    container.setLayout(layout);
			    
			    Group grpFiles = new Group(container, SWT.NONE);
			    grpFiles.setLayout(new GridLayout(1, false));
			    grpFiles.setLayoutData(new GridData(800, 300));
			    grpFiles.setText("Files");
			    
			    lblFiles = new Label(grpFiles, SWT.NONE);
			    lblFiles.setText("Select the Files on which to run the Analysis:");
			    
			    lstFiles = new List(grpFiles, SWT.BORDER | SWT.V_SCROLL | SWT.MULTI);
			    lstFiles.setLayoutData(new GridData(GridData.FILL_BOTH));
			    lstFiles.setEnabled(false);
			    lstFiles.addSelectionListener(new SelectionListener(){

			    	/**
			    	 * Sets which Project Data are currently selected.
			    	 */
					@Override
					public void widgetSelected(SelectionEvent e) {
						LinkedList<IProjectData> selectedProjectData =
								new LinkedList<IProjectData>();
						
						for(String dataName: lstFiles.getSelection()){
							//Can't show files yet
//							selectedProjectData.add(wizardData.getChosenProject().
//									getProjectData(dataName));
						}
						
//						wizardData.setChosenProjectData(selectedProjectData);
//						checkIfPageComplete();
					}

					@Override
					public void widgetDefaultSelected(SelectionEvent e) {}
			    });
			selectFileTab.setControl(container);
			Button nButtonFile = new Button(container, SWT.NONE);
			nButtonFile.setText("Next");
			nButtonFile.addSelectionListener(new SelectionAdapter() {
		        @Override
		        public void widgetSelected(SelectionEvent e) {
		            openAnalysisTab();
		        }
		    });
			tab2 = selectFileTab;
		}
		tabFolder.setSelection(tab2);
		
	}
	public void openAnalysisTab(){
		if(tab3 == null){
			CTabItem analysisTab = new CTabItem(tabFolder, SWT.NONE);
			analysisTab.setText("Select an Analysis ->");
			Composite container = new Composite(tabFolder, SWT.NONE);
			 GridLayout layout = new GridLayout();
			    layout.numColumns = 2;
			    layout.marginTop = 15;
			    container.setLayout(layout);
			CTabFolder innerFolder = new CTabFolder(container, SWT.NONE);
			CTabItem basicAnalysis = new CTabItem(innerFolder, SWT.NONE);
			basicAnalysis.setText("Basic Analysis Set");
			CTabItem advancedAnalysis = new CTabItem(innerFolder,SWT.NONE);
			advancedAnalysis.setText("Advanced Analysis Set");
			analysisTab.setControl(container);
			tab3 = analysisTab;
			innerFolder.setLayoutData(new GridData(800, 300));
			Button analysisNButton = new Button(container, SWT.NONE);
			analysisNButton.setText("Next");
			analysisNButton.addSelectionListener(new SelectionAdapter() {
		        @Override
		        public void widgetSelected(SelectionEvent e) {
		            openVisualizationTab();		        }
		    });
			
			
			
		}

		tabFolder.setSelection(tab3);
	}
	
	public void openVisualizationTab(){
		if(tab4 == null){
			CTabItem visualizationSelection = new CTabItem(tabFolder, SWT.NONE);
			visualizationSelection.setText("Select Visualization");
			tab4 = visualizationSelection;
		}
		tabFolder.setSelection(tab4);
	}
	
	
 
}
