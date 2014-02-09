package LinGUIne.wizards;

import javax.inject.Inject;

import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.Wizard;

import LinGUIne.model.ProjectManager;

/**
 * Analysis Wizard object; pieces the Wizard Pages together into
 * a comprehensive Wizard Dialog
 * 
 * @author Matthew Talbot
 */

public class AnalysisWizard extends Wizard {
	
  private AnalysisWizardSelectFilePage selectFilePage;
  private AnalysisWizardSelectAnalysisPage selectAnalysisPage;
  private AnalysisWizardSelectSettingsPage settingsPage;

  @Inject
  private ProjectManager projectMan;
  private AnalysisData wizardData;
  
  public AnalysisWizard() {
    super();
    setNeedsProgressMonitor(true);
    
    wizardData = new AnalysisData();
  }

	/**
	 * Automagically sets the Wizard pages in order
	 * 
	 */
  @Override
  public void addPages() {
    selectFilePage = new AnalysisWizardSelectFilePage(wizardData, projectMan);
    selectAnalysisPage = new AnalysisWizardSelectAnalysisPage();
    settingsPage = new AnalysisWizardSelectSettingsPage();
    
    addPage(selectFilePage);
    addPage(selectAnalysisPage);
    addPage(settingsPage);
  }

  @Override
  public IWizardPage getNextPage(IWizardPage page){
	  //TODO: Swap out settingsPage for page provided by the Analysis plugin
	  return super.getNextPage(page);
  }
  
  /**
   * If there are any actions to be performed when the Wizard closes, they would go in this method.
   * Pending actual content before we can fill this part out.
   * 
   */
  @Override
  public boolean performFinish() {
	  //TODO: Run analysis job
    return true;
  }
}