package LinGUIne.model;

import java.io.File;

public class VisualizationResult extends Result {

	public VisualizationResult(File file) {
		super(file);
	}

	@Override
	public IProjectDataContents getContents() {
		return null;
	}

	@Override
	public boolean updateContents(IProjectDataContents newContents) {
		return false;
	}
}
