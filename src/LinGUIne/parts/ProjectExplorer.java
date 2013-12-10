package LinGUIne.parts;

import java.io.File;
import java.util.ArrayList;
import java.util.TreeMap;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;
import javax.inject.Named;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Platform;
import org.eclipse.e4.ui.di.Focus;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;

import LinGUIne.model.Project;
import LinGUIne.model.Result;

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
//		tree.setContentProvider(new ViewContentProvider());
//		tree.setLabelProvider(new ViewLabelProvider());
		tree.setContentProvider(new ProjectExplorerContentProvider());
		tree.setLabelProvider(new ProjectExplorerLabelProvider());
		tree.getTree().setLayoutData(new GridData(GridData.FILL_BOTH));
//		tree.setInput(Platform.getLocation().toFile().listFiles());
		
		TreeMap<String, Project> projectSet = new TreeMap<String, Project>();
		loadProjects(projectSet);
		tree.setInput(projectSet);
		
		application.getContext().set("ProjectSet", projectSet);
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
	
	private void loadProjects(TreeMap<String, Project> projectSet){
		IPath workspace = Platform.getLocation();
		
		for(File dir: workspace.toFile().listFiles()){
			if(dir.isDirectory()){
				for(String filename: dir.list()){
					if(filename.equals(Project.PROJECT_FILE)){
						String projectName = dir.getName(); //TODO: change this to read from project file
						Project project = new Project();
						
						project.setName(projectName);
						project.setParentDirectory(workspace);
						
						projectSet.put(projectName, project);
					}
				}
			}
		}
	}

	class ProjectExplorerContentProvider implements ITreeContentProvider {
		
		private ArrayList<ProjectExplorerNode> rootNodes;
		
		public ProjectExplorerContentProvider(){
			rootNodes = new ArrayList<ProjectExplorerNode>();
		}
		
		@Inject
		public void inputChanged(@Named("ProjectSet") TreeMap<String, Project> projects){
			System.out.println("Other input changed called.");
			rootNodes = new ArrayList<ProjectExplorerNode>();
			
			for(Project proj: projects.values()){
				ProjectExplorerNode newRoot = new ProjectExplorerNode(proj.getName(), proj);
				ProjectExplorerNode dataNode = newRoot.addChild("Project Data", null);
				ProjectExplorerNode resultsNode = newRoot.addChild("Results", null);
				
				for(File dataFile: proj.getProjectData()){
					dataNode.addChild(dataFile.getName(), dataFile);
				}
				
				for(Result result: proj.getResults()){
//					resultsNode.addChild(result.getName(), result);
				}
				
				rootNodes.add(newRoot);
			}
		}
		
		@Override
		public void dispose() {}

		@Override
		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
			System.out.println("inputChanged called.");
			inputChanged((TreeMap<String, Project>)newInput);
		}

		@Override
		public Object[] getElements(Object inputElement) {
			return rootNodes.toArray();
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
	
	class ProjectExplorerNode{
		
		private String nodeName;
		private ProjectExplorerNode parentNode;
		private ArrayList<ProjectExplorerNode> children;
		
		public ProjectExplorerNode(String name, Object data){
			this(name, data, null);
		}
		
		public ProjectExplorerNode(String name, Object data,
				ProjectExplorerNode parent){
			nodeName = name;
			parent = parentNode;
			children = new ArrayList<ProjectExplorerNode>();
		}
		
		public ProjectExplorerNode addChild(String name, Object data){
			ProjectExplorerNode newNode = new ProjectExplorerNode(name, data, this);
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
		
		public ProjectExplorerNode getParent(){
			return parentNode;
		}
		
		public String toString(){
			return nodeName;
		}
	}
	
	class ProjectExplorerLabelProvider extends LabelProvider{
		@Override
		public String getText(Object element){
			return ((ProjectExplorerNode)element).getName();
		}
	}
	
	/**
	 * Controls the content provided to a TreeViewer, in this case displays a
	 * directory tree.
	 * 
	 * @author Kyle Mullins
	 */
	class ViewContentProvider implements ITreeContentProvider {
		private File[] files;
		
		/**
		 * Updates the input data provided to the TreeViewer.
		 * 
		 * @param view		The view to which data is being provided.
		 * @param oldInput	The previous set of data being proivded.
		 * @param newInput	The new input data to be provided to the view.
		 */
		public void inputChanged(Viewer view, Object oldInput, Object newInput) {
			files = (File[])newInput;
//			view.refresh();
		}

		@Override
		public void dispose() {
		}

		/**
		 * Returns the current file list to the TreeViewer.
		 */
		@Override
		public Object[] getElements(Object inputElement) {
//			return (File[]) inputElement;
			return files;
		}

		/**
		 * Returns all sub-files and directories of the given parent file.
		 * 
		 * @param parentElement	The parent file for which children are to be
		 * 						returned.
		 */
		@Override
		public Object[] getChildren(Object parentElement) {
			File file = (File) parentElement;
			return file.listFiles();
		}

		/**
		 * Returns the parent directory of the given file.
		 * 
		 * @param element	The file for which the parent directory is to be
		 * 					returned.
		 */
		@Override
		public Object getParent(Object element) {
			File file = (File) element;
			return file.getParentFile();
		}

		/**
		 * Returns whether or not the given file is a directory (i.e. has
		 * children).
		 * 
		 * @param element	The file to be checked.
		 */
		@Override
		public boolean hasChildren(Object element) {
			File file = (File) element;
			if (file.isDirectory()) {
				return true;
			}
			return false;
		}

	}

	
	/**
	 * Simple label provider which reutrns a file name to be used as the node
	 * label.
	 * 
	 * @author Kyle Mullins
	 */
	class ViewLabelProvider extends LabelProvider {
		
		/**
		 * Returns the name of the given file to be used as the node label.
		 * 
		 * @param element	The file for which the name is to be returned.
		 */
		@Override
		public String getText(Object element) {
			File file = (File) element;
			String name = file.getName();
			return name.isEmpty() ? file.getPath() : name;
		}
	}
}
