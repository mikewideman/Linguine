package LinGUIne.parts.advanced.projects;

import LinGUIne.model.IProjectData;

	/**
	 * A child node for a Project Explorer tree which has an IProjectData
	 * object associated with it.
	 * 
	 * @author Kyle Mullins
	 */
	public class ProjectDataNode extends ProjectExplorerNode{
		private IProjectData nodeData;
		
		/**
		 * Creates a new ProjectDataNode with the given name, the given
		 * parent node, and the given ProjectData.
		 * 
		 * @param name		The name of this node.
		 * @param parent	This node's parent node.
		 * @param data		The ProjectData to be associated with this node.
		 */
		public ProjectDataNode(String name, ProjectExplorerNode parent,
				IProjectData data){
			
			super(name, parent);
			nodeData = data;
		}
		
		/**
		 * Returns the ProjectData associated with this node.
		 */
		public IProjectData getNodeData(){
			return nodeData;
		}

		@Override
		public boolean hasIcon(){
			return true;
		}
		
		@Override
		public String getIconFileName(){
			return "file_obj.gif";
		}
		
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = super.hashCode();
			
			result = prime * result
					+ ((nodeData == null) ? 0 : nodeData.hashCode());

			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if(this == obj) {
				return true;
			}
			else if(!super.equals(obj) || !(obj instanceof ProjectDataNode)){
				return false;
			}
			
			ProjectDataNode other = (ProjectDataNode)obj;

			if(nodeData == null) {
				if(other.nodeData != null) {
					return false;
				}
			}
			else if(!nodeData.equals(other.nodeData)) {
				return false;
			}
			
			return true;
		}
	}