package LinGUIne.model;

import java.io.File;

/**
 * Interface to which all ProjectData must conform.
 * 
 * @author Kyle Mullins
 */
public interface IProjectData extends Comparable<IProjectData> {

	/**
	 * Returns a File representing the workspace location of this Project Data.
	 * 
	 * @return	File representing workspace location of Project Data.
	 */
	File getFile();
	
	/**
	 * Returns the contents of the associated Project Data file.
	 * Note: This function may read from disk.
	 * 
	 * @return	The contents of this Project Data.
	 */
	IProjectDataContents getContents();
	
	/**
	 * Updates this Project Data with the given new contents.
	 * Note: This function may write to disk.
	 * 
	 * @param newContents	The new contents for this Project Data object.
	 * 
	 * @return	True iff the new contents were valid and the update was
	 * 			successful, false otherwise.
	 */
	boolean updateContents(IProjectDataContents newContents);
}
