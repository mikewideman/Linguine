
package LinGUIne.parts.advanced;

import java.util.Collection;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.eclipse.e4.ui.di.Focus;
import org.eclipse.e4.ui.model.application.MApplication;
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
import org.eclipse.swt.widgets.Text;

import LinGUIne.extensions.IAnalysisPlugin;
import LinGUIne.model.SoftwareModuleManager;

public class QuickAnalysisPart {
	
	private SoftwareModuleManager softwareModuleMan;
	
	private ListViewer lstSoftwareModules;
	private ListViewer lstAnalyses;
	private Text txtDescription;
	private Button btnRunAnalysis;
	
	@Inject
	public QuickAnalysisPart(MApplication app) {
		softwareModuleMan = new SoftwareModuleManager();
		
		app.getContext().set(SoftwareModuleManager.class, softwareModuleMan);
	}

	@PostConstruct
	public void createComposite(Composite parent){

		GridLayout layout = new GridLayout();
		layout.numColumns = 3;
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
					
					txtDescription.setText(analysis.getPluginData().toString());
					btnRunAnalysis.setEnabled(true);
				}
			}
		});

		txtDescription = new Text(parent, SWT.BORDER | SWT.V_SCROLL | SWT.WRAP);
		txtDescription.setEditable(false);
		txtDescription.setLayoutData(new GridData(GridData.FILL_BOTH));
		txtDescription.setBackground(new Color(Display.getCurrent(), 255, 255, 255));

		btnRunAnalysis = new Button(parent, SWT.NONE);
		btnRunAnalysis.setText("Run Analysis");
		btnRunAnalysis.setEnabled(false);
		btnRunAnalysis.addSelectionListener(new SelectionListener(){

			@Override
			public void widgetSelected(SelectionEvent e) {
				IStructuredSelection selection =
						(IStructuredSelection)lstAnalyses.getSelection();
				
				IAnalysisPlugin analysis = (IAnalysisPlugin)selection.getFirstElement();
				analysis.runAnalysis();
				//TODO: Run analysis
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {}
		});

		lstSoftwareModules.setInput(softwareModuleMan);
	}

	@Focus
	public void onFocus() {}
}