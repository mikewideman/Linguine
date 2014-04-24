package LinGUIne.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

/**
 * Generalized format for Results which can be expressed as key-value pairs.
 * Note: It is assumed that the keys used are consisted and that all uses of a
 * given key are of the same data type.
 * 
 * @author Peter Dimou
 * @author Kyle Mullins
 */
public class KeyValueResultContents implements IProjectDataContents,
		Iterable<HashMap<String, ResultData>>, IPlaintextViewable {
	
	private List<HashMap<String, ResultData>> contents;
	
	/**
	 * Constructs a new empty set of data.
	 */
	public KeyValueResultContents() {
		contents = new ArrayList<HashMap<String, ResultData>>();
	}
	
	/**
	 * Constructs a new result object from a previously constructed set of data.
	 * 
	 * @param contents The previously constructed set of data.
	 */
	public KeyValueResultContents(List<HashMap<String, ResultData>> otherContents) {
		contents = new ArrayList<HashMap<String, ResultData>>(otherContents);
	}

	/**
	 * Adds the given set of key-value pairs to the List of data.
	 * 
	 * @param pairs	A set of key-value pairs to be added to the data set.
	 */
	public void addKeyValuePairs(HashMap<String, ResultData> pairs){
		contents.add(pairs);
	}
	
	/**
	 * Returns a set of all keys used in this instance's data.
	 * Note: It is assumed that all entries utilize the same set of keys.
	 * 
	 * @return	A set of all keys.
	 */
	public Set<String> getKeys(){
		return contents.get(0).keySet();
	}

	/**
	 * Returns the data type of values associated with the given key if it
	 * exists, otherwise null is returned.
	 * 
	 * @param key	The key for which the data type is to be returned.
	 * 
	 * @return	The type of values associated with the given key.
	 */
	public Class<?> getDataTypeForKey(String key){
		if(!contents.isEmpty() && contents.get(0).containsKey(key)){
			return contents.get(0).get(key).getClass();
		}
		
		return null;
	}
	
	@Override
	public int compareTo(IProjectDataContents otherContents) {
		if(otherContents != null && otherContents instanceof
				KeyValueResultContents){
			KeyValueResultContents otherKeyValueContents =
					(KeyValueResultContents)otherContents;
			
			List<HashMap<String, ResultData>> otherEntries =
					otherKeyValueContents.contents;
			
			if(contents.size() != otherEntries.size()){
				return Integer.compare(contents.size(), otherEntries.size());
			}
			else if(getKeys().size() != otherKeyValueContents.getKeys().size()){
				return Integer.compare(getKeys().size(),
						otherKeyValueContents.getKeys().size());
			}
			
			for(int x = 0; x < contents.size(); x++){
				for(String key: getKeys()){
					ResultData myVal = contents.get(x).get(key);
					ResultData otherVal = otherEntries.get(x).get(key);
					
					if(!myVal.equals(otherVal)){
						return myVal.compareTo(otherVal);
					}
				}
			}
		}
		
		return 0;
	}

	/**
	 * Deep-copies this object
	 */
	@Override
	public IProjectDataContents copy() {
		KeyValueResultContents newContents = new KeyValueResultContents();
		
		for(HashMap<String, ResultData> pairs: contents){
			HashMap<String, ResultData> newPairSet =
					new HashMap<String, ResultData>();
			
			for(Entry<String, ResultData> pair: pairs.entrySet()){
				newPairSet.put(pair.getKey(), pair.getValue());
			}
			
			newContents.addKeyValuePairs(newPairSet);
		}
		
		return newContents;
	}
	
	@Override
	public Class<? extends IProjectData> getAssociatedDataType() {
		return KeyValueResult.class;
	}

	/**
	 * Returns an iterator over the data in this instance.
	 */
	@Override
	public Iterator<HashMap<String, ResultData>> iterator(){
		return contents.iterator();
	}

	@Override
	public String getAsPlaintext() {
		String resultText = "";
		
		for(HashMap<String, ResultData> pairs: contents){
			for(Entry<String, ResultData> pair: pairs.entrySet()){
				resultText += pair.getKey() + " = " + pair.getValue().getAsString();
			}
			
			resultText += "\n";
		}
		
		return resultText;
	}
}
