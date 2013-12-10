package LinGUIne.model;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.TreeMap;

import org.eclipse.core.runtime.IPath;

public class Project {
	public static final String DATA_SUBDIR = "data";
	public static final String RESULTS_SUBDIR = "results";
	public static final String ANNOTATIONS_SUBDIR = "annotations";
	public static final String PROJECT_FILE = "linguine.project";
	
	private IPath parentDirectory;
	private String name;
	private TreeMap<File, Integer> projectData;
	private TreeMap<Result, HashSet<Integer>> results;
	private HashMap<Integer, Annotation> annotations;
	
	private int lastId;
	
	public Project(){
		lastId = 0;
		
		projectData = new TreeMap<File, Integer>();
		results = new TreeMap<Result, HashSet<Integer>>();
		annotations = new HashMap<Integer, Annotation>();
	}
	
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
	
	public IPath getProjectDirectory(){
		if(name == null || parentDirectory == null){
			return null;
		}

		return parentDirectory.append(name);
	}
	
	public void addProjectDataFile(File dataFile){
		int id = getNextId();
		
		projectData.put(dataFile, id);
		annotations.put(id, null);
	}
	
	public File[] getProjectData(){
		return projectData.keySet().toArray(new File[]{});
	}
	
	public Result[] getResults(){
		return results.keySet().toArray(new Result[]{});
	}
	
	public boolean isAnnotated(File dataFile){
		int id = projectData.get(dataFile);
		
		return annotations.get(id) != null;
	}
	
	public boolean createProjectFiles() throws IOException{
		IPath projectDir = getProjectDirectory();
		
		if(projectDir == null || Files.exists(projectDir.toFile().toPath())){
			return false;
		}
		
		//Create root project directory
		Files.createDirectory(projectDir.toFile().toPath());
		
		//Create sub-directories
		Files.createDirectory(projectDir.append(DATA_SUBDIR).toFile().toPath());
		Files.createDirectory(projectDir.append(RESULTS_SUBDIR).toFile().toPath());
		Files.createDirectory(projectDir.append(ANNOTATIONS_SUBDIR).toFile().toPath());
		
		//Create project file
		Files.createFile(projectDir.append(PROJECT_FILE).toFile().toPath());
		
		return true;
	}
	
	public String toString(){
		return getName();
	}
	
	private int getNextId(){
		return lastId++;
	}
}
