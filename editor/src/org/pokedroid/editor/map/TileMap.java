package org.pokedroid.editor.map;

import java.awt.Graphics;
import java.io.File;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;
import org.pokedroid.editor.map.exception.InvalidTileException;

/**
 * <p>
 * A {@code TileMap} is a map that will be edited.
 * </p>
 * 
 * @author PoketronHacker
 * @version v1.0
 * @since v1.0
 *
 */
public class TileMap {
	private static final int STANDARD_COUNT = 3;

	private File file;
	private String name;
	private int width, height, layerCount;
	private int[][] tiles;
	private Tileset tileset;

	/**
	 * <p>
	 * Constructs an empty {@code TileMap}.
	 * </p>
	 * 
	 * @param file
	 *            The file.
	 * @param name
	 *            The name of the tilemap.
	 * @param tileset
	 *            The tileset the tile map is using.
	 * @param width
	 *            The width of the tilemap.
	 * @param height
	 *            The height of the tilemap.
	 */
	public TileMap(File file, String name, Tileset tileset, int width, int height) {
		this.file = file;
		this.name = name;
		this.tileset = tileset;
		this.width = width;
		this.height = height;
		this.layerCount = STANDARD_COUNT;
		this.tiles = new int[layerCount][width * height];
		for (int l = 0; l < tiles.length; l++) {
			for (int i = 0; i < tiles[l].length; i++) {
				this.tiles[l][i] = -1;
			}
		}
	}

	/**
	 * <p>
	 * Constructs a {@code TileMap} from a JSONObject.
	 * </p>
	 * 
	 * @param file
	 *            The file.
	 * @param tileset
	 *            The tileset the tile map is using.
	 * @param o
	 *            The {@code JSONObject}.
	 * @param tilesets
	 *            The tilesets to get the tileset from.
	 */
	public TileMap(File file, JSONObject o, Map<String, Tileset> tilesets) {
		this.file = file;
		this.name = o.getString("name");
		this.width = o.getInt("width");
		this.height = o.getInt("height");
		this.layerCount = o.getInt("layers");
		this.tiles = new int[layerCount][width * height];
		JSONArray tileArray = o.getJSONArray("tiles");
		for (int i = 0; i < tileArray.length(); i++) {
			for (int j = 0; j < tileArray.getJSONArray(i).length(); j++)
				tiles[i][j] = tileArray.getJSONArray(i).getInt(j) + o.optInt("id_offset", 0);
		}
		this.tileset = tilesets.get(o.getString("tileset"));
	}

