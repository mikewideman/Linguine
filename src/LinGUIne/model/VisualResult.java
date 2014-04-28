package LinGUIne.model;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.charset.Charset;
import java.nio.file.Files;

import LinGUIne.utilities.ClassUtils;

/**
 * Single Result type for all Visualizations which should be saved. Supports all
 * contents objects which extend VisualResultContents.
 * 
 * @author Kyle Mullins
 */
public class VisualResult extends Result {

	private VisualResultContents contents;
	
	public VisualResult(File file) {
		super(file);
	}

	@Override
	public IProjectDataContents getContents() {
		if(contents == null){
			try(BufferedReader reader = Files.newBufferedReader(
					resultFile.toPath(), Charset.defaultCharset())){
				
				VisualResultContents newContents = null;
				
				if(reader.ready()){
					String className = reader.readLine();
					
					try {
						Class<?> clazz = ClassUtils.deserializeClassName(className);
						newContents = (VisualResultContents)clazz.
								getDeclaredConstructor().newInstance();
						
					}
					catch(InstantiationException | IllegalAccessException
							| IllegalArgumentException
							| InvocationTargetException | NoSuchMethodException
							| SecurityException | ClassNotFoundException e) {
						e.printStackTrace();
						
						return null;
					}
				}
				
				if(newContents != null && newContents.parse(reader)){
					contents = newContents;
				}
				else{
					return null;
				}
			}
			catch(IOException e){
				return null;
			}
		}
		
		return contents.copy();
	}

	@Override
	public boolean updateContents(IProjectDataContents newContents) {
		if(newContents == null){
			contents = null;
		}
		else if(newContents instanceof VisualResultContents){
			VisualResultContents newVisualContents =
					(VisualResultContents)newContents;
			
			if(contents == null || contents.compareTo(newVisualContents) != 0){
				try(BufferedWriter writer = Files.newBufferedWriter(
						resultFile.toPath(), Charset.defaultCharset())){
					
					writer.write(ClassUtils.serializeClassName(newVisualContents.getClass()) + "\n");
					
					if(!newVisualContents.compose(writer)){
						return false;
					}
				}
				catch(IOException e) {
					return false;
				}
			}
			
			contents = (VisualResultContents)newVisualContents.copy();
			
			return true;
		}
		
		return false;
	}
}
