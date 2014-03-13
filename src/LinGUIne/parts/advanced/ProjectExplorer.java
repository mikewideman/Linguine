package LinGUIne.parts.advanced;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;

import org.eclipse.core.commands.Command;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.NotEnabledException;
import org.eclipse.core.commands.NotHandledException;
import org.eclipse.core.commands.ParameterizedCommand;
import org.eclipse.core.commands.common.NotDefinedException;
import org.eclipse.core.runtime.Platform;
import org.eclipse.e4.core.commands.ECommandService;
import org.eclipse.e4.core.commands.EHandlerService;
import org.eclipse.e4.core.contexts.ContextInjectionFactory;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.e4.ui.di.Focus;
import org.eclipse.e4.ui.di.UIEventTopic;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.workbench.modeling.ESelectionService;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StyledCellLabelProvider;
import org.eclipse.jface.viewers.StyledString;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;

import LinGUIne.events.LinGUIneEvents;
import LinGUIne.events.OpenProjectDataEvent;
import LinGUIne.events.ProjectEvent;
import LinGUIne.model.IProjectData;
import LinGUIne.model.Project;
import LinGUIne.model.ProjectManager;
import LinGUIne.model.Result;

/**
 * View which displays Project contents to the user as a collapsable tree.
 * 
 * @author Kyle Mullins
 */
public class ProjectExplorer {

	private ProjectExplorerSelection projectSelection;
	private TreeViewer tree;
	
	@Inject
	ProjectManager projectMan;
	
	@Inject
	private IEventBroker eventBroker;
	
	@Inject
	private ECommandService commandService;
	
	@Inject
	private EHandlerService handlerService;
	
	@Inject
	ESelectionService selectionService;
	
	@Inject
	public ProjectExplorer(){
		projectSelection = new ProjectExplorerSelection();
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
		
		tree.setInput(projectMan);
		
		createContextMenu();
		
		/*
		 * Add listeners to TreeViewer
		 */
		tree.addSelectionChangedListener(new ISelectionChangedListener(){

			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				projectSelection = new ProjectExplorerSelection();
				
				IStructuredSelection selection = (IStructuredSelection)
						tree.getSelection();
				
				buildProjectExplorerSelection(selection);
				
				selectionService.setSelection(projectSelection);
			}
		});
		
