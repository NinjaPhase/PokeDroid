package com.pokedroid.map;

import java.util.Map;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.JsonValue;

/**
 * <p>A {@code TileSet} is a large image that is composed of numerous smaller images
 * to form a tileset. The reason there are small images is due to the fact that the
 * engine on a mobile device cannot handle images larger than 2048x2048.</p>
 * 
 * @author PoketronHacker
 * @version v1.0
 * @since v1.0
 *
 */
public class TileSet implements Disposable {
	private static final int SOLID_FLAG = 0x1, ENCOUNTER_FLAG = 0x2,
			LEDGE_FLAG_DOWN = 0x4, LEDGE_FLAG_LEFT = 0x8, LEDGE_FLAG_RIGHT = 0x16;
	
	private String name;
	private Texture[] textures;
	private TextureRegion[] tiles;
	private int[] tileData;
	private int tileWidth, tileHeight;
	
	/**
	 * <p>Constructor for a new {@code TileSet}.</p>
	 * 
	 * @param set The tileset.
	 * @param textureMap A texturemap of textures for the tileset to use.
	 */
	public TileSet(JsonValue set, Map<String, Texture> textureMap) {
		this.name = set.getString("name");
		this.tileWidth = set.getInt("width");
		this.tileHeight = set.getInt("height");
		JsonValue img = set.get("images");
		JsonValue data = set.get("data");
		textures = new Texture[img.size];
		for(int i = 0; i < img.size; i++) {
			textures[i] = textureMap.get(img.getString(i));
		}
		int size = 0;
		for(int i = 0; i < textures.length; i++) {
			Texture t = textures[i];
			int cols = t.getWidth()/tileWidth;
			int rows = t.getHeight()/tileHeight;
			size += (cols*rows);
		}
		tiles = new TextureRegion[size];
		tileData = new int[size];
		int index = 0;
		for(int i = 0; i < textures.length; i++) {
			Texture t = textures[i];
			int cols = t.getWidth()/tileWidth;
			int rows = t.getHeight()/tileHeight;
			for(int y = 0; y < rows; y++) {
				for(int x = 0; x < cols; x++) {
					if(data.has(String.valueOf(index))) {
						tileData[index] = data.getInt(String.valueOf(index));
					}
					tiles[index++] = new TextureRegion(t, x*tileWidth, y*tileHeight, tileWidth, tileHeight);
				}
			}
		}
	}
	
	/**
	 * <p>Constructor for a new {@code TileSet}.</p>
	 * 
	 * @param set The tileset.
	 */
	public TileSet(JsonValue set) {
		this.name = set.getString("name");
		this.tileWidth = set.getInt("width");
		this.tileHeight = set.getInt("height");
		JsonValue img = set.get("images");
		JsonValue data = set.get("data");
		textures = new Texture[img.size];
		for(int i = 0; i < img.size; i++) {
			textures[i] = new Texture(img.getString(i));
		}
		int size = 0;
		for(int i = 0; i < textures.length; i++) {
			Texture t = textures[i];
			int cols = t.getWidth()/tileWidth;
			int rows = t.getHeight()/tileHeight;
			size += (cols*rows);
		}
		tiles = new TextureRegion[size];
		tileData = new int[size];
		int index = 0;
		for(int i = 0; i < textures.length; i++) {
			Texture t = textures[i];
			int cols = t.getWidth()/tileWidth;
			int rows = t.getHeight()/tileHeight;
			for(int y = 0; y < rows; y++) {
				for(int x = 0; x < cols; x++) {
					if(data.has(String.valueOf(index))) {
						tileData[index] = data.getInt(String.valueOf(index));
					}
					tiles[index++] = new TextureRegion(t, x*tileWidth, y*tileHeight, tileWidth, tileHeight);
				}
			}
		}
	}
	
	@Override
	public void dispose() {
		for(Texture t : textures)
			t.dispose();
		textures = null;
	}
	
	/**
	 * <p>Gets whether a tile with the index of i is solid.</p>
	 * 
	 * @param i The index of the tile.
 	 * @return Whether the tile is solid.
	 */
	public boolean isSolid(int i) {
		if(i < 0 || i >= tileData.length)
			return true;
		return (this.tileData[i] & SOLID_FLAG) > 0;
	}
	
	/**
	 * <p>Gets whether a tile with the index of i is an encounter
	 * tile.</p>
	 * 
	 * @param i The index of the tile.
	 * @return Whether the tile is encounterable.
	 */
	public boolean isEncounter(int i) {
		if(i < 0 || i >= tileData.length)
			return false;
		return (this.tileData[i] & ENCOUNTER_FLAG) > 0;
	}

	/**
	 * <p>Gets whether a tile with the index of i is a downward
	 * ledge</p>
	 * 
	 * @param i The index of the tile.
	 * @return Whether the tile is a downward ledge.
	 */
	public boolean isDownLedge(int i) {
		if(i < 0 || i >= tileData.length)
			return false;
		return (this.tileData[i] & LEDGE_FLAG_DOWN) > 0;
	}

	/**
	 * <p>Gets whether a tile with the index of i is a left
	 * ledge</p>
	 * 
	 * @param i The index of the tile.
	 * @return Whether the tile is a left ledge.
	 */
	public boolean isLeftLedge(int i) {
		if(i < 0 || i >= tileData.length)
			return false;
		return (this.tileData[i] & LEDGE_FLAG_LEFT) > 0;
	}

	/**
	 * <p>Gets whether a tile with the index of i is a right
	 * ledge</p>
	 * 
	 * @param i The index of the tile.
	 * @return Whether the tile is a right ledge.
	 */
	public boolean isRightLedge(int i) {
		if(i < 0 || i >= tileData.length)
			return false;
		return (this.tileData[i] & LEDGE_FLAG_RIGHT) > 0;
	}
	
	/**
	 * <p>Gets a tile with the index of i.</p>
	 * 
	 * @param i The tile index.
	 * @return A {@code TextureRegion} of that index.
	 */
	public TextureRegion getTile(int i) {
		return this.tiles[i];
	}
	
	/**
	 * <p>Gets the width of an individual tile.</p>
	 * 
	 * @return The width of an individual tile.
	 */
	public int getWidth() {
		return this.tileWidth;
	}
	
	/**
	 * <p>Gets the height of an individual tile.</p>
	 * 
	 * @return The height of an individual tile.
	 */
	public int getHeight() {
		return this.tileHeight;
	}
	
	@Override
	public String toString() {
		return this.name;
	}
	
}
