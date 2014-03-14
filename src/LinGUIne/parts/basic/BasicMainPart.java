package LinGUIne.parts.basic;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;

import org.eclipse.core.runtime.Platform;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.di.Focus;
import org.eclipse.e4.ui.model.application.MApplication;
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











import LinGUIne.extensions.IAnalysisPlugin;
import LinGUIne.model.IProjectData;
import LinGUIne.model.Project;
import LinGUIne.model.ProjectManager;
import LinGUIne.model.Result;
import LinGUIne.model.SoftwareModuleManager;
import LinGUIne.wizards.AnalysisData;

public class BasicMainPart {
	
	private CTabFolder tabFolder;
	private Text txtName;
//	private ProjectManager basicProjectMan;
//	private SoftwareModuleManager softwareModuleMan;
	private Project newProject;
	private Label lblProjects;
	private List lstProjects;
	private Label lblFiles;
	private List lstFiles;
	private HashMap<String,String> softModMap;
	private AnalysisData analysisData;
	private Label lblAnalyses;
	private List lstAnalyses;
	
	@Inject
	private MApplication application;
	
	@Inject
	private ProjectManager projectMan;
	
	@Inject
	private SoftwareModuleManager softwareModuleMan;
	
	private CTabItem projectTab;
	private CTabItem fileTab;
	private CTabItem analysisTab;
	private CTabItem visualizationTab;
	

	

	
	@PostConstruct
	public void createComposite(Composite parent){
		projectMan.loadProjects();
		tabFolder = new CTabFolder(parent,SWT.NONE);
		tabFolder.setTabHeight(10);
		analysisData = new AnalysisData();
	    newProject = new Project();
	    
	    CTabItem projTabItem = new CTabItem(tabFolder, SWT.NONE);
	    projTabItem.setText("Select Project ->");
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
		 Group grpProjects = new Group(tabComp, SWT.NONE);
		    grpProjects.setLayout(new GridLayout(1, false));
		    grpProjects.setLayoutData(new GridData(GridData.FILL_BOTH));
		    grpProjects.setText("Project");
		    lstProjects = new List(grpProjects, SWT.BORDER | SWT.V_SCROLL);
		    lstProjects.setLayoutData(new GridData(400,400));
		
		    for(Project project: projectMan.getProjects()){
		    	lstProjects.add(project.getName());
		    }
		    
		    lstProjects.addSelectionListener(new SelectionListener(){

		    	/**
		    	 * Sets which Project is currently selected and populates the 
		    	 * List of Project Data in the Project.
		    	 */
				@Override
				public void widgetSelected(SelectionEvent e) {
					if(lstProjects.getSelectionCount() > 0){
						Project selectedProject = projectMan.getProject(lstProjects.getSelection()[0]);
						analysisData.setChosenProject(selectedProject);
						analysisData.setChosenProjectData(new LinkedList<IProjectData>());
					}
				}

				@Override
				public void widgetDefaultSelected(SelectionEvent e) {}
		    });
		    

		
		    final Button nButton = new Button(tabComp, SWT.PUSH);
	    nButton.setText("Next");
	    GridData buttonData = new GridData(SWT.CENTER, SWT.BOTTOM, false, false, 1, 1);
	    buttonData.verticalIndent = 50;
	    nButton.setLayoutData(new GridData(SWT.RIGHT, SWT.BOTTOM, false, false, 1, 1));
	    nButton.addSelectionListener(new SelectionAdapter() {
	        @Override
	        public void widgetSelected(SelectionEvent e) {
	        	if(analysisData.getChosenProject() != null){
	        		openSelectFileTab();
	        	}
	            
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
	    projTabItem.setControl(tabComp);
	    projectTab = projTabItem;
	    tabFolder.setSelection(projectTab);
	   
		
		

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
		else if(projectMan.containsProject(name)){
			isValid = false;
		}
		
		
		return isValid;
	}
	
	public void openSelectFileTab(){
		if(fileTab == null){
			Label lblFiles;
			
			CTabItem selectFileTab = new CTabItem(tabFolder, SWT.NONE);
			selectFileTab.setText("Select File ->");
			Composite container = new Composite(tabFolder, SWT.NONE);
			 GridLayout layout = new GridLayout();
			    layout.numColumns = 1;
			    layout.marginHeight = 15;
			    container.setLayout(layout);
			    
			    Group grpFiles = new Group(container, SWT.NONE);
			    grpFiles.setLayout(new GridLayout(1, false));
			    grpFiles.setLayoutData(new GridData(400,400));
			    grpFiles.setText("Files");
			    
			    lblFiles = new Label(grpFiles, SWT.NONE);
			    lblFiles.setText("Select the Files on which to run the Analysis:");
			    
			    lstFiles = new List(grpFiles, SWT.BORDER | SWT.V_SCROLL | SWT.MULTI);
			    lstFiles.setLayoutData(grpFiles.getLayoutData());
			    lstFiles.setEnabled(true);
			    lstFiles.addSelectionListener(new SelectionListener(){

			    	/**
			    	 * Sets which Project Data are currently selected.
			    	 */
					@Override
					public void widgetSelected(SelectionEvent e) {
						LinkedList<IProjectData> selectedProjectData =
								new LinkedList<IProjectData>();
						
						for(String dataName: lstFiles.getSelection()){
							selectedProjectData.add(analysisData.getChosenProject().getProjectData(dataName));
						}
					}

					@Override
					public void widgetDefaultSelected(SelectionEvent e) {
						// TODO Auto-generated method stub
						
					}
			    });
			    updateFileList();
			selectFileTab.setControl(container);
//			Button importFileButton = new Button(container, SWT.NONE);
//			importFileButton.setText("Import Text File");
			Button nButtonFile = new Button(container, SWT.NONE);
			nButtonFile.setText("Next");
			nButtonFile.addSelectionListener(new SelectionAdapter() {
		        @Override
		        public void widgetSelected(SelectionEvent e) {
		            openAnalysisTab();
		        }
		    });
			fileTab = selectFileTab;
			
		}
		tabFolder.setSelection(fileTab);
		
	}
	public void openAnalysisTab(){
		if(analysisTab == null){
			ArrayList<String> softwareModuleList = new ArrayList<String>();
			softModMap = new HashMap<String,String>();
			softwareModuleList.addAll(softwareModuleMan.getSoftwareModuleNames());
			CTabItem analysisTabItem = new CTabItem(tabFolder, SWT.NONE);
			analysisTabItem.setText("Select Analysis ->");
			Composite container = new Composite(tabFolder, SWT.NONE);
			 GridLayout layout = new GridLayout();
			    layout.numColumns = 1;
			    layout.marginTop = 15;
			container.setLayout(layout);
			lblAnalyses = new Label(container, SWT.TOP);
			lblAnalyses.setText("Select an Analysis to use:");
//			CTabFolder innerFolder = new CTabFolder(container, SWT.NONE);
//			CTabItem basicAnalysis = new CTabItem(innerFolder, SWT.NONE);
//			basicAnalysis.setText("Basic Analysis Set");
//			CTabItem advancedAnalysis = new CTabItem(innerFolder,SWT.NONE);
//			Composite basicContainer = new Composite(innerFolder, SWT.NONE);
			
			Group grpBasicAnalyses = new Group(container, SWT.NONE);
			grpBasicAnalyses.setLayout(new GridLayout(1, false));
		    grpBasicAnalyses.setLayoutData(new GridData(400,400));
		    grpBasicAnalyses.setText("Analyses");
			
			lstAnalyses = new List(grpBasicAnalyses, SWT.BORDER | SWT.V_SCROLL);
			lstAnalyses.setLayoutData(grpBasicAnalyses.getLayoutData());
			lstAnalyses.setEnabled(true);
			
			lstAnalyses.addSelectionListener(new SelectionListener(){

				@Override
				public void widgetSelected(SelectionEvent e) {
					if(lstAnalyses.getSelectionCount() > 0){
						String analysisName = lstAnalyses.getSelection()[0];
						String selectedModule = softModMap.get(analysisName);
		
						
						IAnalysisPlugin analysis = softwareModuleMan.getAnalysisByName(
								selectedModule, analysisName);
						
						if(analysis != null){
							analysisData.setChosenAnalysis(analysis);
						}
					}
				}

				@Override
				public void widgetDefaultSelected(SelectionEvent e) {}
			});
			
			updateAnalysisList();
//			advancedAnalysis.setText("Advanced Analysis Set");
//			basicAnalysis.setControl(innerFolder);
//			innerFolder.setLayoutData(new GridData(800, 300));
			Button analysisNButton = new Button(container, SWT.NONE);
			analysisNButton.setText("Next");
			analysisNButton.addSelectionListener(new SelectionAdapter() {
		        @Override
		        public void widgetSelected(SelectionEvent e) {
		            openVisualizationTab();		        
		        }
		    });
//			innerFolder.setSelection(basicAnalysis);
			analysisTabItem.setControl(container);
			analysisTab = analysisTabItem;
			
		}
		
		tabFolder.setSelection(analysisTab);
	}
	
	public void openVisualizationTab(){
		if(visualizationTab == null){
			CTabItem visualizationSelection = new CTabItem(tabFolder, SWT.NONE);
			visualizationSelection.setText("Select Visualization");
			visualizationTab = visualizationSelection;
		}
		tabFolder.setSelection(visualizationTab);
	}
	
	/**
	 * Updates the contents of lstFiles.
	 */
	private void updateFileList(){
		lstFiles.deselectAll();
		lstFiles.removeAll();
		
		for(IProjectData projData: analysisData.getChosenProject().getOriginalData()){
			lstFiles.add(projData.getName());
		}
		
		lstFiles.update();
	}
	
	private void updateAnalysisList(){
		lstAnalyses.removeAll();
		
		
		IAnalysisPlugin tokenization;
		for( String name : softwareModuleMan.getSoftwareModuleNames()){
			System.out.println(name);
			for( IAnalysisPlugin plug : softwareModuleMan.getAnalyses(name)){
				System.out.println(plug.getName());
				lstAnalyses.add(plug.getName());
				softModMap.put(plug.getName(), name);
			}
		}
//		tokenization = softwareModuleMan.getAnalysisByName("NLTK", "Tokenization");
//		lstAnalyses.add(tokenization.getName());
		
		lstAnalyses.update();
	}
 
}
