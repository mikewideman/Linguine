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
	public String getName() {
		return resultFile.getName();
	}
	
	@Override
	public int compareTo(IProjectData projData) {
		if(projData == null){
			return 1;
		}
		
		return resultFile.compareTo(projData.getFile());
	}

	@Override
	public IProjectDataContents getContents() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean updateContents(IProjectDataContents newContents) {
		// TODO Auto-generated method stub
		return false;
	}
}
