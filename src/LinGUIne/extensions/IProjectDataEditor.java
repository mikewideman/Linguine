package LinGUIne.extensions;

import org.eclipse.swt.widgets.Composite;

import LinGUIne.model.IProjectData;
import LinGUIne.model.Project;

/**
 * Represents an editor or viewer of Project Data.
 * 
 * @author Kyle Mullins
 */
public interface IProjectDataEditor {
	
	/**
	 * Returns whether or not this ProjectDataEditor handles the given type of
	 * ProjectData.
	 */
	public boolean canOpenData(IProjectData data, Project proj);
	
	/**
	 * Returns whether or not this ProjectDataEditor has EditorSettings that
	 * should be displayed.
	 */
	public boolean hasEditorSettings();
	
	/**
	 * Returns an instance of this editor's EditorSettings view to be displayed
	 * in the SettingsPart.
	 * Note: This function may be called several times per ProjectDataEditor;
	 * the same EditorSettings instance should be returned each time.
	 */
	public IEditorSettings getEditorSettings();
	
	/**
	 * Called once to trigger the creation of this ProjectDataEditor's UI
	 * components.
	 * 
	 * @param parent	The parent part of this ProjectDataEditor instance.
	 */
	public void createComposite(Composite parent);
	
	/**
	 * Called to set the input data for this ProjectDataEditor instance; this is
	 * the data for which the editor is responsible.
	 * 
	 * @param data			ProjectData which which was opened by the user.
	 * @param parentProj	The Project to which the ProjectData belongs.
	 */
	public void setInputData(IProjectData data, Project parentProj);
	
	/**
	 * Returns the ProjectData portion of the input data for this editor.
	 */
	public IProjectData getInputProjectData();
	
	/**
	 * Returns the Project portion of the input data for this editor.
	 */
	public Project getInputParentProject();
	
	/**
	 * Registers the given DirtyStateChangedListener which should be notified
	 * whenever the dirty state of this ProjectDataEditor changes. Only one
	 * listener need be registered at a time.
	 * 
	 * @param listener	The listener to be notified of dirty state changes.
	 */
	public void registerDirtyStateListener(DirtyStateChangedListener listener);
	
	/**
	 * Attempts to save any pending changes to the input data.
	 * 
	 * @return	True iff the save operation was successful, false otherwise.
	 */
	public boolean saveChanges();
	
	/**
	 * Returns the label to be shown in this ProjectDataEditor's tab. These are
	 * often based on the input data.
	 * 
	 * @return	A String label for this editor.
	 */
	public String getPartLabel();
	
	/**
	 * Returns the URI of the icon to be shown in this ProjectDataEditor's tab.
	 * If null is returned then a default icon is used instead.
	 * 
	 * @return	A String URI to an icon for this editor.
	 */
	public String getPartIconURI();
	
	/**
	 * A simple listener for changes in the dirty state of a ProjectDataEditor.
	 * 
	 * @author Kyle Mullins
	 */
	public abstract static class DirtyStateChangedListener{
		public abstract void dirtyChanged(boolean isDirty);
	}
}
