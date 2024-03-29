package LinGUIne.parts.advanced;

import javax.inject.Inject;

import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;

import LinGUIne.events.LinGUIneEvents;
import LinGUIne.extensions.IEditorSettings;
import LinGUIne.extensions.IProjectDataEditor;
import LinGUIne.extensions.IPropertiesProvider;
import LinGUIne.model.IProjectData;
import LinGUIne.model.Project;
import LinGUIne.model.TextData;
import LinGUIne.model.TextDataContents;

/**
 * Simple editor for TextData objects.
 * 
 * @author Kyle Mullins
 */
public class TextDataEditor implements IProjectDataEditor, IPropertiesProvider {

	@Inject
	private IEventBroker eventBroker;
	
	private StyledText textArea;
	private Composite propertiesView;
	private Label lblCharCount;
	private Label lblCharCountNoSpaces;
	
	private Project parentProject;
	private TextData projectData;
	private TextDataContents projectDataContents;
	private DirtyStateChangedListener dirtyListener;
	private boolean isDirty;
	
	@Override
	public boolean canOpenData(IProjectData data, Project proj) {
		return data instanceof TextData;
	}
	
	@Override
	public boolean hasEditorSettings() {
		return false;
	}

	@Override
	public IEditorSettings getEditorSettings() {
		return null;
	}

	/**
	 * Creates the editor area and hooks up listeners as needed.
	 */
	public void createComposite(Composite parent) {
		Composite container = new Composite(parent, SWT.NONE);
		container.setLayout(new GridLayout(1, false));
		
		textArea = new StyledText(container, SWT.V_SCROLL | SWT.H_SCROLL);
		textArea.setLayoutData(new GridData(GridData.FILL_BOTH));
		textArea.setAlwaysShowScrollBars(true);
		textArea.addModifyListener(new ModifyListener(){
			@Override
			public void modifyText(ModifyEvent e) {
				if(!projectDataContents.getText().equals(textArea.getText())){
					projectDataContents.setText(textArea.getText());
					setDirty(true);
				}
			}
		});
		
		textArea.append(projectDataContents.getText());
		createContextMenu(textArea);
	}

	@Override
	public void setInputData(IProjectData data, Project parentProj) {
		if(canOpenData(data, parentProj)){
			projectData = (TextData)data;
			projectDataContents = projectData.getContents();
			parentProject = parentProj;
		}
		else{
			throw new IllegalArgumentException("This class only supports "
					+ "TextData objects.");
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
			setDirty(false);
			
			return true;
		}
	
		return false;
	}

	@Override
	public String getPartLabel() {
		return projectData.getName();
	}
	
	@Override
	public String getPartIconURI(){
		return null;
	}
	
	/**
	 * Sets the dirty state of the editor.
	 */
	public void setDirty(boolean dirty){
		if(isDirty != dirty){
			isDirty = dirty;
			dirtyListener.dirtyChanged(isDirty);
		}
	}
	
	@Override
	public Composite getProperties(Composite parent) {
		if(propertiesView == null){
			createPropertiesView(parent);
		}
		
		return propertiesView;
	}
	
	/**
	 * Creates the properties Composite for this editor.
	 */
	private void createPropertiesView(Composite parent){
		propertiesView = new Composite(parent, SWT.NONE);
		propertiesView.setLayout(new GridLayout(1, false));
		
		lblCharCount = new Label(propertiesView, SWT.NONE);
		lblCharCount.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		lblCharCount.setText("Character count: ");
		
		lblCharCountNoSpaces = new Label(propertiesView, SWT.NONE);
		lblCharCountNoSpaces.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		lblCharCountNoSpaces.setText("Character count (no spaces): ");
		
		updateProperties();
	}
	
	/**
	 * Updates the properties Composite based on the editor's contents.
	 */
	private void updateProperties(){
		int charCount = textArea.getText().length();
		int charCountNoSpaces = textArea.getText().replaceAll("\\s", "").length();
		
		lblCharCount.setText("Character count: " + charCount);
		lblCharCountNoSpaces.setText("Character count (no spaces): " +
				charCountNoSpaces);
	}
	
	/**
	 * Creates the context menu for the editor.
	 */
	private void createContextMenu(Composite container){
		final TextDataEditor self = this;
		
		Menu contextMenu = new Menu(container);
		container.setMenu(contextMenu);
		
		MenuItem editAnnotationsItem = new MenuItem(contextMenu, SWT.NONE);
		editAnnotationsItem.setText("Edit Annotations");
		editAnnotationsItem.addSelectionListener(new SelectionListener(){
			@Override
			public void widgetSelected(SelectionEvent e) {
				eventBroker.post(LinGUIneEvents.UILifeCycle.EDIT_ANNOTATIONS,
						self);
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {}
		});
		
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
