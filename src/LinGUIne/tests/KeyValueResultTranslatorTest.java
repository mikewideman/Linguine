package LinGUIne.tests;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.HashMap;

import org.junit.BeforeClass;
import org.junit.Test;

import LinGUIne.model.KeyValueResultContents;
import LinGUIne.model.KeyValueResultTranslator;
import LinGUIne.model.ResultData;

public class KeyValueResultTranslatorTest {

	static String jsonContent;
	
	@BeforeClass
	public static void setUpBeforeClass(){
		String workingDir = System.getProperty("user.dir");
		String testDataDir = workingDir + "/testdata/";
		String testFilePath = testDataDir + "TestKeyValueResult.json";
		
		try(BufferedReader reader = Files.newBufferedReader(new File(testFilePath).toPath(),
				Charset.defaultCharset())){
			
			jsonContent = "";
			
			while(reader.ready()){
				jsonContent += reader.readLine() + "\n";
			}
		}
		catch(IOException e) {
			fail();
		}
	}
	
	@Test
	public void testFromJson(){
		KeyValueResultContents contents = KeyValueResultTranslator.fromJson(
				jsonContent);
		
		assertNotNull("Translator should never return null for valid "
				+ "KeyValueResult json file.", contents);
	}
	
	@Test
	public void testToJson(){
		KeyValueResultContents contents = new KeyValueResultContents();
		
		HashMap<String, ResultData> firstEntry = new HashMap<String, ResultData>();
		HashMap<String, ResultData> secondEntry = new HashMap<String, ResultData>();
		HashMap<String, ResultData> thirdEntry = new HashMap<String, ResultData>();
		
		firstEntry.put("word", new ResultData("the"));
		firstEntry.put("count", new ResultData(10));
		
		secondEntry.put("word", new ResultData("linguine"));
		secondEntry.put("count", new ResultData(7));

		thirdEntry.put("word", new ResultData("test"));
		thirdEntry.put("count", new ResultData(5));
		
		contents.addKeyValuePairs(firstEntry);
		contents.addKeyValuePairs(secondEntry);
		contents.addKeyValuePairs(thirdEntry);
		
		String newJsonContent = KeyValueResultTranslator.toJson(contents);
		
		assertNotNull("Translator should never return null Json.",
				newJsonContent);
		assertEquals("Json returned by translator should be identical to Json "
				+ "describing the same content.", jsonContent.trim(),
				newJsonContent.trim());
	}
}
