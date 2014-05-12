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
import LinGUIne.extensions.IPropertiesProvider;

/**
 * Generic container for ProjectDataEditors that controls dirty state,
 * ActiveEditorChanged events, providing properties, and the Part icon/label.
 * 
 * @author Kyle Mullins
 */
public class ProjectDataEditorContainer implements IPropertiesProvider {

	@Inject
	private MPart myPart;
	
	@Inject
	private MDirtyable dirtyable;
	
	@Inject
	private IEventBroker eventBroker;
	
	private Composite editorParent;
	private IProjectDataEditor projectDataEditor;
	
	/**
	 * Sets the ProjectDataEditor that this container is encapsulating.
	 * 
	 * @param dataEditor	The ProjectDataEditor instance being displayed by
	 * 						this container.
	 */
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
	
	/**
	 * Returns the ProjectDataEditor contained within this instance.
	 */
	public IProjectDataEditor getProjectDataEditor(){
		return projectDataEditor;
	}
	
	/**
	 * Registers a DisposeListener so that an ActiveEditorChanged event can be
	 * sent out when the encapsulated editor is closed.
	 */
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
	
	/**
	 * Calls on the encapsulated editor to save its changes.
	 * 
	 * @param dirty			The editor's dirty state.
	 * 
	 * @throws IOException
	 */
	@Persist
	public void save(MDirtyable dirty) throws IOException{
		projectDataEditor.saveChanges();
	}
	
	/**
	 * Sends out an ActiveEditorChanged event upon receiving focus.
	 */
	@Focus
	public void onFocus(){
		eventBroker.post(LinGUIneEvents.UILifeCycle.ACTIVE_EDITOR_CHANGED,
				projectDataEditor);
	}

	@Override
	public Composite getProperties(Composite parent) {
		if(projectDataEditor instanceof IPropertiesProvider){
			return ((IPropertiesProvider)projectDataEditor).getProperties(parent);
		}
		
		return null;
	}
}
