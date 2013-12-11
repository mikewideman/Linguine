package LinGUIne.model;

import java.io.File;

public class TextData implements IProjectData {

	private File dataFile;
	
	public TextData(File textFile){
		dataFile = textFile;
	}
	
	public File getFile() {
		return dataFile;
	}
}
