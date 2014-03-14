package LinGUIne.model;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;

/**
 * Represents the Result of some analysis on some ProjectData.
 * 
 * @author Kyle Mullins
 */
public abstract class Result implements IProjectData {

	protected File resultFile;
	
	@Override
	public abstract IProjectDataContents getContents();

	@Override
	public abstract boolean updateContents(IProjectDataContents newContents);
	
	/**
	 * Creates a new Result for the given File.
	 * Note: All Result subclasses must provide this constructor.
	 */
	protected Result(File file){
		resultFile = file;
	}
	
	public File getFile() {
		return resultFile;
	}

	@Override
	public String getName() {
		return resultFile.getName();
	}
	
	@Override
	public void deleteContentsOnDisk() throws IOException{
		Files.deleteIfExists(resultFile.toPath());
		updateContents(null);
	}
	
	@Override
	public int compareTo(IProjectData projData) {
		if(projData == null){
			return 1;
		}
		
		return resultFile.compareTo(projData.getFile());
	}

	/**
	 * Creates a new Result of the given subclass and passes it the given
	 * resultFile.
	 * 
	 * @param resultType	The subclass of Result that is to be created.
	 * @param resultFile	The File object to be passed to the constructor.
	 * 
	 * @return	A newly created instance of the given Result subtype.
	 * 
	 * @throws IllegalArgumentException	If the given Result subclass does not
	 * 									provide a 1-argument constructor taking
	 * 									a File parameter.
	 */
	public static <T extends Result> T createResult(Class<T> resultType,
			File resultFile){
		T newInstance;
		
		try {
			newInstance = resultType.getDeclaredConstructor(File.class).
					newInstance(resultFile);
		}
		catch(InstantiationException | IllegalAccessException
				| IllegalArgumentException | InvocationTargetException
				| NoSuchMethodException | SecurityException e) {
			throw new IllegalArgumentException("Invalid Result Type provided: "
					+ "must implement a 1-argument constructor taking a File");
		}
		
		return newInstance;
	}
}
