package LinGUIne.wizards;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

public class InstallWizardPage extends WizardPage implements SelectionListener{

	private Button browseButton;
	private Label repositoryLabel, directoryLabel;
	private Table contentTable;
	private DirectoryDialog directoryDialog;
	
	
	
	protected InstallWizardPage(String pageName) {
		super(pageName);
		setTitle("Install New Plugin");
		setDescription("Select a valid repository directory");
	}

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

		Composite container = new Composite(parent, SWT.NONE);
		container.setLayout(parentLayout);
		Composite topContainer = new Composite(container,SWT.NONE);
		topContainer.setLayout(topLayout);
		
		repositoryLabel = new Label(topContainer,SWT.NONE);
		repositoryLabel.setText("Repository:");
		repositoryLabel.setLayoutData(new RowData(60,15));
		
		directoryLabel = new Label(topContainer,SWT.BORDER);
		directoryLabel.setText("1234567890");
		directoryLabel.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));
		directoryLabel.setLayoutData(new RowData(500, 15));
		
		browseButton = new Button(topContainer,SWT.NONE);
		browseButton.setText("Browse");
		browseButton.addSelectionListener(this);
		browseButton.setLayoutData(new RowData(60,24));
		
		contentTable = new Table(container,SWT.MULTI|SWT.CHECK|SWT.VIRTUAL|SWT.BORDER);
		contentTable.setLayoutData(new RowData(620,300));
		contentTable.setLinesVisible(true);
		contentTable.setHeaderVisible(true);
		String[] titles = {"Plugin Name", "version", "ID"};
		for (int i=0; i<titles.length; i++) {
			TableColumn column = new TableColumn (contentTable, SWT.NONE);
			column.setText (titles [i]);
		}
		for (int i=0; i<titles.length; i++) {
			contentTable.getColumn(i).pack();
		}
		
		parent.pack();
		topContainer.pack();
		
		
		//contentTable = new Table(container,SWT.NONE);
		
		setControl(parent);
		setPageComplete(false);
	}

	@Override
	public void widgetSelected(SelectionEvent e) {
		if(e.getSource() == browseButton){
			directoryDialog = new DirectoryDialog(browseButton.getShell());
			directoryDialog.setText("Install New Plugin");
			directoryDialog.setMessage("Select a valid repository:");
			String directory = directoryDialog.open();
			directoryLabel.setText(directory);
		}
	}

	@Override
	public void widgetDefaultSelected(SelectionEvent e) {
		// TODO Auto-generated method stub
		
	}

}
