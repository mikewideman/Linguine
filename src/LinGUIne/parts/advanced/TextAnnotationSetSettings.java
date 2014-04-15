package LinGUIne.parts.advanced;

import java.util.LinkedList;

import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.FontDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.ColorDialog;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import LinGUIne.extensions.IEditorSettings;
import LinGUIne.model.AnnotationSetContents;
import LinGUIne.model.TextDataContents;
import LinGUIne.model.annotations.IAnnotation;
import LinGUIne.model.annotations.Tag;
import LinGUIne.model.annotations.TextAnnotation;

public class TextAnnotationSetSettings implements IEditorSettings {

	private Composite parentComposite;
	private GlobalPanel globalPanel;
	private TagPanel tagPanel;
	private LocalPanel localPanel;
	
	private TextAnnotationSetEditor parentEditor;
	private TextDataContents textContents;
	private AnnotationSetContents annotationContents;
	
	private Tag selectedTag;	
	private LinkedList<IAnnotation> annotationsAtLocation;
	private IAnnotation selectedAnnotation;
	private int caretOffset;
	private Point selectionOffsets;
	
	public TextAnnotationSetSettings(TextAnnotationSetEditor editor,
			TextDataContents data, AnnotationSetContents annotations){
		parentEditor = editor;
		textContents = data;
		annotationContents = annotations;
		
		selectedTag = null;
		caretOffset = 0;
		annotationsAtLocation = new LinkedList<IAnnotation>();
		selectedAnnotation = null;
	}
	
	@Override
	public void createComposite(Composite parent) {
		parentComposite = parent;
		parentComposite.setLayout(new GridLayout(1, false));
		
		//Set up a scrollable container so the components aren't squashed
		final ScrolledComposite scrollable = new ScrolledComposite(
				parentComposite, SWT.V_SCROLL);
		scrollable.setLayoutData(new GridData(GridData.FILL_BOTH));
		
		GridLayout layout = new GridLayout(1, false);
		layout.verticalSpacing = 15;
		
		final Composite container = new Composite(scrollable, SWT.NONE);
		container.setLayout(layout);

		scrollable.setContent(container);
		scrollable.setExpandHorizontal(true);
		scrollable.setExpandVertical(true);
		scrollable.addControlListener(new ControlListener(){
			boolean haveResized;
			
			@Override
			public void controlMoved(ControlEvent e) {}

			@Override
			public void controlResized(ControlEvent e) {
				Rectangle r = scrollable.getClientArea();
				
				if(!haveResized){
					scrollable.setMinSize(parentComposite.computeSize(r.width,
							SWT.DEFAULT));
					haveResized = true;
				}
			}
		});
		
		globalPanel = new GlobalPanel(container);
		tagPanel = new TagPanel(container);
		localPanel = new LocalPanel(container);
		
		globalPanel.populate();
		tagPanel.populate();
		localPanel.populate();
	}

	@Override
	public Composite getParent() {
		return parentComposite;
	}
	
	public void caretMoved(int newCaretOffset){
		caretOffset = newCaretOffset;
		annotationsAtLocation.clear();
		
		//Find all the annotations overlapping at the cursor location
		for(Tag tag: annotationContents.getTags()){
			for(IAnnotation annotation: annotationContents.getAnnotations(tag)){
				if(annotation instanceof TextAnnotation){
					TextAnnotation textAnnotation = (TextAnnotation)annotation;
					
					if(textAnnotation.isIndexInRange(caretOffset)){
						annotationsAtLocation.add(textAnnotation);
					}
				}
			}
		}
		
		if(localPanel != null){
			localPanel.update();
		}
	}
	
	public void selectionChanged(Point newSelectionOffsets){
		if(newSelectionOffsets.x == newSelectionOffsets.y){
			selectionOffsets = null;
		}
		else{
			selectionOffsets = newSelectionOffsets;
		}
	}
	
	private class LocalPanel{
		
		private Composite thePanel;
		
		private List lstTagsAtLocation;
		private Text txtAnnotationText;
		private Button btnAddAnnotation;
		private Button btnRemoveAnnotation;
		
