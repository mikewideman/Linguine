
package LinGUIne.parts.advanced;

import java.util.Collection;
import java.util.LinkedList;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;

import org.eclipse.core.runtime.SafeRunner;
import org.eclipse.e4.core.di.annotations.Optional;
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
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import LinGUIne.extensions.IAnalysisPlugin;
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
public class QuickAnalysisPart {
	
	@Inject
	private ProjectManager projectMan;
	
	@Inject
	private SoftwareModuleManager softwareModuleMan;
	
	private ProjectExplorerSelection projectSelection;
	
	private Composite myParent;
	private ListViewer lstSoftwareModules;
	private ListViewer lstAnalyses;
	private Text txtDescription;
	private Button btnRunAnalysis;
	private Label lblNumFiles;
	private Label lblNumJobs;
	
	public QuickAnalysisPart() {
		projectSelection = new ProjectExplorerSelection();
	}

	@PostConstruct
	public void createComposite(Composite parent){
		myParent = parent;
		
		GridLayout layout = new GridLayout(3, true);
		parent.setLayout(layout);

		Label lblLibraries = new Label(parent, SWT.NONE);
		lblLibraries.setText("Software Modules");

		Label lblFeatures = new Label(parent, SWT.NONE);
		lblFeatures.setText("Analyses");

		Label lblDescription = new Label(parent, SWT.NONE);
		lblDescription.setText("Description");

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
					txtDescription.setText("");
					btnRunAnalysis.setEnabled(false);
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
					
					txtDescription.setText(analysis.getAnalysisDescription());
					btnRunAnalysis.setEnabled(true);
				}
			}
		});

		txtDescription = new Text(parent, SWT.BORDER | SWT.V_SCROLL | SWT.WRAP);
		txtDescription.setEditable(false);
		txtDescription.setLayoutData(new GridData(GridData.FILL_BOTH));
		txtDescription.setBackground(lstAnalyses.getControl().getBackground());

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

		lblNumFiles = new Label(parent, SWT.NONE);
		lblNumFiles.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		lblNumFiles.setText("Selected files: 0");
		
		lblNumJobs = new Label(parent, SWT.NONE);
		lblNumJobs.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		lblNumJobs.setText("Analysis jobs: 0");
		
		lstSoftwareModules.setInput(softwareModuleMan);
	}

	@Focus
	public void onFocus() {}
	
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
			
			lblNumFiles.setText("Selected files: " + numFiles);
			lblNumJobs.setText("Analysis jobs: " + numProjects);
		}
	}
	
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