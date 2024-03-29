
package LinGUIne.parts.advanced;

import java.util.Collection;
import java.util.LinkedList;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;

import org.eclipse.core.runtime.SafeRunner;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.e4.ui.di.Focus;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import LinGUIne.events.LinGUIneEvents;
import LinGUIne.extensions.IAnalysisPlugin;
import LinGUIne.extensions.IPropertiesProvider;
import LinGUIne.model.IProjectData;
import LinGUIne.model.Project;
import LinGUIne.model.ProjectManager;
import LinGUIne.model.Result;
import LinGUIne.model.SoftwareModuleManager;
import LinGUIne.utilities.SafeAnalysis;

/**
 * Part to provide the user with quick access to all of the available analyses.
 * 
 * @author Pete Maresca
 * @author Kyle Mullins
 */
public class QuickAnalysisPart implements IPropertiesProvider {
	
	@Inject
	private ProjectManager projectMan;
	
	@Inject
	private SoftwareModuleManager softwareModuleMan;
	
	@Inject
	private IEventBroker eventBroker;
	
	private ProjectExplorerSelection projectSelection;
	
	private Composite myParent;
	private ListViewer lstSoftwareModules;
	private ListViewer lstAnalyses;
	private Button btnRunAnalysis;

	private Composite propertiesView;
	private Label lblSelectedAnalysis;
	private Text txtDescription;
	private Label lblNumFiles;
	private Label lblNumJobs;
	
	/**
	 * Creates a new QuickAnalysisPart and populates its controls.
	 */
	public QuickAnalysisPart() {
		projectSelection = new ProjectExplorerSelection();
	}

	/**
	 * Creates the QuickAnalysisView's Composite.
	 */
	@PostConstruct
	public void createComposite(Composite parent){
		myParent = parent;
		
		GridLayout layout = new GridLayout(2, true);
		parent.setLayout(layout);

		Label lblLibraries = new Label(parent, SWT.NONE);
		lblLibraries.setText("Software Modules");

		Label lblFeatures = new Label(parent, SWT.NONE);
		lblFeatures.setText("Analyses");

		lstSoftwareModules = new ListViewer(parent, SWT.BORDER | SWT.V_SCROLL);

		lstSoftwareModules.getControl().setLayoutData(new GridData(GridData.FILL_BOTH));
		lstSoftwareModules.setContentProvider(new IStructuredContentProvider(){

			private Collection<String> libraryNames;
			
			@Override
			public void inputChanged(Viewer viewer, Object oldInput, Object newInput){
				if(newInput != null){
					libraryNames = ((SoftwareModuleManager)newInput).
							getSoftwareModuleNames();
				}
			}

			@Override
			public void dispose() {}

			@Override
			public Object[] getElements(Object inputElement) {
				return libraryNames.toArray();
			}
		});

		lstSoftwareModules.setLabelProvider(new LabelProvider(){
			public String getText(Object element){
				return element.toString();
			}
		});
		
		lstSoftwareModules.addSelectionChangedListener(new ISelectionChangedListener(){

			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				IStructuredSelection selection = (IStructuredSelection)event.getSelection();
				
				if(selection.size() > 0){
					String softwareModuleName = selection.getFirstElement().toString();
					
					lstAnalyses.setInput(softwareModuleMan.getAnalyses(
							softwareModuleName));
					btnRunAnalysis.setEnabled(false);
					
					updatePropertiesView(null);
				}
			}
		});

		lstAnalyses = new ListViewer(parent, SWT.BORDER | SWT.V_SCROLL);

		lstAnalyses.getControl().setLayoutData(new GridData(GridData.FILL_BOTH));
		lstAnalyses.setContentProvider(new IStructuredContentProvider() {

			private Collection<IAnalysisPlugin> analyses;
			
			@Override
			public void inputChanged(Viewer viewer, Object oldInput, Object newInput){
				if(newInput != null){
					analyses = (Collection<IAnalysisPlugin>)newInput;
				}
			}

			@Override
			public void dispose() {}

			@Override
			public Object[] getElements(Object inputElement) {
				return analyses.toArray();
			}
		});

		lstAnalyses.setLabelProvider(new LabelProvider(){
			public String getText(Object element){
				return ((IAnalysisPlugin)element).getName();
			}
		});
		
