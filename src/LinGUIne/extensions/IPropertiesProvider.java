package LinGUIne.extensions;

import org.eclipse.swt.widgets.Composite;

/**
 * Represents a View that provides a Composite for use by the Properties View
 * whenever the PropertiesProvider has focus.
 * 
 * @author Kyle Mullins
 */
public interface IPropertiesProvider {
	
	/**
	 * Returns the Properties Composite for this Properties Provider as a child
	 * of the given Composite.
	 */
	public Composite getProperties(Composite parent);
}
