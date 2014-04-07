package LinGUIne.extensions;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;

import LinGUIne.model.IProjectData;
import LinGUIne.model.KeyValueResult;
import LinGUIne.model.KeyValueResultContents;
import LinGUIne.model.Result;
import LinGUIne.model.ResultData;

/**
 * Simple exporter for KeyValueResults that produces CSV files.
 * 
 * @author Kyle Mullins
 */
public class CSVExporter implements IFileExporter {

	@Override
	public String getFileType() {
		return "Comma Separated Values (CSV)";
	}

	@Override
	public Collection<Class<? extends Result>> getSupportedSourceDataTypes() {
		LinkedList<Class<? extends Result>> supportedTypes =
				new LinkedList<Class<? extends Result>>();
		
		supportedTypes.add(KeyValueResult.class);
		
		return supportedTypes;
	}

	@Override
	public void exportResult(Result sourceResult,
			Collection<IProjectData> associatedData, File destFile)
			throws IOException {
		
		if(sourceResult instanceof KeyValueResult){
			KeyValueResult keyValueSource = (KeyValueResult)sourceResult;
			KeyValueResultContents sourceContents = 
					(KeyValueResultContents)keyValueSource.getContents();
			
			String csvContent = "";
			
			//Set up header
			for(String key: sourceContents.getKeys()){
				csvContent += key + ",";
			}
			
			csvContent.substring(0, csvContent.length() - 2);
			csvContent += "\n";
			
			//Write out each entry's value for each key
			for(HashMap<String, ResultData> pairs: sourceContents){
				for(String key: sourceContents.getKeys()){
					csvContent += pairs.get(key).getAsString() + ",";
				}
				
				csvContent.substring(0, csvContent.length() - 2);
				csvContent += "\n";
			}
			
			try(BufferedWriter writer = Files.newBufferedWriter(
					destFile.toPath(), Charset.defaultCharset())){
				
				writer.write(csvContent);
			}
			catch(IOException ioe){
				//Catch and rethrow the exception so the twr can close the stream
				throw ioe;
			}
		}
		else{
			throw new IllegalArgumentException("Parameter sourceResult must be"
					+ " of type KeyValueResult");
		}
	}
}
