 
package LinGUIne.parts.advanced;

import java.io.IOException;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.di.Focus;
import org.eclipse.e4.ui.di.Persist;
import org.eclipse.e4.ui.di.UIEventTopic;
import org.eclipse.e4.ui.model.application.ui.MDirtyable;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;

import LinGUIne.model.IProjectData;
import LinGUIne.model.TextData;
import LinGUIne.parts.advanced.ProjectDataEditorTab.DirtyStateChangedListener;

public class DataEditorPart implements MouseListener, SelectionListener{
	
	@Inject
	private MDirtyable dirtyable;
	
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
		tabFolder.addSelectionListener(this);
		createTab();
		
		dirtyable.setDirty(false);
	}
	
	@Persist
	public void save(MDirtyable dirty) throws IOException{
		if(tabFolder.getSelection() instanceof ProjectDataEditorTab){
			ProjectDataEditorTab editorTab =
					(ProjectDataEditorTab)tabFolder.getSelection();
			
			editorTab.save();
			dirty.setDirty(false);
		}
	}
	
	@Inject
	@Optional
	public void fileOpenEvent(@UIEventTopic(ProjectExplorer.PROJECT_EXPLORER_DOUBLE_CLICK)
			IProjectData projectData){
		if(projectData instanceof TextData){
			ProjectDataEditorTab newTab = new TextDataEditorTab(tabFolder, SWT.CLOSE,
					(TextData)projectData);
			newTab.addListener(new DirtyStateChangedListener(){
				@Override
				public void notify(ProjectDataEditorTab sender) {
					if(tabFolder.getSelection() == sender){
						dirtyable.setDirty(sender.isDirty());
					}
				}
			});
			
			tabFolder.setSelection(newTab);
		}
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
	public void onFocus() {}

	@Override
	public void mouseDoubleClick(MouseEvent e) {}

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
	public void mouseUp(MouseEvent e) {}

	@Override
	public void widgetSelected(SelectionEvent e) {
		if(e.getSource() == tabFolder){
			if(tabFolder.getSelection() instanceof ProjectDataEditorTab){
				ProjectDataEditorTab editorTab =
						(ProjectDataEditorTab)tabFolder.getSelection();
				
				dirtyable.setDirty(editorTab.isDirty());
			}
			else{
				dirtyable.setDirty(false);
			}
		}
		else{
			if(e.getSource() == newItem){
				createTab();
			}
			else if(e.getSource() == cutItem){
				System.out.println("Cut!");
			}
			else if(e.getSource() == copyItem){
				System.out.println("Copy!");
			}
			else if(e.getSource() == pasteItem){
				System.out.println("Paste!");
			}
		}
	}

	@Override
	public void widgetDefaultSelected(SelectionEvent e) {}
}