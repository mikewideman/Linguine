package LinGUIne.model;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;

/**
 * Represents a result that's data is formatted in a typical key and value 
 * fashion.
 * 
 * @author Peter Dimou
 * @author Kyle Mullins
 */
public class KeyValueResult extends Result {

	private KeyValueResultContents contents;
	
	/**
	 * Constructs the result with a file. Does not read the file at this stage.
	 * Only when the contents are accessed using the getContents method will
	 * the disk be read.
	 * 
	 * @param result	The file from which to construct the results.
	 */
	public KeyValueResult(File result) {
		super(result);
		contents = null;
	}

	@Override
	public IProjectDataContents getContents() {
		if(contents == null){
			try(BufferedReader reader = Files.newBufferedReader(
					resultFile.toPath(), Charset.defaultCharset())){
				
				String jsonStr = "";
				
				while(reader.ready()){
					 jsonStr += reader.readLine();
					 jsonStr += "\n";
				}
				
				KeyValueResultContents newContents = KeyValueResultTranslator.
						fromJson(jsonStr);
				
				if(newContents != null){
					contents = newContents;
				}
				else{
					//TODO: Throw an exception of some sort
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
		else if(newContents instanceof KeyValueResultContents){
			KeyValueResultContents newKeyValueContents =
					(KeyValueResultContents)newContents;
			
			if(contents == null || contents.compareTo(newKeyValueContents) != 0){
				try(BufferedWriter writer = Files.newBufferedWriter(
						resultFile.toPath(), Charset.defaultCharset())){
					
					String jsonStr = KeyValueResultTranslator.toJson(
							newKeyValueContents);
					
					if(jsonStr != null){
						writer.write(jsonStr);
					}
					else{
						//TODO: Throw and exception of some sort
						return false;
					}
				}
				catch(IOException e) {
					return false;
				}
			}
			
			contents = (KeyValueResultContents)newKeyValueContents.copy();
			
			return true;
		}
		
		return false;
	}
}
