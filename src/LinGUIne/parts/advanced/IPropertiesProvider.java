package LinGUIne.parts.advanced;

import org.eclipse.swt.widgets.Composite;

public interface IPropertiesProvider {
	
	/**
	 * Returns the Properties Composite for this Properties Provider as a child
	 * of the given Composite.
	 */
	public Composite getProperties(Composite parent);
}
