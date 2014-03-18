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
import LinGUIne.extensions.IVisualization;
import LinGUIne.model.IProjectData;
import LinGUIne.model.Project;
import LinGUIne.model.ProjectManager;
import LinGUIne.model.Result;
import LinGUIne.model.SoftwareModuleManager;
import LinGUIne.model.VisualizationPluginManager;
import LinGUIne.wizards.AnalysisData;
import LinGUIne.wizards.VisualizationData;

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
	private VisualizationData visualizationData;
	private Label lblAnalyses;
	private List lstAnalyses;
	private final String PROJECT_TAB = "Select Project ->";
	private final String FILE_TAB = "Select File ->";
	private final String ANALYSIS_TAB = "Select Analysis ->";
	private final String VISUAL_TAB = "Select Visualization";
	
	@Inject
	private MApplication application;
	
	@Inject
	private ProjectManager projectMan;
	
	@Inject
	private SoftwareModuleManager softwareModuleMan;
	
	@Inject
	private VisualizationPluginManager visualizationPluginMan;
	
	private CTabItem projectTab;
	private CTabItem fileTab;
	private CTabItem analysisTab;
	private CTabItem visualTab;
	

	

	
	@PostConstruct
	public void createComposite(Composite parent){
		projectMan.loadProjects();
		tabFolder = new CTabFolder(parent,SWT.NONE);
		tabFolder.setData("org.eclipse.e4.ui.css.id", "basicFolder");
		tabFolder.addSelectionListener(new SelectionListener(){
			@Override
			public void widgetSelected(SelectionEvent e){
				if(tabFolder.getSelectionIndex() >= 0){
					tabSelectionChanged(tabFolder.getSelection().getText());
				}
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
		analysisData = new AnalysisData();
		visualizationData = new VisualizationData();
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
	    projLabel.setData("org.eclipse.e4.ui.css.id", "basicProjectLabel");
		txtName = new Text(tabComp, SWT.BORDER | SWT.SINGLE);
		txtName.setText("");
		GridData txtData = new GridData();
		txtData.horizontalSpan = 2;
		txtName.setLayoutData(txtData);
		 Group grpProjects = new Group(tabComp, SWT.NONE);
		    grpProjects.setLayout(new GridLayout(1, false));
		   GridData projData = new GridData(400,400);
		    projData.horizontalIndent = 2;
		    grpProjects.setLayoutData(projData);
		    grpProjects.setText("Project");
			Label existingProjLabel = new Label(grpProjects, SWT.NONE);
			existingProjLabel.setText("Select from list of existing projects");
		    lstProjects = new List(grpProjects, SWT.BORDER | SWT.V_SCROLL);
		    lstProjects.setLayoutData(grpProjects.getLayoutData());
		
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
		    


		    final Button nButton = new Button(tabComp, SWT.NONE);
	    nButton.setText("Next");
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
		tabComp.setData("org.eclipse.e4.ui.css.id", "basicProjTab");
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
			    layout.numColumns = 2;
			    layout.marginHeight = 15;
			    container.setLayout(layout);
			    
			    Group grpFiles = new Group(container, SWT.NONE);
			    grpFiles.setLayout(new GridLayout(1, false));
			    GridData gData = new GridData(400,400);
			    gData.horizontalSpan = 2;
			    grpFiles.setLayoutData(gData);
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
			Button bButtonFile = new Button(container, SWT.NONE);
			bButtonFile.setText("Back");
			Button nButtonFile = new Button(container, SWT.NONE);
			nButtonFile.setText("Next");
			nButtonFile.addSelectionListener(new SelectionAdapter() {
		        @Override
		        public void widgetSelected(SelectionEvent e) {
		            openAnalysisTab();
		        }
		    });
			
			bButtonFile.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e){
					tabFolder.setSelection(projectTab);
					tabSelectionChanged(tabFolder.getSelection().getText());
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
			 	layout.numColumns = 2;
			    layout.marginTop = 15;
			    layout.makeColumnsEqualWidth = false;
			container.setLayout(layout);

//			CTabFolder innerFolder = new CTabFolder(container, SWT.NONE);
//			CTabItem basicAnalysis = new CTabItem(innerFolder, SWT.NONE);
//			basicAnalysis.setText("Basic Analysis Set");
//			CTabItem advancedAnalysis = new CTabItem(innerFolder,SWT.NONE);
//			Composite basicContainer = new Composite(innerFolder, SWT.NONE);
			
			Group grpBasicAnalyses = new Group(container, SWT.NONE);
			grpBasicAnalyses.setLayout(new GridLayout(1, false));
			GridData gData = new GridData(400,400);
			gData.horizontalSpan = 2;
		    grpBasicAnalyses.setLayoutData(gData);
		    grpBasicAnalyses.setText("Analyses");
			lblAnalyses = new Label(grpBasicAnalyses, SWT.NONE);
			lblAnalyses.setText("Select an Analysis to use:");
			
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
			Button analysisBButton = new Button(container, SWT.NONE);
			analysisBButton.setText("Back");
			Button analysisNButton = new Button(container, SWT.NONE);
			analysisNButton.setText("Next");
			analysisNButton.addSelectionListener(new SelectionAdapter() {
		        @Override
		        public void widgetSelected(SelectionEvent e) {
		            openVisualizationTab();		        
		        }
		    });
			
			analysisBButton.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e){
					tabFolder.setSelection(fileTab);
					tabSelectionChanged(tabFolder.getSelection().getText());
				}
			});
//			innerFolder.setSelection(basicAnalysis);
			analysisTabItem.setControl(container);
			analysisTab = analysisTabItem;
			
		}
		
		tabFolder.setSelection(analysisTab);
	}
	
	public void openVisualizationTab(){
		if(visualTab == null){
			CTabItem visualizationTabItem = new CTabItem(tabFolder, SWT.NONE);
			visualizationTabItem.setText("Select Visualization");
			Composite container = new Composite(tabFolder, SWT.NONE);
			GridLayout layout = new GridLayout();
			layout.numColumns = 2;
			layout.marginHeight = 15;
			container.setLayout(layout);
			Group grpVisualizations = new Group(container, SWT.NONE);
			grpVisualizations.setLayout(new GridLayout(1, false));
			GridData vData = new GridData(400,400);
			vData.horizontalSpan = 2;
			grpVisualizations.setLayoutData(vData);
			grpVisualizations.setText("Visualizations");

			Label lblVisualizations = new Label(grpVisualizations, SWT.NONE);
			lblVisualizations.setText("Select a Visualization for your data:");

			List lstVisualizations = new List(grpVisualizations, SWT.BORDER
					| SWT.V_SCROLL);
			lstVisualizations.setLayoutData(grpVisualizations.getLayoutData());
			
			for (String visualization : visualizationPluginMan.getVisualizationNames()) {
				lstVisualizations.add(visualization);
			}
			lstVisualizations.update();
			visualizationPluginMan.getVisualizationNames();
			Button bButton = new Button(container, SWT.NONE);
			Button fButton = new Button(container, SWT.NONE);
			bButton.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e){
					tabFolder.setSelection(analysisTab);
					tabSelectionChanged(tabFolder.getSelection().getText());
				}
			});
			fButton.setText("Generate Visual");
			bButton.setText("Back");
			visualizationTabItem.setControl(container);
			visualTab = visualizationTabItem;
			
			
			
		}
		tabFolder.setSelection(visualTab);
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
	
	private void tabSelectionChanged(String selectedTab){
		if(selectedTab.equals(PROJECT_TAB)){
			if(fileTab != null){fileTab.dispose();}
			fileTab = null;
			if(analysisTab != null){analysisTab.dispose();}
			analysisTab = null;
			if(visualTab != null){visualTab.dispose();}
			visualTab = null;
		}
		else if(selectedTab.equals(FILE_TAB)){
			if(analysisTab != null){analysisTab.dispose();}
			analysisTab = null;
			if(visualTab != null){visualTab.dispose();}
			visualTab = null;
		}
		else if(selectedTab.equals(ANALYSIS_TAB)){
			if(visualTab != null){visualTab.dispose();}
			visualTab = null;
		}
		
	}
 
}
