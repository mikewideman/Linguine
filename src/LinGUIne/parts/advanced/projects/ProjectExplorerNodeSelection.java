package LinGUIne.parts.advanced.projects;

import java.util.LinkedList;

import LinGUIne.model.Result;

/**
 * Represents the currently selected nodes of the ProjectExplorer tree.
 * 
 * @author Kyle Mullins
 */
public class ProjectExplorerNodeSelection {

	private LinkedList<ProjectNode> projectNodes;
	private LinkedList<GroupNode> groupNodes;
	private LinkedList<ProjectDataNode> projectDataNodes;
	
	/**
	 * Creates a new empty selection of nodes.
	 */
	public ProjectExplorerNodeSelection(){
		projectNodes = new LinkedList<ProjectNode>();
		groupNodes = new LinkedList<GroupNode>();
		projectDataNodes = new LinkedList<ProjectDataNode>();
	}
	
	/**
	 * Adds the given ProjectExplorerNode to the selection.
	 * 
	 * @param node	The node to be added to the selection.
	 */
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
	
	/**
	 * Returns the total number of nodes that are selected.
	 */
	public int getSelectionCount(){
		return projectNodes.size() + groupNodes.size() +
				projectDataNodes.size();
	}
	
	/**
	 * Returns a list of all selected ProjectNodes.
	 */
	public LinkedList<ProjectNode> getSelectedProjectNodes(){
		return projectNodes;
	}
	
	/**
	 * Returns a list of all selected GroupNodes.
	 */
	public LinkedList<GroupNode> getSelectedGroupNodes(){
		return groupNodes;
	}
	
	/**
	 * Returns a list of all selected ProjectDataNodes.
	 */
	public LinkedList<ProjectDataNode> getAllSelectedDataNodes(){
		return projectDataNodes;
	}
	
	/**
	 * Returns a list of just the selected ProjectDataNodes representing
	 * original data.
	 */
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
	
	/**
	 * Returns a list of just the selection ProjectDataNodes representing
	 * Results.
	 * 
	 * @return
	 */
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
	
	/**
	 * Clears the selection.
	 */
	public void clearNodeSelection(){
		projectNodes.clear();
		groupNodes.clear();
		projectDataNodes.clear();
	}
}
