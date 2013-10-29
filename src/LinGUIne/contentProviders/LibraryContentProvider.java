package LinGUIne.contentProviders;

import java.util.ArrayList;
import java.util.List;

import LinGUIne.model.MockLibrary;

public enum LibraryContentProvider {
	INSTANCE;
	private List<MockLibrary> libraries;
	
	private LibraryContentProvider(){
		MockLibrary stan = new MockLibrary();
		stan.setLibName("Stanford");
		libraries.add(stan);
	}
	
	public List<MockLibrary> getLibraries(){
		return libraries;
	}
}
