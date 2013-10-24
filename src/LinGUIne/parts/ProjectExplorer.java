package LinGUIne.parts;

import java.io.File;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.core.runtime.Platform;
import org.eclipse.e4.ui.di.Focus;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;

/**
 * View which displays Project contents to the user as a collapsable tree.
 * 
 * @author Kyle Mullins
 */
public class ProjectExplorer {

	private TreeViewer tree;

	/**
	 * Initializes the components of this view.
	 * 
	 * @param parent	The parent component.
	 */
	@PostConstruct
	public void createComposite(Composite parent){
		parent.setLayout(new GridLayout());

		tree = new TreeViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);
		tree.setContentProvider(new ViewContentProvider());
		tree.setLabelProvider(new ViewLabelProvider());
		tree.getTree().setLayoutData(new GridData(GridData.FILL_BOTH));
		tree.setInput(Platform.getLocation().toFile().listFiles());
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
