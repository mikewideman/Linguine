package LinGUIne.lifecycle;

import org.eclipse.core.runtime.Platform;
import org.eclipse.e4.core.contexts.ContextInjectionFactory;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.workbench.lifecycle.PostContextCreate;
import org.eclipse.e4.ui.workbench.lifecycle.ProcessAdditions;

import LinGUIne.model.ProjectManager;
import LinGUIne.model.SoftwareModuleManager;

/**
 * Hooks into the Eclipse life cycle to perform operations at startup.
 * 
 * @author Kyle Mullins
 */
public class LifeCycleManager {

	/**
	 * Called after the application context is created, but before anything is
	 * added to it.
	 * 
	 * @param context	The application's context.
	 */
	@PostContextCreate
	public void postContextCreate(IEclipseContext context){
		
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
		SoftwareModuleManager softwareModuleMan = new SoftwareModuleManager();
		
		ProjectManager projectMan = new ProjectManager(Platform.getLocation(),
				application);
		projectMan.loadProjects();
		
		context.set(SoftwareModuleManager.class, softwareModuleMan);
		context.set(ProjectManager.class, projectMan);
		
		ContextInjectionFactory.inject(projectMan, context);
	}
}
