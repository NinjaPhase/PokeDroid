package org.pokedroid.editor.asset;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;
import org.json.JSONTokener;
import org.pokedroid.editor.map.TileMap;
import org.pokedroid.editor.map.Tileset;

/**
 * <p>
 * The {@code AssetFolder} is used to keep track of the folder tree we are
 * currently using and create a {@code JTree} from it.
 * </p>
 * 
 * @author PoketronHacker
 * @version 10/10/2015
 *
 */
public class GameFolder {

	@SuppressWarnings("unused")
	private File dir, assetDir, mapFolder;
	private List<TileMap> maps;
	private Map<String, Tileset> tilesets;

	/**
	 * <p>
	 * Constructor for a new {@code AssetFolder}, it will store the asets within
	 * a map.
	 * </p>
	 * 
	 * @param dir
	 *            The directory.
	 */
	public GameFolder(File dir) {
		this.dir = dir;
		this.assetDir = new File(dir, "./assets/");
		this.mapFolder = new File(assetDir, "./maps/");
		this.maps = Collections.synchronizedList(new ArrayList<TileMap>());
		this.tilesets = Collections.synchronizedMap(new HashMap<String, Tileset>());
		this.refresh();
	}

	/**
	 * <p>
	 * Refresh's the entire asset folder.
	 * </p>
	 */
	public void refresh() {
		refreshTilesets();
		refreshMap();
	}

	/**
	 * <p>
	 * Refreshes the {@link TileMap}'s.
	 * </p>
	 */
	public void refreshMap() {
		this.maps.clear();
		this.loadMapFromDirectory(assetDir);
	}

	/**
	 * <p>
	 * Refreshes the {@link TileMap}'s.
	 * </p>
	 */
	public void refreshTilesets() {
		this.tilesets.clear();
		this.loadTilesetFromDirectory(assetDir);
	}

	/**
	 * <p>
	 * Refreshes the map list.
	 * </p>
	 * 
	 * @param dir
	 *            The directory to load.
	 */
	private void loadMapFromDirectory(File dir) {
		for (File f : dir.listFiles()) {
			if (f.isDirectory()) {
				loadMapFromDirectory(f);
			} else {
				if (!f.getName().contains("_"))
					continue;
				String tag = f.getName().substring(0, f.getName().indexOf("_"));
				if (tag.equalsIgnoreCase("map")) {
					try {
						maps.add(new TileMap(f, new JSONObject(new JSONTokener(new FileReader(f))), tilesets));
					} catch (FileNotFoundException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

	/**
	 * <p>
	 * Loads tilesets from a directory.
	 * </p>
	 * 
	 * @param dir
	 *            The directory.
	 */
	private void loadTilesetFromDirectory(File dir) {
		for (File f : dir.listFiles()) {
			if (f.isDirectory()) {
				loadTilesetFromDirectory(f);
			} else {
				if (!f.getName().contains("_"))
					continue;
				String tag = f.getName().substring(0, f.getName().indexOf("_"));
				if (tag.equalsIgnoreCase("set")) {
					try {
						JSONObject o = new JSONObject(new JSONTokener(new FileReader(f)));
						tilesets.put(o.getString("name"), new Tileset(o, assetDir));
					} catch (FileNotFoundException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

	/**
	 * <p>
	 * Gets the tileset map.
	 * </p>
	 * 
	 * @return The tileset map.
	 */
	public Map<String, Tileset> getTilesets() {
		return this.tilesets;
	}

	/**
	 * <p>
	 * Gets the map list.
	 * </p>
	 * 
	 * @return The map list.
	 */
	public List<TileMap> getMaps() {
		return this.maps;
	}

	/**
	 * <p>
	 * Gets the folder to put all the maps in.
	 * </p>
	 * 
	 * @return The folder to put all maps in.
	 */
	public File getMapFolder() {
		return this.mapFolder;
	}

}
