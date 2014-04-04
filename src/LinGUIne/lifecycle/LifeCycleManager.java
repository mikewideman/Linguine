package LinGUIne.lifecycle;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
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
import LinGUIne.model.VisualizationPluginManager;
import LinGUIne.parts.advanced.DataEditorManager;
import LinGUIne.utilities.FileUtils;

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
		File workbenchBackup = FileUtils.appendPath(workbenchXMI.getParentFile(),
				"workbench.bak");
		boolean restoredFromBackup = false;
		
		//If there is no workbench.xmi but there is a backup, restore the backup
		if(!workbenchXMI.exists() && workbenchBackup.exists()){
			try(OutputStream outStream = Files.newOutputStream(
					workbenchXMI.toPath())){
				
				Files.copy(workbenchBackup.toPath(), outStream);
				restoredFromBackup = true;
			}
			catch(IOException ioe){
				ioe.printStackTrace();
			}
		}
		
		if(workbenchXMI.exists()){
			if(!restoredFromBackup){
				//Backup the workbench.xmi file in case we terminate unusually
				try(OutputStream outStream = Files.newOutputStream(
						workbenchBackup.toPath())){
					
					Files.copy(workbenchXMI.toPath(), outStream);
				}
				catch(IOException ioe) {
					ioe.printStackTrace();
				}
			}
			
			try(BufferedReader reader = Files.newBufferedReader(
					workbenchXMI.toPath(), Charset.defaultCharset())){
				
				readPersistedStateFromWorkbench(reader);
			}
			catch(IOException ioe) {
				ioe.printStackTrace();
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
		
		VisualizationPluginManager visualisationPluginMan = new VisualizationPluginManager();
		
		DataEditorManager dataEditorMan = new DataEditorManager();
		
		context.set(SoftwareModuleManager.class, softwareModuleMan);
		context.set(ProjectManager.class, projectMan);
		context.set(VisualizationPluginManager.class, visualisationPluginMan);
		
		ContextInjectionFactory.inject(projectMan, context);
		ContextInjectionFactory.inject(dataEditorMan, context);
	}
	
	/**
	 * Called before the application model is saved when the application is
	 * closing.
	 * 
	 * @param application	The application.
	 */
	@PreSave
	public void preSave(MApplication application){}
	
	private void readPersistedStateFromWorkbench(BufferedReader reader)
			throws IOException{
		
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
}
