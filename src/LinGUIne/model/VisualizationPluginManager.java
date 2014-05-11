package LinGUIne.model;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.Platform;

import LinGUIne.extensions.IVisualization;

/**
 * Container for all visualization plugins currently loaded in order to easily
 * provide information about them to other parts of the application.
 * 
 * @author Peter Dimou
 */
public class VisualizationPluginManager {

	HashMap<IVisualization, String> visualizations;

	/**
	 * Creates a new instance and populates it using the ExtensionRegistry.
	 * Builds the internal list of visualizations from the extension point
	 * schema definition.
	 */
	public VisualizationPluginManager() {
		visualizations = new HashMap<IVisualization, String>();

		IConfigurationElement[] visualizationElements = Platform
				.getExtensionRegistry().getConfigurationElementsFor(
						"LinGUIne.LinGUIne.extensions.IVisualization");

		// Iterate over the visualizations
		for (IConfigurationElement visualizationElement : visualizationElements) {

			try {
				IVisualization visualization = (IVisualization) visualizationElement
						.createExecutableExtension("class");

				String description = visualizationElement
						.getAttribute("description");

				visualizations.put(visualization, description);
			} catch (CoreException e) {
				// TODO: Error handling
				e.printStackTrace();
			}
		}
	}

	/**
	 * Returns all visualizations currently loaded.
	 * 
	 * @return A collection visualizations currently loaded into the 
	 * application.
	 */
	public Collection<IVisualization> getVisualizations() {
		return visualizations.keySet();
	}

	/**
	 * Returns all of the visualizations (by name) currently loaded into the
	 * application.
	 * 
	 * @return The list of visualizations as a collection of strings.
	 */
	public Collection<String> getVisualizationNames() {
		Collection<String> retVal = new LinkedList<String>();

		for (IVisualization visualization : visualizations.keySet()) {
			retVal.add(visualization.getName());
		}
		return retVal;
	}

	/**
	 * Returns a list of visualizations that support a given result type set.
	 * Duplicate result types are allowed and encouraged to further add 
	 * specificity to the visualization requirements.
	 * 
	 * @param resultTypeSet
	 *            The result types to query the visualizations for.
	 * @return A collection of visualizations that support the given result 
	 * type.
	 */
	public Collection<IVisualization> getVisualizationsBySupportedResultTypeSet(
			Collection<Class<? extends Result>> resultTypeSet) {
		Collection<IVisualization> retVal = new LinkedList<IVisualization>();

		for (IVisualization visualization : visualizations.keySet()) {
			Collection<Class<? extends Result>> supportedTypes = visualization
					.getSupportedResultTypes();
			boolean isSupported = supportedTypes.containsAll(resultTypeSet)
					&& resultTypeSet.containsAll(supportedTypes);
			if (isSupported) {
				retVal.add(visualization);
			}
		}
		return retVal;
	}

	/**
	 * Returns the visualization based on the name provided. If the
	 * visualization isn't found then null is returned.
	 * 
	 * @param name
	 *            The name of the visualization.
	 * @return The visualization requested by name.
	 */
	public IVisualization getVisualizationByName(String name) {
		for (IVisualization visualization : visualizations.keySet()) {
			if (visualization.getName().equals(name)) {
				return visualization;
			}
		}
		return null;
	}

	/**
	 * Returns the description of a visualization (as specified in the extension
	 * point schema definition) by the visualization's name. If the name is
	 * incorrect, null is returned.
	 * 
	 * @param name
	 *            The name of the visualization.
	 * @return The description of the visualization as specified in the
	 *         extension point schema definition.
	 */
	public String getVisualizationDescriptionByName(String name) {
		for (IVisualization visualization : visualizations.keySet()) {
			if (visualization.getName().equals(name)) {
				return visualizations.get(visualization);
			}
		}
		return null;
	}
}
