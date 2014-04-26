package LinGUIne.parts.advanced;

import java.io.File;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.Platform;
import org.eclipse.e4.core.contexts.ContextInjectionFactory;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.e4.core.services.log.Logger;
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
import LinGUIne.model.AnnotationSet;
import LinGUIne.model.AnnotationSetContents;
import LinGUIne.model.IProjectData;
import LinGUIne.model.Project;
import LinGUIne.model.Project.Subdirectory;

public class DataEditorManager {

	private static final String CONTAINER_PART_ID = "linguine.partdescriptor."
			+ "projectDataEditorContainer";
	
	@Inject
	private EModelService modelService;
	
	@Inject
	private EPartService partService;
	
	@Inject
	private MApplication application;
	
	@Inject
	private Logger logger;
	
	private MPartStack partStack;
	
	@PostConstruct
	public void setup(IEventBroker eventBroker){
		partStack = (MPartStack)modelService.find("linguine.partstack.advanced."
				+ "dataEditorPartStack", application);
		
		eventBroker.subscribe(LinGUIneEvents.UILifeCycle.OPEN_PROJECT_DATA,
				new EventHandler(){
			@Override
			public void handleEvent(Event event) {
				if(event != null){
					for(String propName: event.getPropertyNames()){
						//Does nothing, but I'm afraid to get rid of it
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
		
		if(openEvent != null){
			IProjectData projData = openEvent.getProjectData();
			Project project = openEvent.getParentProject();
			
			//Check if any open editors have the file asking to be opened,
			//if so, simply activate that part and skip the rest of this
			MPart existingPart = findAssociatedPart(projData, project);
			
			if(existingPart != null){
				partService.activate(existingPart);
				
				return;
			}
			
			IProjectDataEditor dataEditor = null;
			
			//Handle cases for our built-in editors
			if(new TextAnnotationSetEditor().canOpenData(projData, project)){
				dataEditor = new TextAnnotationSetEditor();
			}
			else if(new TextDataEditor().canOpenData(projData, project)){
				dataEditor = new TextDataEditor();
			}
			else if(new VisualResultViewEditor().canOpenData(projData, project)){
				dataEditor = new VisualResultViewEditor();
			}
			else if(new UneditableTextViewer().canOpenData(projData, project)){
				dataEditor = new UneditableTextViewer();
			}
			else{
				IConfigurationElement[] configElements = Platform.
						getExtensionRegistry().getConfigurationElementsFor(
						"LinGUIne.LinGUIne.extensions.IProjectDataEditor");
				
				for(IConfigurationElement configElement: configElements){
					try {
						IProjectDataEditor editor = (IProjectDataEditor)
								configElement.createExecutableExtension("class");
						
						if(editor.canOpenData(projData, project)){
							dataEditor = editor;
							break;
						}
					}
					catch(CoreException ce) {
						logger.error(ce);
					}
				}
			}
			
			//If we found a compatible editor, create the part
			if(dataEditor != null){
				MPart newPart = partService.createPart(CONTAINER_PART_ID);
				
				partStack.getChildren().add(newPart);
				partService.activate(newPart);
				
				ContextInjectionFactory.inject(dataEditor, application.getContext());
				dataEditor.setInputData(projData, project);
				
				ProjectDataEditorContainer editorContainer =
						(ProjectDataEditorContainer)newPart.getObject();
				editorContainer.setProjectDataEditor(dataEditor);
			}
		}
	}
	
	/**
	 * Called when a user attempts to edit the Annotations on a file.
	 */
	@Inject
	public void editAnnotations(@Optional @UIEventTopic(LinGUIneEvents.
			UILifeCycle.EDIT_ANNOTATIONS) IProjectDataEditor dataEditor){

		if(dataEditor != null){
			IProjectData projData = dataEditor.getInputProjectData();
			Project project = dataEditor.getInputParentProject();
			MPart parentPart = findAssociatedPart(projData, project);
			AnnotationSet annotations;
			
			if(project.isAnnotated(projData)){
				annotations = project.getAnnotation(projData);
			}
			//Create a new AnnotationSet for the ProjectData
			else{
				String annotationName = projData.getName() + ".ann";
				File annotationFile = project.getSubdirectory(
						Subdirectory.Annotations).append(annotationName).toFile();
				
				annotations = new AnnotationSet(annotationFile);
				annotations.updateContents(new AnnotationSetContents());
				project.addAnnotation(annotations, projData);
			}
			
			OpenProjectDataEvent openEvent = new OpenProjectDataEvent(annotations,
					project);
			
			partService.hidePart(parentPart);
			parentPart.getParent().getChildren().remove(parentPart);
			
			fileOpenEvent(openEvent);
		}
	}
	
	/**
	 * Returns the MPart that is associated with the given ProjectData and
	 * Project or null if there isn't one.
	 */
	private MPart findAssociatedPart(IProjectData projData, Project project){
		for(MPart part: modelService.findElements(application,
				CONTAINER_PART_ID, MPart.class, null)){
			
			if(part.isVisible() && part.getObject() != null){
				ProjectDataEditorContainer editorContainer =
						(ProjectDataEditorContainer)part.getObject();
				IProjectDataEditor editor =
						editorContainer.getProjectDataEditor();
				
				if(editor.getInputParentProject().equals(project) &&
						editor.getInputProjectData().equals(projData)){
					
					return part;
				}
			}
		}
		
		return null;
	}
}
