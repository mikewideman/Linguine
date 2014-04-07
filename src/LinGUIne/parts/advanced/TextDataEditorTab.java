package LinGUIne.parts.advanced;

import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;

import LinGUIne.extensions.IEditorSettings;
import LinGUIne.model.IProjectData;
import LinGUIne.model.TextData;
import LinGUIne.model.TextDataContents;

/**
 * Editor for displaying and making changes to TextData.
 * 
 * @author Kyle Mullins
 */
class TextDataEditorTab extends ProjectDataEditorTab{

	private TextData projectData;
	private TextDataContents projectDataContents;
	
	public TextDataEditorTab(CTabFolder parent, int style, TextData projData) {
		super(parent, style);
		
		projectData = projData;
		projectDataContents = projectData.getContents();
		
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
	public <T extends IProjectData> boolean canOpenDataType(Class<T> type) {
		return type == TextData.class;
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

	@Override
	public boolean hasEditorSettings() {
		return false;
	}

	@Override
	public IEditorSettings getEditorSettings() {
		return null;
	}
}