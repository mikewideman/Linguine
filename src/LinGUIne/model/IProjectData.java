package LinGUIne.model;

import java.io.File;

/**
 * Interface to which all ProjectData must conform.
 * 
 * @author Kyle Mullins
 */
public interface IProjectData extends Comparable<IProjectData> {

	File getFile();
}
