package LinGUIne.tests;

import static org.junit.Assert.*;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Display;
import org.junit.Test;

import LinGUIne.model.annotations.Tag;

public class TagTest {

	@Test
	public void testTagConstructor() {
		try{
			new Tag("Tag name", null);
			fail("Tag should not allow display color to be set to null.");
		}
		catch(IllegalArgumentException iae){}

		try{
			new Tag(null, new Color(Display.getCurrent(), 0, 0, 0));
			fail("Tag should not allow name to be set to null.");
		}
		catch(IllegalArgumentException iae){}
	}

	@Test
	public void testSetColor() {
		try{
			Tag newTag = new Tag("Tag name", new Color(Display.getCurrent(), 0, 0, 0));
			newTag.setColor(null);
			fail("Tag should not allow name to be set to null.");
		}
		catch(IllegalArgumentException iae){}
	}

	@Test
	public void testCompareTo() {
		Color blue = new Color(Display.getCurrent(), 0, 0, 255);
		Color red = new Color(Display.getCurrent(), 255, 0, 0);
		
		Tag tag1 = new Tag("Test tag", blue, "Some comment");
		Tag tag2 = new Tag("Test tag", red, "Some other comment");
		Tag tag3 = new Tag("Different test tag", blue, "Some comment");
		
		assertTrue("Tags with the same name should be equal, even if everything else about them is different.",
				tag1.compareTo(tag2) == 0);
		assertFalse("Tags with different names should not be equal, even if everything else about them is the same.",
				tag1.compareTo(tag3) == 0);
	}
}
