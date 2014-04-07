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
	
	/**
	 * Returns the parent Composite of this EditorSettings' UI components or
	 * null if createComposite has not yet been called.
	 * Note: This should be the same object that was passed to the
	 * createComposite function.
	 * 
	 * @return	The parent Composite provided to the createComposite function,
	 * 			null if it has not been called yet.
	 */
	public Composite getParent();
}
