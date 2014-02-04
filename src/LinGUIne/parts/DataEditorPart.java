 
package LinGUIne.parts;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.eclipse.e4.ui.di.Focus;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;

public class DataEditorPart implements MouseListener, SelectionListener{
	
	private CTabFolder tabFolder;
	private Menu contextMenu;
	private MenuItem newItem;
	private MenuItem cutItem;
	private MenuItem copyItem;
	private MenuItem pasteItem;
	
	@Inject
	public DataEditorPart() {
		
	}
	
	@PostConstruct
	public void createComposite(Composite parent) {
		tabFolder = new CTabFolder(parent,SWT.NONE);
		createTab();
	}
	
	/**
	 * Creates a new blank text editor tab
	 */
	public void createTab(){
		CTabItem newTab = new CTabItem(tabFolder,SWT.CLOSE);
		newTab.setText("New File");
		tabFolder.setSelection(newTab);
		StyledText textArea = new StyledText(tabFolder,SWT.V_SCROLL|SWT.H_SCROLL);
		textArea.addMouseListener(this);
		textArea.setAlwaysShowScrollBars(true);
		newTab.setControl(textArea);
	}
	
	/**
	 * Creates a new text editor tab and populates the editor with data from a text file
	 * @param fileName - the name of the File
	 * @param contents - the text data from the File
	 */
	public void createTab(String fileName, String[] contents){
		CTabItem newTab = new CTabItem(tabFolder,SWT.CLOSE);
		newTab.setText(fileName);
		tabFolder.setSelection(newTab);
		StyledText textArea = new StyledText(tabFolder,SWT.V_SCROLL|SWT.H_SCROLL);
		textArea.addMouseListener(this);
		textArea.setAlwaysShowScrollBars(true);
		newTab.setControl(textArea);
		for(int i = 0; i < contents.length; i++){
			textArea.append(contents[i]);
		}
	}
	
	@Focus
	public void onFocus() {
	}

	@Override
	public void mouseDoubleClick(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseDown(MouseEvent e) {
		if(e.button == 3){
			Control c = (Control)e.getSource();
			contextMenu = new Menu(c);
			newItem = new MenuItem(contextMenu,SWT.NONE);
			newItem.setText("New");
			newItem.addSelectionListener(this);
			cutItem = new MenuItem(contextMenu,SWT.NONE);
			cutItem.setText("Cut");
			cutItem.addSelectionListener(this);
			copyItem = new MenuItem(contextMenu,SWT.NONE);
			copyItem.setText("Copy");
			copyItem.addSelectionListener(this);
			pasteItem = new MenuItem(contextMenu,SWT.NONE);
			pasteItem.setText("Paste");
			pasteItem.addSelectionListener(this);
			c.setMenu(contextMenu);
		}
	}

	@Override
	public void mouseUp(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void widgetSelected(SelectionEvent e) {
		if(e.getSource() == newItem){
			createTab();
		}
		if(e.getSource() == cutItem){
			System.out.println("Cut!");
		}
		if(e.getSource() == copyItem){
			System.out.println("Copy!");
		}
		if(e.getSource() == pasteItem){
			System.out.println("Paste!");
		}
	}

	@Override
	public void widgetDefaultSelected(SelectionEvent e) {
		// TODO Auto-generated method stub
		
	}
	
}