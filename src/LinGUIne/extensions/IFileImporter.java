package LinGUIne.extensions;

import java.io.File;
import java.io.IOException;

import LinGUIne.model.IProjectDataContents;

/**
 * Interface to which all file import extension classes must conform.
 * 
 * @author Kyle Mullins
 */
public interface IFileImporter {
	/**
	 * Returns a human-readable description of the type of file which the
	 * importer can handle. Ex. 'Plaintext File'
	 */
	String getFileType();
	
	/**
	 * Returns a file mask used to filter files that the importer can handle.
	 * Ex. *.txt
	 */
	String getFileMask();
	
	/**
	 * Parses the given File and returns an in-memory copy of its data.
	 * 
	 * @param rawFile	The File to be imported into the workspace.
	 * 
	 * @return	An in-memory version of the data contained in the rawFile.
	 * 
	 * @throws IOException
	 */
	IProjectDataContents importFile(File rawFile) throws IOException;
}
