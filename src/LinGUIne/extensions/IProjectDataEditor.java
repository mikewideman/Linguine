package LinGUIne.extensions;

import org.eclipse.swt.widgets.Composite;

import LinGUIne.model.IProjectData;

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
	 */
	public IEditorSettings getEditorSettings();
	
	/**
	 * Called once to trigger the creation of this ProjectDataEditor's UI
	 * components.
	 * 
	 * @param parent	The parent part of this ProjectDataEditor instance.
	 */
	public void createComposite(Composite parent);
}
