package LinGUIne.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Generalized format for multiple result types.
 * 
 * @author Peter Dimou
 */
public class KeyValueResultContents implements IProjectDataContents {
	
	private List<HashMap<String, ResultData>> contents;
	
	/**
	 * Constructs an empty set of data
	 */
	public KeyValueResultContents() {
		contents = new ArrayList<HashMap<String, ResultData>>();
	}
	
	/**
	 * Constructs a new result object from a previously constructed set of data
	 * 
	 * @param contents The previously constructed set of data
	 */
	public KeyValueResultContents(List<HashMap<String, ResultData>> contents) {
		this.contents = new ArrayList<HashMap<String, ResultData>>(contents);
	}

	@Override
	public int compareTo(IProjectDataContents arg0) {
		// TODO Determine a valid comparison, if any
		return 0;
	}

	/**
	 * Deep-copies this object
	 */
	@Override
	public IProjectDataContents copy() {
		return new KeyValueResultContents(contents);
	}

}
