package org.pokedroid.editor.map;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import javax.imageio.ImageIO;

import org.json.JSONException;
import org.json.JSONObject;
import org.pokedroid.editor.util.ImageUtils;

/**
 * <p>A {@code Tileset} is a collection of tiles.</p>
 * 
 * @author PoketronHacker
 * @version v1.0
 * @since v1.0
 *
 */
public class Tileset {

	private String name;
	private List<File> images;
	private int width, height;
	private Image[] tiles;

	/**
	 * <p>Constructs an empty {@code Tileset}.</p>
	 * 
	 * @param width The width of each tile.
	 * @param height The height of each tile.
	 */
	public Tileset(String name, int width, int height) {
		this.images = Collections.synchronizedList(new ArrayList<File>());
		this.name = name;
		this.width = width;
		this.height = height;
		this.tiles = null;
	}

	/**
	 * <p>Loads a {@code Tileset} from a {@code JSONObject}.</p>
	 * 
	 * @param o The object to load.
	 * @param assetFolder The asset folder location.
	 */
	public Tileset(JSONObject o, File assetFolder) {
		this(o.getString("name"), o.getInt("width"), o.getInt("height"));
		for(int i = 0; i < o.getJSONArray("images").length(); i++) {
			try {
				addImage(new File(assetFolder, o.getJSONArray("images").getString(i)));
			} catch (JSONException | IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * <p>Adds an image to the tileset.</p>
	 * 
	 * @param imageFile The image to add.
	 * @throws IOException Thrown if an IO error occurs
	 */
	public void addImage(File imagefile) throws IOException {
		Image img = ImageIO.read(imagefile);
		BufferedImage[] t = ImageUtils.splitImage(img, this.width, this.height);
		if(tiles != null) {
			int startIndex = tiles.length;
			this.tiles = Arrays.copyOf(this.tiles, this.tiles.length+t.length);
			for(int i = startIndex; i < tiles.length; i++)
				tiles[i] = t[i-startIndex];
		} else {
			tiles = new Image[t.length];
			for(int i = 0; i < tiles.length; i++)
				tiles[i] = t[i];
		}
		this.images.add(imagefile);
	}

	/**
	 * <p>Removes an image from the tileset</p>
	 * 
	 * @param imageFile The image to remove.
	 * @throws IOException Thrown if an IO error occurs
	 */
	public void removeImage(File imageFile) throws IOException {
		reloadImages();
	}

	/**
	 * <p>Reloads the files and resets the tile array.</p>
	 * 
	 * @throws IOException Thrown if an IO error occurs
	 */
	public void reloadImages() throws IOException {

	}

	/**
	 * <p>Gets the amount of tiles within the tileset.</p>
	 * 
	 * @return The amount of tiles within the tileset.
	 */
	public int length() {
		return (tiles == null ? 0 : tiles.length);
	}
	
	/**
	 * <p>Gets a tile image.</p>
	 * 
	 * @param i The tile id.
	 * @return The tile image.
	 */
	public Image getTile(int i) {
		return this.tiles[i];
	}

	/**
	 * <p>Gets the width of a single tile.</p>
	 * 
	 * @return The width of a single tile.
	 */
	public int getWidth() {
		return this.width;
	}

	/**
	 * <p>Gets the height of a single tile.</p>
	 * 
	 * @return The height of a single tile.
	 */
	public int getHeight() {
		return this.height;
	}
	
	@Override
	public String toString() {
		return this.name;
	}

}
