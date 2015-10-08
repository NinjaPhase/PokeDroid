package com.pokedroid.map;

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
	
	private Texture[] textures;
	private TextureRegion[] tiles;
	private int tileWidth, tileHeight;
	
	/**
	 * <p>Constructor for a new {@code TileSet}.</p>
	 * 
	 * @param set The tileset.
	 */
	public TileSet(JsonValue set) {
		this.tileWidth = set.getInt("width");
		this.tileHeight = set.getInt("height");
		JsonValue img = set.get("images");
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
		int index = 0;
		for(int i = 0; i < textures.length; i++) {
			Texture t = textures[i];
			int cols = t.getWidth()/tileWidth;
			int rows = t.getHeight()/tileHeight;
			for(int y = 0; y < rows; y++) {
				for(int x = 0; x < cols; x++) {
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
	
}
