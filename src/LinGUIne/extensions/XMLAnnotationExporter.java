package LinGUIne.extensions;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.Collection;
import java.util.LinkedList;
import java.util.NavigableSet;
import java.util.TreeMap;
import java.util.TreeSet;

import LinGUIne.model.AnnotationSet;
import LinGUIne.model.AnnotationSetContents;
import LinGUIne.model.IProjectData;
import LinGUIne.model.TextData;
import LinGUIne.model.TextDataContents;
import LinGUIne.model.annotations.IAnnotation;
import LinGUIne.model.annotations.Tag;
import LinGUIne.model.annotations.TextAnnotation;

/**
 * Exporter to create an XML file from TextAnnotations in an AnnotationSet.
 * Tags are XML tags surrounding the annotated text. Works with nested
 * Annotations.
 * 
 * @author Kyle Mullins
 */
public class XMLAnnotationExporter implements IAnnotationExporter {

	@Override
	public String getFileType() {
		return "Extensible Markup Language (XML)";
	}

	@Override
	public String getFileMask() {
		return "*.xml";
	}

	@Override
	public Collection<Class<? extends IProjectData>> getSupportedSourceDataTypes() {
		LinkedList<Class<? extends IProjectData>> supportedTypes =
				new LinkedList<Class<? extends IProjectData>>();
		
		supportedTypes.add(TextData.class);
		
		return supportedTypes;
	}
	
	@Override
	public void exportAnnotation(IProjectData sourceData,
			AnnotationSet annotations, File destFile) throws IOException {
		
		if(sourceData instanceof TextData){
			TextData textSource = (TextData)sourceData;
			TextDataContents textContents = (TextDataContents)textSource.
					getContents();
			AnnotationSetContents annotationContents = annotations.getContents();
			
			TreeMap<Integer, String> annotationTags = new TreeMap<Integer,
					String>();
			
			//Build up the sets of all Tags that will appear at each index
			for(Tag tag: annotationContents.getTags()){
				for(IAnnotation annotation: annotationContents.
						getAnnotations(tag)){
					
					if(annotation instanceof TextAnnotation){
						TextAnnotation textAnn = (TextAnnotation)annotation;
						String startTags = "";
						String endTags = "";
						
						if(annotationTags.containsKey(textAnn.getStartIndex())){
							startTags = annotationTags.get(textAnn.getStartIndex());
						}
						
						if(annotationTags.containsKey(textAnn.getEndIndex())){
							endTags = annotationTags.get(textAnn.getEndIndex());
						}
						
						startTags += "<" + tag.getName() + ">";
						endTags = "</" + tag.getName() + ">" + endTags;
						
						annotationTags.put(textAnn.getStartIndex(), startTags);
						annotationTags.put(textAnn.getEndIndex(), endTags);
					}
				}
			}
			
			String xmlContent = textContents.getText();
			
			NavigableSet<Integer> annotationIndices = new TreeSet<Integer>(
					annotationTags.keySet()).descendingSet();
			
			//Iterate through indices in reverse, inserting the Tag Strings that
			//we built above into the original text
			for(Integer annotationIndex: annotationIndices){
				String textHead = xmlContent.substring(0, annotationIndex);
				String textTail = xmlContent.substring(annotationIndex);
				
				xmlContent = textHead + annotationTags.get(annotationIndex) +
						textTail;
			}
			
			try(BufferedWriter writer = Files.newBufferedWriter(
					destFile.toPath(), Charset.defaultCharset())){
				
				writer.write(xmlContent);
			}
			catch(IOException ioe){
				//Catch and rethrow the exception so the twr can close the stream
				throw ioe;
			}
		}
		else{
			throw new IllegalArgumentException("Parameter sourceResult must be"
					+ " of type TextData");
		}
	}
}
