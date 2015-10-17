package org.pokedroid.editor.gui.brush;

import java.awt.Graphics2D;

import org.pokedroid.editor.gui.main.TileSelection;
import org.pokedroid.editor.map.TileMap;

/**
 * <p>A {@code Brush} is used to determine how the paint is going to work.</p>
 * 
 * @author PoketronHacker
 * @version v1.0
 *
 */
public interface Brush {
	
	/**
	 * <p>Draws the overlay of the brush on the screen.</p>
	 * 
	 * @param g The graphics interface.
	 * @param m The map we're editing on.
	 * @param selection The current tile selection.
	 * @param layer The layer to edit on.
	 * @param mX The x mouse location.
	 * @param mY The y mouse location.
	 */
	public void drawOverlay(Graphics2D g, TileMap m, TileSelection selection, int layer, int mX, int mY);
	
	/**
	 * <p>Starts the brush paint on a certain tile.</p>
	 * 
	 * @param m The map we're editing on.
	 * @param selection The tile selection.
	 * @param layer The layer to edit on.
	 * @param x The tile x pressed.
	 * @param y The tile y pressed.
	 * 
	 * @return Whether the map was changed.
	 */
	public boolean startPaint(TileMap m, TileSelection selection, int layer, int x, int y);
	
	/**
	 * <p>Drags the brush on the canvas.</p>
	 * 
	 * @param m The map we're editing on.
	 * @param selection The tile selection.
	 * @param layer The layer to edit on.
	 * @param x The tile x pressed.
	 * @param y The tile y pressed.
	 * 
	 * @return Whether the map was changed.
	 */
	public boolean dragPaint(TileMap m, TileSelection selection, int layer, int x, int y);
	
	/**
	 * <p>Stops the brush on the canvas.</p>
	 * 
	 * @param m The map we're editing on.
	 * @param selection The tile selection.
	 * @param layer The layer to edit on.
	 * @param x The tile x released.
	 * @param y The tile y released.
	 * 
	 * @return Whether the map was changed.
	 */
	public boolean stopPaint(TileMap m, TileSelection selection, int layer, int x, int y);
	
}
