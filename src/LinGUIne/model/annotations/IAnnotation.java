package LinGUIne.model.annotations;

/**
 * The interface for all types of Annotations.
 * 
 * @author Kyle Mullins
 */
public interface IAnnotation extends Comparable<IAnnotation>{
	
	/**
	 * Returns the Tag associated with this Annotation.
	 */
	Tag getTag();
	
	/**
	 * Performs a deep copy of this Annotation and returns it.
	 */
	IAnnotation copy();
	
	/**
	 * Implemented classes must override hashCode.
	 */
	int hashCode();
}
