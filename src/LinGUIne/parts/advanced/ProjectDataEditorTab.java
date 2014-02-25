package LinGUIne.parts.advanced;

import java.util.HashSet;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.widgets.Display;

import LinGUIne.model.IProjectData;

abstract class ProjectDataEditorTab extends CTabItem{

	private boolean isDirty;
	private String tabName;
	private HashSet<ProjectDataEditorTab.DirtyStateChangedListener> listeners;
	
	protected StyledText textArea;
	
	public ProjectDataEditorTab(CTabFolder parent, int style) {
		super(parent, style);
		
		isDirty = false;
		listeners = new HashSet<ProjectDataEditorTab.DirtyStateChangedListener>();
	}
	
	public abstract IProjectData getProjectData();
	public abstract void save();
	public abstract <T extends IProjectData> boolean canOpenDataType(
			Class<T> type);
	protected abstract void updateTextArea();
	protected abstract ModifyListener getModifyListener();
	
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

	public boolean isDirty() {
		return isDirty;
	}
	
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