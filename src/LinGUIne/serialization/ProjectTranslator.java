package LinGUIne.serialization;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;

import org.eclipse.core.runtime.IPath;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;

import LinGUIne.model.AnnotationSet;
import LinGUIne.model.IProjectData;
import LinGUIne.model.Project;
import LinGUIne.model.ProjectGroup;
import LinGUIne.model.Result;
import LinGUIne.model.Project.Subdirectory;
import LinGUIne.utilities.ClassUtils;

/**
 * Used to serialize/deserialize Projects to/from JSON.
 * 
 * @author Kyle Mullins
 */
public class ProjectTranslator {

	private static final String ASSOCIATED_DATA_ATTRIB = "AssociatedData";
	private static final String TYPE_ATTRIB = "Type";
	private static final String RESULTS_ATTRIB = "Results";
	private static final String ANNOTATIONS_ATTRIB = "Annotations";
	private static final String ORIG_DATA_ATTRIB = "OriginalData";
	private static final String GROUPS_ATTRIB = "Groups";
	private static final String NAME_ATTRIB = "Name";
	private static final String PARENT_ATTRIB = "Parent";
	private static final String GROUP_ATTRIB = "Group";

	/**
	 * Converts the given Project object to Json format so that it may be
	 * written to disk.
	 * 
	 * @param proj	The Project object to be converted.
	 * 
	 * @return	A Json String representing the given Project object.
	 */
	public static String toJson(Project proj){
		JsonElement json = composeFromProject(proj);
		
		return json.toString();
	}
	
	/**
	 * Converts the given Json String into a Project object.
	 * 
	 * @param jsonStr	A Json String to be converted to an in-memory
	 * 					object.
	 * @param parentDir	The path to the workspace directory where the
	 * 					Project is located.
	 * 
	 * @return	A Project object representing the given Json or null if the Json
	 * 			could not be parsed.
	 */
	public static Project fromJson(String jsonStr, IPath parentDir){
		JsonParser parser = new JsonParser();
		JsonElement json = parser.parse(jsonStr);
		
		return parseProjectFromJson(json, parentDir);
	}
	
	/*
	 * Composing into Json
	 */
	
	/**
	 * Returns the root JsonElement of the given Project object.
	 */
	private static JsonElement composeFromProject(Project proj){
		JsonObject rootObj = new JsonObject();
		
		rootObj.addProperty(NAME_ATTRIB, proj.getName());
		
		JsonArray groupAry = new JsonArray();
		rootObj.add(GROUPS_ATTRIB, groupAry);
		
		LinkedList<ProjectGroup> orderedGroups = new LinkedList<ProjectGroup>();
		Collection<ProjectGroup> groupsCopy = new HashSet<ProjectGroup>(
				proj.getGroups());
		
		//Add all top-level groups to the orderedGroups list because they must
		//be serialized first
		for(ProjectGroup group: groupsCopy){
			if(!group.hasParent()){
				orderedGroups.add(group);
			}
		}
		
		//Iterate through groupsCopy adding all groups for which the parent is
		//already in orderedGroups, and do this until groupsCopy is empty
		while(!groupsCopy.isEmpty()){
			groupsCopy.removeAll(orderedGroups);
			
			for(ProjectGroup group: groupsCopy){
				if(orderedGroups.contains(group.getParent())){
					orderedGroups.add(group);
				}
			}
		}
		
		//Add all of the ProjectGroups in rough order
		for(ProjectGroup group: orderedGroups){
			groupAry.add(composeFromProjectGroup(group));
		}
		
		JsonArray origDataAry = new JsonArray();
		rootObj.add(ORIG_DATA_ATTRIB, origDataAry);
		LinkedList<IProjectData> annotatedData = new LinkedList<IProjectData>();
		
		//Convert all Original Data to Json and add them
		for(IProjectData projData: proj.getOriginalData()){
			origDataAry.add(composeFromOriginalData(projData, proj));
			
			if(proj.isAnnotated(projData)){
				annotatedData.add(projData);
			}
		}
		
		JsonArray annotationsAry = new JsonArray();
		rootObj.add(ANNOTATIONS_ATTRIB, annotationsAry);
		
		//Convert all AnnotationSets to Json and add them
		for(IProjectData annotated: annotatedData){
			AnnotationSet annotation = proj.getAnnotation(annotated);
			
			annotationsAry.add(composeFromAnnotationSet(annotation, annotated,
					proj));
		}
		
		JsonArray resultsAry = new JsonArray();
		rootObj.add(RESULTS_ATTRIB, resultsAry);
		
		//Convert all Results to Json and add them
		for(Result result: proj.getResults()){
			resultsAry.add(composeFromResult(result,
					proj.getDataForResult(result), proj));
		}
		
		return rootObj;
	}
	
