package LinGUIne.model;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.TreeMap;

import org.eclipse.core.runtime.IPath;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Display;

import LinGUIne.model.annotations.Tag;
import LinGUIne.utilities.FileUtils;

/**
 * Represents a LinGUIne Project with a name, a file system location, and
 * containing Project Data.
 * 
 * @author Kyle Mullins
 */
public class Project {
	/*
	 * Enum used for specifying a subdirectory of a Project.
	 */
	public static enum Subdirectory{
		Data, Results, Annotations;
	}
	
	/*
	 * Constants for directory and file names used when creating a Project on
	 * disk.
	 */
	public static final String DATA_SUBDIR = "data";
	public static final String RESULTS_SUBDIR = "results";
	public static final String ANNOTATIONS_SUBDIR = "annotations";
	public static final String PROJECT_FILE = "linguine.project";
	
	private IPath parentDirectory;
	private String projectName;
	private TreeMap<IProjectData, Integer> projectData;
	private TreeMap<Result, HashSet<Integer>> results;
	private HashMap<Integer, AnnotationSet> annotationSets;
	
	private int lastId;
	private HashSet<ProjectListener> listeners;
	
	/**
	 * Creates a new Project without a name or any Project Data of any kind.
	 * Note: a Project created with this constructor is NOT complete; it must
	 * be given a name.
	 */
	public Project(){
		lastId = 0;
		listeners = new HashSet<ProjectListener>();
		
		projectData = new TreeMap<IProjectData, Integer>();
		results = new TreeMap<Result, HashSet<Integer>>();
		annotationSets = new HashMap<Integer, AnnotationSet>();
	}
	
	/**
	 * Creates a Project just as the default constructor, but the given name is
	 * assigned as well.
	 * 
	 * @param projName	The name of the Project.
	 */
	public Project(String projName){
		this();
		
		projectName = projName;
	}
	
	/**
	 * Parses the given projectFile and creates a new Project based on it.
	 * 
	 * @param projectFile	The linguine.project file for the new Project.
	 * 
	 * @return	A new Project based on the given linguine.project file or null
	 * 			if an error occurred or the file was incorrect.
	 */
	public static Project createFromFile(File projectFile){
		Project newProj = new Project();
		
		try(BufferedReader reader = Files.newBufferedReader(projectFile.toPath(),
				Charset.defaultCharset())){
			
			IPath parentDir = FileUtils.toEclipsePath(projectFile.
					getParentFile().getParentFile());
			newProj.setParentDirectory(parentDir);
			
			//TODO: replace with parsing for actual file format
			if(reader.ready()){
				newProj.setName(reader.readLine());
				
				while(reader.ready()){
					String[] lineParts = reader.readLine().split(":");
					
					//TODO: full format should specify in-memory type of each file
					IPath filePath = newProj.getProjectDirectory().append(lineParts[0]);
					
					if(lineParts[0].startsWith(DATA_SUBDIR)){
						IProjectData projData = new TextData(filePath.toFile());
						
						newProj.addProjectData(projData);
					}
					else if(lineParts[0].startsWith(ANNOTATIONS_SUBDIR)){
						AnnotationSet annotation = new AnnotationSet(filePath.toFile());
						IProjectData annotatedData = newProj.getProjectData(lineParts[1]);
						
						newProj.addAnnotation(annotation, annotatedData);
					}
					else if(lineParts[0].startsWith(RESULTS_SUBDIR)){
//						Result result = new Result(filePath.toFile());
//						
//						//TODO: parse affected files from lineParts[1]
//						newProj.addResult(result, analyzedData)
					}
				}
			}
			else{
				return null;
			}
		}
		catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		
		return newProj;
	}
	
	/**
	 * Sets the name of this Project.
	 */
	public void setName(String projName){
		projectName = projName;
	}
	
	/**
	 * Returns this Project's name.
	 */
	public String getName(){
		return projectName;
	}
	
	/**
	 * Sets the path of the root directory in which this Project resides on
	 * disk.
	 */
	public void setParentDirectory(IPath parentDir){
		parentDirectory = parentDir;
	}
	
	/**
	 * Returns the path of this Project's root directory on disk.
	 */
	public IPath getParentDirectory(){
		return parentDirectory;
	}
	
	/**
	 * Returns the path of this Project's directory which is a folder of the
	 * Project's name within the parent directory.
	 * 
	 * @return	The path of the Project directory, or null if the Project is
	 * 			missing either a name or a parent directory.
	 */
	public IPath getProjectDirectory(){
		if(projectName == null || parentDirectory == null){
			return null;
		}

		return parentDirectory.append(projectName);
	}
	
