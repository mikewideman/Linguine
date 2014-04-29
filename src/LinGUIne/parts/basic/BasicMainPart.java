package LinGUIne.parts.basic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.eclipse.e4.ui.di.Focus;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Text;

import LinGUIne.extensions.IAnalysisPlugin;
import LinGUIne.extensions.IVisualization;
import LinGUIne.model.IProjectData;
import LinGUIne.model.Project;
import LinGUIne.model.ProjectManager;
import LinGUIne.model.SoftwareModuleManager;
import LinGUIne.model.VisualizationPluginManager;
import LinGUIne.wizards.AnalysisData;
import LinGUIne.wizards.VisualizationData;

public class BasicMainPart {
	
	
	private final String PROJECT_TAB = "Select Project ";
	private final String FILE_TAB = "- Select File";
	private final String ANALYSIS_TAB = "- Select Analysis";
	private final String VISUAL_TAB = "- Select Visualization";
	private CTabItem projectTab;
	private CTabItem fileTab;
	private CTabItem analysisTab;
	private CTabItem visualTab;
	private CTabFolder tabFolder;
	private Label lblProjects;
	private List lstProjects;
	private List lstFiles;
	private HashMap<String,String> softModMap;
	private AnalysisData analysisData;
	private VisualizationData visualizationData;
	private Label lblAnalyses;
	private List lstAnalyses;
	
	private Button analysisButton;
	private Button visButton;

	
	@Inject
	private MApplication application;
	
	@Inject
	private ProjectManager projectMan;
	
	@Inject
	private SoftwareModuleManager softwareModuleMan;
	
