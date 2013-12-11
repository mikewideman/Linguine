package LinGUIne.model;

import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collection;
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
	private TreeMap<IProjectData, Integer> projectData;
	private TreeMap<Result, HashSet<Integer>> results;
	private HashMap<Integer, Annotation> annotations;
	
	private int lastId;
	
	public Project(){
		lastId = 0;
		
		projectData = new TreeMap<IProjectData, Integer>();
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
	
	public void addProjectData(IProjectData projData){
		int id = getNextId();
		
		projectData.put(projData, id);
		annotations.put(id, null);
	}
	
	public boolean addResult(Result result, Collection<IProjectData> analyzedData){
		HashSet<Integer> dataIds = new HashSet<Integer>();
		
		for(IProjectData projData: analyzedData){
			if(!containsProjectData(projData)){
				return false;
			}
			
			dataIds.add(projectData.get(projData));
		}
		
		addProjectData(result);
		results.put(result, dataIds);
		
		return true;
	}
	
	public boolean addAnnotation(Annotation annotation, IProjectData annotatedData){
		int dataId;
		
		if(containsProjectData(annotatedData)){
			dataId = projectData.get(annotatedData);
			
			addProjectData(annotation);
			annotations.put(dataId, annotation);
			
			return true;
		}
		
		return false;
	}
	
	public boolean containsProjectData(IProjectData projData){
		return projectData.containsKey(projData);
	}
	
	public Collection<IProjectData> getProjectData(){
		return projectData.keySet();
	}
	
	public Collection<Result> getResults(){
		return results.keySet();
	}
	
	public Collection<TextData> getTextData(){
		ArrayList<TextData> textData = new ArrayList<TextData>();
		
		for(IProjectData projData: projectData.keySet()){
			if(projData instanceof TextData){
				textData.add((TextData)projData);
			}
		}
		
		return textData;
	}
	
	public boolean isAnnotated(IProjectData dataFile){
		int id = projectData.get(dataFile);
		
		return annotations.get(id) != null;
	}
	
	public Annotation getAnnotation(IProjectData projData){
		if(containsProjectData(projData)){
			return annotations.get(projData);
		}
		
		return null;
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
