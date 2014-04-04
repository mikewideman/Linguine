package LinGUIne.extensions;

import org.eclipse.swt.widgets.Composite;

import LinGUIne.model.IProjectData;
import LinGUIne.model.Project;

/**
 * Represents an editor of Project Data.
 * 
 * @author Kyle Mullins
 */
public interface IProjectDataEditor {
	
	/**
	 * Returns whether or not this ProjectDataEditor handles the given type of
	 * ProjectData.
	 */
	public <T extends IProjectData> boolean canOpenDataType(Class<T> type);
	
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
	
	public void setInputData(IProjectData data, Project parentProj);
	
//	public void setDirtyStateListener(DirtyStateChangedListener listener);
	
	public boolean saveChanges();
	
	public String getPartLabel();
}
