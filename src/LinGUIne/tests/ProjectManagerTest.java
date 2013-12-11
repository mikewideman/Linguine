package LinGUIne.tests;

import static org.junit.Assert.*;

import org.eclipse.core.runtime.Path;
import org.junit.Before;
import org.junit.Test;

import LinGUIne.model.Project;
import LinGUIne.model.ProjectManager;

public class ProjectManagerTest {

	ProjectManager projectMan;
	
	@Before
	public void setUp() throws Exception {
		projectMan = new ProjectManager(new Path(""));
	}

	@Test
	public void testAddProject() {
		Project testProj1 = new Project();
		Project testProj2 = new Project();
		
		assertFalse("Projects without names cannot be added.",
				projectMan.addProject(testProj1));
		
		testProj1.setName("TestProject1");
		testProj2.setName("TestProject1");
		
		assertTrue("Projects with unique, valid names should be added.",
				projectMan.addProject(testProj1));
		assertFalse("Cannot add two Projects with the same name.",
				projectMan.addProject(testProj2));
		
		testProj2.setName("TestProject2");
		
		assertTrue("Projects with unique, valid names should be added.",
				projectMan.addProject(testProj2));
	}
	
	@Test
	public void testContainsProject() {
		String projName = "TestProject1";
		Project testProj = new Project();
		testProj.setName(projName);
		
		assertFalse("No Projects have been added; ProjectManager should contain no Projects.",
				projectMan.containsProject(projName));
		
		projectMan.addProject(testProj);
		
		assertTrue("Project was just added with this name; it should be retrievable by it.",
				projectMan.containsProject(testProj.getName()));
		assertTrue("Project was just added with this name; it should be retrievable by it.",
				projectMan.containsProject(projName));
		assertTrue("Project names should be case insensitive.",
				projectMan.containsProject(projName.toUpperCase()));
		
		assertFalse("No Project with this name has been added.",
				projectMan.containsProject("TestProject2"));
	}

	@Test
	public void testGetProject() {
		Project testProj = new Project();
		testProj.setName("TestProject");
		
		projectMan.addProject(testProj);
		
		assertNull("No Project of that name has been added, so none should be returned.",
				projectMan.getProject("NonExistentProject"));
		
		assertSame("Project should not be copied when added or returned.",
				projectMan.getProject(testProj.getName()), testProj);
		assertNotNull("Projects should be retrievable in a case-insensitive manner.",
				projectMan.getProject("TeStPrOjEcT"));
	}

	@Test
	public void testGetProjects() {
		Project testProj = new Project();
		
		assertNotNull("Project collection should not be null.",
				projectMan.getProjects());
		assertEquals("Project collection should be empty.",
				projectMan.getProjects().size(), 0);
		
		testProj.setName("TestProject1");
		projectMan.addProject(testProj);
		testProj.setName("TestProject2");
		projectMan.addProject(testProj);
		testProj.setName("TestProject3");
		projectMan.addProject(testProj);
		projectMan.addProject(testProj);
		
		assertEquals("Project collection should contain 3 Projects.",
				projectMan.getProjects().size(), 3);
	}

}
