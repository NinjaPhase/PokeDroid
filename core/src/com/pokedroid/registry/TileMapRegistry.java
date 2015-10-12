package com.pokedroid.registry;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.pokedroid.map.TileMap;

/**
 * <p>The {@code TileMapRegistry} is the collection of {@code TileMap}'s.</p>
 * 
 * @author PoketronHacker
 * @version v1.0
 * @since v1.0
 *
 */
public final class TileMapRegistry {
	
	private static final Map<String, TileMap> MAPS = Collections.synchronizedMap(new HashMap<String, TileMap>());
	
	/** <p>Private Constructor.</p> */
	private TileMapRegistry() {}
	
	/**
	 * <p>Registers the {@code TileMap} to the registry.</p>
	 * 
	 * @param key The key of the map.
	 * @param map The map.
	 * @return The tile map just loaded.
	 */
	public static TileMap registerMap(String key, TileMap map) {
		if(key == null)
			throw new NullPointerException("key given was null");
		if(map == null)
			throw new NullPointerException("map given was null");
		MAPS.put(key, map);
		return map;
	}
	
	/**
	 * <p>Gets the {@code TileMap} with a certain key.</p>
	 * 
	 * @param key The key of the {@code TileMap}.</p>
	 * @return The tile map.
	 */
	public static TileMap getMap(String key) {
		return MAPS.get(key);
	}
	
}
