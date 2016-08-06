package com.pokedroid.editor.map;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

import org.json.JSONException;
import org.json.JSONObject;
import com.pokedroid.editor.util.ImageUtils;

/**
 * <p>
 * A {@code TileSet} is a collection of tiles.
 * </p>
 * 
 * @author PoketronHacker
 * @version v1.0
 * @since v1.0
 *
 */
public class TileSet {

	private String name;
	private String image;
	private int width, height;
	private Image[] tiles;

	/**
	 * <p>
	 * Constructs an empty {@code TileSet}.
	 * </p>
	 * 
	 * @param width
	 *            The width of each tile.
	 * @param height
	 *            The height of each tile.
	 */
	public TileSet(String name, int width, int height) {
		this.name = name;
		this.width = width;
		this.height = height;
		this.image = null;
		this.tiles = null;
	}

	/**
	 * <p>
	 * Loads a {@code TileSet} from a {@code JSONObject}.
	 * </p>
	 * 
	 * @param o
	 *            The object to load.
	 * @param images
	 *            The image map of the story.
	 */
	public TileSet(JSONObject o, Map<String, BufferedImage> images) {
		this(o.getString("name"), o.getInt("width"), o.getInt("height"));
		this.image = o.getString("image");
		BufferedImage img = ImageUtils.toBuffered(images.get(o.getString("image")));
		int cols = img.getWidth()/this.width;
		int rows = img.getHeight()/this.height;
		this.tiles = new Image[cols*rows];
		for(int y = 0; y < rows; y++) {
			for(int x = 0; x < cols; x++) {
				this.tiles[x + (y * cols)] = img.getSubimage(x*width, y*height, width, height);
			}
		}
	}

	/**
	 * <p>
	 * Gets the amount of tiles within the tileset.
	 * </p>
	 * 
	 * @return The amount of tiles within the tileset.
	 */
	public int length() {
		return (tiles == null ? 0 : tiles.length);
	}

	/**
	 * <p>
	 * Gets a tile image.
	 * </p>
	 * 
	 * @param i
	 *            The tile id.
	 * @return The tile image.
	 */
	public Image getTile(int i) {
		return this.tiles[i];
	}

	/**
	 * <p>
	 * Gets the width of a single tile.
	 * </p>
	 * 
	 * @return The width of a single tile.
	 */
	public int getWidth() {
		return this.width;
	}

	/**
	 * <p>
	 * Gets the height of a single tile.
	 * </p>
	 * 
	 * @return The height of a single tile.
	 */
	public int getHeight() {
		return this.height;
	}

	/**
	 * <p>Converts this TileSet into a json file.</p>
	 *
	 * @return The json file.
     */
	public String toJSON() {
		StringBuilder str = new StringBuilder();
		str.append("{\n");
		str.append("\t\"name\": \"" + this.name + "\",\n");
		str.append("\t\"image\": \"" + this.image + "\",\n");
		str.append("\t\"width\": " + this.width + ",\n");
		str.append("\t\"height\": " + this.height + ",\n");
		str.append("\t\"data\": {}\n");
		str.append("}");
		return str.toString();
	}

	@Override
	public String toString() {
		return this.name;
	}

}
