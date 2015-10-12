package com.pokedroid.registry;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.pokedroid.map.TileSet;

/**
 * <p>The {@code TilesetRegistry} is used to hold {@link TileSet}'s.</p>
 * 
 * @author PoketronHacker
 * @version 11 October 2015
 *
 */
public final class TilesetRegistry {
	
	private static Map<String, TileSet> TILESETS = Collections.synchronizedMap(new HashMap<String, TileSet>());
	
	/** <p>Private Constructor.</p> */
	private TilesetRegistry() {}
	
	/**
	 * <p>Registers a {@link TileSet}.</p>
	 * 
	 * @param key The key of the {@link TileSet}.
	 * @param tileset The {@link TileSet} to register.
	 * @return The {@link TileSet} just registered.
	 * 
	 * @throws NullPointerException Thrown if the key or tileset is null.
	 */
	public static TileSet registerTileset(String key, TileSet tileset) {
		if(key == null)
			throw new NullPointerException("key given was null");
		if(tileset == null)
			throw new NullPointerException("tileset given was null");
		TILESETS.put(key, tileset);
		return tileset;
	}
	
	/**
	 * <p>Gets a {@link TileSet} in the registry.</p>
	 * 
	 * @param key The key of the tileset.
	 * @return The tileset, if it doesn't exist then {@code null}.
	 */
	public static TileSet getTileset(String key) {
		return TILESETS.get(key);
	}
	
	/**
	 * <p>Releases all tileset resources.</p>
	 * 
	 */
	public static void dispose() {
		for(TileSet t : TILESETS.values()) t.dispose();
		TILESETS.clear();
	}
	
}
