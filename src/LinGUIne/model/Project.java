package LinGUIne.model;

import java.io.IOException;
import java.nio.file.Files;

import org.eclipse.core.runtime.IPath;

public class Project {
	public static final String DATA_SUBDIR = "Data";
	public static final String RESULTS_SUBDIR = "Results";
	public static final String ANNOTATIONS_SUBDIR = "Annotations";
	
	private IPath parentDirectory;
	private String name;
	
	public Project(){}
	
	public void setName(String projName){
		name = projName;
	}
	
	public String getName(){
		return name;
	}
	
	public void setParentDirectory(IPath parentDir){
		parentDirectory = parentDir;
	}
	
	public IPath getParentDirectory(){
		return parentDirectory;
	}
	
	public boolean createProjectFiles() throws IOException{
		if(name == null || parentDirectory == null){
			return false;
		}

		IPath projectDir = parentDirectory.append(name);
		
		if(Files.exists(projectDir.toFile().toPath())){
			return false;
		}
		
		Files.createDirectory(projectDir.toFile().toPath());
		Files.createDirectory(projectDir.append(DATA_SUBDIR).toFile().toPath());
		Files.createDirectory(projectDir.append(RESULTS_SUBDIR).toFile().toPath());
		Files.createDirectory(projectDir.append(ANNOTATIONS_SUBDIR).toFile().toPath());
		
		Files.createFile(projectDir.append(".metadata").toFile().toPath());
		Files.createFile(projectDir.append(".properties").toFile().toPath());
		
		return true;
	}
}
