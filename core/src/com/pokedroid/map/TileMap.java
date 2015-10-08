package com.pokedroid.map;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.utils.JsonValue;
import com.pokedroid.entity.Entity;

/**
 * <p>The {@code TileMap} class is used to render an entire map.</p>
 * 
 * @author PoketronHacker
 * @version v1.0
 * @since v1.0
 *
 */
public class TileMap {
	
	private String name;
	private int width, height, layerCount;
	private int[][] tiles;
	private TileSet tileset;
	private List<Entity> entityList;
	
	/**
	 * <p>Creates a new {@code TileMap} from a JSON file.</p>
	 * 
	 * @param map The json value.
	 * @param tileset The tileset this map is using.
	 * @throws MapInitialisationException Thrown if a problem occurs during initialisation.
	 */
	public TileMap(JsonValue map, TileSet tileset) {
		this.tileset = tileset;
		this.entityList = Collections.synchronizedList(new ArrayList<Entity>());
		this.name = map.getString("name");
		this.width = map.getInt("width");
		this.height = map.getInt("height");
		this.layerCount = map.getInt("layers");
		if(map.get("tiles").size != layerCount)
			throw new MapInitialisationException("Layer count mismatch");
		for(int i = 0; i < layerCount; i++)
			if(map.get("tiles").get(i).size != (width*height))
				throw new MapInitialisationException("Tile count mismatch on layer " + i);
		this.tiles = new int[layerCount][width*height];
		for(int l = 0; l < layerCount; l++) {
			for(int y = 0; y < height; y++) {
				for(int x = 0; x < width; x++) {
					tiles[l][x + (y * width)] = map.get("tiles").get(l).getInt(x + (y * width));
				}
			}
		}
	}
	
	/**
	 * <p>Renders this map onto a batch with an offset.</p>
	 * 
	 * @param batch The batch to render onto.
	 * @param l The layer to render.
	 * @param offsetX The x offset.
	 * @param offsetY The y offset.
	 */
	public void render(Batch batch, int l, float offsetX, float offsetY) {
		for(int y = 0; y < height; y++) {
			for(int x = 0; x < width; x++) {
				if(tiles[l][x + (y * width)] == -1)
					continue;
				float rel_y = (height-1)-y;
				batch.draw(tileset.getTile(tiles[l][x + (y * width)]),
						(x*tileset.getWidth())+offsetX, (rel_y*tileset.getHeight())+offsetY);
			}
		}
	}
	
	/**
	 * <p>Gets the width of this {@code TileMap}.</p>
	 * 
	 * @return The width of this {@code TileMap}.</p>
	 */
	public int getWidth() {
		return this.width;
	}
	
	/**
	 * <p>Gets the height of this {@code TileMap}.</p>
	 * 
	 * @return The height of this {@code TileMap}.
	 */
	public int getHeight() {
		return this.height;
	}
	
	/**
	 * <p>Gets the amount of layers this {@code TileMap} has.</p>
	 * 
	 * @return The amount of layers this {@code TileMap} has.</p>
	 */
	public int getLayerCount() {
		return this.layerCount;
	}
	
	/**
	 * <p>Gets a list of entities this map contains.</p>
	 * 
	 * @return A list of entities this map contains.
	 */
	public List<Entity> getEntityList() {
		return this.entityList;
	}
	
	/**
	 * <p>Gets the {@link TileSet} this map is using to render the
	 * tiles.</p>
	 * 
	 * @return The {@link TileSet}.
	 */
	public TileSet getTileset() {
		return this.tileset;
	}
	
	@Override
	public String toString() {
		return name + "(" + width + "," + height + ")";
	}
	
}
