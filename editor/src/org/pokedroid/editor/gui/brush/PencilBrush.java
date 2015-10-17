package org.pokedroid.editor.gui.brush;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;

import org.pokedroid.editor.gui.main.TileSelection;
import org.pokedroid.editor.map.TileMap;
import org.pokedroid.editor.map.Tileset;

public class PencilBrush implements Brush {

	public static final PencilBrush PENCIL_BRUSH = new PencilBrush();

	private PencilBrush() {}

	@Override
	public void drawOverlay(Graphics2D g, TileMap m, TileSelection selection, int layer, int mX, int mY) {
		Tileset t = m.getTileset();
		for(int tY = 0; tY < selection.h; tY++) {
			for(int tX = 0; tX < selection.w; tX++) {
				if(!m.isValid(mX+tX, mY+tY))
					continue;
				int selectedTile = selection.tiles[tX+(tY*selection.w)];
				g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.75f));
				if(selectedTile >= 0 && selectedTile < t.length())
					g.drawImage(t.getTile(selectedTile), (mX+tX)*t.getWidth(), (mY+tY)*t.getHeight(), null);
				g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.33f));
				g.setColor(new Color(0x4444AA));
				g.fillRect((mX+tX)*t.getWidth(), (mY+tY)*t.getHeight(), t.getWidth(), t.getHeight());
				g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
			}
		}
	}

	@Override
	public boolean startPaint(TileMap m, TileSelection selection, int layer, int x, int y) {
		boolean change = false;
		for(int tY = 0; tY < selection.h; tY++) {
			for(int tX = 0; tX < selection.w; tX++) {
				int tId = selection.tiles[tX + (tY * selection.h)];
				if(m.isValid(x+tX, y+tY) && m.getTile(layer, x+tX, y+tY) != tId) {
					m.setTile(layer, x+tX, y+tY, tId);
					change = true;
				}
			}
		}
		return change;
	}

	@Override
	public boolean dragPaint(TileMap m, TileSelection selection, int layer, int x, int y) {
		boolean change = false;
		for(int tY = 0; tY < selection.h; tY++) {
			for(int tX = 0; tX < selection.w; tX++) {
				int tId = selection.tiles[tX + (tY * selection.h)];
				if(m.isValid(x+tX, y+tY) && m.getTile(layer, x+tX, y+tY) != tId) {
					m.setTile(layer, x+tX, y+tY, tId);
					change = true;
				}
			}
		}
		return change;
	}

	@Override
	public boolean stopPaint(TileMap m, TileSelection selection, int layer, int x, int y) {
		return false;
	}
	
	@Override
	public String toString() {
		return "Pencil";
	}

}