		tree.addDoubleClickListener(new IDoubleClickListener(){
			
			@Override
			public void doubleClick(DoubleClickEvent event) {
				IStructuredSelection selection =
						(IStructuredSelection)event.getSelection();
				Object selectedNode = selection.getFirstElement();
				
				if(selectedNode instanceof ProjectExplorerDataNode){
					//If user double clicks a file, post an event for the editor
					ProjectExplorerDataNode dataNode =
							(ProjectExplorerDataNode)selectedNode;
					IProjectData data = dataNode.getNodeData();
					Project containingProject = ((ProjectExplorerTree)
							dataNode.getRootNode()).getProject();
					OpenProjectDataEvent openEvent = new OpenProjectDataEvent(
							data, containingProject.getAnnotation(data));
					
					eventBroker.post(LinGUIneEvents.UILifeCycle.OPEN_PROJECT_DATA, openEvent);
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
					Command removeProjectCommand = commandService.getCommand(
							"linguine.command.removeProject");
					
					try {
						removeProjectCommand.executeWithChecks(new ExecutionEvent());
					}
					catch(ExecutionException | NotDefinedException
							| NotEnabledException | NotHandledException e1) {
						//TODO: Oh no the command is not defined!
						e1.printStackTrace();
					}
				}
			}
		});
	}
	
	@Inject
	@Optional
	public void projectEvent(@UIEventTopic(LinGUIneEvents.Project.ALL_EVENTS)
			ProjectEvent projectEvent){
		ProjectManager projectMan = projectEvent.getProjectManager();
		
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
	 * Builds the ProjectExplorer's context menu
	 */
	private void createContextMenu(){
		Menu contextMenu = new Menu(tree.getTree());
		
		MenuItem newProject = new MenuItem(contextMenu, SWT.NONE);
		newProject.setText("New Project...");
		newProject.addSelectionListener(new SelectionListener(){

			@Override
			public void widgetSelected(SelectionEvent e) {
				Command newProjectCommand = commandService.getCommand(
						"linguine.command.newProject");
				
				try {
					newProjectCommand.executeWithChecks(new ExecutionEvent());
				}
				catch(ExecutionException | NotDefinedException
						| NotEnabledException | NotHandledException e1) {
					//TODO: Oh no the command is not defined!
					e1.printStackTrace();
				}
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {}
		});
		
		MenuItem refreshExplorer = new MenuItem(contextMenu, SWT.NONE);
		refreshExplorer.setText("Refresh");
		refreshExplorer.addSelectionListener(new SelectionListener(){

			@Override
			public void widgetSelected(SelectionEvent e) {
				Command refreshCommand = commandService.getCommand(
						"linguine.command.refresh");
				
				try {
					refreshCommand.executeWithChecks(new ExecutionEvent());
				}
				catch(ExecutionException | NotDefinedException
						| NotEnabledException | NotHandledException e1) {
					//TODO: Oh no the command is not defined!
					e1.printStackTrace();
				}
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {}
		});
		
		MenuItem removeProject = new MenuItem(contextMenu, SWT.NONE);
		removeProject.setText("Remove Project");
		removeProject.addSelectionListener(new SelectionListener(){
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				//TODO: Make this more robust and only enable command when a Project is selected
				String selectedProjectName = projectSelection.
						getSelectedProjects().iterator().next();
				
				HashMap<String, String> params = new HashMap<String, String>();
				params.put("linguine.command.removeProject.parameter."
						+ "projectForRemoval", selectedProjectName);
				
				Command removeProjectCommand = commandService.getCommand(
						"linguine.command.removeProject");
				ParameterizedCommand parameterizedCmd = ParameterizedCommand.
						generateCommand(removeProjectCommand, params);
				
				handlerService.executeHandler(parameterizedCmd);
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {}
		});
		
		tree.getTree().setMenu(contextMenu);
	}

	private void buildProjectExplorerSelection(IStructuredSelection selection){
		for(Object selected: selection.toList()){
			//If the node is a root node, add it's Project
			if(selected instanceof ProjectExplorerTree){
				Project selectedProject =
						((ProjectExplorerTree)selected).getProject();
				
				projectSelection.addToSelection(selectedProject);
			}
			else{
				ProjectExplorerNode selectedNode =
						(ProjectExplorerNode)selected;
				Project selectedProject = ((ProjectExplorerTree)
						selectedNode.getRootNode()).getProject();
				List<IProjectData> selectedData = new LinkedList<IProjectData>();
				
				//If the node has children, add all of them
				if(selectedNode.hasChildren()){
					for(ProjectExplorerNode childNode:
						selectedNode.getChildren()){
						
						selectedData.add(((ProjectExplorerDataNode)
								childNode).getNodeData());
					}
				}
				//Otherwise just add the node's ProjectData
				else if(selectedNode instanceof ProjectExplorerDataNode){
					selectedData.add(((ProjectExplorerDataNode)selectedNode).
							getNodeData());
				}
				
				projectSelection.addToSelection(selectedProject,
						selectedData);
			}
		}
	}
	
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
		public void inputChanged(ProjectManager projectMan){
			projectTrees = new ArrayList<ProjectExplorerTree>();
			
			for(Project proj: projectMan.getProjects()){
				ProjectExplorerTree newRoot = new ProjectExplorerTree(proj);
				ProjectExplorerNode dataNode = newRoot.addChild("Project Data");
				ProjectExplorerNode resultsNode = newRoot.addChild("Results");
				
				for(IProjectData data: proj.getOriginalData()){
					dataNode.addDataChild(data.getName(), data);
				}
				
				for(Result result: proj.getResults()){
					resultsNode.addDataChild(result.getName(), result);
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
				Object[] expandedElements = tree.getExpandedElements();
				
				inputChanged((ProjectManager)newInput);
				
				tree.setExpandedElements(expandedElements);
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
			else if(!super.equals(obj) || !(obj instanceof ProjectExplorerTree)) {
				return false;
			}
			
			ProjectExplorerTree other = (ProjectExplorerTree)obj;
			
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
			else if(!super.equals(obj) || !(obj instanceof ProjectExplorerDataNode)){
				return false;
			}
			
			ProjectExplorerDataNode other = (ProjectExplorerDataNode)obj;

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
	
	/**
	 * Simple label provider which returns the name of a ProjectExplorerNode
	 * to be used as a label.
	 * 
	 * @author Kyle Mullins
	 */
	class ProjectExplorerLabelProvider extends StyledCellLabelProvider{
		
		/**
		 * Returns a String label for a ProjectExplorerNode based on its name.
		 */
		@Override
		public void update(ViewerCell cell){
			ProjectExplorerNode node = (ProjectExplorerNode)cell.getElement();
			StyledString label = new StyledString(node.getName());
			
			if(node instanceof ProjectExplorerDataNode){
				ProjectExplorerDataNode dataNode = (ProjectExplorerDataNode)node;
				
				Project parentProject =
						((ProjectExplorerTree)node.getRootNode()).getProject();
				
				if(parentProject.isAnnotated(dataNode.getNodeData())){
					label.append(" (Annotated)", StyledString.COUNTER_STYLER);
				}
			}
			
			cell.setText(label.toString());
			cell.setStyleRanges(label.getStyleRanges());
			
			super.update(cell);
		}
	}
}
