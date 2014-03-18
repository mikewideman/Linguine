package LinGUIne.model;

import java.util.HashMap;
import java.util.Map.Entry;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Display;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;

import LinGUIne.model.annotations.IAnnotation;
import LinGUIne.model.annotations.MetaAnnotation;
import LinGUIne.model.annotations.Tag;
import LinGUIne.model.annotations.TextAnnotation;

class AnnotationSetTranslator {

	private final static String TAGS_ATTRIB = "Tags";
	private final static String ANNOTATIONS_ATTRIB = "Annotations";
	
	/**
	 * Converts the given AnnotationSetContents to Json format so that it may
	 * be written to disk.
	 * 
	 * @param contents	The AnnotationSetContents object to be converted.
	 * 
	 * @return	A Json String representing the given AnnotationSetContents.
	 */
	public static String toJson(AnnotationSetContents contents){
		JsonElement json = composeJsonFromContents(contents);
		
		return json.toString();
	}
	
	/**
	 * Converts the given Json String into an AnnotationSetContents object.
	 * 
	 * @param jsonStr	A Json String to be converted to an in-memory object.
	 * 
	 * @return	An AnnotationSetContents object representing the given Json
	 * 			or null if the Json could not be parsed.
	 */
	public static AnnotationSetContents fromJson(String jsonStr){
		JsonParser parser = new JsonParser();
		JsonElement json = parser.parse(jsonStr);
		
		return parseContentsFromJson(json);
	}
	
	/*
	 * Composing into Json
	 */
	
	/**
	 * Returns the root JsonElement of the given AnnotationSetContents object.
	 */
	private static JsonElement composeJsonFromContents(AnnotationSetContents contents){
		JsonObject contentsRootObj = new JsonObject();
		JsonArray tagsArray = new JsonArray();
		JsonArray annotationsArray = new JsonArray();
		
		HashMap<Tag, Integer> tags = new HashMap<Tag, Integer>();
		int id = 1;
		
		for(Tag tag: contents.getTags()){
			tags.put(tag, id);
			tagsArray.add(composeJsonFromTag(tag, id));
			
			if(!contents.getAnnotations(tag).isEmpty()){
				JsonObject annotationRootObj = new JsonObject();
				JsonArray locationsArray = new JsonArray();
				
				annotationRootObj.addProperty("TagID", id);
				annotationRootObj.add("Locations", locationsArray);
				
				for(IAnnotation annotation: contents.getAnnotations(tag)){
					locationsArray.add(composeJsonFromAnnotation(annotation, tags));
				}
				
				annotationsArray.add(annotationRootObj);
			}
			
			id++;
		}
		
		contentsRootObj.add(TAGS_ATTRIB, tagsArray);
		contentsRootObj.add(ANNOTATIONS_ATTRIB, annotationsArray);
		
		return contentsRootObj;
	}
	
	/**
	 * Returns the root JsonElement of the given Tag object, assigning it the
	 * provided id.
	 */
	private static JsonElement composeJsonFromTag(Tag tag, int id){
		JsonObject tagRootObj = new JsonObject();
		JsonArray colorArray = new JsonArray();
		
		colorArray.add(new JsonPrimitive(tag.getColor().getRed()));
		colorArray.add(new JsonPrimitive(tag.getColor().getGreen()));
		colorArray.add(new JsonPrimitive(tag.getColor().getBlue()));
		
		tagRootObj.addProperty("ID", id);
		tagRootObj.addProperty("Tagname", tag.getName());
		tagRootObj.add("Color", colorArray);
		
		if(tag.getComment() != null){
			tagRootObj.addProperty("Description", tag.getComment());
		}
		
		return tagRootObj;
	}
	
	/**
	 * Returns the JsonElement of the Location entry corresponding to the given
	 * IAnnotation object. The tags map is used to look up the ids associated
	 * with different Tags.
	 */
	private static JsonElement composeJsonFromAnnotation(IAnnotation annotation,
			HashMap<Tag, Integer> tags){
		JsonElement locationElem = null;
		
		//TODO: Make all locations objects and add a 'Type' property
		if(annotation instanceof MetaAnnotation){
			MetaAnnotation metaAnnotation = (MetaAnnotation)annotation;
			
			locationElem = new JsonPrimitive(tags.get(metaAnnotation.getAnnotatedTag()));
		}
		else if(annotation instanceof TextAnnotation){
			TextAnnotation textAnnotation = (TextAnnotation)annotation;
			
			JsonObject locationObj = new JsonObject();
			
			locationObj.addProperty("Start", textAnnotation.getStartIndex());
			locationObj.addProperty("End", textAnnotation.getEndIndex());
			
			locationElem = locationObj;
		}
		
		return locationElem;
	}
	
