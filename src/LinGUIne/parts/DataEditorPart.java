 
package LinGUIne.parts;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.eclipse.e4.ui.di.Focus;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

public class DataEditorPart {
	
	private CTabFolder tabFolder;
	private StyledText styledText;
	
	@Inject
	public DataEditorPart() {
	}
	
	@PostConstruct
	public void createComposite(Composite parent) {
		tabFolder = new CTabFolder(parent,SWT.NONE);
		CTabItem newTab = new CTabItem(tabFolder,SWT.NONE);
		newTab.setText("New File");
		tabFolder.setSelection(0);
		styledText = new StyledText(tabFolder,SWT.V_SCROLL|SWT.H_SCROLL);
		styledText.setAlwaysShowScrollBars(true);
		styledText.setText("Testing 1 2 3");
		newTab.setControl(styledText);
		
	}
	
	@Focus
	public void onFocus() {
	}
	
	
}