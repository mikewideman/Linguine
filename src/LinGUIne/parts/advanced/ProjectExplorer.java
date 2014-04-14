package LinGUIne.parts.advanced;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
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
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Platform;
import org.eclipse.e4.core.commands.ECommandService;
import org.eclipse.e4.core.commands.EHandlerService;
import org.eclipse.e4.core.contexts.ContextInjectionFactory;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.e4.ui.di.Focus;
import org.eclipse.e4.ui.di.UIEventTopic;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.ui.menu.MMenu;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.e4.ui.workbench.modeling.ESelectionService;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StyledCellLabelProvider;
import org.eclipse.jface.viewers.StyledString;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.MenuEvent;
import org.eclipse.swt.events.MenuListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Table;

import LinGUIne.events.LinGUIneEvents;
import LinGUIne.events.OpenProjectDataEvent;
import LinGUIne.events.ProjectEvent;
import LinGUIne.model.IProjectData;
import LinGUIne.model.Project;
import LinGUIne.model.ProjectGroup;
import LinGUIne.model.ProjectManager;
import LinGUIne.model.Result;
import LinGUIne.model.RootProjectGroup;
import LinGUIne.model.VisualizationResult;
import LinGUIne.parts.advanced.projects.ProjectDataNode;
import LinGUIne.parts.advanced.projects.GroupNode;
import LinGUIne.parts.advanced.projects.ProjectExplorerNode;
import LinGUIne.parts.advanced.projects.ProjectExplorerNodeSelection;
import LinGUIne.parts.advanced.projects.ProjectNode;
import LinGUIne.utilities.FileUtils;

/**
 * View which displays Project contents to the user as a collapsable tree.
 * 
 * @author Kyle Mullins
 */
public class ProjectExplorer implements IPropertiesProvider{

	@Inject
	ProjectManager projectMan;
	
	@Inject
	private IEventBroker eventBroker;
	
	@Inject
	private ECommandService commandService;
	
	@Inject
	private EHandlerService handlerService;
	
	@Inject
	private ESelectionService selectionService;
	
	private TreeViewer tree;
	private ProjectExplorerProperties propertiesView;
	
	private ProjectExplorerSelection projectSelection;
	private ProjectExplorerNodeSelection nodeSelection;
	
	@Inject
	public ProjectExplorer(){
		projectSelection = new ProjectExplorerSelection();
		nodeSelection = new ProjectExplorerNodeSelection();
	}
	
	/**
	 * Initializes the components of this view.
	 * 
	 * @param parent	The parent component.
	 */
	@PostConstruct
	public void createComposite(Composite parent, MApplication application){
		parent.setData("org.eclipse.e4.ui.css.id", "projExplorerPart");
		parent.setLayout(new GridLayout());

		tree = new TreeViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);
		tree.setContentProvider(new ProjectExplorerContentProvider());
		tree.setLabelProvider(new ProjectExplorerLabelProvider());
		tree.getTree().setLayoutData(new GridData(GridData.FILL_BOTH));
		
		tree.setInput(projectMan);
		
		createContextMenu(application);
		
