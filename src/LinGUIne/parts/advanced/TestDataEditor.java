package LinGUIne.parts.advanced;

import javax.annotation.PostConstruct;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

import LinGUIne.extensions.IEditorSettings;
import LinGUIne.extensions.IProjectDataEditor;
import LinGUIne.model.IProjectData;
import LinGUIne.model.Project;

public class TestDataEditor implements IProjectDataEditor {

	@Override
	public <T extends IProjectData> boolean canOpenDataType(Class<T> type) {

		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean hasEditorSettings() {
		return false;
	}

	@Override
	public IEditorSettings getEditorSettings() {
		return null;
	}

	public void createComposite(Composite parent) {
		Composite container = new Composite(parent, SWT.NONE);
		container.setLayout(new GridLayout(1, false));
		
		Label label = new Label(container, SWT.NONE);
		label.setText("Test text");
	}

	@Override
	public void setInputData(IProjectData data, Project parentProj) {

		// TODO Auto-generated method stub
		
	}

//	@Override
//	public void setDirtyStateListener(DirtyStateChangedListener listener) {
//
//		// TODO Auto-generated method stub
//		
//	}

	@Override
	public boolean saveChanges() {

		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String getPartLabel() {

		// TODO Auto-generated method stub
		return null;
	}
}
