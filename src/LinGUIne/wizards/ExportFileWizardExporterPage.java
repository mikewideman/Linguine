package LinGUIne.wizards;

import java.util.HashMap;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.Platform;
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

import LinGUIne.extensions.CSVExporter;
import LinGUIne.extensions.IFileExporter;
import LinGUIne.extensions.XMLExporter;

/**
 * Page for the user to select which exporter they are going to use.
 * 
 * @author Kyle Mullins
 */
public class ExportFileWizardExporterPage extends WizardPage {

	private Label lblExporters;
	private List lstExporters;
	
	private ExportFileData wizardData;
	
	public ExportFileWizardExporterPage(ExportFileData data){
		super("Export Result Wizard");
		setTitle("Export Result Wizard");
		setMessage("Select the type of File to which you wish to export.");
		
		wizardData = data;
	}
	
	@Override
	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NONE);
		container.setLayout(new GridLayout(1, false));
		
		Group grpExporter = new Group(container, SWT.NONE);
		grpExporter.setLayout(new GridLayout(1, false));
		grpExporter.setLayoutData(new GridData(GridData.FILL_BOTH));
		grpExporter.setText("Export type");
		
		lblExporters = new Label(grpExporter, SWT.NONE);
		lblExporters.setText("Select the type of file you want to export to:");
		
		lstExporters = new List(grpExporter, SWT.BORDER | SWT.V_SCROLL);
		lstExporters.setLayoutData(new GridData(GridData.FILL_BOTH));
		
		IConfigurationElement[] configElements = Platform.getExtensionRegistry().
				getConfigurationElementsFor("LinGUIne.LinGUIne.extensions.IFileExporter");
		
		final HashMap<String, IConfigurationElement> exporterConfigs =
				new HashMap<String, IConfigurationElement>();
		final HashMap<String, IFileExporter> builtInExporters = new HashMap<
				String, IFileExporter>();
		
		//Add built-in exporters to the list
		IFileExporter csv = new CSVExporter();
		builtInExporters.put(csv.getFileType(), csv);
		lstExporters.add(csv.getFileType());
		
		IFileExporter xml = new XMLExporter();
		builtInExporters.put(xml.getFileType(), xml);
		lstExporters.add(xml.getFileType());
		
		for(IConfigurationElement configElement: configElements){
			String fileType = configElement.getAttribute("file_type");
			
			lstExporters.add(fileType);
			exporterConfigs.put(fileType, configElement);
		}
		
		lstExporters.addSelectionListener(new SelectionListener(){

			/**
			 * Set which IFileExporter is currently selected.
			 */
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(lstExporters.getSelectionCount() != 0){
					String selectedFileType = lstExporters.getSelection()[0];
					IFileExporter exporter = null;
					
					if(builtInExporters.containsKey(selectedFileType)){
						exporter = builtInExporters.get(selectedFileType);
					}
					else if(exporterConfigs.containsKey(selectedFileType)){
						IConfigurationElement exporterConfig =
								exporterConfigs.get(selectedFileType);
						
						try {
							exporter = (IFileExporter)exporterConfig.
									createExecutableExtension("class");
						}
						catch (CoreException e1) {
							e1.printStackTrace();
						}
					}
					
					wizardData.setExporter(exporter);
					
					checkIfPageComplete();
				}
				else{
					wizardData.setExporter(null);
				}
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {}
		});
		
		setPageComplete(false);
		setControl(container);
	}
	
	/**
	 * Sets the page complete if an exporter has been chosen.
	 */
	private void checkIfPageComplete(){
		setPageComplete(wizardData.getChosenExporter() != null);
	}
}
