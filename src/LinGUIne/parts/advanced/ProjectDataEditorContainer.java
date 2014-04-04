package LinGUIne.parts.advanced;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.swt.widgets.Composite;

import LinGUIne.extensions.IProjectDataEditor;

public class ProjectDataEditorContainer {

	@Inject
	private MPart myPart;
	
	private Composite editorParent;
	private IProjectDataEditor projectDataEditor;
	
	public void setProjectDataEditor(IProjectDataEditor dataEditor){
		projectDataEditor = dataEditor;
		
		projectDataEditor.createComposite(editorParent);
		editorParent.layout();
		myPart.setLabel("Test Editor");
	}
	
	@PostConstruct
	public void createComposite(Composite parent){
		editorParent = parent;
	}
}