	/**
	 * Returns the root JsonElement of the given IProjectData object assuming it
	 * is original data and is in the given Project.
	 */
	private static JsonElement composeFromOriginalData(IProjectData projData,
			Project proj){
		
		JsonObject rootObj = new JsonObject();
		
		rootObj.addProperty(NAME_ATTRIB, projData.getName());
		rootObj.addProperty(GROUP_ATTRIB, proj.getGroupFor(projData).getName());
		rootObj.addProperty(TYPE_ATTRIB, ClassUtils.serializeClassName(projData.getClass()));
		
		return rootObj;
	}
	
	/**
	 * Returns the root JsonElement of the given AnnotationSet which annotates
	 * the given IProjectData, and is in the given Project.
	 */
	private static JsonElement composeFromAnnotationSet(
			AnnotationSet annotations, IProjectData projData, Project proj){
		
		JsonObject rootObj = new JsonObject();
		
		rootObj.addProperty(NAME_ATTRIB, annotations.getName());
		rootObj.addProperty(GROUP_ATTRIB, proj.getGroupFor(annotations).getName());
		rootObj.addProperty(TYPE_ATTRIB, ClassUtils.serializeClassName(annotations.getClass()));
		rootObj.addProperty(ASSOCIATED_DATA_ATTRIB, projData.getName());
		
		return rootObj;
	}
	
	/**
	 * Returns the root JsonElement of the given Result which affects the given
	 * collection of IProjectData and is in the given Project.
	 */
	private static JsonElement composeFromResult(Result result,
			Collection<IProjectData> projData, Project proj){
		
		JsonObject rootObj = new JsonObject();
		
		rootObj.addProperty(NAME_ATTRIB, result.getName());
		rootObj.addProperty(GROUP_ATTRIB, proj.getGroupFor(result).getName());
		rootObj.addProperty(TYPE_ATTRIB, ClassUtils.serializeClassName(result.getClass()));
		
		JsonArray assocDataAry = new JsonArray();
		rootObj.add(ASSOCIATED_DATA_ATTRIB, assocDataAry);
		
		for(IProjectData affectedData: projData){
			assocDataAry.add(new JsonPrimitive(affectedData.getName()));
		}
		
		return rootObj;
	}
	
	/**
	 * Returns the root JsonElement of the given ProjectGroup.
	 */
	private static JsonElement composeFromProjectGroup(ProjectGroup group){
		JsonObject rootObj = new JsonObject();
		
		rootObj.addProperty(NAME_ATTRIB, group.getName());
		
		if(group.hasParent()){
			rootObj.addProperty(PARENT_ATTRIB, group.getParent().getName());
		}
		
		return rootObj;
	}
	
	/*
	 * Parsing from Json
	 */
	
