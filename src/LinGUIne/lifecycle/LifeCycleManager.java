package LinGUIne.lifecycle;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;

import org.eclipse.core.runtime.Platform;
import org.eclipse.e4.core.contexts.ContextInjectionFactory;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.workbench.lifecycle.PostContextCreate;
import org.eclipse.e4.ui.workbench.lifecycle.PreSave;
import org.eclipse.e4.ui.workbench.lifecycle.ProcessAdditions;

import LinGUIne.model.ProjectManager;
import LinGUIne.model.SoftwareModuleManager;

/**
 * Hooks into the Eclipse life cycle to perform operations at startup.
 * 
 * @author Kyle Mullins
 */
public class LifeCycleManager {

	private String projectManagerProjectList;
	
	/**
	 * Called after the application context is created, but before anything is
	 * added to it and before the application model is loaded.
	 * 
	 * @param context	The application's context.
	 */
	@PostContextCreate
	public void postContextCreate(IEclipseContext context){
		//TODO: Prompt user for workspace location
		
		//Workaround for Eclipse Persisted State bug: load the data directly
		//from the workbench.xmi file and put it into the Persisted State later
		File workbenchXMI = Platform.getLocation().append(".metadata").append(
				".plugins").append("org.eclipse.e4.workbench").append(
				"workbench.xmi").toFile();
		
		if(workbenchXMI.exists()){
			try(BufferedReader reader = Files.newBufferedReader(
					workbenchXMI.toPath(), Charset.defaultCharset())){
				
				String persistedStateLineStart = "<persistedState key=\"" + 
						ProjectManager.PROJECT_LIST_KEY + "\" value=\"";
				String persistedStateLineEnd = "\"/>";
				
				while(reader.ready()){
					String line = reader.readLine().trim();
					
					if(line.startsWith(persistedStateLineStart)){
						int beginIndex = persistedStateLineStart.length();
						int endIndex = line.length() -
								persistedStateLineEnd.length();
						
						projectManagerProjectList = line.substring(beginIndex, 
								endIndex);
					}
				}
			}
			catch(IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Called after the application context is populated, but before any parts
	 * are initialized.
	 * 
	 * @param context		The application's context.
	 * @param application	The application.
	 */
	@ProcessAdditions
	public void processAdditions(IEclipseContext context,
			MApplication application){
		//Workaround for Eclipse Persisted State bug: put data into Persisted
		//State map after it has been loaded from the workbench.xmi file
		application.getPersistedState().put(ProjectManager.PROJECT_LIST_KEY,
				projectManagerProjectList);
		
		SoftwareModuleManager softwareModuleMan = new SoftwareModuleManager();
		
		ProjectManager projectMan = new ProjectManager(Platform.getLocation(),
				application);
		projectMan.loadProjects();
		
		context.set(SoftwareModuleManager.class, softwareModuleMan);
		context.set(ProjectManager.class, projectMan);
		
		ContextInjectionFactory.inject(projectMan, context);
	}
	
	/**
	 * Called before the application model is saved when the application is
	 * closing.
	 * 
	 * @param application	The application.
	 */
	@PreSave
	public void preSave(MApplication application){}
}
