package LinGUIne.wizards;

import java.util.TreeMap;

import javax.inject.Inject;

import org.eclipse.core.commands.Command;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.NotEnabledException;
import org.eclipse.core.commands.NotHandledException;
import org.eclipse.core.commands.common.NotDefinedException;
import org.eclipse.e4.core.commands.ECommandService;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.wizard.Wizard;

/**
 * A Generic Wizard for choosing between a number of Commands, with the chosen
 * Command executed once the Wizard closes.
 * 
 * @author Kyle Mullins
 */
public class SelectCommandWizard extends Wizard {

	@Inject
	private ECommandService commandService;
	
	private SelectCommandWizardPage wizardPage;
	private TreeMap<String, String> commandOptions;
	
	/**
	 * Creates a new SelectCommandWizard to be displayed with the given title,
	 * message, and command options presented.
	 * 
	 * @param title		The title of the Wizard.
	 * @param message	The message displayed at the top of the Wizard.
	 * @param options	A mapping from command name to the id of the command,
	 * 					enumerating all options to be presented to the user.
	 */
	public SelectCommandWizard(String title, String message,
			TreeMap<String, String> options){
		
		super();
		
		commandOptions = options;
		wizardPage = new SelectCommandWizardPage(title, message,
				commandOptions.keySet());
	}
	
	@Override
	public void addPages(){
		addPage(wizardPage);
	}
	
	@Override
	public boolean performFinish() {
		String selectedOption = wizardPage.getSelectedOption();
		String commandId = commandOptions.get(selectedOption);
		Command selectedCommand = commandService.getCommand(commandId);
		
		try{
			selectedCommand.executeWithChecks(new ExecutionEvent());
		}
		catch(ExecutionException | NotDefinedException
				| NotEnabledException | NotHandledException e){
			MessageDialog.openError(getShell(), "Error", "The selected Command"
					+ " could not be executed successfully.");
			
			return false;
		}
		
		return true;
	}
}
