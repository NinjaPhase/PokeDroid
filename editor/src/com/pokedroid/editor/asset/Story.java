package com.pokedroid.editor.asset;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import org.json.JSONObject;
import org.json.JSONTokener;
import com.pokedroid.editor.map.TileMap;
import com.pokedroid.editor.map.TileSet;

import javax.imageio.ImageIO;

/**
 * <p>
 * The {@code Story} is used to keep track of the folder tree we are
 * currently using and create a {@code JTree} from it.
 * </p>
 * 
 * @author PoketronHacker
 * @version 10/10/2015
 *
 */
public class Story {

	private String name;
	private File file;
	private Map<String, BufferedImage> images;
	private Map<String, TileMap> maps;
	private Map<String, TileSet> tilesets;

	/**
	 * <p>Constructs a new story.</p>
	 *
	 * @param name The name of the story.
     */
	public Story(String name) {
		this.name = name;
		this.images = Collections.synchronizedMap(new LinkedHashMap<String, BufferedImage>());
		this.maps = Collections.synchronizedMap(new LinkedHashMap<String, TileMap>());
		this.tilesets = Collections.synchronizedMap(new LinkedHashMap<String, TileSet>());
	}

	/**
	 * <p>
	 * Constructor for a new {@code AssetFolder}, it will store the asets within
	 * a map.
	 * </p>
	 * 
	 * @param f
	 *            The directory or zip file.
	 */
	public Story(File f) {
		this("Loaded Story");
		this.file = f;
		this.loadImagesFromDirectory(f, "");
		this.loadTilesetFromDirectory(f);
		this.loadMapFromDirectory(f);
	}

	/**
	 * <p>Loads images from a directory.</p>
	 * @param dir The directory.
	 * @param path The path.
     */
	private void loadImagesFromDirectory(File dir, String path) {
		if(dir == null)
			throw new NullPointerException("directory given was null.");
		for (File f : dir.listFiles()) {
			if (f.isDirectory()) {
				loadImagesFromDirectory(f, path + f.getName() + "/");
			} else {
				if (!f.getName().endsWith(".png"))
					continue;
				try {
					this.images.put(f.getName(), ImageIO.read(f));
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
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
		if(dir == null)
			throw new NullPointerException("directory given was null.");
		for (File f : dir.listFiles()) {
			if (f.isDirectory()) {
				loadMapFromDirectory(f);
			} else {
				if (!f.getName().endsWith(".mapDef"))
					continue;
				try {
					FileReader fr = new FileReader(f);
					TileMap m = new TileMap(
							new JSONObject(new JSONTokener(fr)),
							tilesets);
					maps.put(m.getName(), m);
					fr.close();
				} catch (IOException e) {
					e.printStackTrace();
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
		if(dir == null)
			throw new NullPointerException("directory given was null.");
		for (File f : dir.listFiles()) {
			if (f.isDirectory()) {
				loadTilesetFromDirectory(f);
			} else {
				if (!f.getName().endsWith(".tilesetDef"))
					continue;
				try {
					FileReader fr = new FileReader(f);
					JSONObject o = new JSONObject(new JSONTokener(fr));
					tilesets.put(o.getString("name"), new TileSet(o, this.images));
					fr.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * <p>Saves this story to the file it's on.</p>
	 */
	public void save() {
		save(file);
	}

	/**
	 * <p>Saves this story to the file/directory.</p>
	 *
	 * @param f The file.
     */
	public void save(File f) {
		this.file = f;
		if(f.isDirectory()) {
			File assetDir = new File(f, "./assets/");
			File mapDir = new File(assetDir, "./maps/");
			File tilesetDir = new File(assetDir, "./tilesets/");
			File imageDir = new File(assetDir, "./images/");
			if(!assetDir.exists())
				assetDir.mkdirs();
			if(!mapDir.exists())
				mapDir.mkdirs();
			if(!tilesetDir.exists())
				tilesetDir.mkdirs();
			if(!imageDir.exists())
				imageDir.mkdirs();
			delete(f, new String[]{".tilesetDef", ".mapDef", ".png"});
			for(String key : images.keySet()) {
				String name = key.substring(key.lastIndexOf('/')+1, key.length());
				String ext = key.substring(key.lastIndexOf('.')+1, key.length());
				try {
					ImageIO.write(images.get(key), ext, new File(imageDir, name));
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			for(String key : tilesets.keySet()) {
				try {
					FileWriter fw = new FileWriter(new File(tilesetDir, key.toLowerCase().replaceAll(" ", "_") + ".tilesetDef"));
					fw.write(tilesets.get(key).toJSON());
					fw.close();
				} catch (IOException e) {
					Thread.getDefaultUncaughtExceptionHandler().uncaughtException(
							Thread.currentThread(), e.getCause());
				}
			}
			for(String key : maps.keySet()) {
				try {
					FileWriter fw = new FileWriter(new File(mapDir, key.toLowerCase().replaceAll(" ", "_") + ".mapDef"));
					fw.write(maps.get(key).toJSON());
					fw.close();
				} catch (IOException e) {
					Thread.getDefaultUncaughtExceptionHandler().uncaughtException(
							Thread.currentThread(), e.getCause());
				}
			}
		} else {

		}
	}

	private void delete(File dir, String[] ext) {
		for(File f : dir.listFiles()) {
			if(f.isDirectory()) {
				delete(f, ext);
			} else {
				for(String s : ext) {
					if(f.getName().endsWith(s))
						f.delete();
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
	public Map<String, TileSet> getTileSets() {
		return this.tilesets;
	}

	/**
	 * <p>
	 * Gets the map list.
	 * </p>
	 * 
	 * @return The map list.
	 */
	public Map<String, TileMap> getMaps() {
		return this.maps;
	}

	/**
	 * @return The images.
     */
	public Map<String, BufferedImage> getImages() {
		return this.images;
	}

	/**
	 * @return The name of the story.
     */
	public String getName() {
		return this.name;
	}

	/**
	 * @return The file of the story for saving and reloading.
     */
	public File getFile() {
		return this.file;
	}

	@Override
	public String toString() {
		return getName();
	}

}