	/*
	 * Parsing from Json
	 */
	
	/**
	 * Returns the AnnotationSetContents object described by the given Json
	 * root object.
	 */
	private static AnnotationSetContents parseContentsFromJson(JsonElement json){
		AnnotationSetContents newContents = new AnnotationSetContents();
		HashMap<Integer, Tag> tags = new HashMap<Integer, Tag>();
		
		if(json.isJsonObject()){
			JsonObject jsonRootObj = json.getAsJsonObject();
			
			for(Entry<String, JsonElement> entry: jsonRootObj.entrySet()){
				if(entry.getKey().equalsIgnoreCase(TAGS_ATTRIB)){
					JsonArray tagsArray = entry.getValue().getAsJsonArray();
					
					for(JsonElement tagJson: tagsArray){
						if(!parseTagFromJson(tagJson, tags, newContents)){
							//TODO: Error handling
							return null;
						}
					}
				}
				else if(entry.getKey().equalsIgnoreCase(ANNOTATIONS_ATTRIB)){
					JsonArray annotationsArray = entry.getValue().getAsJsonArray();
					
					for(JsonElement annotationJson: annotationsArray){
						if(!parseAnnotationsFromJson(annotationJson, tags, newContents)){
							//TODO: Error handling
							return null;
						}
					}
				}
			}
		}
		else{
			return null;
		}
		
		return newContents;
	}
	
	/**
	 * Parses the Tag described by the given JsonElement and adds it to both
	 * the tags map and the AnnotationSetContents instance being constructed.
	 */
	private static boolean parseTagFromJson(JsonElement json,
			HashMap<Integer, Tag> tags, AnnotationSetContents contents){
		if(json.isJsonObject()){
			JsonObject tagRootObj = json.getAsJsonObject();
			
			try{
				int id = tagRootObj.get("ID").getAsInt();
				String tagname = tagRootObj.get("Tagname").getAsString();
				String description = null;
				
				if(tagRootObj.has("Description")){
					description = tagRootObj.get("Description").getAsString();
				}
				
				JsonArray colorArray = tagRootObj.get("Color").getAsJsonArray();
				Color color = new Color(Display.getCurrent(),
						colorArray.get(0).getAsInt(),
						colorArray.get(1).getAsInt(),
						colorArray.get(2).getAsInt());
				
				Tag newTag = new Tag(tagname, color, description);
				tags.put(id, newTag);
				contents.addTag(newTag);
				
				return true;
			}
			catch(ClassCastException | IllegalStateException e){
				//Couldn't parse the Tag
				return false;
			}
		}
		
		return false;
	}
	
	/**
	 * Parses all of the Annotations described by the given JsonElement and
	 * adds all of them to the AnnotationSetContents instance being constructed.
	 * The tags map is used to look up ids associated with different Tags.
	 */
	private static boolean parseAnnotationsFromJson(JsonElement json,
			HashMap<Integer, Tag> tags, AnnotationSetContents contents){
		if(json.isJsonObject()){
			JsonObject annotationRootObj = json.getAsJsonObject();
			
			try{
				int tagId = annotationRootObj.get("TagID").getAsInt();
				JsonArray locationsArray = annotationRootObj.get("Locations").
						getAsJsonArray();
				
				if(!tags.containsKey(tagId)){
					//TagID doesn't correspond to an existing Tag
					return false;
				}
				
				Tag myTag = tags.get(tagId);

				for(JsonElement location: locationsArray){
					IAnnotation newAnnotation = null;
					
					//TODO: Make all locations objects and add a 'Type' property
					if(location.isJsonObject()){
						//Standard TextAnnotation
						JsonObject locationObject = location.getAsJsonObject();
						
						int start = locationObject.get("Start").getAsInt();
						int end = locationObject.get("End").getAsInt();
						
						newAnnotation = new TextAnnotation(myTag, start, end);
					}
					else if(location.isJsonPrimitive()){
						//MetaAnnotation
						int annotatedTagId = location.getAsInt();
						
						if(!tags.containsKey(annotatedTagId)){
							//TagID doesn't correspond to an existing Tag
							return false;
						}
						
						Tag annotatedTag = tags.get(annotatedTagId);
						
						newAnnotation = new MetaAnnotation(myTag, annotatedTag);
					}
					else{
						return false;
					}
					
					contents.addAnnotation(newAnnotation);
				}
				
				return true;
			}
			catch(ClassCastException | IllegalStateException e){
				//Couldn't parse the Annotation
				return false;
			}
		}
		
		return false;
	}
}
