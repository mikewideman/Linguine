package LinGUIne.model;

/**
 * A special case of ProjectGroup for top-level groups which cannot have parents
 * and are allowed to have display names which are different from the on-disk
 * folder name.
 * 
 * @author Kyle Mullins
 */
public class RootProjectGroup extends ProjectGroup {
	
	private String groupDisplayName;
	private boolean isHidden;
	
	/**
	 * Creates a new RootProjectGroup with the given name used as both the
	 * display name and actual name.
	 * 
	 * @param name	Both the display name and actual name of the new group.
	 */
	public RootProjectGroup(String name) {
		super(name);
		
		groupDisplayName = name;
		isHidden = false;
	}

	/**
	 * Creates a new RootProjectGroup with the given display name and actual
	 * names.
	 * 
	 * @param displayName	The name which should be displayed to the user for
	 * 						this group.
	 * @param actualName	The actual name of the folder on-disk for this
	 * 						group.
	 */
	public RootProjectGroup(String displayName, String actualName){
		this(actualName);
		
		groupDisplayName = displayName;
	}
	
	@Override
	public String getName(){
		return groupDisplayName;
	}
	
	/**
	 * Sets whether or not this RootProjectGroup should be hidden from the user.
	 */
	public void setHidden(boolean hidden){
		isHidden = hidden;
	}
	
	/**
	 * Returns whether or not this RootProjectGroup is to be hidden from the
	 * user.
	 */
	public boolean isHidden(){
		return isHidden;
	}
	
	/*
	 * Unsupported Operations
	 */
	
	/**
	 * Unsupported for RootProjectGroups.
	 */
	@Override
	public void setName(String newName){
		throw new UnsupportedOperationException();
	}
	
	/**
	 * Unsupported for RootProjectGroups.
	 */
	@Override
	public void setParent(ProjectGroup parent){
		throw new UnsupportedOperationException();
	}
	
	/**
	 * Unsupported for RootProjectGroups.
	 */
	@Override
	public void removeParent(){
		throw new UnsupportedOperationException();
	}
}
