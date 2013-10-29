package LinGUIne.wizards;

import org.eclipse.jface.wizard.Wizard;

/**
 * Analysis Wizard object; pieces the Wizard Pages together into
 * a comprehensive Wizard Dialog
 * 
 * @author Matthew Talbot
 */

public class AnalysisWizard extends Wizard {
	
  protected AnalysisWizardSelectFilePage one;
  protected AnalysisWizardSelectAnalysisPage two;
  protected AnalysisWizardSelectSettingsPage three;

  public AnalysisWizard() {
    super();
    setNeedsProgressMonitor(true);
  }

	/**
	 * Automagically sets the Wizard pages in order
	 * 
	 */
  @Override
  public void addPages() {
    one = new AnalysisWizardSelectFilePage();
    two = new AnalysisWizardSelectAnalysisPage();
    three = new AnalysisWizardSelectSettingsPage();
    addPage(one);
    addPage(two);
    addPage(three);
  }

  /**
   * If there are any actions to be performed when the Wizard closes, they would go in this method.
   * Pending actual content before we can fill this part out.
   * 
   */
  @Override
  public boolean performFinish() {
    return true;
  }
}