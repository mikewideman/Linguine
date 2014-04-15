 
package LinGUIne.parts.advanced;

import java.io.IOException;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;

import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.di.Focus;
import org.eclipse.e4.ui.di.Persist;
import org.eclipse.e4.ui.di.UIEventTopic;
import org.eclipse.e4.ui.model.application.ui.MDirtyable;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.e4.ui.workbench.modeling.ESelectionService;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabFolder2Listener;
import org.eclipse.swt.custom.CTabFolderEvent;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;

import LinGUIne.events.LinGUIneEvents;
import LinGUIne.events.OpenProjectDataEvent;
import LinGUIne.model.IProjectData;
import LinGUIne.model.TextData;
import LinGUIne.parts.advanced.ProjectDataEditorTab.DirtyStateChangedListener;

public class DataEditorPart implements SelectionListener, CTabFolder2Listener{
	
	@Inject
	private MDirtyable dirtyable;
	
	@Inject
	private ESelectionService selectionService;
	
	private CTabFolder tabFolder;
	private Menu contextMenu;
	private MenuItem newItem;
	private MenuItem cutItem;
	private MenuItem copyItem;
	private MenuItem pasteItem;
	
	private DirtyStateChangedListener dirtyListener;
	
	@Inject
	public DataEditorPart() {}
	
	@PostConstruct
	public void createComposite(Composite parent) {
		tabFolder = new CTabFolder(parent,SWT.NONE);
		tabFolder.addSelectionListener(this);
		tabFolder.addCTabFolder2Listener(this);
		
		createContextMenu();
		
		dirtyable.setDirty(false);
		
		dirtyListener = new DirtyStateChangedListener(){
			@Override
			public void notify(ProjectDataEditorTab sender) {
				if(tabFolder.getSelection() == sender){
					dirtyable.setDirty(sender.isDirty());
				}
			}
		};
	}
	
	/**
	 * Called when the user requests that the contents of an EditorTab be
	 * saved.
	 */
	@Persist
	public void save(MDirtyable dirty) throws IOException{
		if(tabFolder.getSelection() instanceof ProjectDataEditorTab){
			ProjectDataEditorTab editorTab =
					(ProjectDataEditorTab)tabFolder.getSelection();
			
			editorTab.save();
			dirty.setDirty(false);
		}
	}
	
	/**
	 * Called when a user attempts to open a File.
	 */
	@Inject
	@Optional
	public void fileOpenEvent(@UIEventTopic(LinGUIneEvents.UILifeCycle.OPEN_PROJECT_DATA)
			OpenProjectDataEvent openEvent, @Named(IServiceConstants.ACTIVE_SHELL)
			Shell shell){
		
		IProjectData projectData = openEvent.getProjectData();
		ProjectDataEditorTab newTab;
		
		if(projectData instanceof TextData){
			if(openEvent.getParentProject().isAnnotated(projectData)){
				newTab = new AnnotatedTextDataEditorTab(tabFolder, SWT.CLOSE,
						(TextData)projectData, openEvent.getParentProject().
						getAnnotation(projectData));
			}
			else{
				newTab = new TextDataEditorTab(tabFolder, SWT.CLOSE,
					(TextData)projectData);
			}
			
			newTab.addListener(dirtyListener);
			
			newTab.getControl().setMenu(contextMenu);
			tabFolder.setSelection(newTab);
			
			selectionService.setSelection(newTab);
		}
		else{
			MessageDialog.openError(shell, "Error", "Could not open " +
					projectData.getName() + ", there is no associated editor.");
		}
		
		tabFolder.setFocus();
	}
	
	/**
	 * Creates a new blank text editor tab
	 */
	public void createTab(){
		CTabItem newTab = new CTabItem(tabFolder,SWT.CLOSE);
		newTab.setText("New File");
		
		StyledText textArea = new StyledText(tabFolder,SWT.V_SCROLL|SWT.H_SCROLL);
		textArea.setAlwaysShowScrollBars(true);
		textArea.setMenu(contextMenu);
		
		newTab.setControl(textArea);
		
		tabFolder.setSelection(newTab);
	}
	
	/**
	 * Creates a new text editor tab and populates the editor with data from a text file
	 * @param fileName - the name of the File
	 * @param contents - the text data from the File
	 */
	public void createTab(String fileName, String[] contents){
		CTabItem newTab = new CTabItem(tabFolder,SWT.CLOSE);
		newTab.setText(fileName);
		
		StyledText textArea = new StyledText(tabFolder,SWT.V_SCROLL|SWT.H_SCROLL);
		textArea.setAlwaysShowScrollBars(true);
		textArea.setMenu(contextMenu);
		newTab.setControl(textArea);
		
		for(int i = 0; i < contents.length; i++){
			textArea.append(contents[i]);
		}
		

		tabFolder.setSelection(newTab);
	}
	
	@Focus
	public void onFocus() {}

	@Override
	public void widgetSelected(SelectionEvent e) {
		if(e.getSource() == tabFolder){
			CTabItem tab = tabFolder.getSelection();
			
			selectionService.setSelection(tab);
			
			if(tab instanceof ProjectDataEditorTab){
				ProjectDataEditorTab editorTab = (ProjectDataEditorTab)tab;
				
				dirtyable.setDirty(editorTab.isDirty());
			}
			else{
				dirtyable.setDirty(false);
			}
		}
	}

	@Override
	public void widgetDefaultSelected(SelectionEvent e) {}
	
	/**
	 * Creates the context menu for the editor
	 */
	private void createContextMenu(){
		contextMenu = new Menu(tabFolder);
		
		newItem = new MenuItem(contextMenu,SWT.NONE);
		newItem.setText("New");
		newItem.addSelectionListener(new SelectionListener(){
			@Override
			public void widgetSelected(SelectionEvent e) {
				//TODO: Change to run the NewFile command
				createTab();
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {}
		});
		
		cutItem = new MenuItem(contextMenu,SWT.NONE);
		cutItem.setText("Cut");
		cutItem.addSelectionListener(new SelectionListener(){
			@Override
			public void widgetSelected(SelectionEvent e) {
				//TODO: Change to run built-in command
				System.out.println("Cut!");
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {}
		});
		
		copyItem = new MenuItem(contextMenu,SWT.NONE);
		copyItem.setText("Copy");
		copyItem.addSelectionListener(new SelectionListener(){
			@Override
			public void widgetSelected(SelectionEvent e) {
				//TODO: Change to run built-in command
				System.out.println("Copy!");
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {}
		});
		
		pasteItem = new MenuItem(contextMenu,SWT.NONE);
		pasteItem.setText("Paste");
		pasteItem.addSelectionListener(new SelectionListener(){
			@Override
			public void widgetSelected(SelectionEvent e) {
				//TODO: Change to run built-in command
				System.out.println("Paste!");
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {}
		});
		
		tabFolder.setMenu(contextMenu);
	}

	@Override
	public void close(CTabFolderEvent event) {
		CTabItem tab = tabFolder.getSelection();
		
		if(tab instanceof ProjectDataEditorTab){
			ProjectDataEditorTab editorTab = (ProjectDataEditorTab)tab;
			editorTab.removeListener(dirtyListener);
		}
		
		if(tabFolder.getItemCount() == 1){
			selectionService.setSelection(null);
		}
	}

	@Override
	public void minimize(CTabFolderEvent event) {}

	@Override
	public void maximize(CTabFolderEvent event) {}

	@Override
	public void restore(CTabFolderEvent event) {}

	@Override
	public void showList(CTabFolderEvent event) {}
}