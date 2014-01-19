package LinGUIne.extensions;

import java.io.File;

import LinGUIne.model.IProjectDataContents;

public interface IFileImporter {
	String getFileType();
	String getFileMask();
	IProjectDataContents importFile(File rawFile);
}