		public LocalPanel(Composite parent){
			thePanel = new Composite(parent, SWT.BORDER);
			thePanel.setLayout(new GridLayout(1, false));
			
			GridData layoutData = new GridData(GridData.FILL_BOTH);
			layoutData.heightHint = 275;
			thePanel.setLayoutData(layoutData);
		}
		
		public void populate(){
			Label lblHeader = new Label(thePanel, SWT.NONE);
			lblHeader.setText("Current Selection");
			
			FontDescriptor descriptor = FontDescriptor.createFrom(
					lblHeader.getFont());
			descriptor = descriptor.setStyle(SWT.BOLD).setHeight(10);

			lblHeader.setFont(descriptor.createFont(lblHeader.getDisplay()));
			
			Label lblTagsAtLocation = new Label(thePanel, SWT.NONE);
			lblTagsAtLocation.setText("Tags in selection:");
			
			lstTagsAtLocation = new List(thePanel, SWT.BORDER | SWT.V_SCROLL |
					SWT.H_SCROLL);
			lstTagsAtLocation.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
			lstTagsAtLocation.addSelectionListener(new SelectionListener(){
				@Override
				public void widgetSelected(SelectionEvent e) {
					if(lstTagsAtLocation.getSelectionCount() != 0){
						String selection = lstTagsAtLocation.getSelection()[0];
						
						for(IAnnotation annotation: annotationsAtLocation){
							if(annotation.getTag().getName().equals(selection)){
								selectedAnnotation = annotation;
							}
						}
					}
					else{
						selectedAnnotation = null;
					}
					
					update(false);
				}

				@Override
				public void widgetDefaultSelected(SelectionEvent e) {}
			});
			
			Label lblSelectedAnnotation = new Label(thePanel, SWT.NONE);
			lblSelectedAnnotation.setText("Annotation text:");
			
			txtAnnotationText = new Text(thePanel, SWT.BORDER | SWT.WRAP |
					SWT.V_SCROLL);
			txtAnnotationText.setLayoutData(new GridData(GridData.FILL_BOTH));
			txtAnnotationText.setEditable(false);
			
			Composite annotationButtons = new Composite(thePanel, SWT.NONE);
			annotationButtons.setLayout(new GridLayout(2, false));
			annotationButtons.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
			
			btnAddAnnotation = new Button(annotationButtons, SWT.NONE);
			btnAddAnnotation.setText("Add");
			btnAddAnnotation.addSelectionListener(new SelectionListener(){
				@Override
				public void widgetSelected(SelectionEvent e) {
					int startIndex = selectionOffsets == null ?
							caretOffset - 1 : selectionOffsets.x;
					int length = selectionOffsets == null ? 1 :
						selectionOffsets.y - selectionOffsets.x;
					
					TextAnnotation newAnnotation = new TextAnnotation(
							selectedTag, startIndex, length);
					
					annotationContents.addAnnotation(newAnnotation);
					parentEditor.annotationsChanged();
					parentEditor.setDirty(true);
				}

				@Override
				public void widgetDefaultSelected(SelectionEvent e) {}
			});
			
			btnRemoveAnnotation = new Button(annotationButtons, SWT.NONE);
			btnRemoveAnnotation.setText("Remove");
			btnRemoveAnnotation.addSelectionListener(new SelectionListener(){
				@Override
				public void widgetSelected(SelectionEvent e) {
					annotationContents.removeAnnotation(selectedAnnotation);
					parentEditor.annotationsChanged();
					parentEditor.setDirty(true);
				}

				@Override
				public void widgetDefaultSelected(SelectionEvent e) {}
			});
			
			update();
		}
		
		public void update(){
			update(true);
		}
		
