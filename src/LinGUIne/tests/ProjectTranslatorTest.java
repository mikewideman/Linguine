package LinGUIne.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.LinkedList;

import org.eclipse.core.runtime.IPath;
import org.junit.BeforeClass;
import org.junit.Test;

import LinGUIne.model.AnnotationSet;
import LinGUIne.model.IProjectData;
import LinGUIne.model.KeyValueResult;
import LinGUIne.model.Project;
import LinGUIne.model.TextData;
import LinGUIne.serialization.ProjectTranslator;
import LinGUIne.utilities.FileUtils;

public class ProjectTranslatorTest {

	static String jsonContent;
	static String workingDir;
	
	@BeforeClass
	public static void setUpBeforeClass(){
		workingDir = System.getProperty("user.dir");
		String testDataDir = workingDir + "/testdata/";
		String testFilePath = testDataDir + "TestProject.json";
		
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
	public void toJson(){
		IPath parentDir = FileUtils.toEclipsePath(new File(workingDir));
		Project newProj = ProjectTranslator.fromJson(jsonContent, parentDir);
		
		assertNotNull("Translator should never return null for valid "
				+ "Project json file.", newProj);
	}
	
	@Test
	public void fromJson(){
		IPath parentDir = FileUtils.toEclipsePath(new File(workingDir));
		Project proj = new Project();
		
		proj.setName("TestProject");
		proj.setParentDirectory(parentDir);
		
		TextData testProjData = new TextData(new File("TestProjectData.txt"));
		TextData someOtherProjData = new TextData(new File("SomeOtherProjectData.txt"));
		
		proj.addProjectData(testProjData);
		proj.addProjectData(someOtherProjData);
		
		AnnotationSet testProjDataAnnotation = new AnnotationSet(
				new File("TestProjectDataAnnotation.json"));
		
		proj.addAnnotation(testProjDataAnnotation, testProjData);
		
		KeyValueResult testResult = new KeyValueResult(new File("TestResult.keyval"));
		LinkedList<IProjectData> associatedData = new LinkedList<IProjectData>();
		
		associatedData.add(testProjData);
		associatedData.add(someOtherProjData);
		proj.addResult(testResult, associatedData);
		
		String newJsonContent = ProjectTranslator.toJson(proj);
		
		assertNotNull("Translator should never return null Json.",
				newJsonContent);
		assertEquals("Json returned by translator should be identical to Json "
				+ "describing the same content.", jsonContent.trim(),
				newJsonContent.trim());
	}
}
