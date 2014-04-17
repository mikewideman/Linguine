package LinGUIne.model;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;

import LinGUIne.utilities.ParameterCheck;

/**
 * Represents text-based ProjectData.
 * 
 * @author Kyle Mullins
 */
public class TextData implements ITypedProjectData<TextDataContents> {

	private File dataFile;
	private TextDataContents contents;
	
	/**
	 * Creates a new TextData object representing the given File.
	 * Note: textFile parameter must not be null.
	 * 
	 * @param textFile	The File in the workspace that this TextData represents.
	 */
	public TextData(File textFile){
		ParameterCheck.notNull(textFile, "textFile");
		
		dataFile = textFile;
		contents = null;
	}
	
	public File getFile() {
		return dataFile;
	}
	
	@Override
	public String getName() {
		return dataFile.getName();
	}

	/**
	 * Returns a TextDataContents associated with this TextData file, reading
	 * from disk if necessary.
	 * 
	 * @return	A copy of the TextDataContents object for this TextData or null
	 * 			if the file could not be read.
	 */
	@Override
	public TextDataContents getContents() {
		if(contents == null){
			try(BufferedReader reader = Files.newBufferedReader(dataFile.toPath(),
					Charset.forName("UTF-8"))){
				
				String contentStr = "";
				
				while(reader.ready()){
					 contentStr += reader.readLine();
					 contentStr += "\n";
				}
				
				contents = new TextDataContents(contentStr);
			}
			catch (IOException e) {
				return null;
			}
		}
		
		return (TextDataContents)contents.copy();
	}

	/**
	 * Updates this TextData with the new given TextDataContents and writes
	 * them to disk, overwriting the existing file contents.
	 * 
	 * @param newContents	TextDataContents instance to write to disk.
	 * 
	 * @return	Returns true iff newContents is an instance of TextDataContents
	 * 			and was written to disk successfully, false otherwise.
	 */
	@Override
	public boolean updateContents(IProjectDataContents newContents) {
		if(newContents != null && newContents instanceof TextDataContents){
			TextDataContents newTextContents = (TextDataContents)newContents;
			
			//Only write to disk if the two contents are different
			if(contents == null || contents.compareTo(newTextContents) != 0){
				Charset cs = Charset.defaultCharset();
				try(BufferedWriter writer = Files.newBufferedWriter(dataFile.toPath(),
							Charset.forName("UTF-8"))){
					
					writer.write(newTextContents.getText());
				}
				catch(IOException e){
					return false;
				}
			}
				
			contents = (TextDataContents)newTextContents.copy();
			
			return true;
		}
		
		return false;
	}
	
	@Override
	public void deleteContentsOnDisk() throws IOException{
		Files.deleteIfExists(dataFile.toPath());
		contents = null;
	}
	
	@Override
	public int compareTo(IProjectData projData) {
		if(projData == null){
			return 1;
		}
		
		return dataFile.compareTo(projData.getFile());
	}
}
