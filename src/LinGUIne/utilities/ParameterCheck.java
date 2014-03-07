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
	
	/**
	 * Throws and IllegalArgumentException if parameter is equal to zero (0).
	 * 
	 * @param parameter	The parameter whose value is to be checked.
	 * @param paramName	Name of the checked parameter; used in the Exception
	 * 					message.
	 */
	public static void notZero(Number parameter, String paramName){
		boolean isZero = false;
		
		if(parameter instanceof Long || parameter instanceof Integer ||
				parameter instanceof Short || parameter instanceof Byte){
			
			isZero = parameter.longValue() == 0;
		}
		else if(parameter instanceof Double || parameter instanceof Float){
			isZero = parameter.doubleValue() == 0.0;
		}
		
		if(isZero){
			throw new IllegalArgumentException("Parameter '" + paramName +
					"' must not be zero.");
		}
	}
}
