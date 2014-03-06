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
	public Class getType() {
		return data.getClass();
	}
	
	/**
	 * Returns the data's toString method.
	 *  
	 * @return The object's toString method.
	 */
	public String getAsString() {
		return data.toString();
	}
	
	/**
	 * Returns the object as an Integer iff the held data is of type Integer.
	 * 
	 * @return The object as an Integer
	 */
	public Integer getAsInteger() {
		if(data.getClass().equals(Integer.class)) {
			return (Integer)data;
		} else {
			return null;
		}
	}

	/**
	 * Returns the object as a Double iff the held data is of type Double.
	 * 
	 * @return The object as an Double
	 */
	public Double getAsDouble() {
		if(data.getClass().equals(Integer.class)) {
			return (Double)data;
		} else {
			return null;
		}
	}
}
