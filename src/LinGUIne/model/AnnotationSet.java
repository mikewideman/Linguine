package LinGUIne.model;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;

/**
 * Represents the annotations of some single ProjectData.
 * 
 * @author Kyle Mullins
 */
public class AnnotationSet implements ITypedProjectData<AnnotationSetContents> {
	
	private File annotationFile;
	private AnnotationSetContents contents;
	
	public AnnotationSet(File annotation){
		annotationFile = annotation;
		contents = null;
	}
	
	public File getFile() {
		return annotationFile;
	}

	@Override
	public String getName() {
		return annotationFile.getName();
	}
	
	@Override
	public AnnotationSetContents getContents() {
		if(contents == null){
			try(BufferedReader reader = Files.newBufferedReader(
					annotationFile.toPath(), Charset.defaultCharset())){
				
				String jsonStr = "";
				
				while(reader.ready()){
					 jsonStr += reader.readLine();
					 jsonStr += "\n";
				}
				
				AnnotationSetContents newContents = AnnotationSetTranslator.fromJson(jsonStr);
				
				if(newContents != null){
					contents = newContents;
				}
				else{
					//TODO: Throw an exception of some sort
				}
			}
			catch(IOException ioe) {
				return null;
			}
		}
		
		return (AnnotationSetContents)contents.copy();
	}

	@Override
	public boolean updateContents(IProjectDataContents newContents) {
		if(newContents != null && newContents instanceof AnnotationSetContents){
			AnnotationSetContents newAnnotationContents =
					(AnnotationSetContents)newContents;
			
			if(contents == null || contents.compareTo(newAnnotationContents) != 0){
				try(BufferedWriter writer = Files.newBufferedWriter(
						annotationFile.toPath(), Charset.defaultCharset())){
					
					String jsonStr = AnnotationSetTranslator.toJson(newAnnotationContents);
					
					if(jsonStr != null){
						writer.write(jsonStr);
					}
					else{
						//TODO: Throw and exception of some sort
					}
				}
				catch(IOException e){
					return false;
				}
			}
				
			contents = (AnnotationSetContents)newAnnotationContents.copy();
			
			return true;
		}
		
		return false;
	}
	
	@Override
	public void deleteContentsOnDisk() throws IOException{
		Files.deleteIfExists(annotationFile.toPath());
		contents = null;
	}
	
	@Override
	public int compareTo(IProjectData projData) {
		if(projData == null){
			return 1;
		}
		
		return annotationFile.compareTo(projData.getFile());
	}
}
