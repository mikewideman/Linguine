package LinGUIne.wizards;

import org.eclipse.equinox.p2.core.IProvisioningAgent;
import org.eclipse.jface.wizard.Wizard;

import LinGUIne.utilities.InstallUtils;

public class InstallWizard extends Wizard{

	private InstallWizardPage page;
	private P2Data data;
	
	public InstallWizard(IProvisioningAgent agent){
		super();
		data = new P2Data(agent);
		setNeedsProgressMonitor(true);
	}
	
	@Override
	public void addPages() {
		page = new InstallWizardPage("Install New Plugin",data);
		addPage(page);
	}
	
	@Override
	public boolean performFinish() {
		return InstallUtils.installIUs(data.getAgent(), data.getSelectedIUs(),data.getRepoLocation());
	}

}
