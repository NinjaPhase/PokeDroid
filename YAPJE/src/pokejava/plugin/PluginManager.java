package pokejava.plugin;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import pokejava.api.events.EnableEvent;
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
	private static final String PLUGIN_FOLDER = "./plugins/";

	private Map<String, PluginContainer> plugins;

	/**
	 * Constructor for a new plugin manager.
	 */
	public PluginManager() {
		plugins = new HashMap<String, PluginContainer>();
		loadPluginFolder(new File(PLUGIN_FOLDER));
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
	
	/**
	 * Loads a group of Jar's from a Plugin Folder
	 * 
	 * @param dir The directory to load.
	 */
	public void loadPluginFolder(File dir) {
		if(!dir.isDirectory())
			return;
		for(File f : dir.listFiles()) {
			if(f.isFile() && f.getName().endsWith(".jar")) {
				loadJarFile(f);
			}
		}
	}

	/**
	 * Loads a plugin from a JarFile.
	 * 
	 * @param file The file to load.
	 */
	public void loadJarFile(File file) {
		JarFile jf = null;
		URLClassLoader classLoader = null;
		try {
			jf = new JarFile(file);
			URL[] urls = new URL[]{new URL("jar:file://" + file.getPath() + "!/")};
			classLoader = new URLClassLoader(urls);
			for(Enumeration<JarEntry> jenum = jf.entries(); jenum.hasMoreElements();) {
				JarEntry je = jenum.nextElement();
				if(je.getName().endsWith(".class")) {
					String className = je.getName().replaceAll("/", ".").substring(0, je.getName().length()-6);
					try {
						Class<?> c = classLoader.loadClass(className);
						if(c.getAnnotation(Plugin.class) != null) {
							Plugin p = c.getAnnotation(Plugin.class);
							Object o = c.getConstructor().newInstance();
							plugins.put(p.id(), new PluginContainer(p, o));
						}
					} catch (ClassNotFoundException e) {
						System.err.println("[PluginManager] Unable to load class: " + className);
						e.printStackTrace();
					} catch (InstantiationException e) {
						System.err.println("[PluginManager] Unable to instantiate class: " + className);
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						System.err.println("[PluginManager] Illegal Access: " + className);
						e.printStackTrace();
					} catch (IllegalArgumentException e) {
						System.err.println("[PluginManager] Cannot pass params: " + className);
						e.printStackTrace();
					} catch (InvocationTargetException e) {
						System.err.println("[PluginManager] Unable to invoke contructor: " + className);
						e.printStackTrace();
					} catch (NoSuchMethodException e) {
						System.err.println("[PluginManager] Cannot find default constructor: " + className);
						e.printStackTrace();
					} catch (SecurityException e) {
						System.err.println("[PluginManager] Security Exception: " + className);
						e.printStackTrace();
					}
				}
			}
		} catch (IOException e) {
			System.err.println("[PluginManager] Unable to load JarFile: " + file.getName());
			e.printStackTrace();
		} finally {
			try {
				if(jf != null) jf.close();
				jf = null;
				if(classLoader != null) classLoader.close();
				classLoader = null;
			} catch (IOException e) {
				System.err.println("[PluginManager] Unable to close JarFile stream.");
				e.printStackTrace();
			}
		}
	}

	public static void main(String[] args) {
		new PluginManager();
	}

}
