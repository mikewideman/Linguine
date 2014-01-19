package LinGUIne.extensions;

import java.io.File;

import LinGUIne.model.IProjectDataContents;

public class PlaintextImporter implements IFileImporter {

	@Override
	public String getFileType() {
		return "Plaintext";
	}

	@Override
	public String getFileMask() {
		return "*.txt";
	}

	@Override
	public IProjectDataContents importFile(File rawFile) {
		// TODO Auto-generated method stub
		return null;
	}

}
