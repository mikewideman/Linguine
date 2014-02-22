package LinGUIne.parts.advanced;

import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;

import LinGUIne.model.IProjectData;
import LinGUIne.model.TextData;
import LinGUIne.model.TextDataContents;

class TextDataEditorTab extends ProjectDataEditorTab{

	private TextData projectData;
	private TextDataContents projectDataContents;
	
	public TextDataEditorTab(CTabFolder parent, int style, TextData projData) {
		super(parent, style);
		
		projectData = projData;
		projectDataContents = (TextDataContents)projectData.getContents();
		
		super.createComposite();
	}

	@Override
	public IProjectData getProjectData() {
		return projectData;
	}
	
	@Override
	public void save() {
		projectData.updateContents(projectDataContents);
		setDirty(false);
	}

	@Override
	protected void updateTextArea() {
		textArea.append(projectDataContents.getText());
	}

	@Override
	protected ModifyListener getModifyListener() {
		return new ModifyListener(){
			@Override
			public void modifyText(ModifyEvent e) {
				if(!projectDataContents.getText().equals(textArea.getText())){
					projectDataContents.setText(textArea.getText());
					setDirty(true);
				}
			}
		};
	}
}