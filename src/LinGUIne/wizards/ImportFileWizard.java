package LinGUIne.wizards;

import javax.inject.Inject;

import org.eclipse.core.commands.Command;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.NotEnabledException;
import org.eclipse.core.commands.NotHandledException;
import org.eclipse.core.commands.common.NotDefinedException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.SafeRunner;
import org.eclipse.e4.core.commands.ECommandService;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.di.UIEventTopic;
import org.eclipse.e4.core.di.extensions.EventTopic;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.swt.widgets.Display;

import LinGUIne.events.ProjectEvent;
import LinGUIne.model.ProjectManager;
import LinGUIne.utilities.Monitor;
import LinGUIne.utilities.SafeImporter;

/**
 * Wizard for importing one or more Files into a Project.
 * 
 * @author Kyle Mullins
 */
public class ImportFileWizard extends Wizard {

	@Inject
	private ProjectManager projectMan;
	
	@Inject
	private ECommandService commandService;
	
	private ImportFileData wizardData;
	private ImportFileWizardSetupPage setupPage;
	private ImportFileWizardChooseFilesPage chooseFilesPage;
	
	private Monitor newProjectMonitor;
	
	/**
	 * Creates a new ImportFileWizard.
	 */
	public ImportFileWizard(){
		super();
		
		wizardData = new ImportFileData();
		newProjectMonitor = new Monitor();
	}
	
	/**
	 * Sets up the pages in the wizard.
	 */
	@Override
	public void addPages(){
		setupPage = new ImportFileWizardSetupPage(wizardData, projectMan);
		chooseFilesPage = new ImportFileWizardChooseFilesPage(wizardData);
		
		addPage(setupPage);
		addPage(chooseFilesPage);
	}
	
	/**
	 * Runs the import job once the wizard has finished.
	 */
	@Override
	public boolean performFinish() {
		if(wizardData.shouldCreateNewProject()){
			Command newProjectCommand = commandService.getCommand(
					"linguine.command.newProject");
			
			try(Monitor theMonitor = newProjectMonitor.enter()){
				IStatus status = (IStatus)newProjectCommand.executeWithChecks(
						new ExecutionEvent());
				
				if(status.isOK()){
					while(wizardData.getChosenProject() == null){
						//Processes any waiting events
						Display.getCurrent().readAndDispatch();
						newProjectMonitor.await();
					}
				}
			}
			catch(InterruptedException ie){
				//Monitor was interrupted
				ie.printStackTrace();
			}
			catch(ExecutionException | NotDefinedException
					| NotEnabledException | NotHandledException e1) {
				//TODO: Oh no the command is not defined!
				e1.printStackTrace();
			}
		}
		
		if(wizardData.getChosenProject() != null){
			SafeImporter safeImporter = new SafeImporter(getShell(),
					wizardData.getChosenImporter(), wizardData.getChosenFiles(),
					wizardData.getChosenProject());
			
			SafeRunner.run(safeImporter);
			
			return true;
		}
		
		return false;
	}
	
	@Inject
	@Optional
	public void projectEvent(@EventTopic(ProjectManager.PROJECT_ADDED)
			ProjectEvent projectEvent){
		try(Monitor theMonitor = newProjectMonitor.enter()){
			wizardData.setProject(projectEvent.getAffectedProject());

			newProjectMonitor.signal();
		}
	}
}