	/**
	 * Returns the path of the requested subdirectory of this Project's
	 * directory.
	 * 
	 * @param subdir	Enum denoting for which subdirectory to return the path.
	 * 
	 * @return	The path of the requested subdirectory or null if an invalid
	 * 			enum option is provided.
	 */
	public IPath getSubdirectory(Subdirectory subdir){
		String subdirPath;
		
		switch(subdir){
			case Data:
				subdirPath = DATA_SUBDIR;
				break;
			case Results:
				subdirPath = RESULTS_SUBDIR;
				break;
			case Annotations:
				subdirPath = ANNOTATIONS_SUBDIR;
				break;
			default:
				return null;
		}
		
		return getProjectDirectory().append(subdirPath);
	}
	
	/**
	 * Returns the path of the project file for this Project.
	 * 
	 * @return	Path to the project file.
	 */
	public IPath getProjectFile(){
		return getProjectDirectory().append(PROJECT_FILE);
	}
	
	/**
	 * Adds the given ProjectData to the Project if it is not already in the
	 * Project and is not null.
	 * Note: Results and Annotations should not be added with this method.
	 * 
	 * @param projData	The ProjectData to be added to the Project.
	 * 
	 * @return	True iff the ProjectData was successfully added, false
	 * 			otherwise.
	 */
	public boolean addProjectData(IProjectData projData){
		int id = getNextId();
		
		if(projData == null || projectData.containsKey(projData)){
			return false;
		}
		
		projectData.put(projData, id);
		annotationSets.put(id, null);
		
		notifyListeners();
		
		return true;
	}
	
	/**
	 * Adds the given Result to the Project as dependent upon all of the
	 * ProjectData provided in the analyzedData collection. Null Results or
	 * ProjectData collections are disallowed, and all of the ProjectData
	 * objects in the collection must be present within this Project.
	 * 
	 * @param result		The Result to be added.
	 * @param analyzedData	A collection of ProjectData objects upon which the
	 * 						Result is dependent.
	 * 
	 * @return	True iff the Result was added successfully, false otherwise.
	 */
	public boolean addResult(Result result, Collection<IProjectData> analyzedData){
		HashSet<Integer> dataIds = new HashSet<Integer>();
		
		if(analyzedData == null || analyzedData.isEmpty()){
			return false;
		}
		
		for(IProjectData projData: analyzedData){
			if(!containsProjectData(projData)){
				return false;
			}
			
			dataIds.add(projectData.get(projData));
		}
		
		if(addProjectData(result)){
			results.put(result, dataIds);
			
			return true;
		}
		
		return false;
	}
	
	/**
	 * Adds the given AnnotationSet to the Project as markup for the given
	 * ProjectData. Both the AnnotationSet and ProjectData objects must not be
	 * null, and the ProjectData object must be both in the Project and not
	 * already annotated.
	 * 
	 * @param annotationSet	The AnnotationSet to be added to the Project.
	 * @param annotatedData	The ProjectData that the AnnotationSet is marking up.
	 * 
	 * @return	True iff the AnnotationSet was successfully added, false
	 * 			otherwise.
	 */
	public boolean addAnnotation(AnnotationSet annotationSet, IProjectData annotatedData){
		int dataId;
		
		if(containsProjectData(annotatedData) && !isAnnotated(annotatedData)){
			dataId = projectData.get(annotatedData);
			
			if(addProjectData(annotationSet)){
				annotationSets.put(dataId, annotationSet);
				
				return true;
			}
		}
		
		return false;
	}
	
	/**
	 * Returns whether or not the given ProjectData is in this Project.
	 */
	public boolean containsProjectData(IProjectData projData){
		if(projData == null){
			return false;
		}
		
		return projectData.containsKey(projData);
	}
	
	/**
	 * Returns whether or not this Project contains ProjectData of the given
	 * name.
	 */
	public boolean containsProjectData(String projDataName){
		return getProjectData(projDataName) != null;
	}
	
	/**
	 * Returns the ProjectData with the given name if one such ProjectData
	 * object exists in this Project.
	 * 
	 * @param projDataName	The name of the ProjectData to return.
	 * 
	 * @return	The ProjectData instance of the given name, or null.
	 */
	public IProjectData getProjectData(String projDataName){
		for(IProjectData data: projectData.keySet()){
			if(data.getName().equals(projDataName)){
				return data;
			}
		}
		
		return null;
	}
	
	/**
	 * Returns a collection of all the ProjectData objects in this Project.
	 */
	public Collection<IProjectData> getProjectData(){
		return projectData.keySet();
	}
	
	/**
	 * Returns all of the Results in this Project.
	 */
	public Collection<Result> getResults(){
		return results.keySet();
	}
	
