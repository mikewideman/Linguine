package LinGUIne.wizards;

import java.util.LinkedList;
import java.util.List;

import LinGUIne.extensions.IAnalysisPlugin;
import LinGUIne.model.IProjectData;
import LinGUIne.model.Project;

/**
 * Data object used to carry data between pages in the AnalysisWizard.
 * 
 * @author Kyle Mullins
 */
public class AnalysisData {
	private Project chosenProject;
	private List<IProjectData> chosenProjectData;
	private IAnalysisPlugin chosenAnalysis;
	
	/**
	 * Creates a default empty instance.
	 */
	public AnalysisData(){
		chosenProject = null;
		chosenProjectData = new LinkedList<IProjectData>();
	}

	/**
	 * Sets the Project that was chosen.
	 */
	public void setChosenProject(Project chosenProj) {
		chosenProject = chosenProj;
	}

	/**
	 * Returns the currently chosen Project.
	 */
	public Project getChosenProject() {
		return chosenProject;
	}

	/**
	 * Returns a list of the currently chosen ProjectData (if any).
	 */
	public List<IProjectData> getChosenProjectData() {
		return chosenProjectData;
	}

	/**
	 * Sets the currently chosen ProjectData to the given list.
	 * List may be empty.
	 */
	public void setChosenProjectData(List<IProjectData> chosenData) {
		chosenProjectData = chosenData;
	}
	
	/**
	 * Returns the currently chosen AnalysisPlugin.
	 */
	public IAnalysisPlugin getChosenAnalysis(){
		return chosenAnalysis;
	}
	
	/**
	 * Sets the currently chosen AnalysisPlugin to the given one.
	 */
	public void setChosenAnalysis(IAnalysisPlugin analysis){
		chosenAnalysis = analysis;
	}
}
