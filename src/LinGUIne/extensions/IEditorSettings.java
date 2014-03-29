package LinGUIne.extensions;

import org.eclipse.swt.widgets.Composite;

/**
 * Represents a view for the settings of some ProjectDataEditor.
 * 
 * @author Kyle Mullins
 */
public interface IEditorSettings {
	
	/**
	 * Called once to trigger the creation of this EditorSettings' UI
	 * components.
	 * 
	 * @param parent	The parent part of this EditorSettings instance.
	 */
	public void createComposite(Composite parent);
}
