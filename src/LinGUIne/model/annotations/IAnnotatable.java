package LinGUIne.model.annotations;

import LinGUIne.model.IProjectDataContents;

/**
 * Interface to which a class must conform if it is to be annotatable.
 * TODO: This might be too specific to Text-based annotatables
 * TODO: Making this more general or adding more functions would be weird
 * 
 * @author Kyle Mullins
 */
public interface IAnnotatable {
	String getText(IProjectDataContents data);
}
