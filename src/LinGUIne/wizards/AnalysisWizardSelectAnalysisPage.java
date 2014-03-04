package LinGUIne.wizards;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;

import LinGUIne.extensions.IAnalysisPlugin;
import LinGUIne.model.SoftwareModuleManager;

/**
 * Analysis Page object; this is the GUI components
 * for the second step in the Analysis Wizard. The user
 * will select the analyses he or she wishes to run.
 * 
 * @author Matthew Talbot
 * @author Kyle Mullins
 */

public class AnalysisWizardSelectAnalysisPage extends WizardPage {

	private SoftwareModuleManager softwareModuleMan;
	private AnalysisData wizardData;
	
	private Label lblSoftwareModules;
	private List lstSoftwareModules;
	private Label lblAnalyses;
	private List lstAnalyses;
	private Label lblDescription;

	public AnalysisWizardSelectAnalysisPage(AnalysisData data,
			SoftwareModuleManager softwareModules) {
		super("Analysis Wizard");
		setTitle("Analysis Wizard - Step 2");
		setDescription("Select the analyses you wish to run.");

		wizardData = data;
		softwareModuleMan = softwareModules;
	}

	@Override
	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		container.setLayout(layout);
		
		Group grpSoftwareModules = new Group(container, SWT.NONE);
		grpSoftwareModules.setLayout(new GridLayout(1, false));
	    grpSoftwareModules.setLayoutData(new GridData(GridData.FILL_BOTH));
	    grpSoftwareModules.setText("Software Modules");
		
		lblSoftwareModules = new Label(grpSoftwareModules, SWT.NONE);
		lblSoftwareModules.setText("Select a Software Module to use:");

		lstSoftwareModules = new List(grpSoftwareModules, SWT.BORDER | SWT.V_SCROLL);
		lstSoftwareModules.setLayoutData(new GridData(GridData.FILL_BOTH));
		
		for(String moduleName : softwareModuleMan.getSoftwareModuleNames()){
			lstSoftwareModules.add(moduleName);
		}
		
		lstSoftwareModules.addSelectionListener(new SelectionListener(){

			@Override
			public void widgetSelected(SelectionEvent e) {
				if(lstSoftwareModules.getSelectionCount() > 0){
					String selectedModule = lstSoftwareModules.getSelection()[0];
					
					wizardData.setChosenAnalysis(null);
					updateAnalysisList(selectedModule);
					
					lstAnalyses.setEnabled(true);
					lblDescription.setText("");
					lblDescription.update();
					
					setPageComplete(false);
				}
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {}
		});
		
		Group grpAnalyses = new Group(container, SWT.NONE);
		grpAnalyses.setLayout(new GridLayout(1, false));
	    grpAnalyses.setLayoutData(new GridData(GridData.FILL_BOTH));
	    grpAnalyses.setText("Analyses");
		
		lblAnalyses = new Label(grpAnalyses, SWT.NONE);
		lblAnalyses.setText("Select an Analysis to use:");
		
		lstAnalyses = new List(grpAnalyses, SWT.BORDER | SWT.V_SCROLL);
		lstAnalyses.setLayoutData(new GridData(GridData.FILL_BOTH));
		lstAnalyses.setEnabled(false);
		
		lstAnalyses.addSelectionListener(new SelectionListener(){

			@Override
			public void widgetSelected(SelectionEvent e) {
				if(lstAnalyses.getSelectionCount() > 0){
					String analysisName = lstAnalyses.getSelection()[0];
					String selectedModule = lstSoftwareModules.getSelection()[0];
	
					IAnalysisPlugin analysis = softwareModuleMan.getAnalysisByName(
							selectedModule, analysisName);
					
					if(analysis != null){
						wizardData.setChosenAnalysis(analysis);
						lblDescription.setText(analysis.getAnalysisDescription());
						lblDescription.update();
						
						setPageComplete(true);
					}
				}
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {}
		});
		
		lblDescription = new Label(container, SWT.NONE);
		lblDescription.setLayoutData(new GridData(GridData.FILL_BOTH));
		lblDescription.setText("");
		
		setControl(container);
		setPageComplete(false);
	}
	
	private void updateAnalysisList(String selectedSoftwareModule){
		lstAnalyses.deselectAll();
		lstAnalyses.removeAll();
		
		for(IAnalysisPlugin analysis: softwareModuleMan.getAnalyses(
				selectedSoftwareModule)){
			lstAnalyses.add(analysis.getName());
		}
		
		lstAnalyses.update();
	}
}

