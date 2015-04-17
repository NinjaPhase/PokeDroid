package pokejava.plugin;

import java.lang.reflect.Method;
import java.util.EnumMap;
import java.util.Map;

import pokejava.api.events.DisableEvent;
import pokejava.api.events.EnableEvent;
import pokejava.api.events.EventHandler;
import pokejava.api.plugin.Plugin;

/**
 * The plugin container used to hold a plugin, this also holds an instance
 * of the plugin for future use and all the hooks which can be used.
 * 
 * @author Joshua Adam Kitchen
 * @version 1.0
 *
 */
public class PluginContainer {

	private Object instance;
	private Plugin plugin;

	private Map<HookType, Method> hooks;

	/**
	 * The container of this plugin.
	 * 
	 * @param plugin The plugin annotation.
	 * @param instance The instance of the plugin.
	 */
	public PluginContainer(Plugin plugin, Object instance) {
		this.plugin = plugin;
		this.instance = instance;
		this.hooks = new EnumMap<HookType, Method>(HookType.class);
		try {
			this.parseHooks();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Gets the hook of a plugin
	 * 
	 * @param type The type of hook
	 * @return The hook method to invoke
	 */
	public Method getHook(HookType type) {
		return this.hooks.get(type);
	}

	/**
	 * Parses the known hooks and adds them to the method list ready to be invoked.
	 * @throws ClassNotFoundException 
	 */
	private void parseHooks() throws ClassNotFoundException {
		Class<?> c = Class.forName(instance.getClass().getName());
		for(Method m : c.getDeclaredMethods()) {
			if(m.getAnnotation(EventHandler.class) != null) {
				if(m.getParameterCount() == 1) {
					if(m.getParameters()[0].getType().equals(EnableEvent.class)) {
						hooks.put(HookType.ENABLE_HOOK, m);
						System.out.println("[" + plugin.name() + "] Added " + m.getName() + " to enable hook.");
					} else if(m.getParameters()[0].getType().equals(DisableEvent.class)) {
						hooks.put(HookType.DISABLE_HOOK, m);
						System.out.println("[" + plugin.name() + "] Added " + m.getName() + " to disable hook.");
					}
				}
			}
		}
	}

	/**
	 * @return The plugin annotation of the plugin.
	 */
	public Plugin getPluginDetails() {
		return this.plugin;
	}

	/**
	 * @return An instance of the plugin.
	 */
	public Object getInstance() {
		return this.instance;
	}

}
