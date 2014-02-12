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
	 * 
	 * 
	 * @param contents
	 * @return
	 */
	public static String toJson(AnnotationSetContents contents){
		return "";
	}
	
	/**
	 * 
	 * 
	 * @param jsonStr
	 * @return
	 */
	public static AnnotationSetContents fromJson(String jsonStr){
		JsonParser parser = new JsonParser();
		JsonElement json = parser.parse(jsonStr);
		
		return parseContentsFromJson(json);
	}
	
	/*
	 * Composing
	 */
	
	/**
	 * 
	 * 
	 * @param contents
	 * @return
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
			
			JsonObject annotationRootObj = new JsonObject();
			JsonArray locationsArray = new JsonArray();
			
			annotationRootObj.addProperty("TagID", id);
			annotationRootObj.add("Locations", locationsArray);
			
			for(IAnnotation annotation: contents.getAnnotations(tag)){
				locationsArray.add(composeJsonFromAnnotation(annotation, tags));
			}
			
			annotationsArray.add(annotationRootObj);
			id++;
		}
		
		contentsRootObj.add(TAGS_ATTRIB, tagsArray);
		contentsRootObj.add(ANNOTATIONS_ATTRIB, annotationsArray);
		
		return contentsRootObj;
	}
	
	/**
	 * 
	 * 
	 * @param tag
	 * @return
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
		tagRootObj.addProperty("Description", tag.getComment());
		
		return tagRootObj;
	}
	
	/**
	 * 
	 * 
	 * @param annotation
	 * @return
	 */
	private static JsonElement composeJsonFromAnnotation(IAnnotation annotation,
			HashMap<Tag, Integer> tags){
		JsonElement locationElem = null;
		
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
	 * Parsing
	 */
	
	/**
	 * 
	 * 
	 * @param json
	 * @return
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
		
		return newContents;
	}
	
	/**
	 * 
	 * 
	 * @param json
	 * @param tags
	 * @param contents
	 * @return
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
	 * 
	 * 
	 * @param json
	 * @param tags
	 * @param contents
	 * @return
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
