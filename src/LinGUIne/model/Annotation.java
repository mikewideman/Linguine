package LinGUIne.model;

import java.io.File;

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
}