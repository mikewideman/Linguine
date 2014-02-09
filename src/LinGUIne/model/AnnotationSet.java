package LinGUIne.model;

import java.io.File;

/**
 * Represents the annotations of some single ProjectData.
 * 
 * @author Kyle Mullins
 */
public class AnnotationSet implements IProjectData {

	private File annotatedFile;
	
	public AnnotationSet(File annotation){
		annotatedFile = annotation;
	}
	
	public File getFile() {
		return annotatedFile;
	}

	@Override
	public String getName() {
		return annotatedFile.getName();
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
	
	@Override
	public int compareTo(IProjectData projData) {
		if(projData == null){
			return 1;
		}
		
		return annotatedFile.compareTo(projData.getFile());
	}
}
