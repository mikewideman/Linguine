package LinGUIne.model.annotations;

import LinGUIne.model.IProjectDataContents;

/**
 * Basic Annotation of an IAnnotatable subject.
 * TODO: Removal of IAnnotatable would require more IAnnotation subclasses
 * TODO: If no other IAnnotation subclasses are added, IAnnotation isn't needed
 * 
 * @author Kyle Mullins
 */
public class Annotation implements IAnnotation {

	private Tag annotationTag;
	private IAnnotatable annotationSubject;
	
	public Annotation(Tag tag, IAnnotatable subject){
		annotationTag = tag;
		annotationSubject = subject;
	}
	
	@Override
	public String getText(IProjectDataContents data) {
		return annotationSubject.getText(data);
	}

	@Override
	public Tag getTag() {
		return annotationTag;
	}
}
