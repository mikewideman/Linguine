package LinGUIne.wizards;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

public class AboutDialog extends Dialog{

	private Shell shell;
	private ScrolledComposite topScroller;
	private Table contentTable;
	private TableColumn idColumn, versionColumn;
	
	private Label topLabel;
	
	public AboutDialog(Shell parent) {
		super(parent);
	}

	public void open(){
		shell = new Shell(getParent());
		shell.setText("About LinGUIne");
		GridLayout shellLayout = new GridLayout();
		shellLayout.numColumns = 1;
		shell.setLayout(shellLayout);
		topScroller = new ScrolledComposite(shell, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		topLabel = new Label(topScroller, SWT.NONE);
		topLabel.setText("Example text data.");
		topScroller.setContent(topLabel);
		contentTable = new Table(shell, SWT.VIRTUAL | SWT.BORDER);
		contentTable.setLinesVisible(true);
		contentTable.setHeaderVisible(true);
		idColumn = new TableColumn (contentTable, SWT.NONE);
		idColumn.setText("Bundle ID");
		versionColumn = new TableColumn(contentTable, SWT.NONE);
		versionColumn.setText("Version");
		idColumn.pack();
		versionColumn.pack();
	}
	
}
