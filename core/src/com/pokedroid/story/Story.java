package com.pokedroid.story;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Collections;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.pokedroid.map.TileMap;
import com.pokedroid.map.TileSet;

/**
 * <p>A {@code Story} is loaded from a ZipFile or a directory and it determines
 * the maps that will be played on and the character that will be used.</p>
 * 
 * @author PoketronHacker
 * @version 28 October 2015
 *
 */
public class Story implements Disposable {

	private String storyName;
	private FileHandle fileHandle;
	private JsonValue storyJson;
	private Map<String, TileMap> mapList;
	private Map<String, TileSet> tilesetList;
	private Map<String, Texture> textureList;
	private Map<String, Music> musicList;
	private TileMap startMap;
	private Texture playerTexture;
	private int startX, startY;

	/**
	 * <p>Constructor for a new {@code Story}.</p>
	 * 
	 * @param fileHandle The file handle the story is in.
	 */
	public Story(FileHandle fileHandle) {
		if(fileHandle == null)
			throw new NullPointerException("file handle cannot be null");
		this.fileHandle = fileHandle;
		this.mapList = Collections.synchronizedMap(new HashMap<String, TileMap>());
		this.tilesetList = Collections.synchronizedMap(new HashMap<String, TileSet>());
		this.textureList = Collections.synchronizedMap(new HashMap<String, Texture>());
		this.musicList = Collections.synchronizedMap(new HashMap<String, Music>());
		if(fileHandle.isDirectory()) {
			loadStoryFromDirectory(fileHandle);
		} else if(fileHandle.extension().equalsIgnoreCase("zip")) {
			loadStoryFromZip(fileHandle);
		}
	}

	/**
	 * <p>Loads the {@code Story} data from a directory.</p>
	 * 
	 * @param dir The directory of the story.
	 */
	protected void loadStoryFromDirectory(FileHandle dir) {
		this.storyJson = new JsonReader().parse(dir.child("story.json"));
		this.storyName = this.storyJson.getString("name");
		traverseDirectory(dir, Texture.class);
		traverseDirectory(dir, Music.class);
		traverseDirectory(dir, TileSet.class);
		traverseDirectory(dir, TileMap.class);
		for(TileMap m : mapList.values())
			m.linkMaps(mapList);
		startMap = mapList.get(storyJson.get("start_map").getString(0));
		startX = storyJson.get("start_map").getInt(1);
		startY = storyJson.get("start_map").getInt(2);
		playerTexture = textureList.get(storyJson.get("characters").getString(0));
		
	}

	protected void traverseDirectory(FileHandle dir, Class<?> c) {
		for(FileHandle f : dir.list()) {
			if(f.isDirectory()) {
				traverseDirectory(f, c);
			} else {
				if(c.equals(Texture.class)) {
					String name = f.path().substring(fileHandle.path().length()+1);
					if(name.toLowerCase().endsWith(".png") || name.toLowerCase().endsWith(".jpg")
							|| name.toLowerCase().endsWith(".jpeg")
							|| name.toLowerCase().endsWith(".bmp")) {
						Texture tex = new Texture(f);
						this.textureList.put(name, tex);
					}
				} else if(c.equals(TileMap.class)) {
					String name = f.name();
					if(name.endsWith(".json") && name.startsWith("map_")) {
						System.out.println("Loading Tilemap: " + name);
						JsonValue v = new JsonReader().parse(f);
						TileMap m = new TileMap(v, tilesetList.get(v.getString("tileset")));
						m.linkMusic(musicList);
						mapList.put(m.getName(), m);
					}
				} else if(c.equals(TileSet.class)) {
					String name = f.name();
					if(name.endsWith(".json") && name.startsWith("set_")) {
						System.out.println("Loading Tileset: " + name);
						TileSet ts = new TileSet(new JsonReader().parse(f), textureList);
						tilesetList.put(ts.toString(), ts);
					}
				} else if(c.equals(Music.class)) {
					String name = f.path().substring(fileHandle.path().length()+1);
					if(name.toLowerCase().endsWith(".mp3") || name.toLowerCase().endsWith(".wav")) {
						System.out.println("Loading Music: " + name);
						Music m = Gdx.audio.newMusic(f);
						musicList.put(name, m);
					}
				}
			}
		}
	}

