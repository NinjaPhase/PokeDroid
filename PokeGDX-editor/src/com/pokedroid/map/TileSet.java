package com.pokedroid.map;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;

/**
 * <p>A {@code TileSet} is a group of tiles.</p>
 * 
 * @author J. Kitchen
 * @version 09 June 2016
 *
 */
public class TileSet {
	
	private String name;
	private Image[] tiles;
	private int tileWidth, tileHeight;
	
	/**
	 * <p>Constructs a new {@code TileSet}.</p>
	 * 
	 * @param name The name of the tileset.
	 * @param image The image of the tileset.
	 * @param tileWidth The tile width.
	 * @param tileHeight The tile height.
	 */
	public TileSet(String name, Image image, int tileWidth, int tileHeight) {
		this.name = name;
		this.tileWidth = tileWidth;
		this.tileHeight = tileHeight;
		BufferedImage img = toBufferedImage(image);
		int w = (int)Math.ceil((float)img.getWidth()/(float)tileWidth);
		int h = (int)Math.ceil((float)img.getHeight()/(float)tileHeight);
		this.tiles = new Image[w*h];
		for(int y = 0; y < h ; y++) {
			for(int x = 0; x < w; x++) {
				this.tiles[x + (y * w)] = img.getSubimage(x*tileWidth, y*tileWidth, tileWidth, tileHeight);
			}
		}
	}
	
	/**
	 * @return The name of the tileset.
	 */
	public String getName() {
		return this.name;
	}
	
	/**
	 * @param i The tile index.
	 * @return The tile at i.
	 */
	public Image getTile(int i) {
		if(i < 0 || i >= tiles.length)
			throw new ArrayIndexOutOfBoundsException(i);
		return this.tiles[i];
	}
	
	/**
	 * @return The tile count.
	 */
	public int getTileCount() {
		return this.tiles.length;
	}
	
	/**
	 * @return The width of a single tile.
	 */
	public int getTileWidth() {
		return this.tileWidth;
	}
	
	/**
	 * @return The height of a single tile.
	 */
	public int getTileHeight() {
		return this.tileHeight;
	}
	
	@Override
	public String toString() {
		return getName();
	}
	
	/**
	 * <p>Converts an image to a buffered image.</p>
	 * 
	 * @param img The image.
	 * @return The image as a buffered image.
	 */
	public static BufferedImage toBufferedImage(Image img) {
		if(img instanceof BufferedImage)
			return (BufferedImage)img;
		BufferedImage buffered = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);
		Graphics g = buffered.getGraphics();
		g.drawImage(img, 0, 0, null);
		g.dispose();
		return buffered;
	}
	
}
