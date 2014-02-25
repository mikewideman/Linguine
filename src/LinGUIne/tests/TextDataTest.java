package LinGUIne.tests;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import LinGUIne.model.TextData;
import LinGUIne.model.TextDataContents;

public class TextDataTest {

	TextData data;
	static File testFile;
	static File sourceFile;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		String workingDir = System.getProperty("user.dir");
		String testDataDir = workingDir + "/testdata/";
		
		testFile = new File(testDataDir + "TestFile.txt");
		sourceFile = new File(testDataDir + "LoremIpsum.txt");
	}
	
	@Before
	public void setUp() throws Exception {
		Files.copy(sourceFile.toPath(), testFile.toPath());
		
		data = new TextData(testFile);
	}
	
	@After
	public void tearDown() throws Exception {
		Files.delete(testFile.toPath());
	}

	@Test
	public void testConstructor() {
		try{
			new TextData(null);
			fail("Constructor should not allow File parameter to be null.");
		}
		catch(IllegalArgumentException iae){}
	}
	
	@Test
	public void testGetFile() {
		assertTrue("File returned should be equal to the one passed during construction",
				data.getFile().equals(testFile));
	}

	@Test
	public void testGetName() {
		assertTrue("Name of TextData should be the name of its file.",
				data.getName().equals("TestFile.txt"));
	}

	@Test
	public void testGetContents() {
		String sourceText = "";
		
		try(BufferedReader reader = Files.newBufferedReader(sourceFile.toPath(),
				Charset.defaultCharset())){
			while(reader.ready()){
				sourceText += reader.readLine() + "\n";
			}
		}
		catch(IOException e) {
			fail("Could not find or read from sourceFile.");
		}
		
		TextDataContents contents = data.getContents();
		TextDataContents contents2 = data.getContents();
		
		assertNotNull("Could not find or read from contents File.", contents);
		assertEquals("Returned text should be equal to the source File's text.",
				contents.getText(), sourceText);
		assertFalse("Should not return the same TextDataContents instance twice.",
				contents == contents2);
	}

	@Test
	public void testUpdateContents() {
		String sourceText = "";
		
		try(BufferedReader reader = Files.newBufferedReader(sourceFile.toPath(),
				Charset.defaultCharset())){
			while(reader.ready()){
				sourceText += reader.readLine() + "\n";
			}
		}
		catch(IOException e) {
			fail("Could not find or read from sourceFile.");
		}
		
		TextDataContents newContents = new TextDataContents("New contents.");
		
		data.updateContents(newContents);
		TextDataContents updatedContents = data.getContents();
		
		assertFalse("Contents should have been updated and should no longer match the original contents.",
				updatedContents.compareTo(new TextDataContents(sourceText)) == 0);
		assertTrue("Contents should have been updated to match the passed contents.",
				updatedContents.compareTo(newContents) == 0);
		assertFalse("Should not return the same TextDataContents instance as was passed in.",
				updatedContents == newContents);
	}

	@Test
	public void testCompareTo() {
		
		assertTrue("TextData objects pointing to the same File should be equal.",
				data.compareTo(new TextData(testFile)) == 0);
		assertFalse("TextData objects pointing to different Files (even if the Files are identical) should not be equal.",
				data.compareTo(new TextData(sourceFile)) == 0);
		
		String sourceText = "";
		
		try(BufferedReader reader = Files.newBufferedReader(sourceFile.toPath(),
				Charset.defaultCharset())){
			while(reader.ready()){
				sourceText += reader.readLine() + "\n";
			}
		}
		catch(IOException e) {
			fail("Could not find or read from sourceFile.");
		}
		
		TextDataContents sourceContents = new TextDataContents(sourceText);
		TextDataContents otherContents = new TextDataContents("Other contents.");
		TextDataContents actualContents = data.getContents();
		
		assertTrue("Two TextDataContents with the same text should be equal.",
				actualContents.compareTo(sourceContents) == 0);
		assertFalse("Two TextDataContents with different text should not be equal.",
				actualContents.compareTo(otherContents) == 0);
	}

	@Test
	public void testTextDataContentsSetText(){
		TextDataContents contents;
		
		try{
			contents = new TextDataContents(null);
			fail("Constructor should not allow parameter to be null.");
		}
		catch(IllegalArgumentException iae){}
		
		try{
			contents = new TextDataContents("");
			contents.setText(null);
			fail("Text should never be allowed to be set to null.");
		}
		catch(IllegalArgumentException iae){}
	}
}
