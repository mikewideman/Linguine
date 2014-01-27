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
 * A WizardPage for the NewProjectWizard which prompts the user to input a name
 * for their new Project.
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
	 * Creates a new instance of the page with the given empty Project object,
	 * and the given instance of ProjectManager.
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
		
		lstFiles = new List(container, SWT.BORDER | SWT.V_SCROLL);
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
				FileDialog openDialog = new FileDialog(finalParent.getShell(), SWT.OPEN);
				openDialog.setFilterExtensions(new String[]{
						wizardData.getChosenImporter().getFileMask()});
				
				String chosenFile = openDialog.open();
				
				wizardData.addFile(new File(chosenFile));
				updateFileList();
				checkListSelection();
				checkIfPageComplete();
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
				
				wizardData.removeFile(new File(selection[0]));
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
	
	private void updateFileList(){
		lstFiles.setItems(wizardData.getChosenFileNames());
	}
	
	private void checkListSelection() {
		if(lstFiles.getSelectionCount() == 0){
			btnRemoveFile.setEnabled(false);
		}
		else{
			btnRemoveFile.setEnabled(true);
		}
	}

	private void checkIfPageComplete(){
		if(wizardData.isComplete()){
			setPageComplete(true);
		}
		else{
			setPageComplete(false);
		}
	}
}
