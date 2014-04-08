 
package LinGUIne.parts.advanced;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.di.Focus;
import org.eclipse.e4.ui.di.UIEventTopic;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

import LinGUIne.events.LinGUIneEvents;
import LinGUIne.extensions.IEditorSettings;
import LinGUIne.extensions.IProjectDataEditor;

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
	
	@Inject
	public void activeEditorChangedEvent(@Optional @UIEventTopic(LinGUIneEvents.
			UILifeCycle.ACTIVE_EDITOR_CHANGED) IProjectDataEditor editor){
		
		if(editor != null){
			if(editor.hasEditorSettings()){
				IEditorSettings editorSettings = editor.getEditorSettings();
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
			
			mainLayout.topControl = currentComposite;
			
			if(!mainLayout.topControl.isDisposed()){
				mainParent.layout();
			}
		}
	}
}