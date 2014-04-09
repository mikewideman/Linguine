package LinGUIne.parts.advanced;

import java.io.IOException;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.e4.ui.di.Focus;
import org.eclipse.e4.ui.di.Persist;
import org.eclipse.e4.ui.model.application.ui.MDirtyable;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.widgets.Composite;

import LinGUIne.events.LinGUIneEvents;
import LinGUIne.extensions.IProjectDataEditor;
import LinGUIne.extensions.IProjectDataEditor.DirtyStateChangedListener;

public class ProjectDataEditorContainer {

	@Inject
	private MPart myPart;
	
	@Inject
	private MDirtyable dirtyable;
	
	@Inject
	private IEventBroker eventBroker;
	
	private Composite editorParent;
	private IProjectDataEditor projectDataEditor;
	
	public void setProjectDataEditor(IProjectDataEditor dataEditor){
		projectDataEditor = dataEditor;
		
		projectDataEditor.createComposite(editorParent);
		projectDataEditor.registerDirtyStateListener(new DirtyStateChangedListener(){
			@Override
			public void dirtyChanged(boolean isDirty) {
				dirtyable.setDirty(isDirty);
			}
		});
		
		editorParent.layout();
		myPart.setLabel(projectDataEditor.getPartLabel());
		
		if(projectDataEditor.getPartIconURI() != null){
			myPart.setIconURI(projectDataEditor.getPartIconURI());
		}
		
		eventBroker.post(LinGUIneEvents.UILifeCycle.ACTIVE_EDITOR_CHANGED,
				projectDataEditor);
	}
	
	public IProjectDataEditor getProjectDataEditor(){
		return projectDataEditor;
	}
	
	@PostConstruct
	public void createComposite(Composite parent){
		editorParent = parent;
		editorParent.addDisposeListener(new DisposeListener(){
			@Override
			public void widgetDisposed(DisposeEvent e) {
				//When the Composite gets disposed (the Part closes) notify
				//the SettingsPart that it has closed
				eventBroker.post(LinGUIneEvents.UILifeCycle.
						ACTIVE_EDITOR_CHANGED, null);
			}
		});
	}
	
	@Persist
	public void save(MDirtyable dirty) throws IOException{
		projectDataEditor.saveChanges();
	}
	
	@Focus
	public void onFocus(){
		eventBroker.post(LinGUIneEvents.UILifeCycle.ACTIVE_EDITOR_CHANGED,
				projectDataEditor);
	}
}
