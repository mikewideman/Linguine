package LinGUIne.utilities;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;

public class ClassUtils {

	private static BundleContext context;
	
	public static void setBundleContext(BundleContext bundleContext){
		context = bundleContext;
	}
	
	public static String serializeClassName(Class<?> clazz){
		return getBundleIdForClass(clazz) + "-" + clazz.getCanonicalName();
	}
	
	public static Class<?> deserializeClassName(String className)
			throws ClassNotFoundException{
		
		long bundleId = Long.parseLong(className.split("-")[0]);
		className = className.split("-")[1];
		
		return loadClassFromBundle(bundleId, className);
	}
	
	public static Class<?> loadClassFromBundle(long bundleId, String className) 
			throws ClassNotFoundException{
		
		Bundle bundle = context.getBundle(bundleId);
		
		return bundle.loadClass(className);
	}
	
	public static long getBundleIdForClass(Class<?> clazz){
		return FrameworkUtil.getBundle(clazz).getBundleId();
	}
}
