package com.pokedroid.story;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.SerializationException;
import com.pokedroid.map.TileMap;
import com.pokedroid.map.TileSet;
import com.pokedroid.util.ResourceManager;

/**
 * <p>A {@code Story} is a separate game that is
 * created using either the editor or raw files.</p>
 * 
 * @author J. Kitchen
 * @version 15 March 2016
 *
 */
public class Story implements Disposable {

	private String name;
	private String author;
	private FileHandle storyFile;
	private ResourceManager resourceManager;
	private List<String> flavours;

	/**
	 * <p>Constructs an empty {@code Story}.</p>
	 * 
	 * @param file The main .zip or directory.
	 */
	public Story(FileHandle file) {
		this.storyFile = file;
		this.resourceManager = new ResourceManager();
		this.flavours = Collections.synchronizedList(new ArrayList<>());
		getInfo(file);
	}

	/**
	 * <p>Loads the full story.</p>
	 */
	public void load() {
		if(storyFile.isDirectory()) {
			loadStoryFromDirectory(storyFile, "/", new ArrayDeque<>(), new ArrayDeque<>());
		} else if(storyFile.extension().toLowerCase().equalsIgnoreCase("zip")) {
			loadStoryFromZip(storyFile);
		} else throw new StoryLoadException("Story is incompatable type " + storyFile.extension());
	}

	/**
	 * <p>Gets the information for the story.</p>
	 * 
	 * @param file The file.
	 */
	private void getInfo(FileHandle file) {
		if(file.isDirectory()) {
			parseInfoFile(file.list(".storyDef")[0].read());
		} else if(file.extension().toLowerCase().equalsIgnoreCase("zip")) {
			loadStoryFromZip(file);
		} else throw new StoryLoadException("Story is incompatible type " + file.extension());
	}

	/**
	 * <p>Gets the information of the story from a
	 * stream.</p>
	 * 
	 * @param stream The stream.
	 */
	private void parseInfoFile(InputStream stream) {
		JsonValue json = null;
		InputStreamReader isr = null;
		BufferedReader br = null;
		try {
			isr = new InputStreamReader(stream);
			br = new BufferedReader(isr);

			StringBuilder b = new StringBuilder();
			String str;
			while((str = br.readLine()) != null) b.append(str);

			json = new JsonReader().parse(b.toString());
		} catch (SerializationException e) {
			throw new StoryLoadException("Unable to correctly parse JSON", e.getCause());
		} catch (IOException e) {
			throw new StoryLoadException("Unable to read info file streams", e.getCause());
		} finally {
			try {
				br.close();
				isr.close();
			} catch (IOException e) {
				throw new StoryLoadException("Unable to close file streams", e.getCause());
			}
		}
		if(json == null)
			throw new StoryLoadException("null JSON story info");
		try {
		this.name = json.getString("name");
		this.author = json.getString("author");
		for(JsonValue v : json.get("flavours"))
			this.flavours.add(v.asString());
		} catch (IllegalArgumentException e) {
			throw new StoryLoadException("Unable to load info file", e.getCause());
		}
	}

	/**
	 * <p>Constructs a story from a zip files.</p>
	 * 
	 * @param file The file.
	 */
	private void loadStoryFromZip(FileHandle file) {

	}

	/**
	 * <p>Constructs a story from a directory.</p>
	 * 
	 * @param dir The directory.
	 */
	private void loadStoryFromDirectory(FileHandle dir, String path,
										Queue<FileHandle> tilesets, Queue<FileHandle> tilemaps) {
		for(FileHandle f : dir.list()) {
			if(f.isDirectory())
				loadStoryFromDirectory(f, path + f.name() + '/', tilesets, tilemaps);
			else {
				if(f.extension().equalsIgnoreCase("tileDef"))
					tilesets.add(f);
				else if(f.extension().equalsIgnoreCase("mapDef"))
					tilemaps.add(f);
				else if(f.extension().equalsIgnoreCase("png"))
					resourceManager.load(path + f.name(), new Texture(f));
			}
		}
		if(path.equals("/")) {
			FileHandle f;
			while((f = tilesets.poll()) != null) {
				TileSet ts = new TileSet(this, new JsonReader().parse(f));
				resourceManager.load(ts.getName(), ts);
			}
			while((f = tilemaps.poll()) != null) {
				TileMap map = new TileMap(this, new JsonReader().parse(f));
				resourceManager.load(map.getName(), map);
			}
		}
	}

	@Override
	public void dispose() {
		resourceManager.dispose();
	}
	
	/**
	 * <p>Gets the name of this {@code Story}.</p>
	 * 
	 * @return The name of this {@code Story}.
	 */
	public String getName() {
		return this.name;
	}
	
	/**
	 * <p>Gets the author of this {@code Story}.</p>
	 * 
	 * @return The author of this {@code Story}.
	 */
	public String getAuthor() {
		return this.author;
	}
	
	/**
	 * <p>Gets the currently available flavours.</p>
	 * 
	 * @return The currently available flavours.
	 */
	public List<String> getFlavours() {
		return this.flavours;
	}

	/**
	 * @return The {@link ResourceManager} for this story.
	 */
	public ResourceManager getResourceManager() { return this.resourceManager; }
	
	@Override
	public String toString() {
		return "(" + getName() + " by " + getAuthor() + ")";
	}

}
