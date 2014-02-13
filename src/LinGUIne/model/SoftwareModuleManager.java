package LinGUIne.model;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.TreeSet;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.Platform;

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
				getConfigurationElementsFor("LinGUIne.model.IAnalysisPlugin"); //TODO: Update with actual extension point id
		
		for(IConfigurationElement analysisElement: analysisElements){
//			String libName = analysisElement.getAttribute("Library");
//			String analysisName = analysisElement.getAttribute("Name");
			
			try {
				IAnalysisPlugin analysis = (IAnalysisPlugin)analysisElement.
						createExecutableExtension("class"); //TODO: Update with actual name of this attribute
				
				if(!analyses.containsKey(analysis.getAnalysisLibrary())){
					analyses.put(analysis.getAnalysisLibrary(),
							new HashSet<IAnalysisPlugin>());
				}
				
				analyses.get(analysis.getAnalysisLibrary()).add(analysis);
			}
			catch(CoreException e) {
				//TODO: Error handling
				e.printStackTrace();
			}
		}
		
		//TODO: REMOVE! For demonstrative purposes only
		analyses.put("NLTK", new HashSet<IAnalysisPlugin>());
		analyses.get("NLTK").add(new IAnalysisPlugin(){

			@Override
			public String getName() {
				return "Tokenization";
			}

			@Override
			public String getAnalysisLibrary() {
				return "NLTK";
			}

			@Override
			public Object getPluginData() {
				return "This is an NLTK Tokenization analysis";
			}

			@Override
			public Result runAnalysis() {
				return null;
			}
		});
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
}
