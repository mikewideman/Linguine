package LinGUIne.model;

/**
 * Represents IProjectDataContents that can be reasonably viewed as plain text
 * and has no other view implemented.
 * 
 * @author Kyle Mullins
 */
public interface IPlaintextViewable {

	/**
	 * Returns a plain text version of the contents of the object to be
	 * displayed to the user.
	 * 
	 * @return	Plain text String representation of the contents.
	 */
	public String getAsPlaintext();
}
