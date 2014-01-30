package LinGUIne.model.annotations;

/**
 * The composite interface for all types of annotations.
 * TODO: Potentially obviated by single Annotation subclass
 * 
 * @author Kyle Mullins
 */
public interface IAnnotation extends IAnnotatable{
	Tag getTag();
}
