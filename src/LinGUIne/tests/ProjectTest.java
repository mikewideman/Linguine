package LinGUIne.tests;

import static org.junit.Assert.*;

import java.io.File;
import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import LinGUIne.model.Annotation;
import LinGUIne.model.IProjectData;
import LinGUIne.model.Project;
import LinGUIne.model.Result;
import LinGUIne.model.TextData;

public class ProjectTest {

	Project project;
	TextData textData1;
	TextData textData2;
	TextData textData3;
	
	@Before
	public void setUp() throws Exception {
		project = new Project();
		textData1 = new TextData(new File("dont/care.txt"));
		textData2 = new TextData(new File("tesfile.doc"));
		textData3 = new TextData(new File("doesnt/matter/somefile.extension"));
		
		project.addProjectData(textData1);
		project.addProjectData(textData2);
		project.addProjectData(textData3);
	}

	@Test
	public void testAddProjectData() {
		TextData projData = new TextData(new File("directory/newFile.file"));
		
		assertFalse("Cannot add null ProjectData.",
				project.addProjectData(null));
		assertTrue("Should be able to add arbitrary ProjectData.",
				project.addProjectData(projData));
		assertFalse("Should not be able to add duplicate ProjectData.",
				project.addProjectData(projData));
	}
	
	@Test
	public void testAddResult() {
		Result result = new Result(new File("someresult.result"));
		ArrayList<IProjectData> affectedData = new ArrayList<IProjectData>();
		
		assertFalse("Cannot add a Result with a null ProjectData list.",
				project.addResult(result, null));
		assertFalse("Cannot add a Result with an empty ProjectData list.",
				project.addResult(result, affectedData));
		
		affectedData.add(textData2);
		affectedData.add(textData3);
		
		assertTrue("Should be able to add a Result with a valid ProjectData list.",
				project.addResult(result, affectedData));
		assertFalse("Cannot add a null Result.",
				project.addResult(null, affectedData));
		
		affectedData.add(new TextData(new File(".")));
		
		assertFalse("Cannot add Results affecting files not in the Project.",
				project.addResult(result, affectedData));
	}

	@Test
	public void testAddAnnotation() {
		Annotation annotation1 = new Annotation(new File("somefile.annotation"));
		Annotation annotation2 = new Annotation(new File("some_other_file"));
		
		assertFalse("Cannot add an Annotation with null AnnotatedData.",
				project.addAnnotation(annotation1, null));
		assertFalse("Cannot add a null Annotation.",
				project.addAnnotation(null, textData1));
		
		assertTrue("Should be able to add a valid Annotation with valid AnnotatedData.",
				project.addAnnotation(annotation1, textData1));
		assertFalse("Cannot have multiple Annotations for the same AnnotatedData",
				project.addAnnotation(annotation2, textData1));
		assertTrue("Should be able to add a Valid Annotation with valid AnnotatedData.",
				project.addAnnotation(annotation2, textData2));
	}

	@Test
	public void testContainsProjectData() {
		assertFalse("Project cannot contain null ProjectData.",
				project.containsProjectData(null));
		assertFalse("Project does not contain the given ProjectData.",
				project.containsProjectData(new Result(new File("someResult"))));
		assertTrue("Project should contain ProjectData that has been added to it.",
				project.containsProjectData(textData2));
	}

	@Test
	public void testGetProjectData() {
		Project testProj = new Project();
		
		assertNotNull("ProjectData collection should never be null.",
				testProj.getProjectData());
		assertEquals("ProjectData collection should be empty.",
				testProj.getProjectData().size(), 0);
		assertEquals("ProjectData collection should have 3 elements.",
				project.getProjectData().size(), 3);
		
		project.addProjectData(new Result(new File("somefile.txt")));
		
		assertEquals("ProjectData collection should now have 4 elements.",
				project.getProjectData().size(), 4);
	}

	@Test
	public void testGetResults() {
		Result result = new Result(new File("someresult"));
		ArrayList<IProjectData> affectedFiles = new ArrayList<IProjectData>();
		affectedFiles.add(textData3);
		
		assertNotNull("Results collection should never be null.",
				project.getResults());
		assertEquals("Results collection should be empty.",
				project.getResults().size(), 0);
		
		project.addResult(result, affectedFiles);
		
		assertEquals("Results collection should contain 1 element.",
				project.getResults().size(), 1);
	}

	@Test
	public void testGetTextData() {
		Project testProj = new Project();
		
		assertNotNull("TextData collection should never be null.",
				testProj.getTextData());
		assertEquals("TextData collection should be empty.",
				testProj.getTextData().size(), 0);
		assertEquals("TextData collection should contain 3 elements.",
				project.getTextData().size(), 3);
	}

	@Test
	public void testIsAnnotated() {
		Annotation annotation = new Annotation(new File("annotationFile.annotation"));
		
		assertFalse("ProjectData that has not been annotated should not return that it is.",
				project.isAnnotated(textData3));
		
		project.addAnnotation(annotation, textData3);
		
		assertFalse("ProjectData that has not been annotated should not return that it is.",
				project.isAnnotated(textData2));
		assertTrue("ProjectData that has been annotated should return that it has been.",
				project.isAnnotated(textData3));
	}

	@Test
	public void testGetAnnotation() {
		Annotation annotation = new Annotation(new File("annotationFile.annotation"));
		
		assertNull("ProjectData that has not been annotated should return a null annotation.",
				project.getAnnotation(textData1));
		
		project.addAnnotation(annotation, textData2);
		
		assertFalse("ProjectData that has not been annotated should not return that it is.",
				project.isAnnotated(textData3));
		assertSame("ProjectData that has been annotated should be able to have the Annotation retrieved.",
				project.getAnnotation(textData2), annotation);
	}
}
