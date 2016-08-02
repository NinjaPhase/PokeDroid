package com.pokedroid.story;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.imageio.ImageIO;

import com.pokedroid.map.TileMap;
import com.pokedroid.map.TileSet;

/**
 * <p>Constructs a new {@code Story}.</p>
 * 
 * @author J. Kitchen
 * @version 16 March 2016
 *
 */
public class Story {
	
	private String name;
	private List<TileMap> mapList;
	private List<TileSet> tileList;
	
	/**
	 * <p>Constructs a new {@code Story}.</p>
	 * 
	 * @param name The name of the story.
	 */
	public Story(String name) {
		this.name = name;
		this.tileList = Collections.synchronizedList(new ArrayList<>());
		try {
			this.tileList.add(new TileSet("Johto", ImageIO.read(new File("./PokeDroid/Graphics/tileset.png")), 32, 32));
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.mapList = Collections.synchronizedList(new ArrayList<>());
		this.mapList.add(new TileMap("Kanto", tileList.get(0), 20, 16));
	}
	
	/**
	 * <p>Gets the overall map list.</p>
	 * 
	 * @return The overall map list.
	 */
	public List<TileMap> getMapList() {
		return this.mapList;
	}
	
	/**
	 * @return The list of tilesets.
	 */
	public List<TileSet> getTileList() {
		return this.tileList;
	}
	
	@Override
	public String toString() {
		return this.name;
	}
	
}
