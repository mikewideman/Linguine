package LinGUIne.parts.advanced.projects;

import java.util.LinkedList;

import LinGUIne.model.Result;

public class ProjectExplorerNodeSelection {

	private LinkedList<ProjectNode> projectNodes;
	private LinkedList<GroupNode> groupNodes;
	private LinkedList<ProjectDataNode> projectDataNodes;
	
	public ProjectExplorerNodeSelection(){
		projectNodes = new LinkedList<ProjectNode>();
		groupNodes = new LinkedList<GroupNode>();
		projectDataNodes = new LinkedList<ProjectDataNode>();
	}
	
	public void addSelectedNode(ProjectExplorerNode node){
		if(node instanceof ProjectNode){
			projectNodes.add((ProjectNode)node);
		}
		else if(node instanceof GroupNode){
			groupNodes.add((GroupNode)node);
		}
		else if(node instanceof ProjectDataNode){
			projectDataNodes.add((ProjectDataNode)node);
		}
	}
	
	public int getSelectionCount(){
		return projectNodes.size() + groupNodes.size() +
				projectDataNodes.size();
	}
	
	public LinkedList<ProjectNode> getSelectedProjectNodes(){
		return projectNodes;
	}
	
	public LinkedList<GroupNode> getSelectedGroupNodes(){
		return groupNodes;
	}
	
	public LinkedList<ProjectDataNode> getAllSelectedDataNodes(){
		return projectDataNodes;
	}
	
	public LinkedList<ProjectDataNode> getSelectedOriginalDataNodes(){
		LinkedList<ProjectDataNode> selectedDataNodes =
				new LinkedList<ProjectDataNode>();
		
		for(ProjectDataNode node: projectDataNodes){
			if(!(node.getNodeData() instanceof Result)){
				selectedDataNodes.add(node);
			}
		}
		
		return selectedDataNodes;
	}
	
	public LinkedList<ProjectDataNode> getSelectedResultNodes(){
		LinkedList<ProjectDataNode> selectedDataNodes =
				new LinkedList<ProjectDataNode>();
		
		for(ProjectDataNode node: projectDataNodes){
			if(node.getNodeData() instanceof Result){
				selectedDataNodes.add(node);
			}
		}
		
		return selectedDataNodes;
	}
	
	public void clearNodeSelection(){
		projectNodes.clear();
		groupNodes.clear();
		projectDataNodes.clear();
	}
}
