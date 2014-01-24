package LinGUIne.parts;

import java.util.ArrayList;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;

import org.eclipse.core.runtime.Platform;
import org.eclipse.e4.core.contexts.ContextInjectionFactory;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.e4.ui.di.Focus;
import org.eclipse.e4.ui.di.UIEventTopic;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;

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

	public static final String PROJECT_EXPLORER_DOUBLE_CLICK = "Project_Explorer/Double_Click";
	
	private TreeViewer tree;
	private MApplication application;

	@Inject
	private IEventBroker eventBroker;
	
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
		
		application.getContext().set(ProjectManager.class, projectMan);
		ContextInjectionFactory.inject(projectMan, application.getContext());
		
		/*
		 * Add listeners to TreeViewer
		 */
		tree.addSelectionChangedListener(new ISelectionChangedListener(){

			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				//TODO
			}
		});
		
		tree.addDoubleClickListener(new IDoubleClickListener(){
			
			@Override
			public void doubleClick(DoubleClickEvent event) {
				IStructuredSelection selection = (IStructuredSelection)event.getSelection();
				Object selectedNode = selection.getFirstElement();
				
				if(selectedNode instanceof ProjectExplorerDataNode){
					//If user double clicks a file, post an event for the editor
					ProjectExplorerDataNode dataNode = (ProjectExplorerDataNode)selectedNode;
					IProjectData data = dataNode.getNodeData();
					
					eventBroker.post(PROJECT_EXPLORER_DOUBLE_CLICK, data);
				}
				else {
					tree.setExpandedState(selectedNode,
							!tree.getExpandedState(selectedNode));
				}
			}
		});
		
		tree.getTree().addKeyListener(new KeyListener(){

			@Override
			public void keyPressed(KeyEvent e) {}

			@Override
			public void keyReleased(KeyEvent e) {
				if(e.keyCode == SWT.DEL){
					//TODO: if user presses delete, attempt to delete the selected element
				}
			}
		});
	}
	
	@Inject
	@Optional
	public void projectEvent(@UIEventTopic(ProjectManager.ALL_PROJECT_EVENTS)
			ProjectManager projectMan){
		
		tree.getContentProvider().inputChanged(tree, null, projectMan);
		tree.refresh();
	}

	/**
	 * Sets the focus to the TreeViewer.
	 */
	@Focus
	public void setFocus(){
		tree.getTree().setFocus();
	}

	@PreDestroy
	public void dispose() {}

	/**
	 * Builds up a tree of all the Projects so that they can be displayed in
	 * the TreeViewer.
	 * 
	 * @author Kyle Mullins
	 */
	class ProjectExplorerContentProvider implements ITreeContentProvider {
		
		private ArrayList<ProjectExplorerTree> projectTrees;
		
		/**
		 * Creates a new empty ProjectExplorerContentProvider.
		 */
		public ProjectExplorerContentProvider(){
			projectTrees = new ArrayList<ProjectExplorerTree>();
		}
		
		/**
		 * Rebuilds the TreeViewer's content based on the Projects in the
		 * given ProjectManager.
		 */
		@Inject
		public void inputChanged(ProjectManager projectMan){
			projectTrees = new ArrayList<ProjectExplorerTree>();
			
			for(Project proj: projectMan.getProjects()){
				ProjectExplorerTree newRoot = new ProjectExplorerTree(proj);
				ProjectExplorerNode dataNode = newRoot.addChild("Project Data");
				ProjectExplorerNode resultsNode = newRoot.addChild("Results");
				
				for(IProjectData data: proj.getOriginalData()){
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

		/**
		 * Called when the input to the TreeViewer changes.
		 */
		@Override
		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
			if(newInput != null){
				inputChanged((ProjectManager)newInput);
			}
		}

		/**
		 * Returns all of the root elements for the TreeViewer.
		 */
		@Override
		public Object[] getElements(Object inputElement) {
			return projectTrees.toArray();
		}

		/**
		 * Returns the given element's parent in the tree.
		 */
		@Override
		public Object getParent(Object element) {
			return ((ProjectExplorerNode)element).getParent();
		}

		/**
		 * Returns whether or not the given element has any children.
		 */
		@Override
		public boolean hasChildren(Object element) {
			return ((ProjectExplorerNode)element).hasChildren();
		}
		
		/**
		 * Returns the children of the given element, if any.
		 */
		@Override
		public Object[] getChildren(Object parentElement) {
			return ((ProjectExplorerNode)parentElement).getChildren();
		}
	}
	
	/**
	 * A tree node within the Project Explorer with an arbitrary number of
	 * children, 0-1 parents, and a name.
	 * 
	 * @author Kyle Mullins
	 */
	class ProjectExplorerNode{
		
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
		 * Creates a new ProjectExplorerNode child with the given name and with
		 * this node as its parent.
		 * 
		 * @param name	The name of the new node to be created.
		 * 
		 * @return	The node that was created.
		 */
		public ProjectExplorerNode addChild(String name){
			ProjectExplorerNode newNode = new ProjectExplorerNode(name, this);
			children.add(newNode);
			
			return newNode;
		}
		
		/**
		 * Creates a new ProjectExplorerDataNode child with the given name, the
		 * given ProjectData, and with this node as its parent.
		 * 
		 * @param name	The name of the new node to be created.
		 * @param data	The ProjectData the new node is to be given at
		 * 				creation.
		 * 
		 * @return	The node that was created.
		 */
		public ProjectExplorerNode addDataChild(String name, IProjectData data){
			ProjectExplorerNode newNode = new ProjectExplorerDataNode(name, this, data);
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
	}
	
	/**
	 * A root node for a Project Explorer tree which has no parents and a
	 * Project object associated with it.
	 * 
	 * @author Kyle Mullins
	 */
	class ProjectExplorerTree extends ProjectExplorerNode{
		private Project project;
		
		/**
		 * Creates a new ProjectExplorerTree to be used as the root for a tree
		 * of the given Project. The Project name is used as the name for this
		 * node.
		 * 
		 * @param proj	The Project object for which this is the root node.
		 */
		public ProjectExplorerTree(Project proj){
			super(proj.getName(), null);
			project = proj;
		}
		
		/**
		 * Returns the associated Project object.
		 */
		public Project getProject(){
			return project;
		}
	}
	
	/**
	 * A child node for a Project Explorer tree which has an IProjectData
	 * object associated with it.
	 * 
	 * @author Kyle Mullins
	 */
	class ProjectExplorerDataNode extends ProjectExplorerNode{
		private IProjectData nodeData;
		
		/**
		 * Creates a new ProjectExplorerDataNode with the given name, the given
		 * parent node, and the given ProjectData.
		 * 
		 * @param name		The name of this node.
		 * @param parent	This node's parent node.
		 * @param data		The ProjectData to be associated with this node.
		 */
		public ProjectExplorerDataNode(String name, ProjectExplorerNode parent,
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
	}
	
	/**
	 * Simple label provider which returns the name of a ProjectExplorerNode
	 * to be used as a label.
	 * 
	 * @author Kyle Mullins
	 */
	class ProjectExplorerLabelProvider extends LabelProvider{
		
		/**
		 * Returns a String label for a ProjectExplorerNode based on its name.
		 */
		@Override
		public String getText(Object element){
			return ((ProjectExplorerNode)element).getName();
		}
	}

}
