package LinGUIne.model;

import java.io.BufferedReader;
import java.io.BufferedWriter;

import LinGUIne.extensions.IEditorSettings;
import LinGUIne.extensions.VisualizationView;

/**
 * Superclass for contents objects which hold data for a specific type of
 * Visualization.
 * 
 * @author Kyle Mullins
 */
public abstract class VisualResultContents implements IProjectDataContents {

	/**
	 * Subclasses must implement a no-args constructor.
	 */
	public VisualResultContents(){}
	
	/**
	 * Parses the data in the given stream and populates the
	 * VisualResultContents instance from it.
	 * 
	 * @param reader	An input stream from which the data should be parsed.
	 * 
	 * @return	True if the contents could be parsed or there was a problem with
	 * 			the given stream, false otherwise.
	 */
	public abstract boolean parse(BufferedReader reader);
	
	/**
	 * Composes the VisualResultContents instance into a parsable form and
	 * writes it to the given stream.
	 * Note: The data written should be sufficient to recreate the
	 * VisualResultContents instance.
	 * 
	 * @param writer	An output stream to which the composed data should be
	 * 					written.
	 * 
	 * @return	True if the contents could not be composed or there was a
	 * 			problem with the given stream, false otherwise.
	 */
	public abstract boolean compose(BufferedWriter writer);
	
	/**
	 * Returns the VisualizationView that displays this contents' data.
	 */
	public abstract VisualizationView getVisualizationView();
	
	/**
	 * Returns true iff this contents' view has settings to be displayed.
	 */
	public abstract boolean hasSettings();
	
	/**
	 * Returns an IEditorSettings instance for this contents' view.
	 */
	public abstract IEditorSettings getSettings();
	
	@Override
	public abstract IProjectDataContents copy();
	
	@Override
	public abstract int compareTo(IProjectDataContents arg0);

	@Override
	public Class<? extends IProjectData> getAssociatedDataType() {
		return VisualResult.class;
	}	
}
