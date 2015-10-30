package com.pokedroid.map;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.badlogic.gdx.audio.Music;
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
	private Map<String, MapConnection> connectionMaps;
	private JsonValue mapJson;
	private Music music;

	/**
	 * <p>Creates a new {@code TileMap} from a JSON file.</p>
	 * 
	 * @param map The json value.
	 * @param tileset The tileset this map is using.
	 * @throws MapInitialisationException Thrown if a problem occurs during initialisation.
	 */
	public TileMap(JsonValue map, TileSet tileset) {
		this.mapJson = map;
		this.tileset = tileset;
		this.entityList = Collections.synchronizedList(new ArrayList<Entity>());
		this.connectionMaps = Collections.synchronizedMap(new HashMap<String, MapConnection>());
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
					int rel_y = (height-1)-y;
					tiles[l][x + (rel_y * width)] = map.get("tiles").get(l).getInt(x + (y * width))+map.getInt("id_offset", 0);
				}
			}
		}
	}

	/**
	 * <p>The event to do on entry of this {@code TileMap}.</p>
	 */
	public void onEnter() {
		if(music != null && !music.isPlaying()) {
			music.setLooping(true);
			music.play();
		}
	}

	/**
	 * <p>The event to do on exit of this {@code TileMap}.</p>
	 */
	public void onExit() {
		if(music != null && music.isPlaying()) music.stop();
	}

	/**
	 * <p>Links music to this {@code TileMap}.</p>
	 * 
	 * @param music The music map to get music from.
	 */
	public void linkMusic(Map<String, Music> music) {
		if(!mapJson.has("music"))
			return;
		this.music = music.get(mapJson.getString("music"));
	}

	/**
	 * <p>Links the maps with a map list, this is used to ensure
	 * all maps are loaded.</p>
	 * 
	 * @param maps The maps to link.
	 */
	public void linkMaps(Map<String, TileMap> maps) {
		connectionMaps.clear();
		if(!mapJson.has("connections"))
			return;
		JsonValue conn = mapJson.get("connections");
		for(int i = 0; i < conn.size; i++) {
			JsonValue connection = conn.get(i);
			if(connection.name.endsWith("_offset")) continue;
			if(maps.containsKey(connection.asString())) {
				connectionMaps.put(connection.name, new MapConnection(maps.get(connection.asString()),
						conn.getInt(connection.name + "_offset", 0)));
				System.out.println("Successfully connected map: " +
						toString() + " to " + maps.get(connection.asString()).toString() + " with offset " +
						connectionMaps.get(connection.name).offset);
			} else throw new NullPointerException("unable to find map connection " +
					connection.asString() + " in " + toString());
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
				batch.draw(tileset.getTile(tiles[l][x + (y * width)]),
						(x*tileset.getWidth())+offsetX, (y*tileset.getHeight())+offsetY);
			}
		}
	}

	/**
	 * <p>Renders the connections the player can move onto.</p>
	 * 
	 * @param batch The batch to render onto.
	 * @param l The layer to render.
	 * @param offsetX The offset x.
	 * @param offsetY The offset y.
	 */
	public void renderConnections(Batch batch, int l, float offsetX, float offsetY) {
		if(connectionMaps.containsKey("south")) {
			connectionMaps.get("south").map
				.render(batch, l, offsetX
						+connectionMaps.get("south").offset*connectionMaps.get("south").map.getTileset().getWidth(),
						(-connectionMaps.get("south").map.getFullHeight())+offsetY);
		}
		if(connectionMaps.containsKey("north")) {
			connectionMaps.get("north").map
				.render(batch, l, offsetX
						+connectionMaps.get("north").offset*connectionMaps.get("north").map.getTileset().getWidth(),
						(getFullHeight())+offsetY);
		}
	}

	/**
	 * <p>Checks whether the tile at x, y is solid or not, this
	 * tests all layers.</p>
	 * 
	 * @param x The x coordinate.
	 * @param y The y coordinate.
	 * @return Whether the player can move there.
	 */
	public boolean canMove(int x, int y) {
		if(x < 0 || x >= width
				|| y < 0 || y >= height)
			return false;
		for(int l = 0; l < layerCount; l++) {
			if(tiles[l][x + (y * width)] == -1) continue;
			if(tileset.isSolid(tiles[l][x + (y * width)]))
				return false;
		}
		return true;
	}

	/**
	 * <p>Checks whether the {@code TileMap} has a connection in a certain
	 * direction.</p>
	 * 
	 * @param connectionName The name of the connection.
	 * @return Whether the {@code TileMap} has a connection.
	 */
	public boolean hasConnection(String connectionName) {
		return connectionMaps.containsKey(connectionName);
	}

	/**
	 * <p>Gets the {@code MapConnection} with the connection name.</p>
	 * 
	 * @param connectionName The name of the connection.
	 * @return The {@code MapConnection}.
	 */
	public MapConnection getConnection(String connectionName) {
		return connectionMaps.get(connectionName);
	}

	/**
	 * <p>Gets the name of this {@code TileMap}.</p>
	 * 
	 * @return The name of this {@code TileMap}.
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * <p>Gets the tile at an x and y.</p>
	 * 
	 * @param l The layer.
	 * @param x The x position.
	 * @param y The y position.
	 * @return The tile at x and y.
	 */
	public int getTile(int l, int x, int y) {
		if(l < 0 || l >= tiles.length)
			return -1;
		if(x < 0 || x >= width || y < 0 || y >= height)
			return -1;
		return tiles[l][x + (y * width)];
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
	 * <p>Gets the full width in pixels.</p>
	 * 
	 * @return The full width in pixels.
	 */
	public float getFullWidth() {
		return this.width * this.tileset.getWidth();
	}

	/**
	 * <p>Gets the full height in pixels.</p>
	 * 
	 * @return The full height in pixels.
	 */
	public int getFullHeight() {
		return this.height * this.tileset.getHeight();
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
	
	public static final class MapConnection {
		public final TileMap map;
		public final int offset;
		public MapConnection(TileMap map, int offset) {
			this.map = map;
			this.offset = offset;
		}
	}

}
