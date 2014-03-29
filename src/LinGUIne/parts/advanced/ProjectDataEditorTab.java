package LinGUIne.parts.advanced;

import java.util.HashSet;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.widgets.Display;

import LinGUIne.extensions.IEditorSettings;
import LinGUIne.model.IProjectData;

/**
 * Abstract superclass for all editors of ProjectData.
 * 
 * @author Kyle Mullins
 */
public abstract class ProjectDataEditorTab extends CTabItem{

	private boolean isDirty;
	private String tabName;
	private HashSet<ProjectDataEditorTab.DirtyStateChangedListener> listeners;
	
	protected StyledText textArea;
	
	public ProjectDataEditorTab(CTabFolder parent, int style) {
		super(parent, style);
		
		isDirty = false;
		listeners = new HashSet<ProjectDataEditorTab.DirtyStateChangedListener>();
	}
	
	/**
	 * Returns the ProjectData being edited.
	 */
	public abstract IProjectData getProjectData();
	
	/**
	 * Updates the ProjectData with the new content edits and sets the diry
	 * state to false.
	 */
	public abstract void save();
	
	/**
	 * Returns whether or not this ProjectData editor handles the given type of
	 * ProjectData.
	 */
	public abstract <T extends IProjectData> boolean canOpenDataType(
			Class<T> type);
	
	public abstract boolean hasEditorSettings();
	
	public abstract IEditorSettings getEditorSettings();
	
	/**
	 * Updates the primary text area to reflect the current contents of the
	 * ProjectData being edited.
	 */
	protected abstract void updateTextArea();
	
	/**
	 * Returns a ModifyListener that will get called whenever the contents of
	 * the editor are changed.
	 */
	protected abstract ModifyListener getModifyListener();
	
	/**
	 * Places and configures the UI elements for this editor.
	 */
	protected void createComposite(){
		tabName = getProjectData().getName();
		setText(tabName);
		
		textArea = new StyledText(getParent(),
				SWT.V_SCROLL | SWT.H_SCROLL);
		
		textArea.setAlwaysShowScrollBars(true);
		textArea.addModifyListener(getModifyListener());
		
		updateTextArea();
		setControl(textArea);
	}

	/**
	 * Returns whether or not the editor's state is dirty.
	 */
	public boolean isDirty() {
		return isDirty;
	}
	
	/**
	 * Sets the dirty state of the editor and updates the tab name to reflect
	 * the new dirty state.
	 */
	public void setDirty(boolean dirty){
		final CTabItem me = this;
		
		isDirty = dirty;
		notifyListeners();
		
		final String newTabName;
		
		if(isDirty){
			newTabName = "*" + tabName;
		}
		else{
			newTabName = tabName;
		}
		
		//Update tab name from UI thread
		Display.getDefault().asyncExec(new Runnable(){
			public void run(){
				me.setText(newTabName);
			}
		});
	}
	
	public void addListener(ProjectDataEditorTab.DirtyStateChangedListener listener){
		listeners.add(listener);
	}
	
	public void removeListener(ProjectDataEditorTab.DirtyStateChangedListener listener){
		listeners.remove(listener);
	}
	
	public void notifyListeners(){
		for(ProjectDataEditorTab.DirtyStateChangedListener listener: listeners){
			listener.notify(this);
		}
	}
	
	public abstract static class DirtyStateChangedListener{
		public abstract void notify(ProjectDataEditorTab sender);
	}
}