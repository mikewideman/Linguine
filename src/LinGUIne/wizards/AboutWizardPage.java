package LinGUIne.wizards;

import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.equinox.p2.engine.IProfileRegistry;
import org.eclipse.equinox.p2.metadata.IInstallableUnit;
import org.eclipse.equinox.p2.operations.ProvisioningSession;
import org.eclipse.equinox.p2.query.IQuery;
import org.eclipse.equinox.p2.query.IQueryResult;
import org.eclipse.equinox.p2.query.IQueryable;
import org.eclipse.equinox.p2.query.QueryUtil;
import org.eclipse.equinox.p2.ui.ProvisioningUI;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Wizard Page for viewing installed bundles running on the application.
 * 
 * @author Matthew Talbot
 */
public class AboutWizardPage extends WizardPage{

	private Composite container;
	private Table contentTable;
	private TableColumn idColumn, versionColumn;
	private ArrayList<IInstallableUnit> installedBundles;
	
	/**
	 * Creates a new instance of the About page.
	 * 
	 * @param pageName the desired name of the page
	 */
	protected AboutWizardPage(String pageName) {
		super(pageName);
		setTitle("Installed Software");
		installedBundles = new ArrayList<IInstallableUnit>();
		getBundleInfo();
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
		
		contentTable = new Table(container,SWT.MULTI|SWT.CHECK|SWT.VIRTUAL|SWT.BORDER);
		contentTable.setLayoutData(new RowData(620,300));
		contentTable.setLinesVisible(true);
		contentTable.setHeaderVisible(true);
		
		idColumn = new TableColumn (contentTable, SWT.NONE);
		idColumn.setText("Plugin ID");
		idColumn.setWidth(400);
		
		versionColumn = new TableColumn(contentTable, SWT.NONE);
		versionColumn.setText("Version");
		versionColumn.setWidth(200);
		
		setDisplayData();
		parent.pack();
		container.pack();
		topContainer.pack();

		setControl(parent);
		setPageComplete(false);
	}

		
	
	/**
	 * Generates the data for the table component
	 */
	public void setDisplayData(){
		contentTable.removeAll();
		for(int i = 0; i < installedBundles.size(); i++){
			TableItem item = new TableItem(contentTable,SWT.NONE);
			item.setText(0,installedBundles.get(i).getId());
			item.setText(1,installedBundles.get(i).getVersion().toString());
		}
	}
	
	/**
	 * Gathers a list of all the IInstallableUnits currently running in the application during runtime.
	 */
	public void getBundleInfo(){
		ProvisioningUI provisioningUI = ProvisioningUI.getDefaultUI();
		String profileId = provisioningUI.getProfileId();
		ProvisioningSession provisioningSession = provisioningUI.getSession();
		IProfileRegistry profileReg = (IProfileRegistry)provisioningSession.getProvisioningAgent().getService(IProfileRegistry.SERVICE_NAME);
		IQueryable<IInstallableUnit> queryable = profileReg.getProfile(profileId);
		IQuery<IInstallableUnit> query = QueryUtil.createIUAnyQuery();
		IQueryResult<IInstallableUnit> result = queryable.query(query, new NullProgressMonitor());
		for (final IInstallableUnit iu : result)
		  {
			installedBundles.add(iu);
		  }
		Collections.sort(installedBundles);
	}
	
}
