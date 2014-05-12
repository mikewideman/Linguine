package LinGUIne.parts.advanced;

import org.eclipse.swt.widgets.Composite;

import LinGUIne.extensions.IEditorSettings;
import LinGUIne.extensions.IProjectDataEditor;
import LinGUIne.extensions.VisualizationView;
import LinGUIne.model.IProjectData;
import LinGUIne.model.Project;
import LinGUIne.model.VisualResult;
import LinGUIne.model.VisualResultContents;

/**
 * Generic container editor for all VisualResult subclasses.
 * 
 * @author Kyle Mullins
 */
public class VisualResultViewEditor implements IProjectDataEditor {

	private VisualResult visualResult;
	private VisualResultContents contents;
	private Project parentProject;
	
	private Composite viewParent;
	private DirtyStateChangedListener dirtyListener;
	private boolean isDirty;
	
	@Override
	public boolean canOpenData(IProjectData data, Project proj) {
		return data.getClass().equals(VisualResult.class);
	}

	@Override
	public boolean hasEditorSettings() {
		return contents.hasSettings();
	}

	@Override
	public IEditorSettings getEditorSettings() {
		return contents.getSettings();
	}

	@Override
	public void createComposite(Composite parent) {
		viewParent = parent;
		
		VisualizationView visualization = contents.getVisualizationView();
		visualization.createPartControl(viewParent);
	}

	@Override
	public void setInputData(IProjectData data, Project parentProj) {
		if(canOpenData(data, parentProj)){
			visualResult = (VisualResult)data;
			contents = (VisualResultContents)visualResult.getContents();
			parentProject = parentProj;
		}
	}

	@Override
	public IProjectData getInputProjectData() {
		return visualResult;
	}

	@Override
	public Project getInputParentProject() {
		return parentProject;
	}

	@Override
	public void registerDirtyStateListener(DirtyStateChangedListener listener) {
		dirtyListener = listener;
	}

	@Override
	public boolean saveChanges() {
		//At least for now we'll say since the editor will never get dirty,
		//you can't save it either.
		return false;
	}

	@Override
	public String getPartLabel() {
		return visualResult.getName();
	}

	@Override
	public String getPartIconURI() {
		return null;
	}

	public void setDirty(boolean dirty){
		if(isDirty != dirty){
			isDirty = dirty;
			dirtyListener.dirtyChanged(isDirty);
		}
	}
}
