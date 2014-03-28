package LinGUIne.model;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;

import LinGUIne.model.annotations.IAnnotation;
import LinGUIne.model.annotations.Tag;
import LinGUIne.utilities.ParameterCheck;

/**
 * IProjectDataContents for an AnnotationSet, stores Tags and associated
 * Annotations.
 * 
 * @author Kyle Mullins
 */
public class AnnotationSetContents implements IProjectDataContents {

	private HashMap<Tag, HashSet<IAnnotation>> annotations;
	
	/**
	 * Creates a new empty AnnotationSetContents instance.
	 */
	public AnnotationSetContents(){
		annotations = new HashMap<Tag, HashSet<IAnnotation>>();
	}
	
	/**
	 * Adds a new Tag to this AnnotationSet which can be used for Annotations.
	 * Note: newTag must not be null.
	 * 
	 * @param newTag	The new Tag to be added to the set.
	 * 
	 * @return	True iff the Tag was not already included in the set and was
	 * 			added successfully, false otherwise.
	 */
	public boolean addTag(Tag newTag){
		ParameterCheck.notNull(newTag, "newTag");
		
		if(annotations.containsKey(newTag)){
			return false;
		}
		
		annotations.put(newTag, new HashSet<IAnnotation>());
		return true;
	}
	
	/**
	 * Adds a new Annotation to this AnnotationSet, adding the referenced Tag
	 * if it is not already included.
	 * Note: newAnnotation must not be null.
	 * 
	 * @param newAnnotation	The new Annotation to be added to the set.
	 * 
	 * @return	True iff the Annotation was not already part of the set and
	 * 			was added successfully, false otherwise.
	 */
	public boolean addAnnotation(IAnnotation newAnnotation){
		ParameterCheck.notNull(newAnnotation, "newAnnotation");
		
		Tag referencedTag = newAnnotation.getTag();
		
		if(!annotations.containsKey(referencedTag)){
			addTag(referencedTag);
		}

		HashSet<IAnnotation> annotationSet = annotations.get(referencedTag);
		
		if(!annotationSet.contains(newAnnotation)){
			return annotationSet.add(newAnnotation);
		}
		
		return false;
	}
	
	/**
	 * Returns a collection of the Tags contained in this AnnotationSet.
	 * 
	 * @return	All Tags this AnnotationSet contains.
	 */
	public Collection<Tag> getTags(){
		return annotations.keySet();
	}
	
	/**
	 * Returns all of the Annotations associated with the given tag.
	 * 
	 * @param tag	The Tag for which Annotations are to be returned.
	 * 
	 * @return	The associated Annotations.
	 */
	public Collection<IAnnotation> getAnnotations(Tag tag){
		if(annotations.containsKey(tag)){
			return annotations.get(tag);
		}
		
		return new HashSet<IAnnotation>();
	}
	
	@Override
	public IProjectDataContents copy() {
		AnnotationSetContents newContents = new AnnotationSetContents();
		
		for(HashSet<IAnnotation> annotationSet: annotations.values()){
			for(IAnnotation anotation: annotationSet){
				newContents.addAnnotation(anotation);
			}
		}
		
		return newContents;
	}
	
	@Override
	public Class<? extends IProjectData> getAssociatedDataType() {
		return AnnotationSet.class;
	}
	
	@Override
	public int compareTo(IProjectDataContents otherContents) {
		if(otherContents != null && otherContents instanceof
				AnnotationSetContents){
			AnnotationSetContents otherAnnotationSet =
					(AnnotationSetContents)otherContents;
			
			for(Tag otherTag: otherAnnotationSet.annotations.keySet()){
				if(annotations.containsKey(otherTag)){
					HashSet<IAnnotation> myAnnotations = annotations.get(otherTag);
					
					for(IAnnotation otherAnnotation:
						otherAnnotationSet.annotations.get(otherTag)){
						
						if(!myAnnotations.contains(otherAnnotation)){
							return -1;
						}
					}
				}
				else{
					return -1;
				}
			}
			
			return 0;
		}
		
		return 1;
	}
}
