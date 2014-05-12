package LinGUIne.model;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.TreeSet;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.Platform;

import LinGUIne.extensions.IAnalysisPlugin;

/**
 * Container for all plug-ins implementing IAnalysisPlugin to provide quick
 * access to them.
 * 
 * @author Kyle Mullins
 */
public class SoftwareModuleManager {
	
	HashMap<String, HashSet<IAnalysisPlugin>> analyses;
	
	/**
	 * Creates a new instance and populates it using the ExtensionRegistry.
	 */
	public SoftwareModuleManager(){
		analyses = new HashMap<String, HashSet<IAnalysisPlugin>>();
		
		IConfigurationElement[] analysisElements = Platform.getExtensionRegistry().
				getConfigurationElementsFor("LinGUIne.LinGUIne.extensions.IAnalysisPlugin");
		
		for(IConfigurationElement analysisElement: analysisElements){
			try {
				IAnalysisPlugin analysis = (IAnalysisPlugin)analysisElement.
						createExecutableExtension("class");
				if(!analyses.containsKey(analysis.getAnalysisLibrary())){
					analyses.put(analysis.getAnalysisLibrary(),
							new HashSet<IAnalysisPlugin>());
				}
				
				analyses.get(analysis.getAnalysisLibrary()).add(analysis);
			}
			catch(CoreException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Returns the names of all Software Modules referenced by installed
	 * Analysis plug-ins.
	 */
	public Collection<String> getSoftwareModuleNames(){
		return analyses.keySet();
	}
	
	/**
	 * Returns all of the Analysis plug-ins registered to the given Software
	 * Module name or an empty set if there are none.
	 */
	public Collection<IAnalysisPlugin> getAnalyses(String softwareModule){
		if(analyses.containsKey(softwareModule)){
			return analyses.get(softwareModule);
		}
		
		return new TreeSet<IAnalysisPlugin>();
	}
	
	/**
	 * Returns the named Analysis registered to the given Software Module
	 * if it exists, otherwise null is returned.
	 */
	public IAnalysisPlugin getAnalysisByName(String softwareModule,
			String analysisName){
		
		for(IAnalysisPlugin analysis: getAnalyses(softwareModule)){
			if(analysis.getName().equals(analysisName)){
				return analysis;
			}
		}
		
		return null;
	}
	
	/**
	 * Returns all Analysis plug-ins which return the given Result Type or an
	 * empty collection if there are none.
	 * 
	 * @param type	The Type of Result providers for which we are looking.
	 * 
	 * @return	A collection of Analysis plug-ins which return the given Type.
	 */
	public Collection<IAnalysisPlugin> getProvidersForType(
			Class<? extends Result> type){
		HashSet<IAnalysisPlugin> providers = new HashSet<IAnalysisPlugin>();
		
		for(HashSet<IAnalysisPlugin> analysisSet: analyses.values()){
			for(IAnalysisPlugin analysis: analysisSet){
				if(analysis.getReturnedResultType().equals(type)){
					providers.add(analysis);
				}
			}
		}
		
		return providers;
	}
}
