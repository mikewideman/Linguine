package LinGUIne.model;

import java.io.File;

/**
 * Represents a result that's data is formatted in a typical key and value 
 * fashion.
 * 
 * @author Peter Dimou
 */
public class KeyValueResult extends Result {

	private KeyValueResultContents contents;
	
	/**
	 * Constructs the result with a file. Does not read the file at this stage.
	 * Only when the contents are accessed using the getContents method will
	 * the disk be read.
	 * 
	 * @param result The file to construct the results from
	 */
	public KeyValueResult(File result) {
		super(result);
		contents = null;
	}

	@Override
	public IProjectDataContents getContents() {
		// TODO Figure out the on-disk format and then implement the
		// appropriate logic
		return null;
	}

	@Override
	public boolean updateContents(IProjectDataContents newContents) {
		// TODO Auto-generated method stub
		return false;
	}

}
