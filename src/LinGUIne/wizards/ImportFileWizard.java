package LinGUIne.wizards;

import java.io.IOException;

import javax.inject.Inject;

import org.eclipse.core.runtime.SafeRunner;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.SWT;

import LinGUIne.model.Project;
import LinGUIne.model.ProjectManager;
import LinGUIne.utilities.SafeImporter;

/**
 * Wizard for importing one or more Files into a Project.
 * 
 * @author Kyle Mullins
 */
public class ImportFileWizard extends Wizard {

	@Inject
	private ProjectManager projectMan;
	
	private ImportFileData wizardData;
	private ImportFileWizardSetupPage setupPage;
	private ImportFileWizardChooseFilesPage chooseFilesPage;
	private boolean termsAccepted; //Acceptance of terms for twitter import
	
	/**
	 * Creates a new ImportFileWizard.
	 */
	public ImportFileWizard(){
		super();
		
		wizardData = new ImportFileData();
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
			//Launch NewProjectWizard and capture the created Project
			NewProjectWizard projectWizard = new NewProjectWizard(projectMan);
			WizardDialog wizardDialog = new WizardDialog(getShell(), projectWizard);
			
			int retval = wizardDialog.open();
			
			if(retval == WizardDialog.OK) {
				Project newProj = projectWizard.getProject();
				newProj.setParentDirectory(projectMan.getWorkspace());
				
				try {
					newProj.createProjectFiles();
					projectMan.addProject(newProj);
					wizardData.setProject(newProj);
				}
				catch(IOException ioe) {
					MessageDialog.openError(getShell(), "Error", "Could not create "
							+ "Project directory: " + ioe.getMessage());
				}
			}
		}
		if(wizardData.isInternetSource()){
			TwitDataWizard twitterWizard = new TwitDataWizard(wizardData);
			WizardDialog tWizDialog = new WizardDialog(getShell(), twitterWizard);
			
			//Terms haven't been accepted yet, if they aren't just close out.
			if(!termsAccepted){
			
				if(MessageDialog.open(MessageDialog.QUESTION, getShell(), "Terms of Use", termsOfUse, SWT.NONE)){
					//Terms accepted, change boolean and open twitter wizard
					termsAccepted = true;
					tWizDialog.open();
				}
			}
			else{
				tWizDialog.open();
			}
			
			
		}
		
		if(wizardData.getChosenProject() != null){
			SafeImporter safeImporter = new SafeImporter(getShell(),
					wizardData.getChosenImporter(), wizardData.getChosenFiles(),
					wizardData.getChosenProject(),wizardData.getInternetSourceDetails());
			
			SafeRunner.run(safeImporter);
			
			return true;
		}
		
		return false;
	}
	
	private static final String termsOfUse = "Terms of Use:\n" +
	"1. Acquisition of data into LinGUIne through third parties such as Twitter"
	+ " or other social media API's requires adherence to said third parties terms"
	+ " and use of services. 2. Disclaimer of Warranties - LinGUIne and any other "
	+ "services are provided “as is” and “as-available,” with all"
	+ "faults, and without warranties of any kind. LinGUIne and its vendors and licensors"
	+ "disclaim all other warranties, express and implied, including, but not limited"
	+ "to, the implied warranties of merchantability, fitness for a particular purpose,"
	+ " quiet enjoyment, quality of information, and title/non-infringement. all third party"
	+ " materials are provided as-is, without warranties of any kind. LinGUIne and"
	+ " its vendors and licensors makes no warranties of any kind, express or "
	+ "implied, relating to any present or future methodology employed in its"
	+ " gathering or reproducing of any third party material, or as to the accuracy,"
	+ " currency, or comprehensiveness of the same. user expressly agrees and "
	+ "acknowledges that use of LinGUIne and any other services is at user’s sole "
	+ "risk. no oral or written information or advice given by LinGUIne or its "
	+ "authorized representatives shall create any other warranties or in any way "
	+ "increase the scope of LinGUIne's obligations hereunder. LinGUIne may be used to "
	+ "access and transfer information over the internet. user acknowledges and "
	+ "agrees that LinGUIne and its vendors and licensors do not operate or control "
	+ "the internet and that: (a) viruses, worms, trojan horses, or other undesirable"
	+ " data or software; or (b) unauthorized third parties (e.g., hackers) may"
	+ "attempt to obtain access to and damage user’s data, websites, computers, or networks."
	+ " LinGUIne shall not be responsible or liable for any such activities nor shall "
	+ "any such activities constitute a breach by LinGUIne of its obligations."
	+ "\n3. Limitation of Liability and Damages -  Neither LinGUIne nor its vendors "
	+ "and licensors shall have any liability to user or any third party for any loss "
	+ "of profits, sales, business, data, or other incidental, consequential, or "
	+ "special loss or damage, including exemplary and punitive damages, of any "
	+ "kind or nature resulting from or arising out of this agreement, LinGUIne, "
	+ "and any services rendered hereunder.  "
	+ "\n\nCLICKING THE BOX THAT CONTAINS THE WORD “YES” ACKNOWLEDGES THAT YOU "
	+ "HAVE READ, UNDERSTOOD AND AGREED TO BE BOUND BY THE ABOVE TERMS";
}
