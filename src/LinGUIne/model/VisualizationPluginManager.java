package LinGUIne.model;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.TreeSet;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.Platform;

import LinGUIne.extensions.IAnalysisPlugin;
import LinGUIne.extensions.IVisualization;
import LinGUIne.extensions.IVisualizationProvider;

/**
 * Container for all visualization plugins currently loaded in order to easily
 * provide information about them to other parts of the application.
 * 
 * @author Peter Dimou
 */
public class VisualizationPluginManager {

	HashMap<String, IVisualizationProvider> visualizationProviders;
	HashMap<IVisualization, String> visualizations;

	/**
	 * Creates a new instance and populates it using the ExtensionRegistry.
	 */
	public VisualizationPluginManager() {

		initializeProviders();
		initializeVisualizations();

		// TODO: REMOVE! For demonstrative purposes only
		final IVisualization vis1 = new IVisualization() {
			@Override
			public String getName() {
				return "Test Visualization 1";
			}

			@Override
			public String getVisualizationDescription() {
				return "This is a test for visualization 1";
			}

			@Override
			public void runVisualization() {
			}

			@Override
			public Collection<Class<? extends Result>> getSupportedResultTypes() {
				Collection<Class<? extends Result>> retVal = new LinkedList<Class<? extends Result>>();
				retVal.add(KeyValueResult.class);
				return retVal;
			}
		};

		final IVisualization vis2 = new IVisualization() {
			@Override
			public String getName() {
				return "Test Visualization 2";
			}

			@Override
			public String getVisualizationDescription() {
				return "This is a test for visualization 2";
			}

			@Override
			public void runVisualization() {
			}

			@Override
			public Collection<Class<? extends Result>> getSupportedResultTypes() {
				Collection<Class<? extends Result>> retVal = new LinkedList<Class<? extends Result>>();
				retVal.add(KeyValueResult.class);
				return retVal;
			}
		};

		visualizations.put(vis1, "Description of Visualization 1");
		visualizations.put(vis2, "Description of Visualization 2");

		visualizationProviders.put("Test Provider",
				new IVisualizationProvider() {
					@Override
					public String getName() {
						return "Test Provider";
					}

					@Override
					public Collection<? extends IVisualization> getVisualizations() {

						Collection<IVisualization> retVal = new LinkedList<IVisualization>();
						retVal.add(vis1);
						retVal.add(vis2);
						return retVal;
					}
				});
	}

	/**
	 * Build the internal list of visualization providers from the extension
	 * point schema definition
	 */
	private void initializeProviders() {
		visualizationProviders = new HashMap<String, IVisualizationProvider>();

		IConfigurationElement[] visualizationProviderElements = Platform
				.getExtensionRegistry().getConfigurationElementsFor(
						"LinGUIne.extensions.IVisualizationProvider");

		// Iterate over the providers
		for (IConfigurationElement providerElement : visualizationProviderElements) {
			String providerName = providerElement.getAttribute("name");

			try {
				IVisualizationProvider provider = (IVisualizationProvider) providerElement
						.createExecutableExtension("class");

				visualizationProviders.put(providerName, provider);

			} catch (CoreException e) {
				// TODO: Error handling
				e.printStackTrace();
			}
		}
	}

	/**
	 * Builds the internal list of visualizations from the extension point
	 * schema definition
	 */
	private void initializeVisualizations() {
		visualizations = new HashMap<IVisualization, String>();

		IConfigurationElement[] visualizationElements = Platform
				.getExtensionRegistry().getConfigurationElementsFor(
						"LinGUIne.extensions.IVisualization");

		// Iterate over the visualizations
		for (IConfigurationElement visualizationElement : visualizationElements) {
			String description = visualizationElement
					.getAttribute("description");

			try {
				IVisualization visualization = (IVisualization) visualizationElement
						.createExecutableExtension("class");

				visualizations.put(visualization, description);
			} catch (CoreException e) {
				// TODO: Error handling
				e.printStackTrace();
			}
		}

	}

	/**
	 * Returns all of the visualization providers currently loaded into the
	 * application.
	 * 
	 * @return The list of visualization providers as a collection of Strings.
	 */
	public Collection<String> getVisualizationProviderNames() {
		return visualizationProviders.keySet();
	}

	/**
	 * Returns the visualization provider based on the name provided. If the
	 * provider isn't found then null is returned.
	 * 
	 * @param providerName
	 *            The name of the visualization provider. This MUST match the
	 *            'name' attribute in the extension point.
	 * @return The visualization provider
	 */
	public IVisualizationProvider getProviderByName(String providerName) {

		for (String provider : visualizationProviders.keySet()) {
			if (providerName.equals(provider)) {
				return visualizationProviders.get(provider);
			}
		}
		return null;
	}

	/**
	 * Returns all visualizations currently loaded from plugins
	 * 
	 * @return A collection visualizations currently loaded into the application
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
	 * Returns a list of visualizations that support a given result type.
	 * 
	 * @param resultType
	 *            The result type to query the visualizations for
	 * @return A collection of visualizations that support the given result type
	 */
	public Collection<IVisualization> getVisualizationsBySupportedResultType(
			Result resultType) {
		Collection<IVisualization> retVal = new LinkedList<IVisualization>();

		for (IVisualization visualization : visualizations.keySet()) {
			boolean isSupported = visualization.getSupportedResultTypes()
					.contains(resultType);
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
	 * @return The visualization provider
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
	 *            The name of the visualization
	 * @return The description of the visualization as specified in the
	 *         extension point schema definition
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
