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

	@Override
	public int compareTo(IProjectData projData) {
		if(projData == null){
			return 1;
		}
		
		return dataFile.compareTo(projData.getFile());
	}
}
