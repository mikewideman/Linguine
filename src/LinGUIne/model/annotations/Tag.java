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
public class Tag {
	
	private String tagName;
	private String tagComment;
	private Color tagColor;
	
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
}
