package LinGUIne.parts.advanced.projects;

import java.util.ArrayList;

import LinGUIne.model.IProjectData;
import LinGUIne.model.ProjectGroup;

	/**
	 * A tree node within the Project Explorer with an arbitrary number of
	 * children, 0-1 parents, and a name.
	 * 
	 * @author Kyle Mullins
	 */
	public abstract class ProjectExplorerNode{
		
		protected String nodeName;
		protected ProjectExplorerNode parentNode;
		protected ArrayList<ProjectExplorerNode> children;
		
		/**
		 * Creates a new ProjectExplorerNode with a name and a parent node.
		 * 
		 * @param name		The name of this node.
		 * @param parent	This node's parent node.
		 */
		public ProjectExplorerNode(String name, ProjectExplorerNode parent){
			nodeName = name;
			parentNode = parent;
			children = new ArrayList<ProjectExplorerNode>();
		}
		
		/**
		 * Creates a new ProjectDataNode child with the given name, the
		 * given ProjectData, and with this node as its parent.
		 * 
		 * @param name	The name of the new node to be created.
		 * @param data	The ProjectData the new node is to be given at
		 * 				creation.
		 * 
		 * @return	The node that was created.
		 */
		public ProjectExplorerNode addChild(String name, IProjectData data){
			ProjectExplorerNode newNode = new ProjectDataNode(name, this, data);
			children.add(newNode);
			
			return newNode;
		}
		
		/**
		 * Creates a new GroupNode child with the given name, the
		 * given ProjectGroup, and with this node as its parent.
		 * 
		 * @param name	The name of the new node to be created.
		 * @param group	The ProjectGroup the new node is to be given at
		 * 				creation.
		 * 
		 * @return	The node that was created.
		 */
		public ProjectExplorerNode addChild(String name, ProjectGroup group){
			ProjectExplorerNode newNode = new GroupNode(name, this, group);
			children.add(newNode);
			
			return newNode;
		}
		
		/**
		 * Returns the node's name.
		 */
		public String getName(){
			return nodeName;
		}
		
		/**
		 * Returns whether or not the node has any child nodes.
		 */
		public boolean hasChildren(){
			return !children.isEmpty();
		}
		
		/**
		 * Returns a list of all of this ProjectExplorerNode's child nodes.
		 */
		public ProjectExplorerNode[] getChildren(){
			return children.toArray(new ProjectExplorerNode[]{});
		}
		
		/**
		 * Returns whether or not this node has a parent node.
		 */
		public boolean hasParent(){
			return parentNode != null;
		}
		
		/**
		 * Returns this node's parent node, or null if it does not have one.
		 */
		public ProjectExplorerNode getParent(){
			return parentNode;
		}
		
		/**
		 * Returns this node's name.
		 */
		public String toString(){
			return nodeName;
		}
		
		/**
		 * Traces through this node's parents (if any) until the root node is
		 * found for the tree this node belongs to.
		 * 
		 * @return	The root node of this node's tree, or this node if it is the
		 * 			root of the tree.
		 */
		public ProjectExplorerNode getRootNode(){
			ProjectExplorerNode rootNode = this;
			
			while(rootNode.hasParent()){
				rootNode = rootNode.getParent();
			}
			
			return rootNode;
		}
		
		/**
		 * Returns whether or not this node should have an icon.
		 */
		public boolean hasIcon(){
			return false;
		}
		
		/**
		 * Returns the name of the icon file to be used as this node's icon.
		 */
		public String getIconFileName(){
			return null;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;

			result = prime * result
					+ ((nodeName == null) ? 0 : nodeName.hashCode());
			result = prime * result
					+ ((parentNode == null) ? 0 : parentNode.hashCode());
			
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if(this == obj) {
				return true;
			}
			else if(obj == null || !(obj instanceof ProjectExplorerNode)) {
				return false;
			}

			ProjectExplorerNode other = (ProjectExplorerNode)obj;
			
			if(nodeName == null) {
				if(other.nodeName != null) {
					return false;
				}
			}
			if(!nodeName.equals(other.nodeName)) {
				return false;
			}
			else if(parentNode == null) {
				if(other.parentNode != null) {
					return false;
				}
			}
			else if(!parentNode.equals(other.parentNode)) {
				return false;
			}
			
			return true;
		}
	}