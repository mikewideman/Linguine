package LinGUIne.tests;

import java.io.File;

import LinGUIne.model.IProjectDataContents;
import LinGUIne.model.Result;

public class MockResult extends Result {

	public MockResult(File file) {
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
