package LinGUIne.model;

import java.io.File;

/**
 * Represents the annotations of some ProjectData.
 * 
 * @author Kyle Mullins
 */
public class Annotation implements IProjectData {

	private File annotationFile;
	
	public Annotation(File annotation){
		annotationFile = annotation;
	}
	
	public File getFile() {
		return annotationFile;
	}

	@Override
	public int compareTo(IProjectData projData) {
		if(projData == null){
			return 1;
		}
		
		return annotationFile.compareTo(projData.getFile());
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
