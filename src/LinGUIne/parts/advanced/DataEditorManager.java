package LinGUIne.parts.advanced;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.e4.ui.di.UIEventTopic;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.model.application.ui.basic.MPartStack;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventHandler;

import LinGUIne.events.LinGUIneEvents;
import LinGUIne.events.OpenProjectDataEvent;
import LinGUIne.extensions.IProjectDataEditor;
import LinGUIne.model.IProjectData;
import LinGUIne.model.Project;

public class DataEditorManager {

	private static final String CONTAINER_PART_ID = "linguine.partdescriptor."
			+ "projectDataEditorContainer";
	
	@Inject
	private EModelService modelService;
	
	@Inject
	private EPartService partService;
	
	@Inject
	private MApplication application;
	
	private MPartStack partStack;
	
	@PostConstruct
	public void setup(IEventBroker eventBroker){
		partStack = (MPartStack)modelService.find("linguine.partstack.advanced."
				+ "dataEditorPartStack", application);
		
//		System.out.println("In setup");
		
		eventBroker.subscribe(LinGUIneEvents.UILifeCycle.OPEN_PROJECT_DATA,
				new EventHandler(){
			@Override
			public void handleEvent(Event event) {
				if(event != null){
					for(String propName: event.getPropertyNames()){
//						System.out.println(propName);
					}
				}
			}
		});
	}
	
	/**
	 * Called when a user attempts to open a File.
	 */
	@Inject
	public void fileOpenEvent(@Optional @UIEventTopic(LinGUIneEvents.
			UILifeCycle.OPEN_PROJECT_DATA) OpenProjectDataEvent openEvent){
		
//		System.out.println("In fileOpenEvent");
		
		if(openEvent != null){
			IProjectData projData = openEvent.getProjectData();
			Project project = openEvent.getParentProject();
			
			//Iterate through all currently open editor containers and check if any
			//of them have the file asking to be opened, if so, simply activate that
			//part and skip the rest of this
			for(MPart part: modelService.findElements(application,
					CONTAINER_PART_ID, MPart.class, null)){
				
				if(part.isVisible()){
					ProjectDataEditorContainer editorContainer =
							(ProjectDataEditorContainer)part.getObject();
					IProjectDataEditor editor =
							editorContainer.getProjectDataEditor();
					
					if(editor.getInputParentProject().equals(project) &&
							editor.getInputProjectData().equals(projData)){
						
						partService.activate(part);
						
						return;
					}
				}
			}
			
			MPart newPart = partService.createPart(CONTAINER_PART_ID);
			
			partStack.getChildren().add(newPart);
			partService.activate(newPart);
			
			IProjectDataEditor dataEditor;
			
			
			//TODO: Actually iterate through all choices
			if(new TextAnnotationSetEditor().canOpenData(projData, project)){
				dataEditor = new TextAnnotationSetEditor();
				
			}
			else if(new TextDataEditor().canOpenData(projData, project)){
				dataEditor = new TextDataEditor();
			}
			else{
				return;
			}
			
			dataEditor.setInputData(projData, project);
			
			ProjectDataEditorContainer editorContainer =
					(ProjectDataEditorContainer)newPart.getObject();
			editorContainer.setProjectDataEditor(dataEditor);
		}
	}
	
	
}
