package com.pokedroid.story;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.SerializationException;
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
	private ResourceManager resourceManager;
	private List<String> flavours;
	private FileHandle storyFile;

	/**
	 * <p>Constructs an empty {@code Story}.</p>
	 * 
	 * @param file The main .zip or directory.
	 */
	public Story(FileHandle file) {
		this.resourceManager = new ResourceManager();
		this.flavours = Collections.synchronizedList(new ArrayList<>());
		getInfo(file);
	}

	/**
	 * <p>Loads the full story.</p>
	 */
	public void load() {
		if(storyFile.isDirectory()) {
			loadStoryFromDirectory(storyFile);
		} else if(storyFile.extension().toLowerCase() == "zip") {
			loadStoryFromZip(storyFile);
		} else throw new StoryLoadException("Story is incompatable type " + storyFile.extension());
	}

	/**
	 * <p>Gets the information for the story.</p>
	 * 
	 * @param file The file.
	 */
	protected void getInfo(FileHandle file) {
		if(file.isDirectory()) {
			for(FileHandle f : file.list(".storyDef")) {
				parseInfoFile(f.read());
				break;
			}
		} else if(file.extension().toLowerCase() == "zip") {
			loadStoryFromZip(file);
		} else throw new StoryLoadException("Story is incompatable type " + file.extension());
	}

	/**
	 * <p>Gets the information of the story from a
	 * stream.</p>
	 * 
	 * @param stream The stream.
	 */
	protected void parseInfoFile(InputStream stream) {
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
	protected void loadStoryFromZip(FileHandle file) {

	}

	/**
	 * <p>Constructs a story from a directory.</p>
	 * 
	 * @param dir The directory.
	 */
	protected void loadStoryFromDirectory(FileHandle dir) {

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
	
	@Override
	public String toString() {
		return "(" + getName() + " by " + getAuthor() + ")";
	}

}
