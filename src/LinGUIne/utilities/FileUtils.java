package LinGUIne.utilities;

import java.io.File;

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
}
