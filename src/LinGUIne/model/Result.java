package LinGUIne.model;

import java.io.File;

public class Result implements IProjectData {

	private File resultFile;
	
	public Result(File result){
		resultFile = result;
	}
	
	public File getFile() {
		return resultFile;
	}
}
