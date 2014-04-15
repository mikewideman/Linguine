package LinGUIne.parts.advanced.projects;

import LinGUIne.model.Project;

	/**
	 * A root node for a Project Explorer tree which has no parents and a
	 * Project object associated with it.
	 * 
	 * @author Kyle Mullins
	 */
	public class ProjectNode extends ProjectExplorerNode{
		private Project project;
		
		/**
		 * Creates a new ProjectNode to be used as the root for a
		 * tree of the given Project. The Project name is used as the name for
		 * this node.
		 * 
		 * @param proj	The Project object for which this is the root node.
		 */
		public ProjectNode(Project proj){
			super(proj.getName(), null);
			project = proj;
		}
		
		/**
		 * Returns the associated Project object.
		 */
		public Project getProject(){
			return project;
		}

		@Override
		public boolean hasIcon(){
			return true;
		}
		
		@Override
		public String getIconFileName(){
			return "prj_obj.gif";
		}
		
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = super.hashCode();
			
			result = prime * result
					+ ((project == null) ? 0 : project.hashCode());
			
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if(this == obj) {
				return true;
			}
			else if(!super.equals(obj) ||
					!(obj instanceof ProjectNode)) {
				return false;
			}
			
			ProjectNode other = (ProjectNode)obj;
			
			if(project == null) {
				if(other.project != null) {
					return false;
				}
			}
			else if(!project.equals(other.project)) {
				return false;
			}
			
			return true;
		}
	}