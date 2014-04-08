package LinGUIne.parts.advanced;

import java.util.ArrayList;
import java.util.Date;

import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.TableColumn;

import LinGUIne.model.IProjectData;
import LinGUIne.model.Project;
import LinGUIne.model.ProjectManager;
import LinGUIne.model.Result;

public class ProjectExplorerProperties {

	private ProjectManager projectMan;
	
	private Composite mainComposite;
	private TableViewer tblMetadata;
	
	public ProjectExplorerProperties(ProjectManager projects){
		projectMan = projects;
	}
	
	public void createComposite(Composite parent){
		mainComposite = new Composite(parent, SWT.NONE);
		mainComposite.setLayout(new GridLayout(1, false));
		
		tblMetadata = new TableViewer(mainComposite, SWT.MULTI | SWT.H_SCROLL
				| SWT.V_SCROLL | SWT.FULL_SELECTION | SWT.BORDER);
		
		createColumns();
		
		tblMetadata.getTable().setHeaderVisible(true);
		tblMetadata.getTable().setLinesVisible(true);
		tblMetadata.setContentProvider(new ArrayContentProvider());
		tblMetadata.getControl().setLayoutData(new GridData(GridData.FILL_BOTH));
	}
	
	public Composite getComposite(){
		return mainComposite;
	}
	
	/**
	 * Updates the part to show metadata about the currently selected
	 * ProjectData. This will get called whenever the current selection in the
	 * ProjectExplorer changes.
	 * 
	 * @param selection	The currently selected ProjectData.
	 */
	public void setInput(ProjectExplorerSelection selection){
		if(selection != null){
			ArrayList<IProjectData> selectedData = new ArrayList<IProjectData>();

			for(String projectName: selection.getSelectedProjects()){
				for(String dataName: selection.getSelectedOriginalData(
					projectName)){
					
					selectedData.add(projectMan.getProject(projectName).
							getProjectData(dataName));
				}
			}
			
			tblMetadata.setInput(selectedData);
		}
	}
	
	/**
	 * Creates the columns necessary for the table. The current columns are:
	 * File Name, Type, Date Modified, Size, Annotated?, and Available Results.
	 */
	private void createColumns() {
		String[] titles = {"File Name", "Type", "Date Modified", "Size",
				"Annotated?", "Available Results"};
		int[] bounds = {100, 100, 100, 100, 100, 100};

		TableViewerColumn col = createTableViewerColumn(titles[0], bounds[0]);
		col.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				IProjectData data = (IProjectData)element;
				
				return data.getName();
			}
		});

		col = createTableViewerColumn(titles[1], bounds[1]);
		col.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				IProjectData data = (IProjectData)element;
				
				return data.getClass().getSimpleName();
			}
		});
		
		col = createTableViewerColumn(titles[2], bounds[2]);
		col.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				IProjectData data = (IProjectData)element;
				
				return (new Date(data.getFile().lastModified())).toString();
			}
		});

		col = createTableViewerColumn(titles[3], bounds[3]);
		col.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				IProjectData data = (IProjectData)element;
				
				return (data.getFile().length() / 1024) + " KB";
			}
		});
		
		col = createTableViewerColumn(titles[4], bounds[4]);
		col.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				IProjectData data = (IProjectData)element;
				Project parentProject = getParentProject(data);
				
				return parentProject.isAnnotated(data) ? "Yes" : "No";
			}
		});
		
		col = createTableViewerColumn(titles[5], bounds[5]);
		col.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				IProjectData data = (IProjectData)element;
				Project parentProject = getParentProject(data);
				String resultStr = "";
				
				for(Result result: parentProject.getResults(data)){
					resultStr += result.getClass().getSimpleName() + ";";
				}
				
				return resultStr;
			}
		});
	}
	
	/**
	 * Creates columns with consistent properties, such as the ability to be 
	 * resized and movable.
	 * 
	 * @param title The title for this column
	 * @param bound The width of the column, in pixels
	 * 
	 * @return A TableViewerColumn ready to be used 
	 */
	private TableViewerColumn createTableViewerColumn(String title, int bound) {
		TableViewerColumn viewerColumn = new TableViewerColumn(tblMetadata,
				SWT.NONE);
		TableColumn column = viewerColumn.getColumn();
		
		column.setText(title);
		column.setWidth(bound);
		column.setResizable(true);
		column.setMoveable(true);
		
		return viewerColumn;
	}
	
	private Project getParentProject(IProjectData projData){
		for(Project proj: projectMan.getProjects()){
			if(proj.containsProjectData(projData)){
				return proj;
			}
		}
		
		return null;
	}
}
