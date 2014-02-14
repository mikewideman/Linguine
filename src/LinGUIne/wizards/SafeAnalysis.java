package LinGUIne.wizards;

import java.util.Collection;
import java.util.LinkedList;

import org.eclipse.core.runtime.ISafeRunnable;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Shell;

import LinGUIne.extensions.IAnalysisPlugin;
import LinGUIne.model.IProjectData;
import LinGUIne.model.Project;
import LinGUIne.model.Result;

public class SafeAnalysis implements ISafeRunnable {

	private Shell shell;
	private IAnalysisPlugin analysisPlugin;
	private Collection<IProjectData> sourceData;
	private Project destProject;
	
	/**
	 * 
	 * 
	 * @param theShell	The current Shell; used to display error dialogs.
	 * @param analysis	The IAnalysisPlugin to be used for the analysis job.
	 * @param sources	The IProjectData that are to be analyzed.
	 * @param proj		The Project into which the Analysis Result(s) are to
	 * 					be placed.
	 */
	public SafeAnalysis(Shell theShell, IAnalysisPlugin analysis,
			Collection<IProjectData> sources, Project proj){
		
		shell = theShell;
		analysisPlugin = analysis;
		sourceData = sources;
		destProject = proj;
	}
	
	/**
	 * Raises an error dialog in the event of an exception during execution.
	 */
	@Override
	public void handleException(Throwable exception) {
		MessageDialog.open(SWT.OK, shell, "Error",
				"An error occurred while performing analysis.", SWT.NONE);
	}

	@Override
	public void run() throws Exception {
		//TODO: IAnalysisPlugin.runAnalysis should take a Collection<IProjectData> parameter and return a Collection<Result>
		//User may want to analyze more than one file at a time and an analysis may want to return more than one result
		Collection<Result> results = new LinkedList<Result>();
//		results.addAll(analysisPlugin.runAnalysis(sourceData));
		results.add(analysisPlugin.runAnalysis());
		
		for(Result result: results){
			destProject.addResult(result, sourceData);
		}
		
		//TODO: Error handling/liveness
	}
}