	/**
	 * <p>Loads the {@code Story} data from a ZipFile.</p>
	 * 
	 * @param file The file to load from.
	 */
	protected void loadStoryFromZip(FileHandle file) {
		ZipInputStream zis = new ZipInputStream(file.read());
		ZipEntry ze;
		try {
			this.storyJson = new JsonReader().parse(findFile(zis, "story.json"));
			this.storyName = this.storyJson.getString("name");
			zis = new ZipInputStream(file.read());
			while((ze = zis.getNextEntry()) != null) {
				if(ze.isDirectory()) continue;
				String name = (ze.getName().contains("/")) ? ze.getName().substring(ze.getName().lastIndexOf("/")+1) :
					ze.toString();
				if(name.toLowerCase().endsWith(".png") || name.toLowerCase().endsWith(".jpg")
						|| name.toLowerCase().endsWith(".jpeg")
						|| name.toLowerCase().endsWith(".bmp")) {
					byte[] b = readFully(zis);
					Texture tex = new Texture(new Pixmap(b, 0, b.length));
					textureList.put(ze.getName(), tex);
				}
			}
			zis.close();
			zis = new ZipInputStream(file.read());
			while((ze = zis.getNextEntry()) != null) {
				if(ze.isDirectory()) continue;
				String name = (ze.getName().contains("/")) ? ze.getName().substring(ze.getName().lastIndexOf("/")+1) :
					ze.toString();
				if(name.endsWith(".json") && name.startsWith("set_")) {
					TileSet ts = new TileSet(new JsonReader().parse(new String(readFully(zis))), textureList);
					tilesetList.put(ts.toString(), ts);
				}
			}
			zis.close();
			zis = new ZipInputStream(file.read());
			while((ze = zis.getNextEntry()) != null) {
				if(ze.isDirectory()) continue;
				String name = (ze.getName().contains("/")) ? ze.getName().substring(ze.getName().lastIndexOf("/")+1) :
					ze.toString();
				if(name.endsWith(".json") && name.startsWith("map_")) {
					JsonValue v = new JsonReader().parse(new String(readFully(zis)));
					TileMap m = new TileMap(v, tilesetList.get(v.getString("tileset")));
					mapList.put(m.getName(), m);
				}
			}
			for(TileMap m : mapList.values())
				m.linkMaps(mapList);
			startMap = mapList.get(storyJson.get("start_map").getString(0));
			startX = storyJson.get("start_map").getInt(1);
			startY = storyJson.get("start_map").getInt(2);
			playerTexture = textureList.get(storyJson.get("characters").getString(0));
		} catch (IOException e) {
			System.err.println("[Story] Unable to read ZipFile(" + file.name() + "): " + e.getMessage());
			e.printStackTrace();
		} finally {
			try {
				zis.close();
			} catch (IOException e) {
				System.err.println("[Story] Unable to close ZipFile(" + file.name() + "): " + e.getMessage());
				e.printStackTrace();
			}
		}
	}

	/**
	 * <p>Reads a specific file within a ZipInputStream.</p>
	 * 
	 * @param zis The zip input stream.
	 * @param fileName The file name.
	 * 
	 * @return An array of bytes as data.
	 * @throws IOException Thrown if an IOException occurs.
	 */
	protected byte[] readFile(ZipInputStream zis, String fileName) throws IOException {
		InputStream is = findFile(zis, fileName);
		byte[] b = null;
		if(is != null)
			b = readFully(is);
		zis.close();
		return b;
	}

	/**
	 * <p>Finds a specific file within a ZipInputStream.</p>
	 * 
	 * @param zis The input stream.
	 * @return The {@code InputStream} of the zip entry.
	 * 
	 * @throws IOException Thrown if an IOException occurs.
	 */
	protected InputStream findFile(ZipInputStream zis, String fileName) throws IOException {
		ZipEntry ze;
		while((ze = zis.getNextEntry()) != null) {
			if(ze.toString().endsWith("/")) continue;
			if(ze.toString().equals(fileName))
				return zis;
		}
		return null;
	}

	/**
	 * <p>Fully reads the entry within a InputStream as bytes.</p>
	 * 
	 * @param zis The input stream.
	 * @return The entry just read.
	 * 
	 * @throws IOException Thrown if an IOException occurs.
	 */
	protected byte[] readFully(InputStream zis) throws IOException {
		byte[] tempBuffer = new byte[2048];
		int readBytes = 0;
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		while((readBytes = zis.read(tempBuffer)) != -1) {
			bos.write(tempBuffer, 0, readBytes);
		}
		return bos.toByteArray();
	}

	@Override
	public void dispose() {
		for(Music m : musicList.values())
			m.dispose();
		for(TileSet t : tilesetList.values())
			t.dispose();
		for(Texture t : textureList.values())
			t.dispose();
		tilesetList.clear();
		musicList.clear();
		textureList.clear();
	}

	/**
	 * <p>Gets the starting map.</p>
	 * 
	 * @return The starting map.
	 */
	public TileMap getStartMap() {
		return this.startMap;
	}

	/**
	 * <p>Gets the starting X point on the map.</p>
	 * 
	 * @return The starting X point on the map.
	 */
	public int getStartX() {
		return this.startX;
	}

	/**
	 * <p>Gets the starting Y point on the map.</p>
	 * 
	 * @return The starting Y point on the map.
	 */
	public int getStartY() {
		return this.startY;
	}

	/**
	 * <p>Gets the players texture.</p>
	 * 
	 * @return The players texture.
	 */
	public Texture getPlayerTexture() {
		return this.playerTexture;
	}

	/**
	 * <p>Gets the {@code HashMap} of maps.</p>
	 * 
	 * @return The {@code HashMap} of maps.
	 */
	public Map<String, TileMap> getMaps() {
		return this.mapList;
	}

	@Override
	public String toString() {
		return this.storyName;
	}

}
