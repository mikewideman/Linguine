
package LinGUIne.parts;

import java.util.ArrayList;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.eclipse.e4.ui.di.Focus;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

import LinGUIne.model.MockLibrary;

public class QuickAnalysisPart {
	@Inject
	public QuickAnalysisPart() {
	}

	@PostConstruct
	public void createComposite(Composite parent){

		GridLayout layout = new GridLayout();
		layout.numColumns = 3;
		parent.setLayout(layout);

		Label lblLibraries = new Label(parent, SWT.NONE);
		lblLibraries.setText("Libraries");

		Label lblFeatures = new Label(parent, SWT.NONE);
		lblFeatures.setText("Features");

		Label lblDescription = new Label(parent, SWT.NONE);
		lblDescription.setText("Description");

		final ListViewer libListViewer = new ListViewer(parent, SWT.BORDER | SWT.V_SCROLL);

		libListViewer.getControl().setLayoutData(new GridData(GridData.FILL_BOTH));
		libListViewer.setContentProvider(new IStructuredContentProvider(){

			@Override
			public void dispose() {
				// TODO Auto-generated method stub

			}

			@Override
			public void inputChanged(Viewer viewer, Object oldInput,
					Object newInput) {
				// TODO Auto-generated method stub

			}

			@Override
			public Object[] getElements(Object inputElement) {
				ArrayList mLibList = (ArrayList) inputElement;
				return mLibList.toArray();

			}

		});

		libListViewer.setLabelProvider(new LabelProvider(){
			public String getText(Object element){
				MockLibrary mLib = (MockLibrary) element;
				return mLib.getLibName();
			}

		});

		final ListViewer featListViewer = new ListViewer(parent, SWT.BORDER | SWT.V_SCROLL);

		featListViewer.getControl().setLayoutData(new GridData(GridData.FILL_BOTH));
		featListViewer.setContentProvider(new IStructuredContentProvider() {

			@Override
			public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
				// TODO Auto-generated method stub

			}

			@Override
			public void dispose() {
				// TODO Auto-generated method stub

			}

			@Override
			public Object[] getElements(Object inputElement) {
				Map<String,String> featsAndDescriptions = (Map<String,String>) inputElement;
				return featsAndDescriptions.keySet().toArray();
			}
		});

		featListViewer.setLabelProvider(new LabelProvider(){
			public String getText(Object element){
				return (String) element;
			}
		});

		final ListViewer dListViewer = new ListViewer(parent, SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL);

		dListViewer.getControl().setLayoutData(new GridData(GridData.FILL_BOTH));
		dListViewer.setContentProvider(new IStructuredContentProvider() {

			@Override
			public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
				// TODO Auto-generated method stub

			}

			@Override
			public void dispose() {
				// TODO Auto-generated method stub

			}

			@Override
			public Object[] getElements(Object inputElement) {
				String description = (String) inputElement;
				return new String[] {description};
			}
		});

		Button btnRunAnalysis = new Button(parent, SWT.NONE);
		btnRunAnalysis.setText("Run Analysis");

		final ArrayList<MockLibrary> demoLibs = MockLibrary.generateDemoLibs();
		libListViewer.setInput(demoLibs);
		libListViewer.addSelectionChangedListener(new ISelectionChangedListener(){

			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				IStructuredSelection selection = (IStructuredSelection) event.getSelection();
				if (selection.size() > 0){
					MockLibrary selectedLib = (MockLibrary) selection.getFirstElement();
					featListViewer.setInput(selectedLib.getFeatureFunctionMap());
				}

			}

		});

		featListViewer.addSelectionChangedListener(new ISelectionChangedListener(){

			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				IStructuredSelection selection = (IStructuredSelection) event.getSelection();
				if (selection.size() > 0){
					String feature = (String) selection.getFirstElement();
					IStructuredSelection libSelection = (IStructuredSelection) libListViewer.getSelection();
					MockLibrary selectedLibrary = (MockLibrary) libSelection.getFirstElement();
					dListViewer.setInput(selectedLibrary.getFeatureFunctionMap().get(feature));
				}

			}

		});
	}

	@Focus
	public void onFocus() {}
}