package LinGUIne.utilities;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;

/**
 * Utilities for serializing/deserializing Classes to/from Strings.
 * 
 * @author Kyle Mullins
 */
public class ClassUtils {

	private static BundleContext context;
	
	/**
	 * Sets the BundleContext for the application.
	 * 
	 * @param bundleContext	The BundleContext.
	 */
	public static void setBundleContext(BundleContext bundleContext){
		context = bundleContext;
	}
	
	/**
	 * Serializes the given Class name to String.
	 * 
	 * @param clazz	The Class to be serialized.
	 * 
	 * @return	The Stringified Class name.
	 */
	public static String serializeClassName(Class<?> clazz){
		return getBundleNameForClass(clazz) + "-" + clazz.getCanonicalName();
	}
	
	/**
	 * Deserializes the given String into a Class.
	 * 
	 * @param className	Stringified Class name to be deserialized.
	 * 
	 * @return	Class object that was deserialized from the Class name String.
	 * 
	 * @throws ClassNotFoundException	If the Class could not be loaded.
	 */
	public static Class<?> deserializeClassName(String className)
			throws ClassNotFoundException{
		String bundleName = className.split("-")[0];
		className = className.split("-")[1];
		
		return loadClassFromBundle(bundleName, className);
	}
	
	/**
	 * Loads the Class of the given name from the Bundle with the given Id.
	 * 
	 * @param bundleId	Id of the Bundle containing the Class.
	 * @param className	Name of the Class to load.
	 * 
	 * @return	The loaded Class.
	 * 
	 * @throws ClassNotFoundException	If the Class could not be loaded from
	 * 									the specified Bundle.
	 */
	public static Class<?> loadClassFromBundle(long bundleId, String className) 
			throws ClassNotFoundException{
		
		Bundle bundle = context.getBundle(bundleId);
		
		return bundle.loadClass(className);
	}
	
	/**
	 * Loads the Class of the given name from the Bundle with the given name.
	 * 
	 * @param bundleName	Name of the Bundle containing the Class.
	 * @param className		Name of the Class to load.
	 * 
	 * @return	The loaded Class.
	 * 
	 * @throws ClassNotFoundException	If the Class could not be loaded from
	 * 									the specified Bundle.
	 */
	public static Class<?> loadClassFromBundle(String bundleName,
			String className) throws ClassNotFoundException{
		
		for(Bundle bundle: context.getBundles()){
			if(bundle.getSymbolicName().equals(bundleName)){
				return bundle.loadClass(className);
			}
		}
		
		return null;
	}
	
	/**
	 * Returns the Id of the Bundle containing the given Class.
	 * 
	 * @param clazz	The Class whose Bundle is to be returned.
	 * 
	 * @return	Id of the Bundle with the given Class.
	 */
	public static long getBundleIdForClass(Class<?> clazz){
		return FrameworkUtil.getBundle(clazz).getBundleId();
	}
	
	/**
	 * Returns the Name of the Bundle containing the given Class.
	 * 
	 * @param clazz	The Class whose Bundle is to be returned.
	 * 
	 * @return	Name of the Bundle with the given Class.
	 */
	public static String getBundleNameForClass(Class<?> clazz){
		return FrameworkUtil.getBundle(clazz).getSymbolicName();
	}
}