		lstAnalyses.addSelectionChangedListener(new ISelectionChangedListener(){

			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				IStructuredSelection selection = (IStructuredSelection) event.getSelection();
				
				if(selection.size() > 0){
					IAnalysisPlugin analysis = (IAnalysisPlugin)selection.getFirstElement();
					
					btnRunAnalysis.setEnabled(true);
					
					updatePropertiesView(analysis);
				}
			}
		});

		btnRunAnalysis = new Button(parent, SWT.NONE);
		btnRunAnalysis.setText("Run Analysis");
		btnRunAnalysis.setEnabled(false);
		btnRunAnalysis.addSelectionListener(new SelectionListener(){

			@Override
			public void widgetSelected(SelectionEvent e) {
				IStructuredSelection selection =
						(IStructuredSelection)lstAnalyses.getSelection();
				
				IAnalysisPlugin analysis = (IAnalysisPlugin)selection.getFirstElement();
				
				runAnalysis(analysis);
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {}
		});

		lstSoftwareModules.setInput(softwareModuleMan);
	}

	@Focus
	public void onFocus() {}
	
	/**
	 * Called whenever the ProjectExplorerSelection changes to trigger an update
	 * to the view.
	 * 
	 * @param selection	The current selection in the ProjectExplorer.
	 */
	@Inject
	public void setProjectDataSelection(@Optional
			@Named(IServiceConstants.ACTIVE_SELECTION)
			ProjectExplorerSelection selection){
		
		if(selection != null){
			projectSelection = selection;
			btnRunAnalysis.setEnabled(btnRunAnalysis.getEnabled() &&
					!projectSelection.isEmpty());
			
			int numProjects = 0;
			int numFiles = 0;
			
			for(String projectName: projectSelection.getSelectedProjects()){
				Collection<String> selectedOGData = projectSelection.
						getSelectedOriginalData(projectName);
				
				if(!selectedOGData.isEmpty()){
					numProjects++;
					numFiles += selectedOGData.size();
				}
			}
			
			updatePropertiesView(numFiles, numProjects);
		}
	}
	/**
	 * Gets the properties view, instantiates it if null
	 */
	@Override
	public Composite getProperties(Composite parent){
		if(propertiesView == null){
			createPropertiesView(parent);
		}
		
		return propertiesView;
	}
	
	/**
	 * Creates the properties Composite for the view.
	 */
	private void createPropertiesView(Composite parent){
		propertiesView = new Composite(parent, SWT.NONE);
		propertiesView.setLayout(new GridLayout(1, false));
		
		lblSelectedAnalysis = new Label(propertiesView, SWT.NONE);
		lblSelectedAnalysis.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		lblSelectedAnalysis.setText("Selected Analysis: ");
		
		Label lblDescription = new Label(propertiesView, SWT.NONE);
		lblDescription.setText("Description");
		
		txtDescription = new Text(propertiesView, SWT.BORDER | SWT.V_SCROLL |
				SWT.WRAP);
		txtDescription.setEditable(false);
		txtDescription.setLayoutData(new GridData(GridData.FILL_BOTH));
		txtDescription.setBackground(lstAnalyses.getControl().getBackground());
		
		Composite bottomContainer = new Composite(propertiesView, SWT.NONE);
		bottomContainer.setLayout(new GridLayout(2, false));
		
		lblNumFiles = new Label(bottomContainer, SWT.NONE);
		lblNumFiles.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		lblNumFiles.setText("Selected files: 0");
		
		lblNumJobs = new Label(bottomContainer, SWT.NONE);
		lblNumJobs.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		lblNumJobs.setText("Analysis jobs: 0");
	}
	
	/**
	 * Updates the properties Composite with the given data taken from the
	 * selection in the ProjectExplorer.
	 */
	private void updatePropertiesView(int numFiles, int numProjects){
		if(propertiesView != null){
			lblNumFiles.setText("Selected files: " + numFiles);
			lblNumJobs.setText("Analysis jobs: " + numProjects);
			
			eventBroker.post(LinGUIneEvents.UILifeCycle.PROPERTIES_VIEW_CHANGED,
					this);
		}
	}
	
	/**
	 * Updates the properties Composite with the given selected AnalysisPlugin.
	 */
	private void updatePropertiesView(IAnalysisPlugin analysis){
		if(propertiesView != null){
			if(analysis != null){
				lblSelectedAnalysis.setText("Selected Analysis: " +
						analysis.getName());
				txtDescription.setText(analysis.getAnalysisDescription());
			}
			else{
				lblSelectedAnalysis.setText("Selected Analysis: ");
				txtDescription.setText("");
			}
			
			eventBroker.post(LinGUIneEvents.UILifeCycle.PROPERTIES_VIEW_CHANGED,
					this);
		}
	}
	
	/**
	 * Runs the given AnalysisPlugin if possible.
	 */
	private void runAnalysis(IAnalysisPlugin analysis){
		Shell theShell = myParent.getShell();
		LinkedList<SafeAnalysis> safeAnalyses = new LinkedList<SafeAnalysis>();
		Collection<Class<? extends Result>> requiredResultTypes =
				analysis.getRequiredResultTypes();
		Collection<Class<? extends IProjectData>> supportedSourceDataTypes = 
				analysis.getSupportedSourceDataTypes();
		
		//Compose all valid SafeAnalysis objects that we can
		for(String projectName: projectSelection.getSelectedProjects()){
			Project destProject = projectMan.getProject(projectName);
			LinkedList<IProjectData> sourceData = new LinkedList<IProjectData>();
			
			for(String dataName: projectSelection.getSelectedOriginalData(
					projectName)){
				IProjectData projData = destProject.getProjectData(dataName);
				
				//Check if all of this ProjectData has the proper Results
				//And is of the proper type for the chosen analysis
				if(supportedSourceDataTypes.contains(projData.getClass())){
					boolean hasAllResults = true;
					LinkedList<Result> results = new LinkedList<Result>();
					//Some analysis don't require a result type
					if(!requiredResultTypes.isEmpty()){
						for(Class<? extends Result> resultType: requiredResultTypes){
							if(destProject.hasResultType(projData, resultType)){
								//All good, carry on
								//TODO: Prevent duplicate results from being added here
								results.add(destProject.getResultType(projData,
										resultType));
							}
							else{
								//Missing required result type
								//Either run something else first or leave this out
								hasAllResults = false;
							}
						}
					}
					
					if(hasAllResults){
						sourceData.add(projData);
						sourceData.addAll(results);
					}
				}
				else{
					//Unsupported source type for this analysis
				}
			}
			
			//Only run the analysis if there was some selected original
			//ProjectData for this Project that was valid for the analysis
			if(!sourceData.isEmpty()){
				SafeAnalysis safeAnalysis = new SafeAnalysis(theShell, analysis,
						sourceData, destProject);
				
				safeAnalyses.add(safeAnalysis);
			}
		}
		
		if(safeAnalyses.isEmpty()){
			//TODO: warn the user that none of the selections were valid
		}
		else{
			for(SafeAnalysis safeAnalysis: safeAnalyses){
				SafeRunner.run(safeAnalysis);
			}
		}
	}
}