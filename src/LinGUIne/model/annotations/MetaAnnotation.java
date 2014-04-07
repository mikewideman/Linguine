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

	@Override
	public int compareTo(IAnnotation otherAnnotation) {
		if(otherAnnotation == null){
			return 1;
		}
		else if(otherAnnotation instanceof MetaAnnotation){
			MetaAnnotation otherMetaAnnotation = (MetaAnnotation)otherAnnotation;
			
			return annotatedTag.compareTo(otherMetaAnnotation.annotatedTag);
		}
		
		return Integer.compare(hashCode(), otherAnnotation.hashCode());
	}

	public IAnnotation copy(){
		return new MetaAnnotation(myTag.copy(), annotatedTag.copy());
	}
	
	@Override
	public int hashCode() {
		return annotatedTag.hashCode();
	}

	@Override
	public boolean equals(Object obj) {

		if(this == obj) {
			return true;
		}
		else if(obj == null || !(obj instanceof MetaAnnotation)) {
			return false;
		}
		
		MetaAnnotation other = (MetaAnnotation)obj;
		
		return annotatedTag.equals(other.annotatedTag);
	}

}
