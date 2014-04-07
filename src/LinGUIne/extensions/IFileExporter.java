package LinGUIne.extensions;

import java.io.File;
import java.io.IOException;
import java.util.Collection;

import LinGUIne.model.IProjectData;
import LinGUIne.model.Result;

/**
 * Interface to which all file export extension classes must conform.
 * 
 * @author Kyle Mullins
 */
public interface IFileExporter {
	
	/**
	 * Returns a human-readable description of the type of file which the
	 * exporter creates. Ex. 'CSV File'
	 */
	String getFileType();
	
	/**
	 * Returns a collection of Result types that this IFileExporter supports.
	 * Note: The types returned should be as specific as possible.
	 */
	Collection<Class<? extends Result>> getSupportedSourceDataTypes();
	
	/**
	 * Converts the given Result and associated data to this IFileExporter's
	 * destination format and places it into the given destination File.
	 * 
	 * @param sourceResult		The Result to be exported.
	 * @param associatedData	Any data associated with the source Result.
	 * @param destFile			The File into which the exported data should be
	 * 							placed.
	 * 
	 * @throws IOException
	 */
	void exportResult(Result sourceResult,
			Collection<IProjectData> associatedData, File destFile) throws
			IOException;
}
