 
package LinGUIne.parts.advanced;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.eclipse.e4.ui.di.Focus;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.workbench.modeling.ESelectionService;
import org.eclipse.e4.ui.workbench.modeling.ISelectionListener;
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
	public SettingsPart(ESelectionService selectionService) {
		selectionService.addSelectionListener(
				"linguine.part.advanced.dataExplorerPart",
				new DataEditorSelectionListener());
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
	public void onFocus() {}
	
	class DataEditorSelectionListener implements ISelectionListener{
		@Override
		public void selectionChanged(MPart part, Object selection) {
			if(selection != null){
				if(selection instanceof ProjectDataEditorTab){
					ProjectDataEditorTab editorTab =
							(ProjectDataEditorTab)selection;
					
					//TODO: Ask editorTab for its SettingsPage instance and display it
					System.out.println(editorTab.getText());
				}
			}
			else{
				System.out.println("Editor empty");
			}
		}
	}
}