	@Inject
	private VisualizationPluginManager visualizationPluginMan;
	private Display display;
	

	

	

	
	@PostConstruct
	public void createComposite(Composite parent){
		display = getDisplay();
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
	    CTabItem projTabItem = new CTabItem(tabFolder, SWT.NONE);
	    projTabItem.setText(PROJECT_TAB);
	    FontData[] fD = projTabItem.getFont().getFontData();
	    fD[0].setHeight(14);
	    projTabItem.setFont( new Font(getDisplay(),fD[0]));
	    Composite tabComp = new Composite(tabFolder,SWT.NONE);
	    GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		layout.marginTop = 15;
		tabComp.setLayout(layout);
		//Group for the tab
	    Group grpProjects = new Group(tabComp, SWT.NONE);
	    grpProjects.setLayout(new GridLayout(1, false));
	    GridData gData = new GridData(400, 400);
	    gData.horizontalSpan = 2;
	    grpProjects.setLayoutData(gData);
	    
	    lblProjects = new Label(grpProjects, SWT.NONE);
	    lblProjects.setText("Select a Project:");
	    FontData[] fDProject = lblProjects.getFont().getFontData();
	    fDProject[0].setHeight(14);
	    lblProjects.setFont( new Font(getDisplay(),fDProject[0]));
	    
	    
	    lstProjects = new List(grpProjects, SWT.BORDER | SWT.V_SCROLL | SWT.MULTI);
	    lstProjects.setLayoutData(new GridData(370,370));
	    lstProjects.setEnabled(true);
	    for(Project project: projectMan.getProjects()){
	    	lstProjects.add(project.getName());
	    }
	    lstProjects.addSelectionListener(new SelectionListener(){

	    	/**
	    	 * Sets which Project Data are currently selected.
	    	 */
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(lstProjects.getSelectionCount() > 0){
					Project selectedProject = projectMan.getProject(lstProjects.getSelection()[0]);
					analysisData.setChosenProject(selectedProject);
				}
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
				
			}
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
	    projTabItem.setControl(tabComp);
	    projectTab = projTabItem;
	    tabFolder.setSelection(projectTab);
	   
		
		

	}
	
	
	@Focus
	public void onFocus(){
		
	}
	

	
	public void openSelectFileTab(){
		if(fileTab == null){
			Label lblFiles;
			
			CTabItem selectFileTab = new CTabItem(tabFolder, SWT.NONE);
			selectFileTab.setText(FILE_TAB);
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
			    FontData[] fD = lblFiles.getFont().getFontData();
			    fD[0].setHeight(14);
			    lblFiles.setFont( new Font(getDisplay(),fD[0]));
			    lblFiles.setText("Select the Files on which to run the Analysis:");
			    
			    lstFiles = new List(grpFiles, SWT.BORDER | SWT.V_SCROLL | SWT.MULTI);
			    lstFiles.setLayoutData(new GridData(370,370));
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
			analysisTabItem.setText(ANALYSIS_TAB);
			Composite container = new Composite(tabFolder, SWT.SCROLLBAR_OVERLAY);
			 GridLayout layout = new GridLayout();
			 	layout.numColumns = 2;
			    layout.marginTop = 15;
			    layout.makeColumnsEqualWidth = false;
			container.setLayout(layout);
			Group grpBasicAnalyses = new Group(container, SWT.NONE);
			grpBasicAnalyses.setLayout(new GridLayout(2, false));
			GridData gData = new GridData(500,500);
			gData.horizontalSpan = 2;
		    grpBasicAnalyses.setLayoutData(gData);
		    grpBasicAnalyses.setText("Analyses");
			lblAnalyses = new Label(grpBasicAnalyses, SWT.NONE);
			lblAnalyses.setText("Select an Analysis to use:");
		    FontData[] fdLbl = lblAnalyses.getFont().getFontData();
		    fdLbl[0].setHeight(14);
		    lblAnalyses.setFont( new Font(getDisplay(),fdLbl[0]));
			gData = new GridData(210,100);
			gData.horizontalSpan = 2;
			lblAnalyses.setLayoutData(gData);
			
			GridData descData = new GridData(300,100);
			descData.horizontalSpan = 2;
			descData.horizontalAlignment = SWT.FILL;
			
			for( String name : softwareModuleMan.getSoftwareModuleNames()){
				for( IAnalysisPlugin plug : softwareModuleMan.getAnalyses(name)){
					softModMap.put(plug.getName(), name);
					Button button = new Button(grpBasicAnalyses, SWT.TOGGLE | SWT.WRAP);
					button.setText(plug.getName() +"\n" + plug.getAnalysisDescription());
				    FontData[] fdButton = button.getFont().getFontData();
				    fdButton[0].setHeight(14);
				    button.setFont( new Font(getDisplay(),fdButton[0]));
					GridData buttonsGridData = new GridData(235,150);
					
					button.setLayoutData(buttonsGridData);
					button.addSelectionListener(new SelectionAdapter() {
						@Override
						public void widgetSelected(SelectionEvent e){
							if(analysisButton !=null && !analysisButton.equals(e.widget)){
								analysisButton.setSelection(false);
							}
							analysisButton = (Button) e.widget;
							IAnalysisPlugin plug = softwareModuleMan.getAnalysisByName(softModMap.get(analysisButton.getText()), analysisButton.getText());
							analysisData.setChosenAnalysis(plug);
							
						}
					});
					
					
					
				}
			}

			
//			lstAnalyses = new List(grpBasicAnalyses, SWT.BORDER | SWT.V_SCROLL);
//			lstAnalyses.setLayoutData(new GridData(370,370));
//			lstAnalyses.setEnabled(true);
//			
//			lstAnalyses.addSelectionListener(new SelectionListener(){
//
//				@Override
//				public void widgetSelected(SelectionEvent e) {
//					if(lstAnalyses.getSelectionCount() > 0){
//						String analysisName = lstAnalyses.getSelection()[0];
//						String selectedModule = softModMap.get(analysisName);
//		
//						
//						IAnalysisPlugin analysis = softwareModuleMan.getAnalysisByName(
//								selectedModule, analysisName);
//						
//						if(analysis != null){
//							analysisData.setChosenAnalysis(analysis);
//						}
//					}
//				}
//
//				@Override
//				public void widgetDefaultSelected(SelectionEvent e) {}
//			});
			
//			updateAnalysisList();
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
			visualizationTabItem.setText(VISUAL_TAB);
			Composite container = new Composite(tabFolder, SWT.NONE);
			GridLayout layout = new GridLayout();
			layout.numColumns = 2;
			layout.marginHeight = 15;
			container.setLayout(layout);
			Group grpVisualizations = new Group(container, SWT.NONE);
			grpVisualizations.setLayout(new GridLayout(2, false));
			GridData vData = new GridData(500,500);
			vData.horizontalSpan = 2;
			grpVisualizations.setLayoutData(vData);
			grpVisualizations.setText("Visualizations");

			Label lblVisualizations = new Label(grpVisualizations, SWT.NONE);
			lblVisualizations.setText("Select a Visualization for your data:");
		    FontData[] fdLbl = lblVisualizations.getFont().getFontData();
		    fdLbl[0].setHeight(14);
		    lblVisualizations.setFont( new Font(getDisplay(),fdLbl[0]));
			GridData lblVisData = new GridData(210,100);
			lblVisData.horizontalSpan = 2;
			lblVisualizations.setLayoutData(lblVisData);

			
			for( String visualization : visualizationPluginMan.getVisualizationNames()){
			
					Button button = new Button(grpVisualizations, SWT.TOGGLE);
					button.setText(visualization);
				    FontData[] fdButton = button.getFont().getFontData();
				    fdButton[0].setHeight(14);
				    button.setFont( new Font(getDisplay(),fdButton[0]));
					GridData buttonsGridData = new GridData(235,150);
					
					button.setLayoutData(buttonsGridData);
					button.addSelectionListener(new SelectionAdapter() {
						@Override
						public void widgetSelected(SelectionEvent e){
							if(visButton !=null && !visButton.equals(e.widget)){
								visButton.setSelection(false);
							}
							visButton = (Button) e.widget;
							visualizationData.setChosenVisualization(visualizationPluginMan.getVisualizationByName(visButton.getText()));
							
						}
					});
			}
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
	
	   public static Display getDisplay() {
		      Display display = Display.getCurrent();
		      //may be null if outside the UI thread
		      if (display == null)
		         display = Display.getDefault();
		      return display;		
		   }
 
}
