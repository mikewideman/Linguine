package LinGUIne.wizards;

import java.io.File;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;

/**
 * A WizardPage for the ImportFileWizard which prompts the user to choose which
 * Files they would like to import.
 * 
 * @author Kyle Mullins
 */
public class ImportFileWizardChooseFilesPage extends WizardPage {

	private Label lblFiles;
	private List lstFiles;
	private Button btnAddFile;
	private Button btnRemoveFile;
	
	private ImportFileData wizardData;
	
	/**
	 * Creates a new instance of the page with the given ImportFileData.
	 * 
	 * @param projects
	 */
	public ImportFileWizardChooseFilesPage(ImportFileData data){
		super("Import File Wizard");
		setTitle("Import File Wizard");
		setDescription("Choose the files you wish to import.");//TODO: pick a description
		setControl(lstFiles);
		
		wizardData = data;
	}
	
	/**
	 * Assembles the UI components for the page and registers
	 * SelectionListeners.
	 */
	@Override
	public void createControl(Composite parent) {
		final Composite finalParent = parent;
		
		Composite container = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout();
		layout.numColumns = 1;
		container.setLayout(layout);
		
		lblFiles = new Label(container, SWT.NONE);
		lblFiles.setText("Add the files that you wish to import:");
		
		lstFiles = new List(container, SWT.BORDER | SWT.V_SCROLL | SWT.MULTI);
		lstFiles.setLayoutData(new GridData(GridData.FILL_BOTH));
		
		lstFiles.addSelectionListener(new SelectionListener(){

			/**
			 * Sets which File is selected.
			 */
			@Override
			public void widgetSelected(SelectionEvent e) {
				checkListSelection();
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {}
		});

		Composite buttonContainer = new Composite(container, SWT.NONE);
		buttonContainer.setLayout(new GridLayout(2, false));
		buttonContainer.setLayoutData(new GridData(GridData.FILL_BOTH));
		
		btnAddFile = new Button(buttonContainer, SWT.NONE);
		btnAddFile.setText("Add file...");
		btnAddFile.addSelectionListener(new SelectionListener(){

			/**
			 * Adds a file to the list of Files.
			 */
			@Override
			public void widgetSelected(SelectionEvent e) {
				FileDialog openDialog = new FileDialog(finalParent.getShell(),
						SWT.OPEN | SWT.MULTI);
				openDialog.setFilterExtensions(new String[]{
						wizardData.getChosenImporter().getFileMask()});
				
				String dialogResult = openDialog.open();
				
				if(dialogResult != null){
					for(String chosenFile: openDialog.getFileNames()){
						wizardData.addFile(new File(openDialog.getFilterPath(),
								chosenFile));
					}
					
					updateFileList();
					checkListSelection();
					checkIfPageComplete();
				}
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {}
		});
		
		btnRemoveFile = new Button(buttonContainer, SWT.NONE);
		btnRemoveFile.setText("Remove file");
		btnRemoveFile.setEnabled(false);
		btnRemoveFile.addSelectionListener(new SelectionListener(){

			/**
			 * Removes the selected File.
			 */
			@Override
			public void widgetSelected(SelectionEvent e) {
				String[] selection = lstFiles.getSelection();
				
				for(String fileToRemove: selection){
					wizardData.removeFile(new File(fileToRemove));
				}
				
				updateFileList();
				checkListSelection();
				checkIfPageComplete();
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {}
		});
		
		setControl(container);
		setPageComplete(false);
	}
	
	/**
	 * Updates the File List with the names of all the currently chosen Files.
	 */
	private void updateFileList(){
		lstFiles.setItems(wizardData.getChosenFileNames());
	}
	
	/**
	 * Checks whether or not there is a File selected and enables/disables the
	 * remove file button accordingly.
	 */
	private void checkListSelection() {
		if(lstFiles.getSelectionCount() == 0){
			btnRemoveFile.setEnabled(false);
		}
		else{
			btnRemoveFile.setEnabled(true);
		}
	}

	/**
	 * Sets whether or not the page is complete.
	 */
	private void checkIfPageComplete(){
		if(wizardData.isComplete()){
			setPageComplete(true);
		}
		else{
			setPageComplete(false);
		}
	}
}
