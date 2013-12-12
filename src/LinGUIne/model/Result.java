package LinGUIne.model;

import java.io.File;

/**
 * Represents the result of some analysis on some ProjectData.
 * 
 * @author Kyle Mullins
 */
public class Result implements IProjectData {

	private File resultFile;
	
	public Result(File result){
		resultFile = result;
	}
	
	public File getFile() {
		return resultFile;
	}

	@Override
	public int compareTo(IProjectData projData) {
		if(projData == null){
			return 1;
		}
		
		return resultFile.compareTo(projData.getFile());
	}
}
