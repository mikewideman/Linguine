package LinGUIne.extensions;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;

import LinGUIne.model.IProjectDataContents;
import LinGUIne.model.TextDataContents;

public class PlaintextImporter implements IFileImporter {

	@Override
	public String getFileType() {
		return "Plaintext";
	}

	@Override
	public String getFileMask() {
		return "*.txt";
	}

	@Override
	public IProjectDataContents importFile(File rawFile) {
		try(BufferedReader reader = Files.newBufferedReader(rawFile.toPath(),
				Charset.defaultCharset())){
			
			String contentStr = "";
			
			while(reader.ready()){
				 contentStr += reader.readLine();
				 contentStr += "\n";
			}
			
			return new TextDataContents(contentStr);
		}
		catch (IOException e) {
			return null;
		}
	}

}
