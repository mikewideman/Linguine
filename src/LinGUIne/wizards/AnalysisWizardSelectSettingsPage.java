package LinGUIne.wizards;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

/**
 * Analysis Page object; this is the GUI components
 * for the third step in the Analysis Wizard. The user
 * can change any of the (context sensitive) settings
 * for the analyses.
 * 
 * @author Matthew Talbot
 */

public class AnalysisWizardSelectSettingsPage extends WizardPage {
  private Text text1;
  private Composite container;

  public AnalysisWizardSelectSettingsPage() {
    super("Analysis Wizard");
    setTitle("Analysis Wizard - Step 3");
    setDescription("Modify the settings for the analyses");
    setControl(text1);
  }

  @Override
  public void createControl(Composite parent) {
    container = new Composite(parent, SWT.NONE);
    GridLayout layout = new GridLayout();
    container.setLayout(layout);
    layout.numColumns = 2;
    Label label1 = new Label(container, SWT.NONE);
    label1.setText("Say hello to Fred");

    text1 = new Text(container, SWT.BORDER | SWT.SINGLE);
    text1.setText("");
    text1.addKeyListener(new KeyListener() {

      @Override
      public void keyPressed(KeyEvent e) {
        // TODO Auto-generated method stub

      }

      @Override
      public void keyReleased(KeyEvent e) {
        if (!text1.getText().isEmpty()) {
          setPageComplete(true);
        }
      }

    });
    GridData gd = new GridData(GridData.FILL_HORIZONTAL);
    text1.setLayoutData(gd);
    Label labelCheck = new Label(container, SWT.NONE);
    labelCheck.setText("This is a check");
    Button check = new Button(container, SWT.CHECK);
    check.setSelection(true);
    // Required to avoid an error in the system
    setControl(container);
    setPageComplete(false);
  }

  public String getText1() {
    return text1.getText();
  }
}
 
