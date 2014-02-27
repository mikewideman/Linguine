
package LinGUIne.parts.advanced;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Platform;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.di.Focus;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

/**
 * Part that shows metadata about the current selection.
 * 
 * @author Peter Dimou
 * @author Kyle Mullins
 */
public class MetaPart {

	private TableViewer viewer;

	@PostConstruct
	public void createComposite(Composite parent){
		viewer = new TableViewer(parent, SWT.MULTI | SWT.H_SCROLL
				| SWT.V_SCROLL | SWT.FULL_SELECTION | SWT.BORDER);
		createColumns();
		final Table table = viewer.getTable();
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		viewer.setContentProvider(new ArrayContentProvider());
		viewer.setInput(getInput());

		// Define layout for the viewer
		GridData gridData = new GridData();
		gridData.verticalAlignment = GridData.FILL;
		gridData.horizontalSpan = 2;
		gridData.grabExcessHorizontalSpace = true;
		gridData.grabExcessVerticalSpace = true;
		gridData.horizontalAlignment = GridData.FILL;
		viewer.getControl().setLayoutData(gridData);
	}

	/**
	 * Updates the part to show metadata about the currently selected
	 * ProjectData. This will get called whenever the current selection in the
	 * ProjectExplorer changes.
	 * 
	 * @param selectedData	The currently selected ProjectData.
	 */
	@Inject
	public void showProjectDataMetadata(@Optional
			@Named(IServiceConstants.ACTIVE_SELECTION)
			ProjectExplorerSelection selection){

		//TODO: Show some metadata about the currently selected ProjectData
	}

	/**
	 * Creates the columns necessary for the table. The current columns are:
	 * File Name, Date Modified, Type, and Size.
	 */
	private void createColumns() {
		String[] titles = { "File Name", "Date Modified", "Type", "Size" };
		int[] bounds = {100, 100, 100, 100};

		TableViewerColumn col = createTableViewerColumn(titles[0], bounds[0]);
		col.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				File f = (File) element;
				return f.getName();
			}
		});

		col = createTableViewerColumn(titles[1], bounds[1]);
		col.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				File f = (File) element;
				return (new Date(f.lastModified())).toString();
			}
		});

		col = createTableViewerColumn(titles[2], bounds[2]);
		col.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				File f = (File) element;
				String extension = "";

				int i = f.getName().lastIndexOf('.');
				if (i > 0) {
					extension = f.getName().substring(i+1);
				}
				return extension;
			}
		});

		col = createTableViewerColumn(titles[3], bounds[3]);
		col.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				File f = (File) element;
				return (f.length() / 1024) + " KB";
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
		final TableViewerColumn viewerColumn = new TableViewerColumn(viewer,
				SWT.NONE);
		final TableColumn column = viewerColumn.getColumn();
		column.setText(title);
		column.setWidth(bound);
		column.setResizable(true);
		column.setMoveable(true);
		return viewerColumn;
	}

	/**
	 * Supplies the TableViewer with input. Currently, the workspace is used.
	 * 
	 * @return A list of elements for the TableViewer to display
	 */
	private ArrayList<File> getInput() {
		IPath path = Platform.getLocation();
		File[] fileList = path.toFile().listFiles();
		fileList = fileList[0].listFiles();
		return new ArrayList<File>(Arrays.asList(fileList));
	}

	@Focus
	public void setFocus() {
		viewer.getControl().setFocus();
	}
}