		private void update(boolean shouldUpdateList){
			if(shouldUpdateList){
				selectedAnnotation = null;
				lstTagsAtLocation.removeAll();
				
				for(IAnnotation annotation: annotationsAtLocation){
					lstTagsAtLocation.add(annotation.getTag().getName());
				}
				
				if(!annotationsAtLocation.isEmpty()){
					lstTagsAtLocation.setSelection(0);
					selectedAnnotation = annotationsAtLocation.getFirst();
				}
			}
			
			if(selectedAnnotation != null){
				if(selectedAnnotation instanceof TextAnnotation){
					TextAnnotation textAnnotation =
							(TextAnnotation)selectedAnnotation;
					
					txtAnnotationText.setEnabled(true);
					txtAnnotationText.setText(textAnnotation.getText(textContents));
				}
				else{
					txtAnnotationText.setEnabled(false);
					txtAnnotationText.setText("");
				}
				
				btnRemoveAnnotation.setEnabled(true);
			}
			else{
				txtAnnotationText.setEnabled(false);
				txtAnnotationText.setText("");
				btnRemoveAnnotation.setEnabled(false);
			}
			
			if(selectedTag != null){
				btnAddAnnotation.setEnabled(true);
			}
			else{
				btnAddAnnotation.setEnabled(false);
			}
		}
	}
	
	private class TagPanel{
		
		private Composite thePanel;
		
		private Label lblTagColor;
		private Button btnChangeColor;
		private Color defaultLabelColor;
		private Text txtTagComment;
		private Button chkTagEnabled;
		
		public TagPanel(Composite parent){
			thePanel = new Composite(parent, SWT.BORDER);
			thePanel.setLayout(new GridLayout(1, false));
			
			GridData layoutData = new GridData(GridData.FILL_BOTH);
			layoutData.heightHint = 175;
			thePanel.setLayoutData(layoutData);
		}
		
		public void populate(){
			Label lblHeader = new Label(thePanel, SWT.NONE);
			lblHeader.setText("Current Tag");
			
			FontDescriptor descriptor = FontDescriptor.createFrom(
					lblHeader.getFont());
			descriptor = descriptor.setStyle(SWT.BOLD).setHeight(10);

			lblHeader.setFont(descriptor.createFont(lblHeader.getDisplay()));
			
			Composite colorPanel = new Composite(thePanel, SWT.NONE);
			colorPanel.setLayout(new GridLayout(3, false));
			colorPanel.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
			
			Label lblColorText = new Label(colorPanel, SWT.NONE);
			lblColorText.setText("Color:");
			
			lblTagColor = new Label(colorPanel, SWT.NONE);
			lblTagColor.setText("     ");
			
			btnChangeColor = new Button(colorPanel, SWT.NONE);
			btnChangeColor.setLayoutData(new GridData(SWT.RIGHT, SWT.FILL, true,
					true, 1, 1));
			btnChangeColor.setText("Change...");
			btnChangeColor.addSelectionListener(new SelectionListener(){
				@Override
				public void widgetSelected(SelectionEvent e) {
					ColorDialog newColorDialog = new ColorDialog(
							thePanel.getShell());
					RGB selectedRGB = newColorDialog.open();
					
					if(selectedRGB != null){
						Color newColor = new Color(Display.getCurrent(),
								selectedRGB);
						
						if(!newColor.equals(selectedTag.getColor())){
							selectedTag.setColor(newColor);
							parentEditor.annotationsChanged();
							parentEditor.setDirty(true);
							update();
						}
					}
				}

				@Override
				public void widgetDefaultSelected(SelectionEvent e) {}
			});
			
			defaultLabelColor = lblTagColor.getBackground();
			
			Label lblTagComment = new Label(thePanel, SWT.NONE);
			lblTagComment.setText("Comment:");
			
			txtTagComment = new Text(thePanel, SWT.BORDER |
					SWT.V_SCROLL | SWT.WRAP);
			txtTagComment.setLayoutData(new GridData(GridData.FILL_BOTH));
			txtTagComment.addModifyListener(new ModifyListener(){
				@Override
				public void modifyText(ModifyEvent e) {
					if(selectedTag != null && !selectedTag.getComment().equals(
							txtTagComment.getText())){
						selectedTag.setComment(txtTagComment.getText());
						parentEditor.setDirty(true);
					}
				}
			});
			
			chkTagEnabled = new Button(thePanel, SWT.CHECK);
			chkTagEnabled.setText("Is Enabled");
			chkTagEnabled.addSelectionListener(new SelectionListener(){
				@Override
				public void widgetSelected(SelectionEvent e) {
					selectedTag.setEnabled(chkTagEnabled.getSelection());
					parentEditor.annotationsChanged();
				}

				@Override
				public void widgetDefaultSelected(SelectionEvent e) {}
			});
			
			update();
		}
		
