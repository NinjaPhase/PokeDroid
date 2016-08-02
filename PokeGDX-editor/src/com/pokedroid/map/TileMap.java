package com.pokedroid.map;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * <p>A {@code TileMap} is a 
 * @author Joshua
 *
 */
public class TileMap {

	private String name;
	private TileSet tileset;
	
	private int width, height;
	private int[][] tiles;
	
	private TileMap parent;
	private List<TileMap> children;
	
	/**
	 * <p>Constructs a new {@code TileMap}.</p>
	 * 
	 * @param name The name.
	 */
	public TileMap(String name, TileSet tileset, int width, int height) {
		this.children = Collections.synchronizedList(new ArrayList<>());
		this.name = name;
		this.tileset = tileset;
		this.width = width;
		this.height = height;
		this.tiles = new int[3][width*height];
		for(int i = 0; i < width*height; i++) {
			tiles[0][i] = 0;
			tiles[1][i] = -1;
			tiles[2][i] = -1;
		}
	}
	
	/**
	 * <p>Renders a layer onto the graphics.</p>
	 * 
	 * @param g The graphics.
	 * @param layer The layer.
	 * @param xPos The x position.
	 * @param yPos The y position.
	 */
	public void render(Graphics g, int layer, int xPos, int yPos) {
		for(int y = 0; y < height; y++) {
			for(int x = 0; x < width; x++) {
				if(tiles[layer][x + (y * width)] == -1)
					continue;
				g.drawImage(tileset.getTile(tiles[layer][x + (y * width)]),
						xPos+(x * tileset.getTileWidth()),
						yPos+(y * tileset.getTileHeight()), null);
			}
		}
	}

	/**
	 * <p>Adds a parent map.</p>
	 * 
	 * @param map The map.
	 */
	public void setParent(TileMap map) {
		if(parent == map)
			return;
		if(parent != null) {
			parent.children.remove(this);
		}
		parent = map;
		if(parent != null && !parent.children.contains(this)) {
			parent.children.add(this);
		}
	}

	/**
	 * <p>Adds a child map.</p>
	 * 
	 * @param child The child.
	 */
	public void addChild(TileMap child) {
		if(child.parent == this)
			return;
		else if(child.parent != null) {
			child.parent.children.remove(child);
		}
		child.parent = this;
		if(child.parent != null && !child.parent.children.contains(this)) {
			child.parent.children.add(child);
		}
	}

	/**
	 * <p>Gets the parent map.</p>
	 * 
	 * @return The parent map.
	 */
	public TileMap getParent() {
		return this.parent;
	}

	/**
	 * <p>Gets the child map.</p>
	 * 
	 * @return The child map.
	 */
	public List<TileMap> getChildren() {
		return this.children;
	}
	
	/**
	 * @return The render width.
	 */
	public int getRenderWidth() {
		return this.width * tileset.getTileWidth();
	}
	
	/**
	 * @return Gets the render height.
	 */
	public int getRenderHeight() {
		return this.height * tileset.getTileHeight();
	}
	
	/**
	 * @return The tileset.
	 */
	public TileSet getTileSet() {
		return this.tileset;
	}
	
	@Override
	public String toString() {
		return this.name;
	}

}
