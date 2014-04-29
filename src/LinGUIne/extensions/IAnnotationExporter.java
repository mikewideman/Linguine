package LinGUIne.extensions;

import java.io.File;
import java.io.IOException;
import java.util.Collection;

import LinGUIne.model.AnnotationSet;
import LinGUIne.model.IProjectData;

/**
 * Interface to which all Annotation export extension classes must conform.
 * 
 * @author Kyle Mullins
 */
public interface IAnnotationExporter {
	
	/**
	 * Returns a human-readable description of the type of file which the
	 * exporter creates. Ex. 'XML File'
	 */
	String getFileType();
	
	/**
	 * Returns a file mask used to filter files for the exporter to export to.
	 * Ex. *.xml
	 */
	String getFileMask();
	
	/**
	 * Returns a collection of IProjectData types that this IAnnotationExporter
	 * supports.
	 * Note: The types returned should be as specific as possible.
	 */
	Collection<Class<? extends IProjectData>> getSupportedSourceDataTypes();
	
	/**
	 * Converts the given Annotation to this IAnnotationExporter's destination
	 * format and places it into the given destination File.
	 * 
	 * @param sourceData	The Annotated Data to be exported.
	 * @param annotations	The Annotations for the sourceData.
	 * @param destFile		The File into which the exported data should be
	 * 						placed.
	 * 
	 * @throws IOException
	 */
	void exportAnnotation(IProjectData sourceData, AnnotationSet annotations,
			File destFile) throws IOException;
}
