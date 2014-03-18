package LinGUIne.wizards;

import org.eclipse.jface.wizard.Wizard;

public class InstallWizard extends Wizard{

	private InstallWizardPage page;
	
	public InstallWizard(){
		super();
		setNeedsProgressMonitor(true);
	}
	
	@Override
	public void addPages() {
		page = new InstallWizardPage("Install New Plugin");
		addPage(page);
	}
	
	@Override
	public boolean performFinish() {
		// TODO Auto-generated method stub
		return false;
	}

}
