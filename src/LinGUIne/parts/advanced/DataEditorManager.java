package LinGUIne.parts.advanced;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;

import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.di.UIEventTopic;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.model.application.ui.basic.MPartStack;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.swt.widgets.Shell;

import LinGUIne.events.LinGUIneEvents;
import LinGUIne.events.OpenProjectDataEvent;
import LinGUIne.extensions.IProjectDataEditor;
import LinGUIne.model.Project;

public class DataEditorManager {

	@Inject
	private EModelService modelService;
	
	@Inject
	private EPartService partService;
	
	@Inject
	private MApplication application;
	
	private MPartStack partStack;
	
	@PostConstruct
	public void setup(){
		partStack = (MPartStack)modelService.find("linguine.partstack.advanced."
				+ "dataEditorPartStack", application);
	}
	
	/**
	 * Called when a user attempts to open a File.
	 */
	@Inject
	@Optional
	public void fileOpenEvent(@UIEventTopic(LinGUIneEvents.UILifeCycle.OPEN_PROJECT_DATA)
			OpenProjectDataEvent openEvent, @Named(IServiceConstants.ACTIVE_SHELL)
			Shell shell){
		
		MPart newPart = partService.createPart("linguine.partdescriptor.projectDataEditorContainer");
		newPart.setCloseable(true);
		
		partStack.getChildren().add(newPart);
		partService.activate(newPart);
		
		IProjectDataEditor dataEditor = new TestDataEditor();
//		dataEditor.setInputData(openEvent.getProjectData(), new Project());
		
		ProjectDataEditorContainer editorContainer =
				(ProjectDataEditorContainer)newPart.getObject();
		editorContainer.setProjectDataEditor(dataEditor);
	}
}
