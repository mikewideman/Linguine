package LinGUIne.parts.advanced;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;

import LinGUIne.model.AnnotationSet;
import LinGUIne.model.AnnotationSetContents;
import LinGUIne.model.IProjectData;
import LinGUIne.model.TextData;
import LinGUIne.model.TextDataContents;
import LinGUIne.model.annotations.IAnnotation;
import LinGUIne.model.annotations.Tag;
import LinGUIne.model.annotations.TextAnnotation;

public class AnnotatedTextDataEditorTab extends ProjectDataEditorTab {

	private TextData projectData;
	private TextDataContents projectDataContents;
	private AnnotationSet annotationSet;
	private AnnotationSetContents annotationSetContents;
	
	public AnnotatedTextDataEditorTab(CTabFolder parent, int style,
			TextData projData, AnnotationSet annotation){
		super(parent, style);
		
		projectData = projData;
		annotationSet = annotation;
		
		projectDataContents = projectData.getContents();
		annotationSetContents = annotationSet.getContents();
		
		super.createComposite();
	}
	
	@Override
	public IProjectData getProjectData() {
		return projectData;
	}

	@Override
	public void save() {
		
	}

	@Override
	public <T extends IProjectData> boolean canOpenDataType(Class<T> type) {
		return type == TextData.class || type == AnnotationSet.class;
	}

	@Override
	protected void updateTextArea() {
		textArea.append(projectDataContents.getText());
		
		for(Tag tag: annotationSetContents.getTags()){
			for(IAnnotation annotation:
				annotationSetContents.getAnnotations(tag)){
				
				if(annotation instanceof TextAnnotation){
					TextAnnotation textAnnotation = ((TextAnnotation)annotation);
					
					StyleRange style = new StyleRange(
							textAnnotation.getStartIndex(),
							textAnnotation.getLength(),
							tag.getColor(), textArea.getBackground());
					
					textArea.setStyleRange(style);
				}
			}
		}
	}

	@Override
	protected ModifyListener getModifyListener() {
		return new ModifyListener(){
			@Override
			public void modifyText(ModifyEvent e) {
				
			}
		};
	}
}
