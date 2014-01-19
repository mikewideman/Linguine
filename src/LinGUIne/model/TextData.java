package LinGUIne.model;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.StandardOpenOption;
import java.util.List;

/**
 * Represents text-based ProjectData.
 * 
 * @author Kyle Mullins
 */
public class TextData implements IProjectData {

	private File dataFile;
	private TextDataContents contents;
	
	public TextData(File textFile){
		dataFile = textFile;
		contents = null;
	}
	
	public File getFile() {
		return dataFile;
	}

	@Override
	public IProjectDataContents getContents() {
		if(contents == null){
			try(BufferedReader reader = Files.newBufferedReader(dataFile.toPath(),
					Charset.defaultCharset())){
				
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
		
		return contents;
	}

	@Override
	public boolean updateContents(IProjectDataContents newContents) {
		if(newContents instanceof TextDataContents){
			TextDataContents newTextContents = (TextDataContents)newContents;
			
			if(newTextContents.getText() != null){
				try(BufferedWriter writer = Files.newBufferedWriter(dataFile.toPath(),
							Charset.defaultCharset(), StandardOpenOption.TRUNCATE_EXISTING)){
					
					writer.write(newTextContents.getText());
				}
				catch(IOException e){
					return false;
				}
				
				contents = newTextContents;
				
				return true;
			}
		}
		
		return false;
	}
	
	@Override
	public int compareTo(IProjectData projData) {
		if(projData == null){
			return 1;
		}
		
		return dataFile.compareTo(projData.getFile());
	}
}
