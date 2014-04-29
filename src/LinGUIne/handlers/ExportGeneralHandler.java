package LinGUIne.handlers;

import java.util.TreeMap;

import javax.inject.Inject;
import javax.inject.Named;

import org.eclipse.e4.core.contexts.ContextInjectionFactory;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Shell;

import LinGUIne.wizards.SelectCommandWizard;

public class ExportGeneralHandler {

	@Inject
	private MApplication application;
	
	@Execute
	public void execute(@Named(IServiceConstants.ACTIVE_SHELL) Shell shell){
		TreeMap<String, String> commands = new TreeMap<String, String>();
		
		commands.put("Export File", "linguine.command.export.result");
		commands.put("Export Annotation", "linguine.command.export.annotation");
		commands.put("Export Visualization", "linguine.command.export.visualization");
		
		SelectCommandWizard selectCommandWizard = new SelectCommandWizard(
				"Select Wizard", "Select a wizard to run.", commands);
		WizardDialog wizardDialog = new WizardDialog(shell, selectCommandWizard);
		
		ContextInjectionFactory.inject(selectCommandWizard, application.getContext());

		int retval = wizardDialog.open();
	}
}