		public void update(){
			if(selectedTag != null){
				txtTagComment.setText(selectedTag.getComment());
				chkTagEnabled.setSelection(selectedTag.getEnabled());
				lblTagColor.setBackground(selectedTag.getColor());

				txtTagComment.setEnabled(true);
				btnChangeColor.setEnabled(true);
				chkTagEnabled.setEnabled(true);
			}
			else{
				txtTagComment.setText("");
				chkTagEnabled.setSelection(false);
				lblTagColor.setBackground(defaultLabelColor);

				txtTagComment.setEnabled(false);
				btnChangeColor.setEnabled(false);
				chkTagEnabled.setEnabled(false);
			}
		}
	}
	
	private class GlobalPanel{
		
		private Composite thePanel;
		
		private List lstAllTags;
		private Button btnAddTag;
		
		public GlobalPanel(Composite parent){
			thePanel = new Composite(parent, SWT.BORDER);
			thePanel.setLayout(new GridLayout(1, false));
			
			GridData layoutData = new GridData(GridData.FILL_BOTH);
			layoutData.heightHint = 150;
			thePanel.setLayoutData(layoutData);
		}
		
		public void populate(){
			Label lblHeader = new Label(thePanel, SWT.NONE);
			lblHeader.setText("Current File");
			
			FontDescriptor descriptor = FontDescriptor.createFrom(
					lblHeader.getFont());
			descriptor = descriptor.setStyle(SWT.BOLD).setHeight(10);

			lblHeader.setFont(descriptor.createFont(lblHeader.getDisplay()));
			
			lstAllTags = new List(thePanel, SWT.BORDER | SWT.V_SCROLL |
					SWT.H_SCROLL);
			lstAllTags.setLayoutData(new GridData(GridData.FILL_BOTH));
			
			lstAllTags.addSelectionListener(new SelectionListener(){
				@Override
				public void widgetSelected(SelectionEvent e) {
					if(lstAllTags.getSelectionCount() != 0){
						String tagName = lstAllTags.getSelection()[0];
						
						selectedTag = annotationContents.getTag(tagName);
					}
					else{
						selectedTag = null;
					}
					
					localPanel.update();
					tagPanel.update();
				}

				@Override
				public void widgetDefaultSelected(SelectionEvent e) {}
			});
			
			btnAddTag = new Button(thePanel, SWT.NONE);
			btnAddTag.setText("Add Tag");
			btnAddTag.addSelectionListener(new SelectionListener(){
				@Override
				public void widgetSelected(SelectionEvent e) {
					Shell theShell = thePanel.getShell();
					InputDialog tagNamePrompt = new InputDialog(theShell, "New Tag",
							"Input new tag name:", "", null);
					
					if(tagNamePrompt.open() == InputDialog.OK){
						String newTagName = tagNamePrompt.getValue();
						
						if(!annotationContents.containsTag(newTagName)){
							Tag newTag = new Tag(newTagName,Display.getCurrent().
									getSystemColor(SWT.COLOR_BLUE), "");
							
							annotationContents.addTag(newTag);
							selectedTag = newTag;
							
							update();
							tagPanel.update();
						}
						else{
							MessageDialog.openError(theShell, "Error",
									"A tag already exists with the given name.");
						}
					}
				}

				@Override
				public void widgetDefaultSelected(SelectionEvent e) {}
			});
			
			update();
		}
		
		public void update(){
			lstAllTags.removeAll();
			
			for(Tag tag: annotationContents.getTags()){
				lstAllTags.add(tag.getName());
			}
			
			if(selectedTag != null){
				lstAllTags.setSelection(new String[]{selectedTag.getName()});
			}
		}
	}
}
