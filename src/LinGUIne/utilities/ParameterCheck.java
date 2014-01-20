package LinGUIne.utilities;

/**
 * Utility class for validating function parameters in a uniform way.
 * 
 * @author Kyle Mullins
 */
public class ParameterCheck {
	/**
	 * Throws an IllegalArgumentException if parameter is null.
	 * 
	 * @param parameter	The parameter whose value is to be checked.
	 * @param paramName	Name of the checked parameter; used in the Exception
	 * 					message.
	 */
	public static void notNull(Object parameter, String paramName){
		if(parameter == null){
			throw new IllegalArgumentException("Parameter '" + paramName +
					"' must not be null.");
		}
	}
}
