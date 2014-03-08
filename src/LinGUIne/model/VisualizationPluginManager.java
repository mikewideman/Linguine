package LinGUIne.model;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
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

	HashMap<String, IVisualizationProvider> visualizations;

	/**
	 * Creates a new instance and populates it using the ExtensionRegistry.
	 */
	public VisualizationPluginManager() {
		visualizations = new HashMap<String, IVisualizationProvider>();

		IConfigurationElement[] visualizationProviderElements = Platform
				.getExtensionRegistry().getConfigurationElementsFor(
						"LinGUIne.extensions.IVisualizationProvider");

		// Iterate over the providers
		for (IConfigurationElement providerElement : visualizationProviderElements) {
			String providerName = providerElement.getAttribute("name");

			try {
				IVisualizationProvider provider = (IVisualizationProvider) providerElement
						.createExecutableExtension("class");

				visualizations.put(providerName, provider);

			} catch (CoreException e) {
				// TODO: Error handling
				e.printStackTrace();
			}
		}

		// TODO: REMOVE! For demonstrative purposes only
		visualizations.put("Test Provider", new IVisualizationProvider() {
			@Override
			public String getName() {
				return "Test Provider";
			}

			@Override
			public IVisualization[] getVisualizations() {
				IVisualization vis1 = new IVisualization(){
					@Override
					public String getName() {
						return "Test Visualization 1";
					}

					@Override
					public String getVisualizationDescription() {
						return "This is a test for visualization 1";
					}

					@Override
					public void runVisualization() {}
				};
				
				IVisualization vis2 = new IVisualization(){
					@Override
					public String getName() {
						return "Test Visualization 2";
					}

					@Override
					public String getVisualizationDescription() {
						return "This is a test for visualization 2";
					}

					@Override
					public void runVisualization() {}
				};
				
				return new IVisualization[]{vis1, vis2};
			}
		});
	}

	/**
	 * Returns all of the visualization providers currently loaded into 
	 * the application.
	 * 
	 * @return The list of visualization providers as a collection of Strings.
	 */
	public Collection<String> getVisualizationProviderNames() {
		return visualizations.keySet();
	}


	/**
	 * Returns the visualization provider based on the name provided.
	 * 
	 * @param providerName The name of the visualization provider. This MUST
	 * match the 'name' attribute in the extension point.
	 * @return The visualization provider
	 */
	public IVisualizationProvider getProviderByName(String providerName) {

		for (String provider : visualizations.keySet()) {
			if (providerName.equals(provider)) {
				return visualizations.get(provider);
			}
		}
		return null;
	}
}
