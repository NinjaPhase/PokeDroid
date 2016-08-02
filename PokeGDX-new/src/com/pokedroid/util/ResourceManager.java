package com.pokedroid.util;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.utils.Disposable;

/**
 * <p>The {@code ResourceManager} will be used to
 * handle resources.</p>
 * 
 * @author J. Kitchen
 * @version 10 March 2016
 *
 */
public class ResourceManager implements Disposable {
	
	private String name;
	private ResourceManager parent;
	private Map<Class<?>, Map<String, Disposable>> resources;
	
	/**
	 * <p>Constructs a new empty {@code ResourceManager}.</p>
	 */
	public ResourceManager() {
		this.resources = Collections.synchronizedMap(new HashMap<>());
	}
	
	/**
	 * <p>Constructs a new empty {@code ResourceManager} with
	 * a given parent.</p>
	 * 
	 * @param parent The parent.
	 */
	private ResourceManager(String name, ResourceManager parent) {
		this();
		this.name = name;
		this.parent = parent;
	}
	
	/**
	 * <p>Loads an object into the resource manager.</p>
	 * 
	 * @param key The key of the object.
	 * @param obj The object.
	 * @return The object just loaded for chaining.
	 */
	public <T extends Disposable> T load(String key, T obj) {
		if(!resources.containsKey(obj.getClass()))
			resources.put(obj.getClass(), Collections.synchronizedMap(new HashMap<>()));
		Map<String, Disposable> map = resources.get(obj.getClass());
		if(resources.containsKey(key))
			unload(obj.getClass(), key);
		map.put(key, obj);
		return obj;
	}
	
	/**
	 * <p>Gets an object from the resource manager.</p>
	 * 
	 * @param c The class.
	 * @param key The key of the object.
	 * @return The object if found.
	 */
	@SuppressWarnings("unchecked")
	public <T extends Disposable> T get(Class<T> c, String key) {
		if(!resources.containsKey(c))
			return null;
		return (T) resources.get(c).get(key);
	}

	/**
	 * <p>Unloads an object from the resource manager.</p>
	 * 
	 * @param c The class.
	 * @param key The key of the object.
	 * @return The object unloaded.
	 */
	@SuppressWarnings("unchecked")
	public <T extends Disposable> T unload(Class<T> c, String key) {
		if(!resources.containsKey(c))
			return null;
		Map<String, Disposable> map = resources.get(c);
		T obj = (T) map.remove(key);
		if(resources.get(c).isEmpty())
			resources.remove(c);
		return obj;
	}

	@Override
	public void dispose() {
		if(parent != null && this.name != null)
			parent.unload(ResourceManager.class, name);
		while(!resources.isEmpty()) {
			Class<?> c = resources.keySet().iterator().next();
			Map<String, Disposable> m = resources.get(c);
			while(!m.isEmpty()) {
				String s = m.keySet().iterator().next();
				System.out.println(c.getSimpleName() + " disposing of " + s);
				m.remove(s).dispose();
			}
			resources.remove(c);
		}
	}
	
	/**
	 * <p>Creates a sub resource manager, when this resource
	 * manager is disposed of it will also remove all the
	 * resources of the sub resource manager.</p>
	 * 
	 * @param name The name/key of the resource manager.
	 * @return The resource manager.
	 * 
	 * @throws NullPointerException Thrown if the given name is null.
	 */
	public ResourceManager createSubManager(String name) {
		if(name == null)
			throw new NullPointerException("name cannot be null");
		return load(name, new ResourceManager(name, this));
	}
	
}
