package LinGUIne.wizards;

import org.eclipse.equinox.p2.core.IProvisioningAgent;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.wizard.Wizard;

import LinGUIne.utilities.InstallUtils;

public class InstallWizard extends Wizard{

	private InstallWizardPage page;
	private P2Data data;
	
	public InstallWizard(IProvisioningAgent agent, P2Data d){
		super();
		data = d;
		setNeedsProgressMonitor(true);
	}
	
	@Override
	public void addPages() {
		page = new InstallWizardPage("Install New Plugin",data);
		addPage(page);
	}
	
	@Override
	public boolean performFinish() {
		boolean installComplete = InstallUtils.installIUs(data.getAgent(), data.getSelectedIUs(),data.getRepoLocation());
		if(installComplete == true){
			boolean restart = MessageDialog.openQuestion(data.getParent(), "Installation Complete",
					"In order for the newly installed plugins to start, LinGUIne needs to be restart. Restart now?");
			if(restart == true)
				data.getWorkbench().restart();
		}
		return installComplete;
	}

}
