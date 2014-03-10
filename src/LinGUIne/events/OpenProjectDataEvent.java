package LinGUIne.events;

import LinGUIne.model.AnnotationSet;
import LinGUIne.model.IProjectData;

/**
 * Container for data related to ProjectData open events.
 * 
 * @author Kyle Mullins
 */
public class OpenProjectDataEvent {
	
	private IProjectData projectData;
	private AnnotationSet annotationSet;
	
	public OpenProjectDataEvent(IProjectData data, AnnotationSet annotation){
		projectData = data;
		annotationSet = annotation;
	}
	
	public OpenProjectDataEvent(IProjectData data){
		this(data, null);
	}
	
	public IProjectData getProjectData() {
		return projectData;
	}

	public AnnotationSet getAnnotationSet() {
		return annotationSet;
	}
	
	public boolean hasAnnotation(){
		return annotationSet != null;
	}
}
