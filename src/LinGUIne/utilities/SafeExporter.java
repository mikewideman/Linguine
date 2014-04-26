package LinGUIne.utilities;

import java.io.File;
import java.util.Collection;

import org.eclipse.core.runtime.ISafeRunnable;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Shell;

import LinGUIne.extensions.IFileExporter;
import LinGUIne.model.IProjectData;
import LinGUIne.model.Result;

public class SafeExporter implements ISafeRunnable {
	private Shell shell;
	private IFileExporter fileExporter;
	private Result sourceResult;
	private Collection<IProjectData> associatedProjectData;
	private File destFile;

	/**
	 * Creates a new SafeExporter using the given exporter, exporting the given
	 * Result into the given destination File.
	 * 
	 * @param theShell		The current Shell; used to display error dialogs.
	 * @param exporter		The IFileExporter to be used for the export job.
	 * @param result		The Result that is to be exported at this time.
	 * @param associated	Any data associated with the Result to be exported.
	 * @param dest			The File into which the exported data is to be
	 * 						placed.
	 */
	public SafeExporter(Shell theShell, IFileExporter exporter, Result result,
			Collection<IProjectData> associated, File dest) {
		
		shell = theShell;
		fileExporter = exporter;
		sourceResult = result;
		associatedProjectData = associated;
		destFile = dest;
	}

	/**
	 * Raises an error dialog in the event of an exception during execution.
	 */
	@Override
	public void handleException(Throwable exception) {
		MessageDialog.open(SWT.OK, shell, "Error",
				"An error occurred while exporting Result.", SWT.NONE);
	}

	/**
	 * Exports the Result into destFile using fileExporter.
	 */
	@Override
	public void run() throws Exception {
		fileExporter.exportResult(sourceResult, associatedProjectData,
				destFile);
	}
}
