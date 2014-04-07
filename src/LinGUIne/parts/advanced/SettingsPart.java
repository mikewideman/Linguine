 
package LinGUIne.parts.advanced;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.eclipse.e4.ui.di.Focus;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.workbench.modeling.ESelectionService;
import org.eclipse.e4.ui.workbench.modeling.ISelectionListener;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

import LinGUIne.extensions.IEditorSettings;

/**
 * Container part to show the EditorSettings view provided by the currently
 * open ProjectDataEditor.
 * 
 * @author Kyle Mullins
 */
public class SettingsPart {
	
	private StackLayout mainLayout;
	private Composite mainParent;
	private Composite currentComposite;
	private Composite defaultComposite;
	
	@Inject
	public SettingsPart(ESelectionService selectionService) {
		//Register for the event manually to avoid getting selection changes for
		//other parts
		selectionService.addSelectionListener(
				"linguine.part.advanced.dataExplorerPart",
				new DataEditorSelectionListener());
	}
	
	@PostConstruct
	public void postConstruct(Composite parent) {
		mainParent = parent;
		
		mainLayout = new StackLayout();
		mainParent.setLayout(mainLayout);
		
		defaultComposite = new Composite(parent, SWT.BORDER);
		defaultComposite.setLayout(new GridLayout(1, false));
		
		Label lblDefaultInfo = new Label(defaultComposite, SWT.NONE);
		lblDefaultInfo.setText("No editor settings available.");
		
		currentComposite = defaultComposite;
		mainLayout.topControl = currentComposite;
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
					
					if(editorTab.hasEditorSettings()){
						IEditorSettings editorSettings = editorTab.getEditorSettings();
						Composite settingsComposite = editorSettings.getParent();
						
						//If this EditorSettings instance is new, create a new
						//parent for it and call EditorSettings.createComposite
						if(settingsComposite == null){
							settingsComposite = new Composite(mainParent,
									SWT.NONE);
							
							editorSettings.createComposite(settingsComposite);
						}
						
						currentComposite = settingsComposite;
					}
					else{
						currentComposite = defaultComposite;
					}
				}
			}
			else{
				currentComposite = defaultComposite;
			}
			
			mainLayout.topControl = currentComposite;
			mainParent.layout();
		}
	}
}