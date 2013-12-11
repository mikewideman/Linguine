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
}
