package pokejava.plugin;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import exampleplugin.SamplePlugin;
import pokejava.api.events.EnableEvent;
import pokejava.api.events.EventHandler;
import pokejava.api.plugin.Plugin;

/**
 * A plugin manager which will be used to load in plugins, access plugin features and check
 * whether certain plugins exist.
 * 
 * @author PoketronHacker
 * @version 1.0
 *
 */
public class PluginManager {
	
	private Map<String, PluginContainer> plugins;
	
	public PluginManager() {
		SamplePlugin plugin = new SamplePlugin();
		plugins = new HashMap<String, PluginContainer>();
		Plugin plugAnn = plugin.getClass().getAnnotation(Plugin.class);
		if(plugAnn != null) {
			plugins.put(plugAnn.id(), new PluginContainer(plugAnn, plugin));
		}
		for(PluginContainer p : plugins.values()) {
			if(p.getHook(HookType.ENABLE_HOOK) != null) {
				try {
					p.getHook(HookType.ENABLE_HOOK).invoke(p.getInstance(), new EnableEvent(){});
				} catch (IllegalAccessException | IllegalArgumentException
						| InvocationTargetException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	
	public static void main(String[] args) {
		new PluginManager();
	}
	
}
