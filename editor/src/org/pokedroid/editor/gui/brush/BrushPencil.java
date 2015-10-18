package org.pokedroid.editor.gui.brush;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;

import org.pokedroid.editor.map.TileMap;

/**
 * <p>
 * The {@code BrushPencil} is a simple pencil brush.
 * </p>
 * 
 * @author PoketronHacker
 * @version 18 October 2015
 *
 */
public class BrushPencil implements Brush {

	@Override
	public void drawOverlay(Graphics2D g, TileMap map, TileSelection sel, int mX, int mY) {
		if (sel.w < 0 || sel.h < 0 || sel.tiles == null)
			return;
		for (int y = 0; y < sel.h; y++) {
			for (int x = 0; x < sel.w; x++) {
				g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.9f));
				g.drawImage(map.getTileset().getTile(sel.tiles[x + (y * sel.w)]), mX * map.getTileset().getWidth(),
						mY * map.getTileset().getHeight(), null);
				g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.33f));
				g.setColor(new Color(0.3f, 0.3f, 1.0f));
				g.fillRect(mX * map.getTileset().getWidth(), mY * map.getTileset().getHeight(),
						map.getTileset().getWidth(), map.getTileset().getHeight());
				g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
			}
		}
	}

	@Override
	public void startPaint(TileMap map, TileSelection sel, int layer, int x, int y) {
		for (int tY = 0; tY < sel.h; tY++) {
			for (int tX = 0; tX < sel.w; tX++) {
				int tId = sel.tiles[tX + (tY * sel.w)];
				if (map.isValid(x + tX, y + tY)) {
					map.setTile(layer, x + tX, y + tY, tId);
				}
			}
		}
	}

	@Override
	public void dragPaint(TileMap map, TileSelection sel, int layer, int x, int y) {
		startPaint(map, sel, layer, x, y);
	}

	@Override
	public void stopPaint(TileMap map, TileSelection sel, int layer, int x, int y) {
		// TODO Auto-generated method stub

	}

	@Override
	public String toString() {
		return "Pencil";
	}

}
