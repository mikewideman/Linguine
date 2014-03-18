package LinGUIne.model;

/**
 * Generic data node that is used in KeyValueResult objects.
 * 
 * @author Peter Dimou
 */
public class ResultData {
	
	private Object data;
	
	/**
	 * Constructs the object with a generic piece of data.
	 * 
	 * @param data The data that should be held by this object.
	 */
	public ResultData(Object data) {
		this.data = data;
	}
	
	/**
	 * Returns the type of the data this object is holding.
	 * 
	 * @return The Class of the data this object is holding.
	 */
	public Class<?> getType() {
		return data.getClass();
	}
	
	/**
	 * Returns the data as a String.
	 *  
	 * @return The stringified version of the data.
	 */
	public String getAsString() {
		return data.toString();
	}
	
	/**
	 * Returns the data as an Integer iff it is of type Integer.
	 * 
	 * @return The data as an Integer or null if it is not an Integer.
	 */
	public Integer getAsInteger() {
		if(data instanceof Integer) {
			return (Integer)data;
		}
		else {
			return null;
		}
	}

	/**
	 * Returns the data as a Double iff it is of type Double.
	 * 
	 * @return The data as an Double or null if it is not a Double.
	 */
	public Double getAsDouble() {
		if(data instanceof Double) {
			return (Double)data;
		}
		else {
			return null;
		}
	}
}
