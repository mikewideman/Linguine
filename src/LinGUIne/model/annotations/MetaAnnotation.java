package LinGUIne.model.annotations;

import LinGUIne.utilities.ParameterCheck;

/**
 * Annotation used for annotating all instances of another Tag.
 * 
 * @author Kyle Mullins
 */
public class MetaAnnotation implements IAnnotation {

	private Tag myTag;
	private Tag annotatedTag;
	
	/**
	 * Creates a new MetaAnnotation to annotate the annotated Tag with the
	 * given mine Tag.
	 * Note: mine and annotated parameters must not be null.
	 * 
	 * @param mine		The Tag with which this Annotation is associated.
	 * @param annotated	The Tag this Annotation annotates.
	 */
	public MetaAnnotation(Tag mine, Tag annotated){
		ParameterCheck.notNull(mine, "mine");
		ParameterCheck.notNull(annotated, "annotated");
		
		myTag = mine;
		annotatedTag = annotated;
	}
	
	/**
	 * Returns the Tag that this Annotation annotates.
	 */
	public Tag getAnnotatedTag(){
		return annotatedTag;
	}
	
	@Override
	public Tag getTag() {
		return myTag;
	}

}
