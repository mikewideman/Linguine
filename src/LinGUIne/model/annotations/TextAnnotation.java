package LinGUIne.model.annotations;

import LinGUIne.model.IProjectDataContents;
import LinGUIne.model.TextDataContents;
import LinGUIne.utilities.ParameterCheck;

/**
 * Basic Annotation for some text.
 * 
 * @author Kyle Mullins
 */
public class TextAnnotation implements IAnnotation {

	private Tag myTag;
	private int startChar;
	private int textLength;
	
	/**
	 * Creates a new TextAnnotation of the given Tag which ranges from the text
	 * beginning with the character at start and with the given length.
	 * Note: tag parameter must not be null.
	 * 
	 * @param tag		The Tag with which this Annotation is associated.
	 * @param start		The index of the first character in the text.
	 * @param length	The length of the text this Annotation represents.
	 */
	public TextAnnotation(Tag tag, int start, int length){
		ParameterCheck.notNull(tag, "tag");
		ParameterCheck.notZero(length, "length");
		
		myTag = tag;
		startChar = start;
		textLength = length;
	}
	
	/**
	 * Shifts the position of this Annotation by shiftAmt, without altering the
	 * length of the Annotation.
	 * 
	 * @param shiftAmt	The amount by which to shift the Annotation.
	 */
	public void shift(int shiftAmt){
		startChar += shiftAmt;
	}
	
	/**
	 * Expands the length of this Annotation by expandAmt, without changing the
	 * starting position of the Annotation.
	 * 
	 * @param expandAmt	The amount by which to expand the Annotation.
	 */
	public void expand(int expandAmt){
		textLength += expandAmt;
	}
	
	/**
	 * Returns the index of the first character of the text this Annotation
	 * represents.
	 */
	public int getStartIndex(){
		return startChar;
	}
	
	/**
	 * Returns the index of the last character of the text this Annotation
	 * represents.
	 */
	public int getEndIndex(){
		return startChar + textLength;
	}
	
	/**
	 * Returns the text that this Annotation references.
	 * 
	 * @param data	IProjectDataContents instance from which to get the text of
	 * 				this Annotation.
	 * 
	 * @return	The text this Annotation references.
	 */
	public String getText(IProjectDataContents data) {
		if(data instanceof TextDataContents){
			TextDataContents textData = (TextDataContents)data;
			
			return textData.getText().substring(startChar,
					getEndIndex());
		}
		
		return null;
	}

	@Override
	public Tag getTag() {
		return myTag;
	}

	@Override
	public int compareTo(IAnnotation otherAnnotation) {
		if(otherAnnotation == null){
			return 1;
		}
		else if(otherAnnotation instanceof TextAnnotation){
			TextAnnotation otherTextAnnotation = (TextAnnotation)otherAnnotation;
			
			int result = Integer.compare(startChar, otherTextAnnotation.startChar);
			
			if(result == 0){
				result = Integer.compare(textLength, otherTextAnnotation.textLength);
			}
			
			return result;
		}
		
		return Integer.compare(hashCode(), otherAnnotation.hashCode());
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		
		result = prime * result + startChar;
		result = prime * result + textLength;
		
		return result;
	}

	@Override
	public boolean equals(Object obj) {

		if(this == obj) {
			return true;
		}
		else if(obj == null || !(obj instanceof TextAnnotation)) {
			return false;
		}

		TextAnnotation other = (TextAnnotation)obj;
		
		if(startChar != other.startChar) {
			return false;
		}
		
		return textLength == other.textLength;
	}
}
