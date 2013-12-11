package LinGUIne.parts;

import java.util.ArrayList;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;
import javax.inject.Named;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.core.runtime.Platform;
import org.eclipse.e4.ui.di.Focus;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;

import LinGUIne.model.IProjectData;
import LinGUIne.model.Project;
import LinGUIne.model.ProjectManager;
import LinGUIne.model.Result;
import LinGUIne.model.TextData;

/**
 * View which displays Project contents to the user as a collapsable tree.
 * 
 * @author Kyle Mullins
 */
public class ProjectExplorer {

	private TreeViewer tree;
	private MApplication application;

	@Inject
	public ProjectExplorer(MApplication app){
		application = app;
	}
	
	/**
	 * Initializes the components of this view.
	 * 
	 * @param parent	The parent component.
	 */
	@PostConstruct
	public void createComposite(Composite parent){
		parent.setLayout(new GridLayout());

		tree = new TreeViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);
		tree.setContentProvider(new ProjectExplorerContentProvider());
		tree.setLabelProvider(new ProjectExplorerLabelProvider());
		tree.getTree().setLayoutData(new GridData(GridData.FILL_BOTH));
		
		ProjectManager projectMan = new ProjectManager(Platform.getLocation());
		projectMan.loadProjects();
		tree.setInput(projectMan);
		
		application.getContext().set("ProjectManager", projectMan);
	}

	/**
	 * Sets the focus to the TreeViewer.
	 */
	@Focus
	public void setFocus(){
		tree.getTree().setFocus();
	}

	@PreDestroy
	public void dispose() {
	}

	class ProjectExplorerContentProvider implements ITreeContentProvider {
		
		private ArrayList<ProjectExplorerTree> projectTrees;
		
		public ProjectExplorerContentProvider(){
			projectTrees = new ArrayList<ProjectExplorerTree>();
		}
		
		@Inject
		public void inputChanged(@Named("ProjectManager") ProjectManager projectMan){
			projectTrees = new ArrayList<ProjectExplorerTree>();
			
			for(Project proj: projectMan.getProjects()){
				ProjectExplorerTree newRoot = new ProjectExplorerTree(proj);
				ProjectExplorerNode dataNode = newRoot.addChild("Project Data");
				ProjectExplorerNode resultsNode = newRoot.addChild("Results");
				
				for(TextData data: proj.getTextData()){
					dataNode.addDataChild(data.getFile().getName(), data);
				}
				
				for(Result result: proj.getResults()){
					resultsNode.addDataChild(result.getFile().getName(), result);
				}
				
				projectTrees.add(newRoot);
			}
		}
		
		@Override
		public void dispose() {}

		@Override
		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
			System.out.println("inputChanged called.");
			
			if(newInput != null){
				inputChanged((ProjectManager)newInput);
			}
		}

		@Override
		public Object[] getElements(Object inputElement) {
			return projectTrees.toArray();
		}

		@Override
		public Object getParent(Object element) {
			return ((ProjectExplorerNode)element).getParent();
		}

		@Override
		public boolean hasChildren(Object element) {
			return ((ProjectExplorerNode)element).hasChildren();
		}
		
		@Override
		public Object[] getChildren(Object parentElement) {
			return ((ProjectExplorerNode)parentElement).getChildren();
		}
	}
	
	/**
	 * A tree node within the Project Explorer with an arbitrary number of
	 * children, 0-1 parents, and a name.
	 * 
	 * @author Kyle
	 */
	class ProjectExplorerNode{
		
		protected String nodeName;
		protected ProjectExplorerNode parentNode;
		protected ArrayList<ProjectExplorerNode> children;
		
		public ProjectExplorerNode(String name, ProjectExplorerNode parent){
			nodeName = name;
			parentNode = parent;
			children = new ArrayList<ProjectExplorerNode>();
		}
		
		public ProjectExplorerNode addChild(String name){
			ProjectExplorerNode newNode = new ProjectExplorerNode(name, this);
			children.add(newNode);
			
			return newNode;
		}
		
		public ProjectExplorerNode addDataChild(String name, IProjectData data){
			ProjectExplorerNode newNode = new ProjectExplorerDataNode(name, this, data);
			children.add(newNode);
			
			return newNode;
		}
		
		public String getName(){
			return nodeName;
		}
		
		public boolean hasChildren(){
			return !children.isEmpty();
		}
		
		public ProjectExplorerNode[] getChildren(){
			return children.toArray(new ProjectExplorerNode[]{});
		}
		
		public boolean hasParent(){
			return parentNode != null;
		}
		
		public ProjectExplorerNode getParent(){
			return parentNode;
		}
		
		public String toString(){
			return nodeName;
		}
	}
	
	/**
	 * A root node for a Project Explorer tree which has no parents and a
	 * Project object associated with it.
	 * 
	 * @author Kyle
	 */
	class ProjectExplorerTree extends ProjectExplorerNode{
		private Project project;
		
		public ProjectExplorerTree(Project proj){
			super(proj.getName(), null);
			project = proj;
		}
		
		public Project getProject(){
			return project;
		}
	}
	
	/**
	 * A child node for a Project Explorer tree which has an IProjectData
	 * object associated with it.
	 * 
	 * @author Kyle
	 */
	class ProjectExplorerDataNode extends ProjectExplorerNode{
		private IProjectData nodeData;
		
		public ProjectExplorerDataNode(String name, ProjectExplorerNode parent,
				IProjectData data){
			
			super(name, parent);
			nodeData = data;
		}
		
		public IProjectData getNodeData(){
			return nodeData;
		}
	}
	
	/**
	 * Simple label provider which returns the name of a ProjectExplorerNode
	 * to be used as a label.
	 * 
	 * @author Kyle
	 */
	class ProjectExplorerLabelProvider extends LabelProvider{
		@Override
		public String getText(Object element){
			return ((ProjectExplorerNode)element).getName();
		}
	}

}