		/*
		 * Add listeners to TreeViewer
		 */
		tree.addSelectionChangedListener(new ISelectionChangedListener(){

			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				projectSelection = new ProjectExplorerSelection();
				nodeSelection.clearNodeSelection();
				
				IStructuredSelection selection = (IStructuredSelection)
						tree.getSelection();
				
				buildProjectExplorerSelection(selection);
				
				for(Object node: selection.toList()){
					if(node instanceof ProjectExplorerNode){
						nodeSelection.addSelectedNode((ProjectExplorerNode)node);
					}
				}
				
				updatePropertiesView();
				selectionService.setSelection(projectSelection);
			}
		});
		
		tree.addDoubleClickListener(new IDoubleClickListener(){
			
			@Override
			public void doubleClick(DoubleClickEvent event) {
				IStructuredSelection selection =
						(IStructuredSelection)event.getSelection();
				Object selectedNode = selection.getFirstElement();
				
				if(selectedNode instanceof ProjectDataNode){
					//If user double clicks a file, post an event for the editor
					ProjectDataNode projectDataNode =
							(ProjectDataNode)selectedNode;
					IProjectData data = projectDataNode.getNodeData();
					Project containingProject = ((ProjectNode)
							projectDataNode.getRootNode()).getProject();
					OpenProjectDataEvent openEvent = new OpenProjectDataEvent(
							data, containingProject);
					
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
							"linguine.command.remove.project");
					
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

	@Override
	public Composite getProperties(Composite parent){
		if(propertiesView == null){
			propertiesView = new ProjectExplorerProperties(projectMan);
			propertiesView.createComposite(parent);
		}
		
		return propertiesView.getComposite();
	}
	
	private void updatePropertiesView(){
		propertiesView.setInput(projectSelection);
		eventBroker.post(LinGUIneEvents.UILifeCycle.PROPERTIES_VIEW_CHANGED,
				this);
	}
	
	/**
	 * Builds the ProjectExplorer's context menu
	 */
	private void createContextMenu(MApplication application){
		Menu contextMenu = new Menu(tree.getTree());	
		
		/*
		 * New Menu
		 */
		
		Menu newMenu = new Menu(contextMenu);
		final MenuItem newCascade = new MenuItem(contextMenu, SWT.CASCADE);
		newCascade.setText("New");
		newCascade.setMenu(newMenu);
		
		final MenuItem newFile = new MenuItem(newMenu, SWT.NONE);
		newFile.setText("New File...");
		newFile.addSelectionListener(new SelectionListener(){
			@Override
			public void widgetSelected(SelectionEvent e) {
				HashMap<String, String> params = new HashMap<String, String>();
				
				Project selectedProject = null;
				ProjectGroup selectedGroup = null;
				
				if(nodeSelection.getSelectedProjectNodes().size() == 1){
					selectedProject = nodeSelection.getSelectedProjectNodes().
							getFirst().getProject();
				}
				else if(nodeSelection.getSelectedGroupNodes().size() == 1){
					GroupNode groupNode = nodeSelection.getSelectedGroupNodes().
							getFirst();
					
					selectedGroup = groupNode.getNodeGroup();
					selectedProject = ((ProjectNode)groupNode.getRootNode()).
							getProject();
				}
				
				if(selectedProject != null){
					params.put("linguine.command.newFile.parameter." +
							"destinationProject", selectedProject.getName());
				}
				
				if(selectedGroup != null){
					params.put("linguine.command.newFile.parameter.parentGroup",
							selectedGroup.getName());
				}
				
				executeParameterizedCommand("linguine.command.new.file", params);
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {}
		});
		
		final MenuItem newGroup = new MenuItem(newMenu, SWT.NONE);
		newGroup.setText("New Group...");
		newGroup.addSelectionListener(new SelectionListener(){
			@Override
			public void widgetSelected(SelectionEvent e) {
				HashMap<String, String> params = new HashMap<String, String>();
				Project selectedProject = null;
				ProjectGroup selectedGroup = null;
				
				if(nodeSelection.getSelectedProjectNodes().size() == 1){
					selectedProject = nodeSelection.getSelectedProjectNodes().
							getFirst().getProject();
				}
				else if(nodeSelection.getSelectedGroupNodes().size() == 1){
					GroupNode groupNode = nodeSelection.getSelectedGroupNodes().
							getFirst();
					
					selectedGroup = groupNode.getNodeGroup();
					selectedProject = ((ProjectNode)groupNode.getRootNode()).
							getProject();
				}
				
				if(selectedProject != null){
					params.put("linguine.command.newGroup.parameter." +
							"destinationProject", selectedProject.getName());
				}
				
				if(selectedGroup != null){
					params.put("linguine.command.newGroup.parameter.parentGroup",
							selectedGroup.getName());
				}
				
				executeParameterizedCommand("linguine.command.new.group",
						params);
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {}
		});
		
		final MenuItem newProject = new MenuItem(newMenu, SWT.NONE);
		newProject.setText("New Project...");
		newProject.addSelectionListener(new SelectionListener(){
			@Override
			public void widgetSelected(SelectionEvent e) {
				executeCommand("linguine.command.new.project");
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {}
		});
	
		/*
		 * Import Menu
		 */
		
		Menu importMenu = new Menu(contextMenu);
		final MenuItem importCascade = new MenuItem(contextMenu, SWT.CASCADE);
		importCascade.setText("Import");
		importCascade.setMenu(importMenu);
		
		final MenuItem importFile = new MenuItem(importMenu, SWT.NONE);
		importFile.setText("Import File...");
		importFile.addSelectionListener(new SelectionListener(){
			@Override
			public void widgetSelected(SelectionEvent e) {
				executeCommand("linguine.command.import.file");
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {}
		});
		
		final MenuItem importProject = new MenuItem(importMenu, SWT.NONE);
		importProject.setText("Import Project...");
		importProject.addSelectionListener(new SelectionListener(){
			@Override
			public void widgetSelected(SelectionEvent e) {
				executeCommand("linguine.command.import.project");
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {}
		});
		
		/*
		 * Export
		 */
		
		final MenuItem export = new MenuItem(contextMenu, SWT.NONE);
		export.setText("Export...");
		export.addSelectionListener(new SelectionListener(){
			@Override
			public void widgetSelected(SelectionEvent e) {
				HashMap<String, String> params = new HashMap<String, String>();
				IProjectData selectedData = nodeSelection.
						getSelectedResultNodes().getFirst().getNodeData();
				String commandId;				
				
				if(selectedData instanceof VisualizationResult){
					params.put("linguine.command.exportVisualization.parameter."
							+ "dataForExport", selectedData.getName());
					
					commandId = "linguine.command.export.visualization";
				}
				else{
					params.put("linguine.command.exportResult.parameter."
							+ "dataForExport", selectedData.getName());
					
					commandId = "linguine.command.export.result";
				}
				
				executeParameterizedCommand(commandId, params);
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {}
		});
		
		//Add Separator
		new MenuItem(contextMenu, SWT.SEPARATOR);
		
		/*
		 * Remove
		 */
		
		final MenuItem remove = new MenuItem(contextMenu, SWT.NONE);
		remove.setText("Remove");
		remove.addSelectionListener(new SelectionListener(){
			@Override
			public void widgetSelected(SelectionEvent e) {
				HashMap<String, String> params = new HashMap<String, String>();
				String commandId;

				if(!nodeSelection.getSelectedProjectNodes().isEmpty()){
					Project selectedProject = nodeSelection.
							getSelectedProjectNodes().getFirst().getProject();
				
					params.put("linguine.command.removeProject.parameter."
							+ "targetProject", selectedProject.getName());
					
					commandId = "linguine.command.remove.project";
				}
				else if(!nodeSelection.getSelectedGroupNodes().isEmpty()){
					GroupNode node = nodeSelection.getSelectedGroupNodes().
							getFirst();
					ProjectGroup selectedGroup = node.getNodeGroup();
					
					params.put("linguine.command.removeGroup.parameter."
							+ "targetGroup", selectedGroup.getName());
					
					params.put("linguine.command.removeGroup.parameter."
							+ "parentProject", ((ProjectNode)node.getRootNode()).
							getProject().getName());
					
					commandId = "linguine.command.remove.group";
				}
				else{
					ProjectDataNode node = nodeSelection.
							getAllSelectedDataNodes().getFirst();
					IProjectData selectedData = node.getNodeData();
					
					params.put("linguine.command.removeProjectData.parameter."
							+ "targetProjectData", selectedData.getName());
					
					params.put("linguine.command.removeProjectData.parameter."
							+ "parentProject", ((ProjectNode)node.getRootNode()).
							getProject().getName());
					
					commandId = "linguine.command.remove.projectData";
				}
				
				executeParameterizedCommand(commandId, params);
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {}
		});
		
		final MenuItem rename = new MenuItem(contextMenu, SWT.NONE);
		rename.setText("Rename");
		rename.addSelectionListener(new SelectionListener(){
			@Override
			public void widgetSelected(SelectionEvent e) {
				HashMap<String, String> params = new HashMap<String, String>();
				String commandId;

				if(!nodeSelection.getSelectedProjectNodes().isEmpty()){
					Project selectedProject = nodeSelection.
							getSelectedProjectNodes().getFirst().getProject();
				
					params.put("linguine.command.renameProject.parameter."
							+ "targetProject", selectedProject.getName());
					
					commandId = "linguine.command.rename.project";
				}
				else if(!nodeSelection.getSelectedGroupNodes().isEmpty()){
					GroupNode node = nodeSelection.getSelectedGroupNodes().
							getFirst();
					ProjectGroup selectedGroup = node.getNodeGroup();
					
					params.put("linguine.command.renameGroup.parameter."
							+ "targetGroup", selectedGroup.getName());
					
					params.put("linguine.command.renameGroup.parameter."
							+ "parentProject", ((ProjectNode)node.getRootNode()).
							getProject().getName());
					
					commandId = "linguine.command.rename.group";
				}
				else{
					ProjectDataNode node = nodeSelection.
							getAllSelectedDataNodes().getFirst();
					IProjectData selectedData = node.getNodeData();
					
					params.put("linguine.command.renameProjectData.parameter."
							+ "targetProjectData", selectedData.getName());
					
					params.put("linguine.command.renameProjectData.parameter."
							+ "parentProject", ((ProjectNode)node.getRootNode()).
							getProject().getName());
					
					commandId = "linguine.command.rename.projectData";
				}
				
				executeParameterizedCommand(commandId, params);
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {}
		});
		
		tree.getTree().setMenu(contextMenu);
		
		contextMenu.addMenuListener(new MenuListener(){
			@Override
			public void menuHidden(MenuEvent e) {}

			@Override
			public void menuShown(MenuEvent e) {
				remove.setEnabled(false);
				rename.setEnabled(false);
				export.setEnabled(false);
				
				if(nodeSelection.getSelectionCount() == 1){
					remove.setEnabled(true);
					rename.setEnabled(true);
					
					if(!nodeSelection.getSelectedResultNodes().isEmpty()){
						export.setEnabled(true);
					}
					else if(!nodeSelection.getSelectedGroupNodes().isEmpty()){
						GroupNode node = nodeSelection.getSelectedGroupNodes().
								getFirst();
						
						if(node.getNodeGroup() instanceof RootProjectGroup){
							remove.setEnabled(false);
							rename.setEnabled(false);
						}
					}
				}
			}
		});
	}
	
	/**
	 * Attempts to execute the command with the given Id.
	 */
	private void executeCommand(String commandId){
		Command command = commandService.getCommand(commandId);
		
		try {
			command.executeWithChecks(new ExecutionEvent());
		}
		catch(ExecutionException | NotDefinedException
				| NotEnabledException | NotHandledException exception) {
			exception.printStackTrace();
		}
	}
	
	/**
	 * Attempts to execute the command with the given Id using the given params.
	 */
	private void executeParameterizedCommand(String commandId,
			HashMap<String, String> params){
		
		Command command = commandService.getCommand(commandId);
		ParameterizedCommand parameterizedCmd = ParameterizedCommand.
				generateCommand(command, params);
		
		handlerService.executeHandler(parameterizedCmd);
	}

	/**
	 * Assembles the ProjectExplorerSelection object for the current state of
	 * the ProjectExplorer.
	 * 
	 * @param selection	The current selection for the TreeViewer.
	 */
	private void buildProjectExplorerSelection(IStructuredSelection selection){
		for(Object selected: selection.toList()){
			//If the node is a root node, add it's Project
			if(selected instanceof ProjectNode){
				Project selectedProject =
						((ProjectNode)selected).getProject();
				
				projectSelection.addToSelection(selectedProject);
			}
			else{
				ProjectExplorerNode selectedNode =
						(ProjectExplorerNode)selected;
				Project selectedProject = ((ProjectNode)
						selectedNode.getRootNode()).getProject();
				LinkedList<IProjectData> selectedData =
						new LinkedList<IProjectData>();
				LinkedList<ProjectGroup> selectedGroups =
						new LinkedList<ProjectGroup>();
				
				addNode(selectedNode, selectedData, selectedGroups, true);
				
				//If the node has children, add all of them
				if(selectedNode.hasChildren()){
					addAllChildren(selectedNode, selectedData, selectedGroups,
							true);
				}
				
				projectSelection.addToSelection(selectedProject,
						selectedData, selectedGroups);
			}
		}
	}
	
	/**
	 * Adds all ProjectData and ProjectGroups in the subtree with parentNode at
	 * its root to the given lists.
	 */
	private void addAllChildren(ProjectExplorerNode parentNode,
			LinkedList<IProjectData> childData,
			LinkedList<ProjectGroup> childGroups, boolean shouldAddGroups){
		
		for(ProjectExplorerNode childNode: parentNode.getChildren()){
			addNode(childNode, childData, childGroups, shouldAddGroups);
			
			if(childNode.hasChildren()){
				addAllChildren(childNode, childData, childGroups,
						shouldAddGroups);
			}
		}
	}
	
	private void addNode(ProjectExplorerNode node, LinkedList<IProjectData> data,
			LinkedList<ProjectGroup> groups, boolean shouldAddGroups){
		
		if(node instanceof GroupNode && shouldAddGroups){
			GroupNode groupNode = (GroupNode)node;
			
			groups.add(groupNode.getNodeGroup());
		}
		else if(node instanceof ProjectDataNode){
			ProjectDataNode projectDataNode = (ProjectDataNode)node;
			
			data.add(projectDataNode.getNodeData());
		}
	}
	
	/**
	 * Builds up a tree of all the Projects so that they can be displayed in
	 * the TreeViewer.
	 * 
	 * @author Kyle Mullins
	 */
	class ProjectExplorerContentProvider implements ITreeContentProvider {
		
		private ArrayList<ProjectNode> projectTrees;
		
		/**
		 * Creates a new empty ProjectExplorerContentProvider.
		 */
		public ProjectExplorerContentProvider(){
			projectTrees = new ArrayList<ProjectNode>();
		}
		
		/**
		 * Rebuilds the TreeViewer's content based on the Projects in the
		 * given ProjectManager.
		 */
		public void inputChanged(ProjectManager projectMan){
			projectTrees = new ArrayList<ProjectNode>();
			
			for(Project proj: projectMan.getProjects()){
				ProjectNode newRoot = new ProjectNode(proj);

				for(ProjectGroup group: proj.getGroups()){
					if(group instanceof RootProjectGroup &&
							((RootProjectGroup)group).isHidden()){
						continue;
					}
					else if(!group.hasParent()){
						buildSubtree(group, proj, newRoot);
					}
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
		
		/**
		 * Recursively builds a subtree for the given ProjectGroup with the
		 * given node as its parent.
		 */
		private void buildSubtree(ProjectGroup group, Project proj,
				ProjectExplorerNode parentNode){
			
			ProjectExplorerNode newGroupNode = parentNode.addChild(
					group.getName(), group);
			
			//Add child groups
			for(ProjectGroup childGroup: group.getChildren()){
				buildSubtree(childGroup, proj, newGroupNode);
			}
			
			//Add child data
			for(IProjectData data: proj.getDataInGroup(group)){
				newGroupNode.addChild(data.getName(), data);
			}
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
			
			if(node instanceof ProjectDataNode){
				ProjectDataNode projectDataNode = (ProjectDataNode)node;
				
				Project parentProject =
						((ProjectNode)node.getRootNode()).getProject();
				
				if(parentProject.isAnnotated(projectDataNode.getNodeData())){
					label.append(" (Annotated)", StyledString.COUNTER_STYLER);
				}
			}
			
			cell.setText(label.toString());
			cell.setStyleRanges(label.getStyleRanges());
			
			try {
				if(node.hasIcon()){
					String nodeIconFileName = node.getIconFileName();
					URL iconFolderURL = new URL("platform:/plugin/LinGUIne/icons/" +
							nodeIconFileName);
					
					cell.setImage(new Image(Display.getCurrent(),
							iconFolderURL.openStream()));
				}
			}
			catch(IOException e) {
				e.printStackTrace();
			}
			
			super.update(cell);
		}
	}
}
