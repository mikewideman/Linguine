package LinGUIne.wizards;

import java.io.File;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import LinGUIne.utilities.FileUtils;

/**
 * Page for the user to choose the File to which they would like to export.
 * 
 * @author Kyle Mullins
 */
public class ExportAnnotationWizardChooseFilePage extends WizardPage {

	private Label lblDestFile;
	private Text txtDestFile;
	private Button btnBrowse;
	
	private ExportAnnotationData wizardData;
	
	public ExportAnnotationWizardChooseFilePage(ExportAnnotationData data){
		super("Export Annotation Wizard");
		setTitle("Export Annotation Wizard");
		setMessage("Select where to export the chosen Annotations.");
		
		wizardData = data;
	}
	
	@Override
	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NONE);
		container.setLayout(new GridLayout(2, false));
		
		lblDestFile = new Label(container, SWT.NONE);
		lblDestFile.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		lblDestFile.setText("Choose to where you would like to export the "
				+ "Annotations.");
		
		@SuppressWarnings("unused")
		Label lblPlaceholder = new Label(container, SWT.NONE);
		
		txtDestFile = new Text(container, SWT.BORDER | SWT.SINGLE);
		txtDestFile.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		txtDestFile.setText("");
		txtDestFile.addModifyListener(new ModifyListener(){
			@Override
			public void modifyText(ModifyEvent e) {
				String selectedFilePath = txtDestFile.getText();

				wizardData.setDestFile(null);
				
				if(FileUtils.isValidFileName(selectedFilePath)){
					File selectedFile = new File(selectedFilePath);
					
					if(!selectedFile.exists() || selectedFile.isFile()){
						wizardData.setDestFile(selectedFile);
					}
				}
				
				checkIfPageComplete();
			}
		});
		
		btnBrowse = new Button(container, SWT.NONE);
		btnBrowse.setText("Browse...");
		btnBrowse.addSelectionListener(new SelectionListener(){
			@Override
			public void widgetSelected(SelectionEvent e) {
				FileDialog fileDialog = new FileDialog(getShell(), SWT.SAVE);
				fileDialog.setFilterExtensions(new String[]{
						wizardData.getChosenExporter().getFileMask()});
				
				String selectedFilePath = fileDialog.open();
				File selectedFile = new File(selectedFilePath);
				
				txtDestFile.setText(selectedFilePath);
				
				if(selectedFile.exists()){
					boolean confirmed = MessageDialog.openConfirm(getShell(),
						"Are you sure?", "The selected file already exists,"
						+ " exporting to that file will overwrite its contents."
						+ "\nDo you wish to continue?");
					
					if(!confirmed){
						txtDestFile.setText("");
					}
				}
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {}
		});
		
		setPageComplete(false);
		setControl(container);
	}
	
	/**
	 * Sets the page complete if a destination File has been chosen.
	 */
	private void checkIfPageComplete(){
		setPageComplete(wizardData.getDestFile() != null);
	}
}
