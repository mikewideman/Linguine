package LinGUIne.utilities;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;

public class ClassUtils {

	private static BundleContext context;
	
	public static void setBundleContext(BundleContext bundleContext){
		context = bundleContext;
		
//		System.out.println("Uninstalled = " + Bundle.UNINSTALLED);
//		System.out.println("Installed = " + Bundle.INSTALLED);
//		System.out.println("Resolved = " + Bundle.RESOLVED);
//		System.out.println("Starting = " + Bundle.STARTING);
//		System.out.println("Stopping = " + Bundle.STOPPING);
//		System.out.println("Active = " + Bundle.ACTIVE);
//		
//		for(Bundle bundle: context.getBundles()){
//			System.out.println(bundle.getSymbolicName() + ": " + bundle.getState());
//		}
	}
	
	public static String serializeClassName(Class<?> clazz){
//		return getBundleIdForClass(clazz) + "-" + clazz.getCanonicalName();
		return getBundleNameForClass(clazz) + "-" + clazz.getCanonicalName();
//		return clazz.getCanonicalName();
	}
	
	public static Class<?> deserializeClassName(String className)
			throws ClassNotFoundException{
//		long bundleId = Long.parseLong(className.split("-")[0]);
		String bundleName = className.split("-")[0];
		className = className.split("-")[1];
		
//		return loadClassFromBundle(bundleId, className);
		return loadClassFromBundle(bundleName, className);
//		return Class.forName(className);
	}
	
	public static Class<?> loadClassFromBundle(long bundleId, String className) 
			throws ClassNotFoundException{
		
		Bundle bundle = context.getBundle(bundleId);
		
		return bundle.loadClass(className);
	}
	
	public static Class<?> loadClassFromBundle(String bundleName,
			String className) throws ClassNotFoundException{
		
		for(Bundle bundle: context.getBundles()){
			if(bundle.getSymbolicName().equals(bundleName)){
				return bundle.loadClass(className);
			}
		}
		
		return null;
	}
	
	public static long getBundleIdForClass(Class<?> clazz){
		return FrameworkUtil.getBundle(clazz).getBundleId();
	}
	
	public static String getBundleNameForClass(Class<?> clazz){
		return FrameworkUtil.getBundle(clazz).getSymbolicName();
	}
}
