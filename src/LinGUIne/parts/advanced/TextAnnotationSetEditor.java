package LinGUIne.parts.advanced;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CaretEvent;
import org.eclipse.swt.custom.CaretListener;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;

import LinGUIne.extensions.IEditorSettings;
import LinGUIne.extensions.IProjectDataEditor;
import LinGUIne.model.AnnotationSet;
import LinGUIne.model.AnnotationSetContents;
import LinGUIne.model.IProjectData;
import LinGUIne.model.Project;
import LinGUIne.model.TextData;
import LinGUIne.model.TextDataContents;
import LinGUIne.model.annotations.IAnnotation;
import LinGUIne.model.annotations.MetaAnnotation;
import LinGUIne.model.annotations.Tag;
import LinGUIne.model.annotations.TextAnnotation;

public class TextAnnotationSetEditor implements IProjectDataEditor {

	private StyledText textArea;
	
	private Project parentProject;
	private TextData projectData;
	private TextDataContents projectDataContents;
	private AnnotationSet annotationSet;
	private AnnotationSetContents annotationSetContents;
	private TextAnnotationSetSettings editorSettings;
	private DirtyStateChangedListener dirtyListener;
	private boolean isDirty;
	private boolean updatingTextArea;
	
	@Override
	public boolean canOpenData(IProjectData data, Project proj) {
		if(data instanceof AnnotationSet){
			AnnotationSet annotations = (AnnotationSet)data;
			
			return proj.getDataForAnnotation(annotations) instanceof TextData;
		}
		
		return false;
	}

	@Override
	public boolean hasEditorSettings() {
		return true;
	}

	@Override
	public IEditorSettings getEditorSettings() {
		return editorSettings;
	}

	@Override
	public void createComposite(Composite parent) {
		Composite container = new Composite(parent, SWT.NONE);
		container.setLayout(new GridLayout(1, false));
		
		textArea = new StyledText(container, SWT.V_SCROLL | SWT.H_SCROLL);
		textArea.setLayoutData(new GridData(GridData.FILL_BOTH));
		textArea.setAlwaysShowScrollBars(true);
		textArea.addModifyListener(new ModifyListener(){
			@Override
			public void modifyText(ModifyEvent e) {
				if(!updatingTextArea){
					if(!projectDataContents.getText().equals(textArea.getText())){
						projectDataContents.setText(textArea.getText());
						setDirty(true);
						//TODO: Intelligently update annotationSetContents
					}
				}
			}
		});
		
		textArea.addCaretListener(new CaretListener(){
			@Override
			public void caretMoved(CaretEvent event) {
				editorSettings.caretMoved(event.caretOffset);
			}
		});
		
		textArea.addSelectionListener(new SelectionListener(){
			@Override
			public void widgetSelected(SelectionEvent e) {
				editorSettings.selectionChanged(new Point(e.x, e.y));
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {}
		});
		
		updateTextArea();
		createContextMenu(textArea);
	}

	@Override
	public void setInputData(IProjectData data, Project parentProj) {
		if(canOpenData(data, parentProj)){
			annotationSet = (AnnotationSet)data;
			annotationSetContents = annotationSet.getContents();
			projectData = (TextData)parentProj.getDataForAnnotation(annotationSet);
			projectDataContents = projectData.getContents();
			parentProject = parentProj;
			
			editorSettings = new TextAnnotationSetSettings(this, projectDataContents,
					annotationSetContents);
		}
		else{
			throw new IllegalArgumentException("This class only supports "
					+ "TextData objects with Annotations!");
		}
	}
	
	@Override
	public IProjectData getInputProjectData(){
		return projectData;
	}
	
	@Override
	public Project getInputParentProject(){
		return parentProject;
	}

	@Override
	public void registerDirtyStateListener(DirtyStateChangedListener listener) {
		dirtyListener = listener;
	}

	@Override
	public boolean saveChanges() {
		if(projectData.updateContents(projectDataContents)){
			if(annotationSet.updateContents(annotationSetContents)){
				setDirty(false);
				
				return true;
			}
		}
		
		return false;
	}

	@Override
	public String getPartLabel() {
		return projectData.getName() + " (Annotations)";
	}

	@Override
	public String getPartIconURI(){
		return null;
	}
	
	public void setDirty(boolean dirty){
		if(isDirty != dirty){
			isDirty = dirty;
			dirtyListener.dirtyChanged(isDirty);
		}
	}
	
	public void annotationsChanged(){
		int caretOffset = textArea.getCaretOffset();
		
		updateTextArea();
		
		textArea.setCaretOffset(caretOffset);
	}
	
	private void updateTextArea() {
		//Silly flagging to keep update calls from triggering the ModifyListener
		updatingTextArea = true;{
			textArea.setText(projectDataContents.getText());
		}
		updatingTextArea = false;
		
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
	
	private void createContextMenu(Composite container){
		Menu contextMenu = new Menu(container);
		container.setMenu(contextMenu);
		
		MenuItem selectAllItem = new MenuItem(contextMenu, SWT.NONE);
		selectAllItem.setText("Select All");
		selectAllItem.addSelectionListener(new SelectionListener(){
			@Override
			public void widgetSelected(SelectionEvent e) {
				textArea.setSelection(0, textArea.getText().length());
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {}
		});
	}
}
