package LinGUIne.tests;

import static org.junit.Assert.*;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Display;
import org.junit.Before;
import org.junit.Test;

import LinGUIne.model.TextDataContents;
import LinGUIne.model.annotations.MetaAnnotation;
import LinGUIne.model.annotations.Tag;
import LinGUIne.model.annotations.TextAnnotation;

public class TextAnnotationTest {

	Tag tag;
	TextAnnotation annotation;
	
	@Before
	public void setUp() throws Exception {
		tag = new Tag("Tag name",
				new Color(Display.getCurrent(), 255, 255, 255));
		
		annotation = new TextAnnotation(tag, 0, 1);
	}
	
	@Test
	public void testConstructor() {
		try{
			new TextAnnotation(null, 0, 1);
			fail("No Annotation should allow Tag to be null.");
		}
		catch(IllegalArgumentException iae){}
		
		try{
			new TextAnnotation(tag, 0, 0);
			fail("TextAnnotation should not allow its length to be zero.");
		}
		catch(IllegalArgumentException iae){}
	}

	@Test
	public void testShift() {
		int shiftAmt = 5;
		
		annotation.shift(shiftAmt);
		
		assertTrue("Start index should be shifted upwards by shiftAmt.",
				annotation.getStartIndex() == (0 + shiftAmt));
		assertTrue("End index should be shifted upwards by shiftAmt.",
				annotation.getEndIndex() == (1 + shiftAmt));
	}

	@Test
	public void testExpand() {
		int expandAmt = 5;
		
		annotation.expand(expandAmt);
		
		assertTrue("Start index should not change when expanded.",
				annotation.getStartIndex() == 0);
		assertTrue("End index should be shifted upwards by expandAmt.",
				annotation.getEndIndex() == (1 + expandAmt));
	}

	@Test
	public void testGetText() {
		TextDataContents contents = new TextDataContents("Some text.");
		
		String text = annotation.getText(contents);
		
		assertEquals("Text returned should be equal to the first character in the contents String.",
				"S", text);
		
		annotation.shift(5);
		annotation.expand(3);
		text = annotation.getText(contents);
		
		assertEquals("Text returned should be equal to the last word in the contents String.",
				"text", text);
	}

	@Test
	public void testEquals() {
		TextAnnotation duplicate = annotation;
		TextAnnotation theSameAnnotation = new TextAnnotation(tag, 0, 1);
		TextAnnotation otherAnnotation = new TextAnnotation(tag, 5, 3);
		MetaAnnotation meta = new MetaAnnotation(tag, tag);
		
		assertEquals("All Annotations should be equal to themselves.",
				annotation, duplicate);
		assertEquals("TextAnnotations should be equal to other TextAnnotations with the same start and end characters.",
				annotation, theSameAnnotation);
		assertNotEquals("TextAnnotations should not be equal to other TextAnnotations with different start and end characters.",
				annotation, otherAnnotation);
		assertNotEquals("No TextAnnotation should be equal to another type of Annotation.",
				annotation, meta);
	}
}
