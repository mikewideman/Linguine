 
package LinGUIne.parts.advanced;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.eclipse.e4.ui.di.Focus;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.ExpandBar;
import org.eclipse.swt.widgets.ExpandItem;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Scale;

public class SettingsPart {
	
	private ExpandBar settingsExpandBar;
	
	@Inject
	public SettingsPart() {
		//TODO Your code here
	}
	
	@PostConstruct
	public void postConstruct(Composite parent) {
		settingsExpandBar = new ExpandBar(parent,SWT.V_SCROLL);
		//Setting the "background panel" for the view
		Composite panel = new Composite(settingsExpandBar,SWT.NONE);
		GridLayout layout = new GridLayout();
		layout.marginLeft = layout.marginTop = layout.marginRight = layout.marginBottom = 10;
		layout.verticalSpacing = 10;
		panel.setLayout(layout);
		//Adding components to the :background panel"
		Label label = new Label(panel,SWT.HORIZONTAL);
		label.setText("Test");
		Scale scale = new Scale(panel,SWT.HORIZONTAL);
		//Establishing an expandable menu with the "background panel" as the content
		ExpandItem generalItem = new ExpandItem(settingsExpandBar,SWT.NONE,0);
		generalItem.setText("General Settings");
		generalItem.setHeight(panel.computeSize(SWT.DEFAULT, SWT.DEFAULT).y);
		generalItem.setControl(panel);
	}
	
	@Focus
	public void onFocus() {
		//TODO Your code here
	}
	
	
}