package com.pokedroid.editor.gui.brush;

import java.awt.Graphics2D;

import com.pokedroid.editor.map.TileMap;

/**
 * <p>
 * A {@code Brush} is used to paint onto a tilemap.
 * </p>
 * 
 * @author PoketronHacker
 * @version 18 October 2015
 *
 */
public interface Brush {

	/**
	 * <p>
	 * The overlay to draw for the brush.
	 * </p>
	 * 
	 * @param g
	 *            The graphics object to draw onto.
	 * @param map
	 *            The map being worked on.
	 * @param sel
	 *            The currently selected tiles.
	 * @param mX
	 *            The mouse tile x.
	 * @param mY
	 *            The mouse tile y.
	 */
	void drawOverlay(Graphics2D g, TileMap map, TileSelection sel, int mX, int mY);

	/**
	 * <p>
	 * Called when the mouse is clicked on a tile.
	 * </p>
	 */
	void startPaint(TileMap map, TileSelection sel, int layer, int x, int y);

	/**
	 * <p>
	 * Called when the mouse is dragged.
	 * </p>
	 */
	void dragPaint(TileMap map, TileSelection sel, int layer, int x, int y);

	/**
	 * <p>
	 * Called when the mouse is removed.
	 * </p>
	 */
	void stopPaint(TileMap map, TileSelection sel, int layer, int x, int y);

}
