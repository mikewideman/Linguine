package LinGUIne.wizards;

import LinGUIne.model.Project;
import LinGUIne.model.ProjectGroup;

public class NewGroupData {

	private Project destProject;
	private ProjectGroup parentGroup;
	private String groupName;
	
	public NewGroupData(){
		destProject = null;
		parentGroup = null;
		groupName = null;
	}

	public Project getDestProject(){
		return destProject;
	}

	public void setDestProject(Project project){
		destProject = project;
	}

	public ProjectGroup getParentGroup(){
		return parentGroup;
	}

	public void setParentGroup(ProjectGroup group){
		parentGroup = group;
	}

	public String getGroupName(){
		return groupName;
	}

	public void setGroupName(String name){
		groupName = name;
	}
}
