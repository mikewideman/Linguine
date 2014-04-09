package LinGUIne.model;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.LinkedList;

import org.eclipse.core.runtime.IPath;

import LinGUIne.utilities.ParameterCheck;

/**
 * Object representing a grouping of ProjectData within a Project (as a folder).
 * 
 * @author Kyle Mullins
 */
public class ProjectGroup {

	protected String groupName;
	protected ProjectGroup parentGroup;
	protected LinkedList<ProjectGroup> childGroups;
	
	/**
	 * Creates a new ProjectGroup with the given name, no parent, and no
	 * children.
	 * Note: Parameter name cannot be null.
	 * 
	 * @param name	The name of the new ProjectGroup.
	 */
	public ProjectGroup(String name){
		ParameterCheck.notNull(name, "name");
		
		groupName = name;
		parentGroup = null;
		childGroups = new LinkedList<ProjectGroup>();
	}
	
	/**
	 * Creates a new ProjectGroup with the given name and the given parent, but
	 * with no children.
	 * 
	 * @param name		The name of the new ProjectGroup.
	 * @param parent	The new ProjectGroup's parent.
	 */
	public ProjectGroup(String name, ProjectGroup parent){
		this(name);
		
		setParent(parent);
	}
	
	/**
	 * Changes the name of this ProjectGroup to the given one.
	 * Note: Parameter newName cannot be null.
	 * 
	 * @param newName	The new name of this ProjectGroup.
	 */
	public void setName(String newName){
		ParameterCheck.notNull(newName, "newName");
		
		groupName = newName;
	}
	
	/**
	 * Returns the name of this ProjectGroup.
	 */
	public String getName(){
		return groupName;
	}
	
	/**
	 * Sets the given ProjectGroup as this one's parent and notifies it that it
	 * has a new child. Automatically removes itself as a child of the previous
	 * parent, if any.
	 * 
	 * @param parent	This ProjectGroup's new parent.
	 */
	public void setParent(ProjectGroup parent){
		removeParent();
		
		parentGroup = parent;
		parentGroup.addChild(this);
	}
	
	/**
	 * Returns this ProjectGroup's current parent group, if any.
	 */
	public ProjectGroup getParent(){
		return parentGroup;
	}
	
	/**
	 * Returns whether or not this ProjectGroup has a parent group.
	 */
	public boolean hasParent(){
		return parentGroup != null;
	}

	/**
	 * Removes this object's parent, if any and notifies it that it has lost a
	 * child.
	 */
	public void removeParent(){
		if(hasParent()){
			parentGroup.removeChild(this);
			parentGroup = null;
		}
	}
	
	/**
	 * Adds the given ProjectGroup as a child of this one.
	 * 
	 * @param child	The ProjectGroup to be added.
	 */
	public void addChild(ProjectGroup child){
		childGroups.add(child);
	}
	
	/**
	 * Returns a collection of the child groups of this ProjectGroup.
	 */
	public Collection<ProjectGroup> getChildren(){
		return childGroups;
	}
	
	/**
	 * Removes the given ProjectGroup as a child of this one.
	 * 
	 * @param child	The child ProjectGroup to be removed.
	 * 
	 * @return	True iff the group was removed, false otherwise.
	 */
	public boolean removeChild(ProjectGroup child){
		return childGroups.remove(child);
	}
	
	/**
	 * Returns a String representing the path from this ProjectGroup's topmost
	 * parent down to this group.
	 * Ex: "ParentOfParentGroup/ParentGroup/ThisGroup"
	 * 
	 * @return	The full path to this ProjectGroup.
	 */
	public String getGroupPath(){
		String groupPath = "";
		
		if(hasParent()){
			groupPath += parentGroup.getGroupPath() + "/";
		}
		
		groupPath += groupName;
		
		return groupPath;
	}
	
	public String getDisplayGroupPath(){
		String groupPath = "";
		
		if(hasParent()){
			groupPath += parentGroup.getDisplayGroupPath() + "/";
		}
		
		groupPath += getName();
		
		return groupPath;
	}
	
	/**
	 * Creates the directory representing this ProjectGroup within the given
	 * Project root directory.
	 * 
	 * @param rootProjectDir	Path to the root directory of the Project
	 * 							housing this ProjectGroup.
	 * 
	 * @return	True iff the directory was created successfully and did not
	 * 			already exist, false otherwise.
	 * 
	 * @throws IOException	If the group's directory could not be created for
	 * 						any reason.
	 */
	public boolean createGroupDirectory(IPath rootProjectDir)
			throws IOException{
		
		String groupPath = getGroupPath();
		Path dirPath = rootProjectDir.append(groupPath).
				toFile().toPath();
		
		if(Files.exists(dirPath)){
			return false;
		}
		
		Files.createDirectory(dirPath);
		
		return true;
	}
	
	/**
	 * Deletes the directory representing this ProjectGroup from disk within the
	 * given Project root directory.
	 * 
	 * @param rootProjectDir	Path to the root directory of the Project
	 * 							housing this ProjectGroup.
	 * 
	 * @return	True iff the directory was deleted successfully and existed in
	 * 			the first place, false otherwise.
	 * 
	 * @throws IOException	If the group's directory could not be deleted for
	 * 						any reason.
	 */
	public boolean deleteGroupDirectory(IPath rootProjectDir)
			throws IOException{
		
		String groupPath = getGroupPath();
		Path dirPath = rootProjectDir.append(groupPath).
				toFile().toPath();
		
		if(Files.exists(dirPath)){
			Files.delete(dirPath);
		
			return true;
		}
		
		return false;
	}
	
	@Override
	public boolean equals(Object otherObj){
		if(otherObj instanceof ProjectGroup){
			ProjectGroup otherGroup = (ProjectGroup)otherObj;
			
			return getName().equals(otherGroup.getName());
		}
		
		return false;
	}
	
	@Override
	public int hashCode(){
		return getName().hashCode();
	}
}