	/**
	 * <p>
	 * Renders a single layer.
	 * </p>
	 * 
	 * @param g
	 *            The graphics.
	 * @param layer
	 *            The layer
	 */
	public void render(Graphics g, int layer) {
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				if (tiles[layer][x + (y * width)] < 0)
					continue;
				g.drawImage(tileset.getTile(tiles[layer][x + (y * width)]), x * tileset.getWidth(),
						y * tileset.getHeight(), null);
			}
		}
	}

	/**
	 * <p>
	 * Gets the {@code TileMap} as a JSON string.
	 * </p>
	 * 
	 * @return The {@code TileMap} as a JSON string.
	 */
	public String toJSON() {
		StringBuilder str = new StringBuilder();
		str.append("{\n");
		str.append("  \"name\": \"" + this.name + "\",\n");
		str.append("  \"tileset\": \"" + this.tileset.toString() + "\",\n");
		str.append("  \"width\": " + this.width + ",\n");
		str.append("  \"height\": " + this.height + ",\n");
		str.append("  \"layers\": " + this.layerCount + ",\n");
		str.append("  \"tiles\": [\n");
		for (int l = 0; l < this.tiles.length; l++) {
			str.append("    [");
			for (int i = 0; i < this.tiles[l].length; i++)
				str.append(this.tiles[l][i] + (i == this.tiles[l].length - 1 ? "" : ", "));
			str.append("]" + (l == this.tiles.length - 1 ? "" : ",") + "\n");
		}
		str.append("  ]\n");
		str.append("}");
		return str.toString();
	}

	/**
	 * <p>
	 * Sets the name of the {@code TileMap}.
	 * </p>
	 * 
	 * @param name
	 *            The name of the map.
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * <p>
	 * Sets the tile at index i on layer l.
	 * </p>
	 * 
	 * @param l
	 *            The layer the tile belongs to.
	 * @param i
	 *            The index of the tile in the map.
	 * @param tileId
	 *            The new tile id.
	 * 
	 * @return The old tile id.
	 * 
	 * @throws ArrayIndexOutOfBoundsException
	 *             Thrown if the tile index is out of bounds.
	 */
	public int setTile(int l, int i, int tileId) {
		if (l < 0 || l >= tiles.length)
			throw new ArrayIndexOutOfBoundsException(l);
		if (i < 0 || i >= tiles[l].length)
			throw new ArrayIndexOutOfBoundsException(i);
		int oldId = tiles[l][i];
		tiles[l][i] = tileId;
		return oldId;
	}

	/**
	 * <p>
	 * Sets the tile at position x, y on layer l.
	 * </p>
	 * 
	 * @param l
	 *            The layer the tile belongs to.
	 * @param x
	 *            The x coordinate.
	 * @param y
	 *            The y coordinate.
	 * @param tileId
	 *            The new tile id.
	 * 
	 * @return The old tile id.
	 * 
	 * @throws ArrayIndexOutOfBoundsException
	 *             Thrown if the tile layer is out of bounds.
	 * @throws InvalidTileException
	 *             Thrown if the x or y is out of bounds.
	 */
	public int setTile(int l, int x, int y, int tileId) {
		if (l < 0 || l >= tiles.length)
			throw new ArrayIndexOutOfBoundsException(l);
		if (x < 0 || x >= width || y < 0 || y >= height)
			throw new InvalidTileException(x, y);
		return setTile(l, x + (y * width), tileId);
	}

	/**
	 * <p>
	 * Gets the tile at index i on layer l.
	 * </p>
	 * 
	 * @param l
	 *            The layer the tile belongs to.
	 * @param i
	 *            The index of the tile.
	 * @return The tile.
	 * 
	 * @throws ArrayIndexOutOfBoundsException
	 *             Thrown if the tile index is out of bounds.
	 */
	public int getTile(int l, int i) {
		if (l < 0 || l >= tiles.length)
			throw new ArrayIndexOutOfBoundsException(l);
		if (i < 0 || i >= tiles[l].length)
			throw new ArrayIndexOutOfBoundsException(i);
		return tiles[l][i];
	}

	/**
	 * <p>
	 * Gets the tile at x, y on layer l.
	 * </p>
	 * 
	 * @param l
	 *            The layer the tile belongs to.
	 * @param x
	 *            The x coordinate of the tile.
	 * @param y
	 *            The y coordinate of the tile.
	 * @return The tile.
	 * 
	 * @throws ArrayIndexOutOfBoundsException
	 *             Thrown if the layer is out of bounds.
	 * @throws InvalidTileException
	 *             Thrown if the tile is out of the width or height.
	 */
	public int getTile(int l, int x, int y) {
		if (l < 0 || l >= tiles.length)
			throw new ArrayIndexOutOfBoundsException(l);
		if (x < 0 || x >= width || y < 0 || y >= height)
			throw new InvalidTileException(x, y);
		return getTile(l, x + (y * width));
	}

	/**
	 * <p>
	 * Whether the x and y location is a valid location.
	 * </p>
	 * 
	 * @param x
	 *            The x location.
	 * @param y
	 *            The y location.
	 * @return Whether it is a valid position.
	 */
	public boolean isValid(int x, int y) {
		return (x >= 0 && x < width && y >= 0 && y < height);
	}

	/**
	 * <p>
	 * Creates a snapshot of the {@code TileMap} for resetting the map to a
	 * previous instance.
	 * </p>
	 * 
	 * @return The array of tiles.
	 */
	public Integer[][] createSnapshot() {
		Integer[][] snapshot = new Integer[layerCount][width * height];
		for (int l = 0; l < layerCount; l++) {
			for (int i = 0; i < snapshot[l].length; i++) {
				snapshot[l][i] = tiles[l][i];
			}
		}
		return snapshot;
	}

	/**
	 * <p>
	 * Loads a snapshot of the {@code TileMap} for reverting back from a
	 * snapshot.
	 * </p>
	 * 
	 * @param snapshot
	 *            The snapshot.
	 */
	public void loadSnapshot(Integer[][] snapshot) {
		for (int l = 0; l < layerCount; l++) {
			for (int i = 0; i < snapshot[l].length; i++) {
				tiles[l][i] = snapshot[l][i];
			}
		}
	}

	/**
	 * <p>
	 * Gets the file of the map.
	 * </p>
	 * 
	 * @return The file of the map.
	 */
	public File getFile() {
		return this.file;
	}

	/**
	 * <p>
	 * Gets the name of the map.
	 * </p>
	 * 
	 * @return The name of the map.
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * <p>
	 * Gets the width of the map.
	 * </p>
	 * 
	 * @return The width of the map.
	 */
	public int getWidth() {
		return this.width;
	}

	/**
	 * <p>
	 * Gets the height of the map.
	 * </p>
	 * 
	 * @return The height of the map.
	 */
	public int getHeight() {
		return this.height;
	}

	/**
	 * <p>
	 * Gets the amount of layers in the map.
	 * </p>
	 * 
	 * @return The amount of layers.
	 */
	public int getLayerCount() {
		return this.layerCount;
	}

	/**
	 * <p>
	 * Gets the map tileset.
	 * </p>
	 * 
	 * @return The map tileset.
	 */
	public Tileset getTileset() {
		return this.tileset;
	}

	@Override
	public String toString() {
		return this.name;
	}

}
