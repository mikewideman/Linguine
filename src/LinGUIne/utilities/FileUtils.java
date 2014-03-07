package LinGUIne.utilities;

import java.io.File;
import java.io.IOException;

import org.eclipse.core.runtime.IPath;

/**
 * A collection of utility functions for working with files and paths.
 * 
 * @author Kyle Mullins
 */
public class FileUtils {
	
	/**
	 * Returns an Eclipse IPath instance representing the given file.
	 * 
	 * @param file	The file which should be returned as an IPath.
	 */
	public static IPath toEclipsePath(File file){
		return new org.eclipse.core.runtime.Path(file.toPath().toString());
	}
	
	/**
	 * Returns a new File object with the given path appended (as a sub-file or
	 * directory).
	 * 
	 * @param basePath	File denoting the parent directory.
	 * @param newPath	The path to be appended to basePath.
	 */
	public static File appendPath(File basePath, String newPath){
		return toEclipsePath(basePath).append(newPath).toFile();
	}
	
	/**
	 * Returns whether or not the given fileName is valid for the current OS.
	 * 
	 * @param fileName	The file name to be validated.
	 * 
	 * @return	True iff the file name can be canonicalized, false otherwise.
	 */
	public static boolean isValidFileName(String fileName){
		File potentialFile = new File(fileName);
		
		try{
			//This should throw an exception if the fileName or path is invalid
			potentialFile.getCanonicalPath();
			return true;
		}
		catch(IOException ioe){
			return false;
		}
	}
}
