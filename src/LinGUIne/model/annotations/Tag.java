package LinGUIne.model.annotations;

public class Tag {
	private String tagName;
	private String tagComment;
	
	public Tag(String name, String comment){
		tagName = name;
		tagComment = comment;
	}
	
	public Tag(String name){
		this(name, null);
	}
	
	public String getName(){
		return tagName;
	}
	
	public String getComment(){
		return tagComment;
	}
}
