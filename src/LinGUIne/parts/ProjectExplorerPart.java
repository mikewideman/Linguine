 
package LinGUIne.parts;

import javax.inject.Inject;
import javax.annotation.PostConstruct;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.e4.ui.di.Focus;

public class ProjectExplorerPart {
	
	private Tree tree;
	
	@Inject
	public ProjectExplorerPart() {
	}
	
	@PostConstruct
	public void postConstruct(Composite parent) {
		tree = new Tree(parent,SWT.BORDER);
		//Creation of demo data
		for(int i = 1; i < 4; i++){	
			TreeItem projectItem = new TreeItem(tree,SWT.NULL);
			projectItem.setText("Project "+i);
			TreeItem catItem = new TreeItem(projectItem,SWT.NULL);
			catItem.setText("Data Files");
			TreeItem dataItem = new TreeItem(catItem,SWT.NULL);
			dataItem.setText("TextFile.txt");
			dataItem = new TreeItem(catItem,SWT.NULL);
			dataItem.setText("SoundFile.wav");
			dataItem = new TreeItem(catItem,SWT.NULL);
			dataItem.setText("Video.avi");
			catItem = new TreeItem(projectItem,SWT.NULL);
			catItem.setText("Result Files");
			dataItem = new TreeItem(catItem,SWT.NULL);
			dataItem.setText("ResultA.res");
			dataItem = new TreeItem(catItem,SWT.NULL);
			dataItem.setText("ResultB.res");
			catItem = new TreeItem(projectItem,SWT.NULL);
			catItem.setText("Visualization Files");
			dataItem = new TreeItem(catItem,SWT.NULL);
			dataItem.setText("VisualA.vis");
			dataItem = new TreeItem(catItem,SWT.NULL);
			dataItem.setText("VisualB.vis");
		}
		
		
	}
	
	
	
	@Focus
	public void onFocus() {
	}
	
	
}