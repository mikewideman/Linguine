package LinGUIne.model;

import java.io.File;
import java.io.IOException;

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
	 * Returns a name for this Project Data which can be used to identify it.
	 * 
	 * @return	String name of this Project Data.
	 */
	String getName();
	
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
	
	/**
	 * Deletes this Project Data's file from disk.
	 * 
	 * @throws IOException	If the contents could not be deleted for any reason.
	 */
	void deleteContentsOnDisk() throws IOException;
}
