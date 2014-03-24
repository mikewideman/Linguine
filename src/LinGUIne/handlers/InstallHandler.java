 
package LinGUIne.handlers;

import javax.inject.Inject;
import javax.inject.Named;

import org.eclipse.e4.core.contexts.ContextInjectionFactory;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.di.UISynchronize;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.e4.ui.workbench.IWorkbench;
import org.eclipse.equinox.p2.core.IProvisioningAgent;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Shell;

import LinGUIne.wizards.InstallWizard;

public class InstallHandler{
	@Inject
	private MApplication application;
	
	@Execute
	public void execute(final IProvisioningAgent agent, final Shell parent, final UISynchronize sync, final IWorkbench workbench){
		InstallWizard installWizard = new InstallWizard(agent);
		WizardDialog wizardDialog = new WizardDialog(parent, installWizard);
		ContextInjectionFactory.inject(installWizard, application.getContext());
		int retval = wizardDialog.open();
		if(retval == WizardDialog.OK){
		}
	}
}