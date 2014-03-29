package LinGUIne.parts.advanced;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

import LinGUIne.extensions.IEditorSettings;

public class AnnotatedTextDataSettings implements IEditorSettings {

	private Composite parentComposite;
	
	@Override
	public void createComposite(Composite parent) {
		parentComposite = parent;
		parentComposite.setLayout(new GridLayout(1, false));
		
		Label testLabel = new Label(parentComposite, SWT.NONE);
		testLabel.setText("Test text");
	}

	@Override
	public Composite getParent() {
		return parentComposite;
	}
}
