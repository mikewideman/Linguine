package LinGUIne.wizards;

import org.eclipse.equinox.p2.core.IProvisioningAgent;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.wizard.Wizard;

import LinGUIne.utilities.InstallUtils;

/**
 * Wizard for installing new bundles (features or plugins) to the application
 * 
 * @author Matthew Talbot
 */

public class InstallWizard extends Wizard{

	private InstallWizardPage page;
	private P2Data data;
	
	
	/**
	 * Creates a new InstallWizard.
	 */
	public InstallWizard(IProvisioningAgent agent, P2Data d){
		super();
		setWindowTitle("Install New Software");
		data = d;
		setNeedsProgressMonitor(true);
	}
	
	/**
	 * Sets up the pages in the wizard.
	 */
	@Override
	public void addPages() {
		page = new InstallWizardPage("Install New Plugin",data);
		addPage(page);
	}
	
	/**
	 * Runs the install job once the wizard has finished.
	 */
	@Override
	public boolean performFinish() {
		boolean installComplete = InstallUtils.installIUs(data.getAgent(), data.getSelectedIUs(),data.getRepoLocation());
		
		if(installComplete){
			boolean restart = MessageDialog.openQuestion(data.getParent(), "Installation Complete",
					"In order for the newly installed plugins to start, LinGUIne needs to be restarted. Restart now?");
			
			if(restart == true){
				data.getWorkbench().restart();
			}
		}
		else{
			MessageDialog.openError(getShell(), "Could not install selected plugins",
					"There was an error trying to install the selected plugins.");
		}
		
		return installComplete;
	}

}