	/**
	 * Parses the Project described by the given JsonElement and returns it.
	 */
	private static Project parseProjectFromJson(JsonElement json,
			IPath parentDir){
		Project newProj = new Project();
		newProj.setParentDirectory(parentDir);
		
		if(json.isJsonObject()){
			JsonObject rootObj = json.getAsJsonObject();
			
			JsonElement projName = rootObj.get(NAME_ATTRIB);
			
			//Parse Project name
			if(projName.isJsonPrimitive()){
				newProj.setName(projName.getAsString());
			}
			else{
				return null;
			}
			
			JsonElement groups = rootObj.get(GROUPS_ATTRIB);
			
			//Parse ProjectGroups
			if(groups.isJsonArray()){
				JsonArray groupsAry = groups.getAsJsonArray();
				
				for(JsonElement groupElem: groupsAry){
					if(!parseProjectGroupFromJson(groupElem, newProj)){
						return null;
					}
				}
			}
			
			JsonElement origData = rootObj.get(ORIG_DATA_ATTRIB);
			
			//Parse Original ProjectData
			if(origData.isJsonArray()){
				JsonArray origDataAry = origData.getAsJsonArray();
				
				for(JsonElement dataElem: origDataAry){
					if(!parseOriginalDataFromJson(dataElem, newProj)){
						return null;
					}
				}
			}
			else{
				return null;
			}
			
			JsonElement annotations = rootObj.get(ANNOTATIONS_ATTRIB);
			
			//Parse AnnotationSets
			if(annotations.isJsonArray()){
				JsonArray annotationsAry = annotations.getAsJsonArray();
				
				for(JsonElement annotationElem: annotationsAry){
					if(!parseAnnotationSetFromJson(annotationElem, newProj)){
						return null;
					}
				}
			}
			else{
				return null;
			}
			
			JsonElement results = rootObj.get(RESULTS_ATTRIB);
			
			//Parse Results
			if(results.isJsonArray()){
				JsonArray resultsAry = results.getAsJsonArray();
				
				for(JsonElement resultsElem: resultsAry){
					if(!parseResultFromJson(resultsElem, newProj)){
						return null;
					}
				}
			}
			else{
				return null;
			}
		}
		else{
			return null;
		}
		
		return newProj;
	}
	
	/**
	 * Parses the ProjectData described by the given JsonElement and adds it to
	 * the Project being constructed.
	 */
	private static boolean parseOriginalDataFromJson(JsonElement json,
			Project proj){
		
		if(json.isJsonObject()){
			JsonObject rootObj = json.getAsJsonObject();
			IProjectData newProjData;
			String name;
			ProjectGroup parentGroup;
			
			JsonElement nameElem = rootObj.get(NAME_ATTRIB);
			
			//Parse data name
			if(nameElem.isJsonPrimitive()){
				name = nameElem.getAsString();
			}
			else{
				return false;
			}
			
			JsonElement groupElem = rootObj.get(GROUP_ATTRIB);
			
			//Parse group name
			if(groupElem.isJsonPrimitive()){
				String groupName = groupElem.getAsString();
				
				parentGroup = proj.getGroup(groupName);
				
				if(parentGroup == null){
					return false;
				}
			}
			else{
				return false;
			}
			
			//TODO: Group paths
			File dataFile = proj.getSubdirectory(Subdirectory.Data).
					append(name).toFile();
			JsonElement typeElem = rootObj.get(TYPE_ATTRIB);
			
			//Parse type and create instance
			if(typeElem.isJsonPrimitive()){
				try {
					//TODO: Enforce Constructor taking File as param
					Class<?> clazz = ClassUtils.deserializeClassName(typeElem.getAsString());
					newProjData = (IProjectData)clazz.getDeclaredConstructor(
							File.class).newInstance(dataFile);
				}
				catch(ClassNotFoundException | InstantiationException
						| IllegalAccessException | IllegalArgumentException
						| InvocationTargetException | NoSuchMethodException
						| SecurityException e) {
					return false;
				}
			}
			else{
				return false;
			}
			
			proj.addProjectData(newProjData, parentGroup);
			
			return true;
		}
		
		return false;
	}

	/**
	 * Parses the AnnotationSet described by the given JsonElement and adds it
	 * to the Project being constructed.
	 */
	private static boolean parseAnnotationSetFromJson(JsonElement json,
			Project proj){
		
		if(json.isJsonObject()){
			JsonObject rootObj = json.getAsJsonObject();
			AnnotationSet newAnnotationSet;
			String name;
			
			JsonElement nameElem = rootObj.get(NAME_ATTRIB);
			
			//Parse data name
			if(nameElem.isJsonPrimitive()){
				name = nameElem.getAsString();
			}
			else{
				return false;
			}
			
			//Ignoring group parsing for AnnotationSets because they can't be
			//moved to anything but the default one at the moment
			
			File dataFile = proj.getSubdirectory(Subdirectory.Annotations).
					append(name).toFile();
			JsonElement typeElem = rootObj.get(TYPE_ATTRIB);
			
			//Parse type and create instance
			if(typeElem.isJsonPrimitive()){
				try{
					Class<?> clazz = ClassUtils.deserializeClassName(typeElem.getAsString());
					newAnnotationSet = (AnnotationSet)clazz.
							getDeclaredConstructor(File.class).newInstance(
							dataFile);
				}
				catch(ClassNotFoundException | IllegalArgumentException
						| SecurityException | InstantiationException
						| IllegalAccessException | InvocationTargetException
						| NoSuchMethodException e){
					return false;
				}
			}
			else{
				return false;
			}
			
			JsonElement associatedDataElem = rootObj.get(ASSOCIATED_DATA_ATTRIB);
			
			//Parse associated data name and add AnnotationSet to Project
			if(associatedDataElem.isJsonPrimitive()){
				IProjectData associatedData = proj.getProjectData(
						associatedDataElem.getAsString());
				
				proj.addAnnotation(newAnnotationSet, associatedData);
			}
			
			return true;
		}
		
		return false;
	}
	
