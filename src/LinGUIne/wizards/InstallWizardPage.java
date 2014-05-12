package LinGUIne.wizards;

import org.eclipse.equinox.p2.metadata.IInstallableUnit;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;


/**
 * Wizard Page for Installing plugins and features to the application.
 * 
 * @author Matthew Talbot
 */
public class InstallWizardPage extends WizardPage implements SelectionListener{

	private Composite container;
	private Button browseButton;
	private Label repositoryLabel, directoryLabel;
	private Table contentTable;
	private TableColumn idColumn, versionColumn;
	private DirectoryDialog directoryDialog;
	
	
	private P2Data data;
	
	/**
	 * Creates a new instance of the page with the given P2Data.
	 * 
	 * @param pageName the desired name of the page
	 * @param data the installation data contained in the wizard
	 */
	protected InstallWizardPage(String pageName, P2Data data) {
		super(pageName);
		this.data = data;
		setTitle("Select a valid repository");
		setDescription("A valid repository is a directory that contains both the artifacts.jar and content.jar files.");
	}

	/**
	 * Generates all the UI components
	 */
	@Override
	public void createControl(Composite parent) {
		//Establish the Layouts
		RowLayout parentLayout = new RowLayout();
		parentLayout.type = SWT.VERTICAL;
		parentLayout.marginTop = 5;
		parentLayout.marginBottom = 5;
		parentLayout.marginLeft = 5;
		parentLayout.marginRight = 5;
		parentLayout.justify = true;
		
		RowLayout topLayout = new RowLayout();
		topLayout.type = SWT.HORIZONTAL;
		topLayout.marginTop = 5;
		topLayout.marginBottom = 5;
		topLayout.marginLeft = 5;
		topLayout.marginRight = 5;
		topLayout.justify = true;

		container = new Composite(parent, SWT.NONE);
		container.setLayout(parentLayout);
		Composite topContainer = new Composite(container,SWT.NONE);
		topContainer.setLayout(topLayout);
		
		repositoryLabel = new Label(topContainer,SWT.NONE);
		repositoryLabel.setText("Repository:");
		repositoryLabel.setLayoutData(new RowData(60,15));
		
		directoryLabel = new Label(topContainer,SWT.NONE);
		directoryLabel.setText("");
		directoryLabel.setLayoutData(new RowData(500, 15));
		
		browseButton = new Button(topContainer,SWT.NONE);
		browseButton.setText("Browse");
		browseButton.addSelectionListener(this);
		browseButton.setLayoutData(new RowData(60,24));
		
		contentTable = new Table(container,SWT.MULTI|SWT.CHECK|SWT.VIRTUAL|SWT.BORDER);
		contentTable.setLayoutData(new RowData(620,300));
		contentTable.setLinesVisible(true);
		contentTable.setHeaderVisible(true);
		contentTable.addSelectionListener(this);
		idColumn = new TableColumn (contentTable, SWT.NONE);
		idColumn.setText("Plugin ID");
		versionColumn = new TableColumn(contentTable, SWT.NONE);
		versionColumn.setText("Version");
		idColumn.pack();
		versionColumn.pack();
		
		parent.pack();
		container.pack();
		topContainer.pack();
		
		setControl(parent);
		setPageComplete(false);
	}

	/**
	 * Selection Listener handlers
	 * @param e The event triggered when a user selects a component
	 */
	@Override
	public void widgetSelected(SelectionEvent e) {
		if(e.getSource() == browseButton){
			directoryDialog = new DirectoryDialog(browseButton.getShell());
			directoryDialog.setText("Install New Plugin");
			directoryDialog.setMessage("Select a valid repository:");
			String directory = directoryDialog.open();
			directoryLabel.setText(directory);
			boolean valid = data.initializeRepositoryData(directory);
			if(!valid){
				
			}
			setDisplayData();
		}
		
		if(e.getSource() == contentTable){
			//If the action was a check/uncheck
			if(e.detail == SWT.CHECK){
				int index = contentTable.indexOf((TableItem)e.item);
				//If the checked index is present in selectedIUs, remove it
				if(data.getSelectedIUs().contains(data.getRepositoryIUs().get(index))){
					data.getSelectedIUs().remove(data.getRepositoryIUs().get(index));
				}
				//Otherwise add it to selectedIUs
				else{
					data.getSelectedIUs().add(data.getRepositoryIUs().get(index));
				}
				if(data.getSelectedIUs().size() > 0){
					setPageComplete(true);
				}
				else{
					setPageComplete(false);
				}
			}
		}
	}
	
	/**
	 * Generates the data for the table component
	 */
	public void setDisplayData(){
		contentTable.removeAll();
		for(int i = 0; i < data.getRepositoryIUs().size(); i++){
			IInstallableUnit currentIU = data.getRepositoryIUs().get(i);
			TableItem item = new TableItem(contentTable,SWT.NONE);
			item.setText(0,data.getRepositoryIUs().get(i).getId());
			item.setText(1,data.getRepositoryIUs().get(i).getVersion().toString());
		}
		idColumn.pack();
		versionColumn.pack();
	}

	@Override
	public void widgetDefaultSelected(SelectionEvent e) {
		// TODO Auto-generated method stub
		
	}

}
