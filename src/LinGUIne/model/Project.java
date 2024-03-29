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
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.TreeSet;

import org.eclipse.core.runtime.IPath;

import LinGUIne.serialization.ProjectTranslator;
import LinGUIne.utilities.FileUtils;
import LinGUIne.utilities.ParameterCheck;

/**
 * Represents a LinGUIne Project with a name, a file system location, and
 * containing Project Data.
 * Note: A Project for which hasProjectFiles returns false is NOT complete and
 * ready to be used; createProjectFiles must be called first.
 * TODO: Add checks for hasProjectFiles to avoid errors if there are none.
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
	public static final String DATA_SUBDIR_DISPLAY = "Project Data";
	public static final String RESULTS_SUBDIR_DISPLAY = "Results";
	public static final String ANNOTATIONS_SUBDIR_DISPLAY = ANNOTATIONS_SUBDIR;
	
	private IPath parentDirectory;
	private String projectName;
	
	private RootProjectGroup projDataGroup;
	private RootProjectGroup resultsGroup;
	private RootProjectGroup annotationsGroup;
	
	/*
	 * Maps ProjectData to its assigned id.
	 */
	private TreeMap<IProjectData, Integer> projectData;
	
	/*
	 * Maps a Result to the set of ids for the associated ProjectData.
	 */
	private TreeMap<Result, HashSet<Integer>> results;
	
	/*
	 * Maps a Project Data id to its associated AnnotationSet.
	 */
	private HashMap<Integer, AnnotationSet> annotationSets;
	
	/*
	 * Maps a ProjectGroup to its assigned id.
	 */
	private HashMap<ProjectGroup, Integer> groups;
	
	/*
	 * Maps a Project Group id to the set of ids for Project Data located within
	 * the group.
	 */
	private HashMap<Integer, HashSet<Integer>> groupContents;
	
	private boolean hasProjectFiles;
	private int lastId;
	private HashSet<ProjectListener> listeners;
	
	/**
	 * Creates a new Project without a name or any Project Data of any kind.
	 * Adds root ProjectGroups to hold ProjectData, Results, and AnnotationSets.
	 * Note: A Project created with this constructor is NOT complete; it must
	 * be given a name.
	 */
	public Project(){
		hasProjectFiles = false;
		lastId = 0;
		listeners = new HashSet<ProjectListener>();
		
		projectData = new TreeMap<IProjectData, Integer>();
		results = new TreeMap<Result, HashSet<Integer>>();
		annotationSets = new HashMap<Integer, AnnotationSet>();
		groups = new HashMap<ProjectGroup, Integer>();
		groupContents = new HashMap<Integer, HashSet<Integer>>();
		
		projDataGroup = new RootProjectGroup(
				DATA_SUBDIR_DISPLAY, DATA_SUBDIR);
		resultsGroup = new RootProjectGroup(
				RESULTS_SUBDIR_DISPLAY, RESULTS_SUBDIR);
		annotationsGroup = new RootProjectGroup(
				ANNOTATIONS_SUBDIR_DISPLAY, ANNOTATIONS_SUBDIR);
		
		annotationsGroup.setHidden(true);
		
		addGroup(projDataGroup);
		addGroup(resultsGroup);
		addGroup(annotationsGroup);
	}
	
	/**
	 * Creates a Project just as the default constructor, but the given name is
	 * assigned as well.
	 * Note: Parameter projName cannot be null.
	 * 
	 * @param projName	The name of the Project.
	 */
	public Project(String projName){
		this();
		
		ParameterCheck.notNull(projName, "projName");
		
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
		Project newProj;
		
		try(BufferedReader reader = Files.newBufferedReader(projectFile.toPath(),
				Charset.defaultCharset())){
			
			IPath parentDir = FileUtils.toEclipsePath(projectFile.
					getParentFile().getParentFile());
			
			String jsonStr = "";
			
			while(reader.ready()){
				 jsonStr += reader.readLine();
				 jsonStr += "\n";
			}
			
			newProj = ProjectTranslator.fromJson(jsonStr, parentDir);
		}
		catch(IOException ioe) {
			ioe.printStackTrace();
			return null;
		}
	
		if (newProj != null) {
			newProj.hasProjectFiles = true;
		}
		
		return newProj;
	}
	
	/**
	 * Returns whether or not this Project has the file structure on disk needed
	 * in order for it to be usable. Instances for which this function returns
	 * false are not usable until createProjectFiles has been called.
	 */
	public boolean hasProjectFiles(){
		return hasProjectFiles;
	}
	
	/**
	 * Sets the name of this Project.
	 * Note: Parameter projName cannot be null.
	 */
	public void setName(String projName){
		ParameterCheck.notNull(projName, "projName");
		
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
		String groupName;
		
		switch(subdir){
			case Data:
				groupName = DATA_SUBDIR_DISPLAY;
				break;
			case Results:
				groupName = RESULTS_SUBDIR_DISPLAY;
				break;
			case Annotations:
				groupName = ANNOTATIONS_SUBDIR;
				break;
			default:
				return null;
		}
		
		return getPathToGroup(getGroup(groupName));
	}
	
	/**
	 * Returns the full path to the given ProjectGroup or null if it is not in
	 * this Project.
	 */
	public IPath getPathToGroup(ProjectGroup group){
		if(containsGroup(group)){
			return getProjectDirectory().append(group.getGroupPath());
		}
		
		return null;
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
	 * The ProjectData is added to the given ProjectGroup.
	 * Note: Results and AnnotationSets should not be added with this method,
	 * they should instead use the addResult and addAnnotation respectively.
	 * 
	 * @param projData	The ProjectData to be added to the Project.
	 * 
	 * @return	True iff the ProjectData was successfully added, false
	 * 			otherwise.
	 */
	public boolean addProjectData(IProjectData projData, 
			ProjectGroup parentGroup){
		
		if(projData == null || projectData.containsKey(projData) ||
				!groups.containsKey(parentGroup)){
			return false;
		}
		
		int id = getNextId();

		projectData.put(projData, id);
		annotationSets.put(id, null);
		addDataToGroup(projData, parentGroup);
		
		notifyListeners();
		
		return true;
	}
	
	/**
	 * Adds the given ProjectData to the Project as addProjectData(IProjectData,
	 * ProjectGroup) but uses the root ProjectData group as a default
	 * ProjectGroup.
	 */
	public boolean addProjectData(IProjectData projData){
		return addProjectData(projData, projDataGroup);
	}
	
	/**
	 * Adds the given Result to the Project as dependent upon all of the
	 * ProjectData provided in the analyzedData collection. Null Results or
	 * ProjectData collections are disallowed, and all of the ProjectData
	 * objects in the collection must be present within this Project.
	 * The Result is added to the given ProjectGroup.
	 * 
	 * @param result		The Result to be added.
	 * @param analyzedData	A collection of ProjectData objects upon which the
	 * 						Result is dependent.
	 * @param parentGroup	The ProjectGroup that the Result is to be placed in.
	 * 
	 * @return	True iff the Result was added successfully, false otherwise.
	 */
	public boolean addResult(Result result,
			Collection<IProjectData> analyzedData, ProjectGroup parentGroup){
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
		
		if(addProjectData(result, parentGroup)){
			results.put(result, dataIds);
			
			return true;
		}
		
		return false;
	}
	
	/**
	 * Adds the given Result to the Project as addResult(Result,
	 * Collection<IProjectData>, ProjectGroup) but uses the root Results group
	 * as a default ProjectGroup.
	 */
	public boolean addResult(Result result,
			Collection<IProjectData> analyzedData){
		
		return addResult(result, analyzedData, resultsGroup);
	}
	
	/**
	 * Adds the given AnnotationSet to the Project as markup for the given
	 * ProjectData. Both the AnnotationSet and ProjectData objects must not be
	 * null, and the ProjectData object must be both in the Project and not
	 * already annotated.
	 * The AnnotationSet is added to the root annotations group as a default.
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
			
			if(addProjectData(annotationSet, annotationsGroup)){
				annotationSets.put(dataId, annotationSet);
				
				return true;
			}
		}
		
		return false;
	}
	
	/**
	 * Adds the given ProjectGroup to this Project if it wasn't already added.
	 * 
	 * @param newGroup	The new ProjectGroup to be added.
	 * 
	 * @return	True if the ProjectGroup was added successfully, false
	 * 			otherwise.
	 */
	public boolean addGroup(ProjectGroup newGroup){
		if(newGroup == null || groups.containsKey(newGroup)){
			return false;
		}
		
		int id = getNextId();
		
		groups.put(newGroup, id);
		groupContents.put(id, new HashSet<Integer>());
		notifyListeners();
		
		return true;
	}
	
	/**
	 * Adds the given Project Data to the given ProjectGroup if they are both
	 * in this Project.
	 * Note: This also removes the Project Data from its previous group,
	 * if any, as Project Data may only be in one group at a time.
	 * 
	 * @param projData	The Project Data to be added to the group.
	 * @param group		The ProjectGroup into which the Project Data is to be
	 * 					added.
	 * 
	 * @return	True iff the data was added successfully to the group, false
	 * 			otherwise.
	 */
	public boolean addDataToGroup(IProjectData projData, ProjectGroup group){
		if(containsGroup(group) && containsProjectData(projData)){
			int projDataId = projectData.get(projData);

			removeProjectDataFromGroup(projDataId);			
			
			groupContents.get(groups.get(group)).add(projDataId);
			//TODO: Move the ProjectData File as needed
			
			return true;
		}
		
		return false;
	}
	
	/**
	 * Attempts to remove the given ProjectData from this Project and returns
	 * whether or not the operation was successful.
	 * 
	 * @param projData	The ProjectData to be removed from the Project.
	 * 
	 * @return	True iff the ProjectData was removed, false if it doesn't exist.
	 */
	public boolean removeProjectData(IProjectData projData){
		//TODO: Should this function also remove associated Results/Annotations?
		if(containsProjectData(projData)){
			int projDataId = projectData.remove(projData);
			removeProjectDataFromGroup(projDataId);
			
			notifyListeners();
			
			return true;
		}
		
		return false;
	}
	
	/**
	 * Removes the given Result from this Project if it exists.
	 * 
	 * @param result	The Result to be removed.
	 * 
	 * @return	True iff the Result was removed, false if it doesn't exist.
	 */
	public boolean removeResult(Result result){
		if(results.containsKey(result)){
			results.remove(result);
			removeProjectData(result);
			
			return true;
		}
		
		return false;
	}
	
	/**
	 * Removes from this Project the AnnotationSet associated with the given
	 * ProjectData if there is one.
	 * 
	 * @param annotatedData	The annotated ProjectData for which to remove the
	 * 						AnnotationSet.
	 * 
	 * @return	True iff the AnnotationSet was removed, false if the given
	 * 			ProjectData was not annotated.
	 */
	public boolean removeAnnotationFrom(IProjectData annotatedData){
		if(containsProjectData(annotatedData)){
			int id = projectData.get(annotatedData);
			
			if(annotationSets.containsKey(id)){
				AnnotationSet removedSet = annotationSets.remove(id);
				removeProjectData(removedSet);
				
				return true;
			}
		}
		
		return false;
	}
	
	/**
	 * Removes from this Project the given ProjectGroup and all of its child
	 * groups.
	 * 
	 * @param group	The ProjectGroup to be removed.
	 * 
	 * @return	True iff the ProjectGroup was removed, false if the group was
	 * 			not in this Project to begin with.
	 */
	public boolean removeGroup(ProjectGroup group){
		if(containsGroup(group)){
			int id = groups.remove(group);
			groupContents.remove(id);
			group.removeParent();
			
			for(ProjectGroup childGroup: group.getChildren()){
				removeGroup(childGroup);
			}
			
			notifyListeners();
			
			return true;
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
	 * Returns whether or not the given ProjectGroup is in this Project.
	 */
	public boolean containsGroup(ProjectGroup group){
		return groups.containsKey(group);
	}
	
	/**
	 * Returns whether or not this Project contains a ProjectGroup of the given
	 * name.
	 */
	public boolean containsGroup(String groupName){
		return getGroup(groupName) != null;
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
	 * Returns all of the Project Data associated with the given Result object
	 * or an empty collection if the Result is not in this Project.
	 */
	public Collection<IProjectData> getDataForResult(Result result){
		if(containsProjectData(result)){
			HashSet<IProjectData> affectedData = new HashSet<IProjectData>();
			
			for(int projDataId: results.get(result)){
				affectedData.add(getProjectDataById(projDataId));
			}
			
			return affectedData;
		}
		
		return new HashSet<IProjectData>();
	}
	
	/**
	 * Returns the Project Data associated with the given AnnotationSet object
	 * or null if the AnnotationSet is not in this Project.
	 */
	public IProjectData getDataForAnnotation(AnnotationSet annotation){
		for(Entry<Integer, AnnotationSet> pair: annotationSets.entrySet()){
			if(annotation.compareTo(pair.getValue()) == 0){
				return getProjectDataById(pair.getKey());
			}
		}
		
		return null;
	}
	
	/**
	 * Returns all of the ProjectGroups in this Project.
	 */
	public Collection<ProjectGroup> getGroups(){
		return groups.keySet();
	}
	
	/**
	 * Returns the ProjectGroup with the given name if one exists in this
	 * Project.
	 * 
	 * @param groupName	The display name of the ProjectGroup to be returned.
	 * 
	 * @return	The ProjectGroup instance of the given name, or null.
	 */
	public ProjectGroup getGroup(String groupName){
		for(ProjectGroup group: groups.keySet()){
			if(group.getName().equals(groupName)){
				return group;
			}
		}
		
		return null;
	}
	
	/**
	 * Returns all of the Project Data contained within the given ProjectGroup
	 * or an empty collection if there is nothing in the group.
	 */
	public Collection<IProjectData> getDataInGroup(ProjectGroup group){
		TreeSet<IProjectData> groupedData = new TreeSet<IProjectData>();
		
		if(containsGroup(group)){
			int groupId = groups.get(group);
			
			for(int projDataId: groupContents.get(groupId)){
				groupedData.add(getProjectDataById(projDataId));
			}
		}
		
		return groupedData;
	}
	
	/**
	 * Returns the ProjectGroup containing the given ProjectData or null if the
	 * given data does not exist in this Project.
	 */
	public ProjectGroup getGroupFor(IProjectData projData){
		if(containsProjectData(projData)){
			int id = projectData.get(projData);
			
			for(Entry<Integer, HashSet<Integer>> groupPair: 
				groupContents.entrySet()){
				
				for(Integer dataId: groupPair.getValue()){
					if(id == dataId){
						return getGroupById(groupPair.getKey());
					}
				}
			}
		}
		
		return null;
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
	 * Returns whether or not the given ProjectData has any Results associated
	 * with it.
	 * 
	 * @param projData	The ProjectData for which to search for Results.
	 * 
	 * @return	True iff the given ProjectData has Results, false otherwise.
	 */
	public boolean hasResults(IProjectData projData){
		if(containsProjectData(projData)){
			int id = projectData.get(projData);
			
			for(HashSet<Integer> dataIds: results.values()){
				for(Integer projectDataId: dataIds){
					if(projectDataId == id){
						return true;
					}
				}
			}
		}
		
		return false;
	}
	
	/**
	 * Returns all of the Results which are associated with the given
	 * ProjectData.
	 * 
	 * @param projData	The ProjectData for which to look up Results.
	 * 
	 * @return	A collection of Results associated with projData or an empty
	 * 			collection if there are none or it does not exist in this
	 * 			Project.
	 */
	public Collection<Result> getResults(IProjectData projData){
		ArrayList<Result> dataResults = new ArrayList<Result>();
		
		if(containsProjectData(projData)){
			int id = projectData.get(projData);
			
			for(Entry<Result, HashSet<Integer>> resultPair: results.entrySet()){
				for(int projectDataId: resultPair.getValue()){
					if(projectDataId == id){
						dataResults.add(resultPair.getKey());
						break;
					}
				}
			}
		}
		
		return dataResults;
	}
	
	/**
	 * Returns whether or not there exists the given Result type for the given
	 * ProjectData.
	 * 
	 * @param projData	The ProjectData for which to look up the given Result
	 * 					type.
	 * @param clazz		The type of Result for which to search.
	 * 
	 * @return	True iff the given ProjectData has a Result of the given type,
	 * 			false otherwise.
	 */
	public <T extends Result> boolean hasResultType(IProjectData projData, Class<T> clazz){
		return getResultType(projData, clazz) != null;
	}
	
	/**
	 * Returns the given Result type for the given ProjectData if it exists.
	 * 
	 * @param projData	The ProjectData for which to look up the given Result
	 * 					type.
	 * @param clazz		The type of Result for which to search.
	 * 
	 * @return	A Result of the given type for the given ProjectData, or null
	 * 			if either the ProjectData does not exist or there was no
	 * 			matching Result.
	 */
	public <T extends Result> Result getResultType(IProjectData projData,
			Class<T> clazz){
		
		if(containsProjectData(projData)){
			int id = projectData.get(projData);
			
			for(Entry<Result, HashSet<Integer>> resultPair: results.entrySet()){
				if(resultPair.getKey().getClass() == clazz){
					for(int projectDataId: resultPair.getValue()){
						if(projectDataId == id){
							return resultPair.getKey();
						}
					}
				}
			}
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
		
		if(projectDir == null || Files.exists(projectDir.toFile().toPath()) ||
				hasProjectFiles){
			return false;
		}
		
		//Create root project directory
		Files.createDirectory(projectDir.toFile().toPath());
		
		for(ProjectGroup group: groups.keySet()){
			group.createGroupDirectory(projectDir);
		}
		
		//Create project file
		updateProjectFile();
		
		hasProjectFiles = true;
		
		return true;
	}
	
	/**
	 * Deletes all ProjectData from disk as well as the Project file and all
	 * Project directories.
	 * 
	 * @return	True iff all ProjectData, files, and directories were deleted
	 * 			successfully, false if the Project had no Project files.
	 * 
	 * @throws IOException	If any of the Project's files could not be deleted
	 * 						from disk for any reason.
	 */
	public boolean deleteProjectContentsOnDisk() throws IOException{
		if(hasProjectFiles)
		{
			//Delete all ProjectData
			for(IProjectData projData: projectData.keySet()){
				projData.deleteContentsOnDisk();
			}
			
			deleteProjectFiles();
			
			//Delete all ProjectGroups
			for(ProjectGroup group: groups.keySet()){
				group.deleteGroupDirectory(getProjectDirectory());
			}
			
			hasProjectFiles = false;
			return true;
		}
		
		return false;
	}
	
	/**
	 * Updates the project file to reflect the current state of the Project.
	 * Creates the file if it does not already exist.
	 * 
	 * @throws IOException
	 */
	public void updateProjectFile() throws IOException{
		Path projectFilePath = getProjectFile().toFile().toPath();
		
		try(BufferedWriter writer = Files.newBufferedWriter(projectFilePath,
				Charset.defaultCharset())){
			
			String jsonStr = ProjectTranslator.toJson(this);
			
			if(jsonStr != null){
				writer.write(jsonStr);
			}
			else{
				//TODO: Throw exception of some sort
			}
		}
		catch(IOException ioe){
			ioe.printStackTrace();
		}
	}
	
	/**
	 * Returns the Project name.
	 */
	public String toString(){
		return getName();
	}
	
	@Override
	public int hashCode(){
		return getName().hashCode();
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
	
	/**
	 * Deletes the Project file and all Project sub-directories, followed by the
	 * Project directory itself.
	 * 
	 * @throws IOException	If any of the files/directories could not be deleted
	 * 						for any reason.
	 */
	private void deleteProjectFiles() throws IOException{
		IPath projectDir = getProjectDirectory();
		
		//Delete project file
		Files.delete(getProjectFile().toFile().toPath());
		
		//Delete root project directory
		Files.delete(projectDir.toFile().toPath());
	}

	/**
	 * Looks up Project Data by its id.
	 */
	private IProjectData getProjectDataById(int id){
		for(Entry<IProjectData, Integer> projData: projectData.entrySet()){
			if(projData.getValue() == id){
				return projData.getKey();
			}
		}
		
		return null;
	}
	
	/**
	 * Looks up a ProjectGroup by its id.
	 */
	private ProjectGroup getGroupById(int id){
		for(Entry<ProjectGroup, Integer> group: groups.entrySet()){
			if(group.getValue() == id){
				return group.getKey();
			}
		}
		
		return null;
	}
	
	/**
	 * Removes the Project Data with the given id from its parent ProjectGroup.
	 * Note: This function leaves the Project Data in an invalid state, as it is
	 * not belonging to at least one ProjectGroup.
	 */
	private void removeProjectDataFromGroup(int projDataId){
		for(HashSet<Integer> contents: groupContents.values()){
			for(int dataId: contents){
				if(dataId == projDataId){
					contents.remove(projDataId);
					
					return;
				}
			}
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
