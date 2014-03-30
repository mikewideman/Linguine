package LinGUIne.parts.advanced;

import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
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

public class AnnotatedTextDataSettings implements IEditorSettings {

	private Composite parentComposite;
	private LocalPanel localPanel;
	private TagPanel tagPanel;
	private GlobalPanel globalPanel;
	
	private ProjectDataEditorTab parentEditorTab;
	private TextDataContents textContents;
	private AnnotationSetContents annotationContents;
	
	private Tag selectedTag;
	private int caretOffset;
	
	public AnnotatedTextDataSettings(ProjectDataEditorTab editorTab,
			TextDataContents data, AnnotationSetContents annotations){
		parentEditorTab = editorTab;
		textContents = data;
		annotationContents = annotations;
	}
	
	@Override
	public void createComposite(Composite parent) {
		parentComposite = parent;
		parentComposite.setLayout(new GridLayout(1, false));
		
		localPanel = new LocalPanel(parentComposite);
		tagPanel = new TagPanel(parentComposite);
		globalPanel = new GlobalPanel(parentComposite);
		
		localPanel.populate();
		tagPanel.populate();
		globalPanel.populate();
	}

	@Override
	public Composite getParent() {
		return parentComposite;
	}
	
	public void caretMoved(int newCaretOffset){
		caretOffset = newCaretOffset;
		localPanel.update();
	}
	
	private class LocalPanel{
		
		private Composite thePanel;
		
		private List lstTagsAtLocation;
		
		public LocalPanel(Composite parent){
			thePanel = new Composite(parent, SWT.BORDER);
			thePanel.setLayout(new GridLayout(1, false));
			thePanel.setLayoutData(new GridData(GridData.FILL_BOTH));
		}
		
		public void populate(){
			Label lblTagsAtLocation = new Label(thePanel, SWT.NONE);
			lblTagsAtLocation.setText("Tags at caret location:");
			
			lstTagsAtLocation = new List(thePanel, SWT.BORDER | SWT.V_SCROLL |
					SWT.H_SCROLL);
			lstTagsAtLocation.setLayoutData(new GridData(GridData.FILL_BOTH));
		}
		
		public void update(){
			lstTagsAtLocation.removeAll();
			
			for(Tag tag: annotationContents.getTags()){
				for(IAnnotation annotation: annotationContents.getAnnotations(tag)){
					if(annotation instanceof TextAnnotation){
						TextAnnotation textAnnotation = (TextAnnotation)annotation;
						
						if(textAnnotation.isIndexInRange(caretOffset)){
							lstTagsAtLocation.add(textAnnotation.getTag().getName());
						}
					}
				}
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
			thePanel.setLayoutData(new GridData(GridData.FILL_BOTH));
		}
		
		public void populate(){
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
							parentEditorTab.setDirty(true);
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
						parentEditorTab.setDirty(true);
					}
				}
			});
			
			chkTagEnabled = new Button(thePanel, SWT.CHECK);
			chkTagEnabled.setText("Is Enabled");
			chkTagEnabled.addSelectionListener(new SelectionListener(){
				@Override
				public void widgetSelected(SelectionEvent e) {
					selectedTag.setEnabled(chkTagEnabled.getSelection());
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
			}
			else{
				txtTagComment.setText("");
				chkTagEnabled.setSelection(false);
				lblTagColor.setBackground(defaultLabelColor);

				txtTagComment.setEnabled(false);
				btnChangeColor.setEnabled(false);
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
			thePanel.setLayoutData(new GridData(GridData.FILL_BOTH));
		}
		
		public void populate(){
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
