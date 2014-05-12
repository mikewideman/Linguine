package LinGUIne.utilities;

import java.io.File;

import org.eclipse.core.runtime.ISafeRunnable;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Shell;

import LinGUIne.extensions.IAnnotationExporter;
import LinGUIne.model.AnnotationSet;
import LinGUIne.model.IProjectData;

/**
 * Runnable which wraps the export of Annotations to an external File in a safe
 * fashion.
 * 
 * @author Kyle Mullins
 */
public class SafeAnnotationExporter implements ISafeRunnable {
	
	private Shell shell;
	private IAnnotationExporter annotationExporter;
	private AnnotationSet annotations;
	private IProjectData annotatedData;
	private File destFile;

	/**
	 * Creates a new SafeFileExporter using the given exporter, exporting the given
	 * Result into the given destination File.
	 * 
	 * @param theShell		The current Shell; used to display error dialogs.
	 * @param exporter		The IAnnotationExporter to be used for the export
	 * 						job.
	 * @param annots		The Annotations that are to be exported at this time.
	 * @param annotated		The Annotated Data to be exported.
	 * @param dest			The File into which the exported data is to be
	 * 						placed.
	 */
	public SafeAnnotationExporter(Shell theShell, IAnnotationExporter exporter,
			AnnotationSet annots, IProjectData annotated, File dest) {
		
		shell = theShell;
		annotationExporter = exporter;
		annotations = annots;
		annotatedData = annotated;
		destFile = dest;
	}

	/**
	 * Raises an error dialog in the event of an exception during execution.
	 */
	@Override
	public void handleException(Throwable exception) {
		MessageDialog.open(SWT.OK, shell, "Error",
				"An error occurred while exporting Annotations.", SWT.NONE);
	}

	/**
	 * Exports the Annotations into destFile using annotationExporter.
	 */
	@Override
	public void run() throws Exception {
		annotationExporter.exportAnnotation(annotatedData, annotations,
				destFile);
	}
}