	/**
	 * Returns all of the original Project Data objects in this Project (i.e.
	 * neither AnnotationSets nor Results).
	 */
	public Collection<IProjectData> getOriginalData(){
		ArrayList<IProjectData> originalData = new ArrayList<IProjectData>();
		
		for(IProjectData projData: projectData.keySet()){
			if(!(projData instanceof Result) && !(projData instanceof AnnotationSet)){
				originalData.add((TextData)projData);
			}
		}
		
		return originalData;
	}
	
	/**
	 * Returns whether or not the given ProjectData object is annotated in this
	 * Project.
	 */
	public boolean isAnnotated(IProjectData projData){
		if(containsProjectData(projData)){
			int id = projectData.get(projData);
			
			return annotationSets.get(id) != null;
		}
		
		return false;
	}
	
	/**
	 * Returns the AnnotationSet associated with the given ProjectData (if any).
	 */
	public AnnotationSet getAnnotation(IProjectData projData){
		if(containsProjectData(projData)){
			int id = projectData.get(projData);
			
			return annotationSets.get(id);
		}
		
		return null;
	}
	
	/**
	 * Creates all of the necessary files and directories for an empty Project
	 * of this Project's name and parent directory.
	 * 
	 * @return	True iff all files and folders were created successfully, false
	 * 			otherwise.
	 * 
	 * @throws IOException	If files or directories cannot be created in the
	 * 						Project's parent directory.
	 */
	public boolean createProjectFiles() throws IOException{
		IPath projectDir = getProjectDirectory();
		
		if(projectDir == null || Files.exists(projectDir.toFile().toPath())){
			return false;
		}
		
		//Create root project directory
		Files.createDirectory(projectDir.toFile().toPath());
		
		//Create sub-directories
		Files.createDirectory(getSubdirectory(Subdirectory.Data).toFile().toPath());
		Files.createDirectory(getSubdirectory(Subdirectory.Results).toFile().toPath());
		Files.createDirectory(getSubdirectory(Subdirectory.Annotations).toFile().toPath());
		
		//Create project file
		updateProjectFile();
		
		return true;
	}
	
	/**
	 * Updates the project file to reflect the current state of the Project.
	 * Creates the file if it does not already exist.
	 * 
	 * @throws IOException
	 */
	public void updateProjectFile() throws IOException{
		
		Path projectFilePath = getProjectFile().toFile().toPath();
		
		BufferedWriter writer = Files.newBufferedWriter(projectFilePath,
				Charset.defaultCharset());
		
		writer.write(projectName + "\n");

		for(IProjectData projData: projectData.keySet()){
				
			String parentFolderName = projData.getFile().getParentFile().getName();
			String lineToWrite = "";
			
			if(parentFolderName.equalsIgnoreCase(DATA_SUBDIR)){
				lineToWrite += DATA_SUBDIR;
				lineToWrite += "/" + projData.getFile().getName();
				
				if(isAnnotated(projData)){
					AnnotationSet annotation = getAnnotation(projData);
					
					lineToWrite += "\n" + ANNOTATIONS_SUBDIR;
					lineToWrite += "/" + annotation.getFile().getName();
					lineToWrite += ":" + projData.getName();
				}
			}
			else if(parentFolderName.equalsIgnoreCase(ANNOTATIONS_SUBDIR)){
//				lineToWrite += ANNOTATIONS_SUBDIR;
				continue;
			}
			else if(parentFolderName.equalsIgnoreCase(RESULTS_SUBDIR)){
				lineToWrite += RESULTS_SUBDIR;
				lineToWrite += "/" + projData.getFile().getName();
			}
			
			writer.write(lineToWrite + "\n");
		}
		
		writer.close();
	}
	
	/**
	 * Returns the Project name.
	 */
	public String toString(){
		return getName();
	}
	
	/**
	 * Registers the given listener which will get notified whenever this
	 * Project is modified.
	 * 
	 * @param listener	The listener to be registered.
	 */
	public void addListener(ProjectListener listener){
		listeners.add(listener);
	}
	
	/**
	 * Unregisters the given listener such that it will no longer receive
	 * notifications when this Project is modified.
	 * 
	 * @param listener	The listener to be unregistered.
	 */
	public void removeListener(ProjectListener listener){
		listeners.remove(listener);
	}
	
	/**
	 * Notifies all listeners on this Project that it was modified.
	 */
	private void notifyListeners(){
		for(ProjectListener listener: listeners){
			listener.notify(this);
		}
	}
	
	private int getNextId(){
		return lastId++;
	}
	
	/**
	 * Simple listener for receiving notifications when a Project is modified.
	 * 
	 * @author Kyle Mullins
	 */
	public abstract static class ProjectListener {
		public abstract void notify(Project modifiedProj);
	}
}
