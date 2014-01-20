package LinGUIne.model;

import LinGUIne.utilities.ParameterCheck;

public class TextDataContents implements IProjectDataContents {
	
	private String textData;
	
	/**
	 * Creates a new TextDataContents object with the given text String.
	 * Note: text parameter must not be null.
	 */
	public TextDataContents(String text) {
		ParameterCheck.notNull(text, "text");
		
		textData = text;
	}
	
	/**
	 * Returns the plain text data as a String.
	 */
	public String getText(){
		return textData;
	}
	
	/**
	 * Sets this object's text data.
	 * Note: newText parameter must not be null.
	 * 
	 * @param newText	The new text to be used.
	 */
	public void setText(String newText){
		ParameterCheck.notNull(newText, "newText");
		
		textData = newText;
	}

	/**
	 * Returns a deep copy of this TextDataContents object.
	 * 
	 * @return	The copied instance.
	 */
	@Override
	public IProjectDataContents copy() {
		return new TextDataContents(textData);
	}
	
	@Override
	public int compareTo(IProjectDataContents otherContents) {
		if(otherContents != null && otherContents instanceof TextDataContents){
			TextDataContents otherTextContents = (TextDataContents)otherContents;
			
			return textData.compareTo(otherTextContents.getText());
		}
		
		return 1;
	}
}
