package LinGUIne.extensions;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map.Entry;

import LinGUIne.model.IProjectData;
import LinGUIne.model.KeyValueResult;
import LinGUIne.model.KeyValueResultContents;
import LinGUIne.model.Result;
import LinGUIne.model.ResultData;

/**
 * Exporter to create an XML file from a KeyValueResult.
 * Keys are XML tags surrounding their associated Values.
 * 
 * @author Kyle Mullins
 */
public class XMLExporter implements IFileExporter {

	@Override
	public String getFileType() {
		return "Extensible Markup Language (XML)";
	}

	@Override
	public String getFileMask() {
		return "*.xml";
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
			
			String xmlContent = "";
			
			for(HashMap<String, ResultData> pairs: sourceContents){
				for(Entry<String, ResultData> pair: pairs.entrySet()){
					xmlContent += "<" + pair.getKey() + ">" +
							pair.getValue().getAsString() + "</" + pair.getKey()
							+ ">";
				}
				
				xmlContent += "\n";
			}
			
			try(BufferedWriter writer = Files.newBufferedWriter(
					destFile.toPath(), Charset.defaultCharset())){
				
				writer.write(xmlContent);
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
