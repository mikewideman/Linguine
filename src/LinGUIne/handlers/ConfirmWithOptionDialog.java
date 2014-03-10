package LinGUIne.handlers;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

/**
 * Simple confirm Dialog which also presents the user with a check box option.
 * 
 * @author Kyle Mullins
 */
public class ConfirmWithOptionDialog extends Dialog {

	private String dialogTitle;
	private String dialogMessage;
	private String dialogOptionText;
	
	private boolean optionChosen;
	
	private Label lblMessage;
	private Button btnOption;
	
	/**
	 * Creates a new Dialog with the given title, primary message, 
	 * check box optionText.
	 * 
	 * @param parentShell	The shell to be used.
	 * @param title			The title of the Dialog.
	 * @param message		The primary message of the Dialog to which the user
	 * 						is responding.
	 * @param optionText	Text associated with the check box option with which
	 * 						the user is presented.
	 */
	public ConfirmWithOptionDialog(Shell parentShell, String title,
			String message, String optionText) {
		super(parentShell);
		
		dialogTitle = title;
		dialogMessage = message;
		dialogOptionText = optionText;
		
		optionChosen = false;
	}
	
	/**
	 * Returns whether or not the presented option was selected.
	 */
	public boolean wasOptionChosen(){
		return optionChosen;
	}
	
	@Override
	protected Control createDialogArea(Composite parent){
		Composite container = (Composite)super.createDialogArea(parent);
		
		lblMessage = new Label(container, SWT.NONE);
		lblMessage.setText(dialogMessage);
		
		GridData messageGridData = new GridData(SWT.BEGINNING, SWT.CENTER,
				false, false);
		lblMessage.setLayoutData(messageGridData);
		
		@SuppressWarnings("unused")
		Label spacing = new Label(container, SWT.NONE);
		
		btnOption = new Button(container, SWT.CHECK);
		btnOption.setText(dialogOptionText);
		btnOption.addSelectionListener(new SelectionListener(){
			@Override
			public void widgetSelected(SelectionEvent e) {
				optionChosen = btnOption.getSelection();
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {}
		});
		
		GridData optionGridData = new GridData(SWT.BEGINNING, SWT.CENTER, false,
				false);
		btnOption.setLayoutData(optionGridData);
		
		return container;
	}
	
	@Override
	protected void configureShell(Shell newShell){
		super.configureShell(newShell);
		
		newShell.setText(dialogTitle);
	}
	
	@Override
	protected Point getInitialSize(){
		return new Point(450, 200);
	}
}
