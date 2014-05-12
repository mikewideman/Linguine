package LinGUIne.serialization;

import java.util.HashMap;
import java.util.Map.Entry;

import LinGUIne.model.KeyValueResultContents;
import LinGUIne.model.ResultData;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;

/**
 * Used to serialize/deserialize KeyValueResults to/from JSON.
 * 
 * @author Kyle Mullins
 */
public class KeyValueResultTranslator {

	private static final String DATA_ATTRIB = "data";

	/**
	 * Converts the given KeyValueResultContents into a Json-format String so
	 * that it may be written to disk.
	 * 
	 * @param contents	The contents object that is to be converted.
	 * 
	 * @return	A Json String representing the given KeyValueResultContents.
	 */
	public static String toJson(KeyValueResultContents contents){
		JsonElement json = composeFromContents(contents);
		
		return json.toString();
	}
	
	/**
	 * Converts the given Json String into a KeyValueResultContents object.
	 * 
	 * @param jsonStr	A Json-format String to be parsed to an in-memory object.
	 * 
	 * @return	A KeyValueResultContents object representing the given Json
	 * 			String or null if the Json could not be parsed.
	 */
	public static KeyValueResultContents fromJson(String jsonStr){
		JsonParser parser = new JsonParser();
		JsonElement json = parser.parse(jsonStr);
		
		return parseContentsFromJson(json);
	}
	
	/**
	 * Returns the populated root JsonElement of the given
	 * KeyValueResultContents object.
	 */
	private static JsonElement composeFromContents(
			KeyValueResultContents contents){
		
		JsonObject rootObj = new JsonObject();
		JsonArray dataAry = new JsonArray();
		
		for(HashMap<String, ResultData> pairs: contents){
			JsonObject newObj = new JsonObject();
			
			for(Entry<String, ResultData> pair: pairs.entrySet()){
				ResultData value = pair.getValue();
				
				if(value.getType() == Integer.class){
					newObj.addProperty(pair.getKey(),
							value.getAsInteger());
				}
				else if(value.getType() == Double.class){
					newObj.addProperty(pair.getKey(),
							value.getAsDouble());
				}
				else{
					newObj.addProperty(pair.getKey(),
							value.getAsString());
				}
			}
			
			dataAry.add(newObj);
		}
		
		rootObj.add(DATA_ATTRIB, dataAry);
		
		return rootObj;
	}
	
	/**
	 * Returns the KeyValueResultContents object described by the given Json
	 * root object.
	 */
	private static KeyValueResultContents parseContentsFromJson(JsonElement json){
		KeyValueResultContents newContents = new KeyValueResultContents();
		
		if(json.isJsonObject()){
			JsonObject rootObject = json.getAsJsonObject();
			
			if(rootObject.has(DATA_ATTRIB)){
				JsonArray dataAry = rootObject.get(DATA_ATTRIB).getAsJsonArray();
				
				//Loop through each object in the data array and add all pairs
				//to the new KeyValueResultContents object.
				for(JsonElement element: dataAry){
					if(element.isJsonObject()){
						JsonObject elementObj = element.getAsJsonObject();
						HashMap<String, ResultData> pairs =
								new HashMap<String, ResultData>();
						
						for(Entry<String, JsonElement> pair:
							elementObj.entrySet()){
							
							if(pair.getValue().isJsonPrimitive()){
								JsonPrimitive jsonValue =
										pair.getValue().getAsJsonPrimitive();
								
								if(jsonValue.isNumber()){
									//TODO: Assume all numbers are doubles for now, discuss
									ResultData value = new ResultData(
											jsonValue.getAsDouble());
									
									pairs.put(pair.getKey(), value);
								}
								else if(jsonValue.isString()){
									ResultData value = new ResultData(
											jsonValue.getAsString());
									
									pairs.put(pair.getKey(), value);
								}
							}
						}
						
						newContents.addKeyValuePairs(pairs);
					}
				}
			}
		}
		else{
			return null;
		}
		
		return newContents;
	}
}
