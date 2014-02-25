package LinGUIne.model;

/**
 * Simple interface to allow IProjectData subclasses to return typed
 * IProjectDataContents from the getContents function.
 * 
 * @author Kyle Mullins
 */
public interface ITypedProjectData<T extends IProjectDataContents> extends
	IProjectData {
	
	/**
	 * Typed override of the getContents function.
	 */
	@Override
	T getContents();
}
