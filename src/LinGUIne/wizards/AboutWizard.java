package LinGUIne.wizards;

import org.eclipse.jface.wizard.Wizard;

/**
 * Wizard for viewing the installed bundles of the application
 * 
 * @author Matthew Talbot
 */

public class AboutWizard extends Wizard{
	
		private AboutWizardPage page;	
		
		/**
		 * Creates a new AboutWizard.
		 */
		public AboutWizard(){
			super();
			setWindowTitle("About LinGUIne");
		}
		
		/**
		 * Sets up the pages in the wizard.
		 */
		@Override
		public void addPages() {
			page = new AboutWizardPage("Installed Software");
			addPage(page);
		}
		
		@Override
		public boolean performFinish() {
			// TODO Auto-generated method stub
			return false;
		}

}