	/**
	 * Parses the Result described by the given JsonElement and adds it to the
	 * Project being constructed.
	 */
	private static boolean parseResultFromJson(JsonElement json, Project proj){
		if(json.isJsonObject()){
			JsonObject rootObj = json.getAsJsonObject();
			Result newResult;
			String name;
			ProjectGroup parentGroup;
			
			JsonElement nameElem = rootObj.get(NAME_ATTRIB);
			
			//Parse data name
			if(nameElem.isJsonPrimitive()){
				name = nameElem.getAsString();
			}
			else{
				return false;
			}
			
			JsonElement groupElem = rootObj.get(GROUP_ATTRIB);
			
			//Parse group name
			if(groupElem.isJsonPrimitive()){
				String groupName = groupElem.getAsString();
				
				parentGroup = proj.getGroup(groupName);
				
				if(parentGroup == null){
					return false;
				}
			}
			else{
				return false;
			}
			
			//TODO: Group paths
			File dataFile = proj.getSubdirectory(Subdirectory.Results).
					append(name).toFile();
			JsonElement typeElem = rootObj.get(TYPE_ATTRIB);
			
			//Parse type and create instance
			if(typeElem.isJsonPrimitive()){
				try{
					Class<?> clazz = ClassUtils.deserializeClassName(typeElem.getAsString());
					newResult = (Result)clazz.getDeclaredConstructor(File.class).
							newInstance(dataFile);
				}
				catch(ClassNotFoundException | InstantiationException
						| IllegalAccessException | IllegalArgumentException
						| InvocationTargetException | NoSuchMethodException
						| SecurityException e){
					return false;
				}
			}
			else{
				return false;
			}
			
			JsonElement associatedDataElem = rootObj.get(ASSOCIATED_DATA_ATTRIB);
			
			//Parse associated data names and add Result to Project
			if(associatedDataElem.isJsonArray()){
				JsonArray associatedDataAry =
						associatedDataElem.getAsJsonArray();
				LinkedList<IProjectData> associatedData =
						new LinkedList<IProjectData>();
				
				for(JsonElement dataElem: associatedDataAry){
					if(dataElem.isJsonPrimitive()){
						associatedData.add(proj.getProjectData(
								dataElem.getAsString()));
					}
				}
				
				proj.addResult(newResult, associatedData, parentGroup);
			}
			
			return true;
		}
		
		return false;
	}
	
	/**
	 * Parses the ProjectGroup described by the given JsonElement and adds it to
	 * the Project being constructed.
	 */
	private static boolean parseProjectGroupFromJson(JsonElement json,
			Project proj){
		
		if(json.isJsonObject()){
			JsonObject rootObj = json.getAsJsonObject();
			String name;
			
			JsonElement nameElem = rootObj.get(NAME_ATTRIB);
			
			//Parse group name
			if(nameElem.isJsonPrimitive()){
				name = nameElem.getAsString();
			}
			else{
				return false;
			}
			
			ProjectGroup newGroup = new ProjectGroup(name);
			
			//Parse parent group (if there is one) and set it as parent
			if(rootObj.has(PARENT_ATTRIB)){
				JsonElement parentElem = rootObj.get(PARENT_ATTRIB);
				
				if(nameElem.isJsonPrimitive()){
					String parentName = parentElem.getAsString();
					ProjectGroup parentGroup = proj.getGroup(parentName);
					
					if(parentGroup != null){
						newGroup.setParent(parentGroup);
					}
					else{
						return false;
					}
				}
				else{
					return false;
				}
			}
			
			proj.addGroup(newGroup);
			
			return true;
		}
		
		return false;
	}
}
