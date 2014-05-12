package LinGUIne.wizards;

import LinGUIne.model.Project;
import LinGUIne.model.ProjectGroup;

/**
 * Data object used to carr data through the NewGroupWizard.
 * 
 * @author Kyle Mullins
 */
public class NewGroupData {

	private Project destProject;
	private ProjectGroup parentGroup;
	private String groupName;
	
	/**
	 * Creates a new, empty instance.
	 */
	public NewGroupData(){
		destProject = null;
		parentGroup = null;
		groupName = null;
	}

	/**
	 * Returns the chosen destination Project.
	 */
	public Project getDestProject(){
		return destProject;
	}

	/**
	 * Sets the chosen destination Project.
	 */
	public void setDestProject(Project project){
		destProject = project;
	}

	/**
	 * Returns the chosen parent ProjectGroup.
	 */
	public ProjectGroup getParentGroup(){
		return parentGroup;
	}

	/**
	 * Sets the parent ProjectGroup.
	 */
	public void setParentGroup(ProjectGroup group){
		parentGroup = group;
	}

	/**
	 * Returns the name of the new ProjectGroup.
	 */
	public String getGroupName(){
		return groupName;
	}

	/**
	 * Sets the name of the new ProjectGroup.
	 */
	public void setGroupName(String name){
		groupName = name;
	}
}
