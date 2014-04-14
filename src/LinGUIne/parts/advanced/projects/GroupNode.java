package LinGUIne.parts.advanced.projects;

import LinGUIne.model.ProjectGroup;

	/**
	 * A child node for a Project Explorer tree which has a ProjectGroup object
	 * associated with it.
	 * 
	 * @author Kyle Mullins
	 */
	public class GroupNode extends ProjectExplorerNode{
		private ProjectGroup nodeGroup;
		
		/**
		 * Creates a new GroupNode with the given name, the given
		 * parent node, and the given ProjectGroup.
		 * 
		 * @param name		The name of this node.
		 * @param parent	The node's parent node.
		 * @param group		The ProjectGroup to be associated with this node.
		 */
		public GroupNode(String name, ProjectExplorerNode parent,
				ProjectGroup group){
			
			super(name, parent);
			nodeGroup = group;
		}
		
		/**
		 * Returns the ProjectGroup associated with this node.
		 */
		public ProjectGroup getNodeGroup(){
			return nodeGroup;
		}

		@Override
		public boolean hasIcon(){
			return true;
		}
		
		@Override
		public String getIconFileName(){
			return "packagefolder_obj.gif";
		}
		
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = super.hashCode();

			result = prime * result
					+ ((nodeGroup == null) ? 0 : nodeGroup.hashCode());
			
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if(this == obj) {
				return true;
			}
			else if(!super.equals(obj) || !(obj instanceof GroupNode)) {
				return false;
			}

			GroupNode other = (GroupNode)obj;
			
			if(nodeGroup == null) {
				if(other.nodeGroup != null) {
					return false;
				}
			}
			else if(!nodeGroup.equals(other.nodeGroup)) {
				return false;
			}
			
			return true;
		}
	}