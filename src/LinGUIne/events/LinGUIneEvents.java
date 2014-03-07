package LinGUIne.events;

/**
 * Constants used for sending and subscribing to LinGUIne-sourced events.
 * 
 * @author Kyle Mullins
 */
public class LinGUIneEvents {
	
	/**
	 * Separator between event topics.
	 */
	public static final String SEP = "/";
	
	/**
	 * Wildcard used to subscribe to an entire topic.
	 */
	public static final String ALL = "*";
	
	/**
	 * Base topic for all LinGUIne events.
	 */
	public static final String BASE_TOPIC = "LinGUIne" + SEP + "events";
	
	/**
	 * Used to subscribe to all LinGUIne events.
	 */
	public static final String ALL_LINGUINE_EVENTS = BASE_TOPIC + SEP + ALL;
	
	/**
	 * Event strings used for Project-related events.
	 */
	public static interface Project {
		
		/**
		 * Base topic for all Project-related events.
		 */
		public static final String PROJECT_BASE_TOPIC = BASE_TOPIC + SEP + "Project";
		
		/**
		 * Used to subscribe to all Project-related events.
		 */
		public static final String ALL_EVENTS = PROJECT_BASE_TOPIC + SEP + ALL;
		
		/**
		 * Sent when a Project is added to the workspace.
		 */
		public static final String ADDED = PROJECT_BASE_TOPIC + SEP + "Added";
		
		/**
		 * Sent when a Project is removed from the workspace.
		 */
		public static final String REMOVED = PROJECT_BASE_TOPIC + SEP + "Removed";
		
		/**
		 * Sent when a Project is modified.
		 */
		public static final String MODIFIED = PROJECT_BASE_TOPIC + SEP + "Modified";
	}
	
	/**
	 * Event strings used to for UI-related events.
	 */
	public static interface UILifeCycle {
		
		/**
		 * Base topic for all UI-related events.
		 */
		public static final String UI_EVENTS_BASE_TOPIC = BASE_TOPIC + SEP + "UI";
		
		/**
		 * Used to subscribe to all UI-related events.
		 */
		public static final String ALL_EVENTS = UI_EVENTS_BASE_TOPIC + SEP + ALL;
		
		/**
		 * Sent when the user attempts to open some ProjectData.
		 */
		public static final String OPEN_PROJECT_DATA = UI_EVENTS_BASE_TOPIC + SEP + "OpenProjectData";
	}
}