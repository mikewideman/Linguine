package LinGUIne.parts.advanced;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

import LinGUIne.extensions.IEditorSettings;
import LinGUIne.extensions.IProjectDataEditor;
import LinGUIne.model.IProjectData;
import LinGUIne.model.IPlaintextViewable;
import LinGUIne.model.Project;
import LinGUIne.model.TextData;
import LinGUIne.model.TextDataContents;

public class UneditableTextViewer implements IProjectDataEditor {

	private StyledText textArea;
	
	private Project parentProject;
	private IProjectData projectData;
	private IPlaintextViewable textViewableContents;
	
	@Override
	public boolean canOpenData(IProjectData data, Project proj){
		return data.getContents() instanceof IPlaintextViewable;
	}
	
	@Override
	public boolean hasEditorSettings() {
		return false;
	}

	@Override
	public IEditorSettings getEditorSettings() {
		return null;
	}
	
	@Override
	public void createComposite(Composite parent) {
		Composite container = new Composite(parent, SWT.NONE);
		container.setLayout(new GridLayout(1, false));
		
		textArea = new StyledText(container, SWT.V_SCROLL | SWT.H_SCROLL);
		textArea.setLayoutData(new GridData(GridData.FILL_BOTH));
		textArea.setEditable(false);
		textArea.setAlwaysShowScrollBars(true);
		
		textArea.append(textViewableContents.getAsPlaintext());
	}
	
	@Override
	public void setInputData(IProjectData data, Project parentProj) {
		if(canOpenData(data, parentProj)){
			parentProject = parentProj;
			projectData = data;
			textViewableContents = (IPlaintextViewable)data.getContents();
		}
		else{
			throw new IllegalArgumentException("This class only supports ");
		}
	}
	
	@Override
	public IProjectData getInputProjectData() {
		return projectData;
	}

	@Override
	public Project getInputParentProject() {
		return parentProject;
	}
	
	@Override
	public void registerDirtyStateListener(DirtyStateChangedListener listener){}
	
	@Override
	public boolean saveChanges(){
		return false;
	}

	@Override
	public String getPartLabel() {
		return projectData.getName();
	}

	@Override
	public String getPartIconURI() {
		return null;
	}
}
