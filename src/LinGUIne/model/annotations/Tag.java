package LinGUIne.model.annotations;

import org.eclipse.swt.graphics.Color;

import LinGUIne.utilities.ParameterCheck;

/**
 * A Tag which can be used to Annotate something. Tags have a Color with which
 * they are displayed, and a name used to distinguish them, as well as an
 * optional comment.
 * Note: instances of this object should be disposed before being garbage
 * collected.
 * 
 * @author Kyle Mullins
 */
public class Tag implements Comparable<Tag>{

	private String tagName;
	private String tagComment;
	private Color tagColor;
	private boolean isEnabled;
	
	/**
	 * Creates a new Tag with the given display name, comment, and display
	 * color.
	 * Note: name and color parameters must not be null.
	 * 
	 * @param name		The display name for this Tag.
	 * @param color		The Color used when displaying Annotations using this
	 * 					Tag.
	 * @param comment	A comment associated with this Tag.
	 */
	public Tag(String name, Color color, String comment){
		ParameterCheck.notNull(name, "name");
		ParameterCheck.notNull(color, "color");
		
		tagName = name;
		tagColor = color;
		tagComment = comment;
		isEnabled = true;
	}
	
	/**
	 * Creates a new Tag with the given display name and display color but
	 * without a comment.
	 * 
	 * @param name	The display name for this Tag.
	 * @param color	The Color sued when displaying Annotations using this
	 * 				Tag.
	 */
	public Tag(String name, Color color){
		this(name, color, null);
	}
	
	/**
	 * Used to dispose this Tag's Color instance.
	 * This should be called before this instance is garbage collected.
	 */
	public void dispose(){
		tagColor.dispose();
	}
	
	/**
	 * Returns the display name of this Tag.
	 */
	public String getName(){
		return tagName;
	}
	
	/**
	 * Returns the comment associated with this Tag.
	 */
	public String getComment(){
		return tagComment;
	}
	
	/**
	 * Sets or changes the comment associated with this Tag.
	 * 
	 * @param newComment	The new comment.
	 */
	public void setComment(String newComment){
		tagComment = newComment;
	}
	
	/**
	 * Returns the Color used when displaying Annotations of this Tag.
	 */
	public Color getColor(){
		return tagColor;
	}

	/**
	 * Changes the display Color for this Tag.
	 * Note: newColor parameter must not be null.
	 * 
	 * @param newColor	The new Color to be used.
	 */
	public void setColor(Color newColor){
		ParameterCheck.notNull(newColor, "newColor");
		
		tagColor = newColor;
	}

	/**
	 * Returns whether or not this tag is enabled.
	 */
	public boolean getEnabled(){
		return isEnabled;
	}
	
	/**
	 * Sets whether or not this Tag is enabled.
	 */
	public void setEnabled(boolean enabled){
		isEnabled = enabled;
	}
	
	/**
	 * Performs a deep copy of this Tag and returns it.
	 */
	public Tag copy(){
		return new Tag(tagName, tagColor, tagComment);
	}
	
	/**
	 * Performs a much deeper comparison of this Tag and the given Tag than the
	 * standard compareTo does.
	 */
	public int deepCompareTo(Tag otherTag){
		if(compareTo(otherTag) == 0){
			if(tagComment != null){
				if(otherTag.tagComment != null){
					if(tagComment.equals(otherTag.tagComment)){
						return compareColors(tagColor, otherTag.tagColor);
					}

					return tagComment.compareTo(otherTag.tagComment);
				}

				return 1;
			}

			return -1;
		}
		
		return compareTo(otherTag);
	}
	
	@Override
	public int compareTo(Tag otherTag) {
		return tagName.compareTo(otherTag.tagName);
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj == null || !(obj instanceof Tag)){
			return false;
		}
		
		return compareTo((Tag)obj) == 0;
	}
	
	@Override
	public int hashCode() {
		return tagName.hashCode();
	}

	/**
	 * Performs a compareTo on the two given Colors.
	 */
	private int compareColors(Color a, Color b){
		if(a.getRed() == b.getRed()){
			if(a.getGreen() == b.getGreen()){
				return Integer.compare(a.getBlue(), b.getBlue());
			}
			
			return Integer.compare(a.getGreen(), b.getGreen());
		}

		return Integer.compare(a.getRed(), b.getRed());
	}
}
