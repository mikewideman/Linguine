package LinGUIne.parts.advanced;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CaretEvent;
import org.eclipse.swt.custom.CaretListener;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;

import LinGUIne.extensions.IEditorSettings;
import LinGUIne.model.AnnotationSet;
import LinGUIne.model.AnnotationSetContents;
import LinGUIne.model.IProjectData;
import LinGUIne.model.TextData;
import LinGUIne.model.TextDataContents;
import LinGUIne.model.annotations.IAnnotation;
import LinGUIne.model.annotations.MetaAnnotation;
import LinGUIne.model.annotations.Tag;
import LinGUIne.model.annotations.TextAnnotation;

/**
 * Editor for displaying and making changes to TextData with Annotations.
 * 
 * @author Kyle Mullins
 */
public class AnnotatedTextDataEditorTab extends ProjectDataEditorTab {

	private TextData projectData;
	private TextDataContents projectDataContents;
	private AnnotationSet annotationSet;
	private AnnotationSetContents annotationSetContents;
	private AnnotatedTextDataSettings editorSettings;
	
	public AnnotatedTextDataEditorTab(CTabFolder parent, int style,
			TextData projData, AnnotationSet annotation){
		super(parent, style);
		
		projectData = projData;
		annotationSet = annotation;
		
		projectDataContents = projectData.getContents();
		annotationSetContents = annotationSet.getContents();
		
		super.createComposite();
		
		editorSettings = new AnnotatedTextDataSettings(this, projectDataContents,
				annotationSetContents);
		
		textArea.addCaretListener(new CaretListener(){
			@Override
			public void caretMoved(CaretEvent event) {
				editorSettings.caretMoved(event.caretOffset);
			}
		});
	}
	
	@Override
	public IProjectData getProjectData() {
		return projectData;
	}

	@Override
	public void save() {
		projectData.updateContents(projectDataContents);
		annotationSet.updateContents(annotationSetContents);
		setDirty(false);
	}

	@Override
	public <T extends IProjectData> boolean canOpenDataType(Class<T> type) {
		return type == TextData.class || type == AnnotationSet.class;
	}

	@Override
	protected void updateTextArea() {
		textArea.append(projectDataContents.getText());
		
		for(Tag tag: annotationSetContents.getTags()){
			if(tag.getEnabled()){
				for(IAnnotation annotation:
					annotationSetContents.getAnnotations(tag)){
					
					if(annotation instanceof TextAnnotation){
						TextAnnotation textAnnotation = ((TextAnnotation)annotation);
						
						StyleRange style = new StyleRange(
								textAnnotation.getStartIndex(),
								textAnnotation.getLength(),
								tag.getColor(), textArea.getBackground(), SWT.BOLD);
						
						textArea.setStyleRange(style);
					}
					else if(annotation instanceof MetaAnnotation){
						//TODO: Handle annotations of other annotations
					}
				}
			}
		}
	}

	@Override
	protected ModifyListener getModifyListener() {
		return new ModifyListener(){
			@Override
			public void modifyText(ModifyEvent e) {
				//TODO: Intelligently adjust annotations while editing
			}
		};
	}

	@Override
	public boolean hasEditorSettings() {
		return true;
	}

	@Override
	public IEditorSettings getEditorSettings() {
		return editorSettings;
	}
}
