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

import LinGUIne.extensions.IAnnotationExporter;
import LinGUIne.extensions.XMLAnnotationExporter;

/**
 * Page for the user to select to which type of file they would like to export.
 * 
 * @author Kyle Mullins
 */
public class ExportAnnotationWizardExporterPage extends WizardPage {

	private Label lblExporters;
	private List lstExporters;
	
	private ExportAnnotationData wizardData;
	
	public ExportAnnotationWizardExporterPage(ExportAnnotationData data){
		super("Export Annotation Wizard");
		setTitle("Export Annotation Wizard");
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
				getConfigurationElementsFor("LinGUIne.LinGUIne.extensions."
						+ "IAnnotationExporter");
		
		final HashMap<String, IConfigurationElement> exporterConfigs =
				new HashMap<String, IConfigurationElement>();
		final HashMap<String, IAnnotationExporter> builtInExporters = new HashMap<
				String, IAnnotationExporter>();
		
		//Add built-in exporters to the list
		IAnnotationExporter xml = new XMLAnnotationExporter();
		builtInExporters.put(xml.getFileType(), xml);
		lstExporters.add(xml.getFileType());
		
		for(IConfigurationElement configElement: configElements){
			String fileType = configElement.getAttribute("file_type");
			
			lstExporters.add(fileType);
			exporterConfigs.put(fileType, configElement);
		}
		
		lstExporters.addSelectionListener(new SelectionListener(){

			/**
			 * Set which IAnnotationExporter is currently selected.
			 */
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(lstExporters.getSelectionCount() != 0){
					String selectedFileType = lstExporters.getSelection()[0];
					IAnnotationExporter exporter = null;
					
					if(builtInExporters.containsKey(selectedFileType)){
						exporter = builtInExporters.get(selectedFileType);
					}
					else if(exporterConfigs.containsKey(selectedFileType)){
						IConfigurationElement exporterConfig =
								exporterConfigs.get(selectedFileType);
						
						try {
							exporter = (IAnnotationExporter)exporterConfig.
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
