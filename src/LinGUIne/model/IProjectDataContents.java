package LinGUIne.model;

/**
 * Interface to which all in-memory representations of ProjectData must conform.
 * 
 * @author Kyle Mullins
 */
public interface IProjectDataContents extends Comparable<IProjectDataContents> {
	
	/**
	 * Performs a deep-copy of the instance and returns the copy.
	 * 
	 * @return	A deep-copy of the instance.
	 */
	IProjectDataContents copy();
	
	/**
	 * Returns the Type of Project Data that this IProjectDataContents is
	 * associated with.
	 */
	Class<? extends IProjectData> getAssociatedDataType();
}
