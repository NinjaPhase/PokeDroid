package pokejava.api.plugin;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;

/**
 * The plugin annotation which will be used to create new plugins, this will then be converted
 * to a plugin interface.
 * 
 * @author PoketronHacker
 * @version 1.0
 *
 */
@Retention(RUNTIME)
public @interface Plugin {
	
	/**
	 * An internal Id for this plugin to be identified by. This will be useful
	 * when checking for Id conflicts
	 * 
	 * @return The plugin id
	 */
	String id();
	
	/**
	 * The name of this plugin to be shown to users who have the plugin installed.
	 * 
	 * @return The plugin name
	 */
	String name();
	
	/**
	 * The version of this plugin
	 * 
	 * @return The plugin version
	 */
	String version() default "unknown";
	
	/**
	 * The author of this plugin
	 * 
	 * @return The plugin author
	 */
	String author() default "unknown";
	
}
