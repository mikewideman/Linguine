package LinGUIne.wizards;

import java.util.Collection;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.List;

public class SelectCommandWizardPage extends WizardPage {

	private List lstOptions;
	
	private Collection<String> commandOptions;
	private String selectedOption;
	
	public SelectCommandWizardPage(String title, String message,
			Collection<String> options){
		
		super(title);
		setTitle(title);
		setMessage(message);
		
		commandOptions = options;
	}
	
	@Override
	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NONE);
		container.setLayout(new GridLayout(1, false));
		
		lstOptions = new List(container, SWT.BORDER | SWT.V_SCROLL |
				SWT.H_SCROLL);
		lstOptions.setLayoutData(new GridData(GridData.FILL_BOTH));
		lstOptions.addSelectionListener(new SelectionListener(){
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(lstOptions.getSelectionCount() != 0){
					selectedOption = lstOptions.getSelection()[0];
					setPageComplete(true);
				}
				else{
					setPageComplete(false);
				}
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {}
		});
		
		for(String option: commandOptions){
			lstOptions.add(option);
		}
		
		setPageComplete(false);
		setControl(lstOptions);
	}
	
	public String getSelectedOption(){
		return selectedOption;
	}
}
