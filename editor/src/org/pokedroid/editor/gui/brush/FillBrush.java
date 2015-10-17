package org.pokedroid.editor.gui.brush;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayDeque;
import java.util.Queue;

import org.pokedroid.editor.gui.main.TileSelection;
import org.pokedroid.editor.map.TileMap;

public class FillBrush implements Brush {
	
	public static final FillBrush FILL_BRUSH = new FillBrush();
	
	private Queue<Integer> fillQueue;
	
	private FillBrush() {
		this.fillQueue = new ArrayDeque<>();
	}

	@Override
	public void drawOverlay(Graphics2D g, TileMap m, TileSelection selection, int layer, int mX, int mY) {
		this.fillQueue.clear();
		this.buildQueue(m, layer, mX, mY, m.getTile(layer, mX, mY), selection.tiles[0], this.fillQueue);
		for(Integer i : fillQueue) {
			int x = i.intValue()%m.getWidth();
			int y = i.intValue()/m.getWidth();
			int tId = selection.tiles[i.intValue()%selection.tiles.length];
			g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.75f));
			if(tId >= 0 && tId < m.getTileset().length())
				g.drawImage(m.getTileset().getTile(tId), x*m.getTileset().getWidth(), y*m.getTileset().getHeight(), null);
			g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.33f));
			g.setColor(new Color(0x4444AA));
			g.fillRect(x*m.getTileset().getWidth(), y*m.getTileset().getHeight(), m.getTileset().getWidth(), m.getTileset().getHeight());
			g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
		}
	}

	@Override
	public boolean startPaint(TileMap m, TileSelection selection, int layer, int x, int y) {
		if(!m.isValid(x, y))
			return false;
		fillQueue.clear();
		buildQueue(m, layer, x, y, m.getTile(layer, x, y), selection.tiles[0], this.fillQueue);
		if(fillQueue.size() <= 0)
			return false;
		beginFill(m, layer, x, y, m.getTile(layer, x, y), selection.tiles[0]);
		return true;
	}

	@Override
	public boolean dragPaint(TileMap m, TileSelection selection, int layer, int x, int y) {
		return false;
	}

	@Override
	public boolean stopPaint(TileMap m, TileSelection selection, int layer, int x, int y) {
		return false;
	}
	
	private void buildQueue(TileMap m, int layer, int x, int y, int targetTile, int selectedTile, Queue<Integer> processed) {
		if(targetTile == selectedTile)
			return;
		if(!m.isValid(x, y) || m.getTile(layer, x, y) != targetTile)
			return;
		if(processed.contains(x + (y * m.getWidth())))
			return;
		processed.add(x + (y * m.getWidth()));
		buildQueue(m, layer, x-1, y, targetTile, selectedTile, processed);
		buildQueue(m, layer, x, y+1, targetTile, selectedTile, processed);
		buildQueue(m, layer, x+1, y, targetTile, selectedTile, processed);
		buildQueue(m, layer, x, y-1, targetTile, selectedTile, processed);
	}
	
	private void beginFill(TileMap m, int layer, int x, int y, int targetTile, int selectedTile) {
		if(targetTile == selectedTile)
			return;
		if(!m.isValid(x, y) || m.getTile(layer, x, y) != targetTile)
			return;
		m.setTile(layer, x, y, selectedTile);
		beginFill(m, layer, x-1, y, targetTile, selectedTile);
		beginFill(m, layer, x+1, y, targetTile, selectedTile);
		beginFill(m, layer, x, y-1, targetTile, selectedTile);
		beginFill(m, layer, x, y+1, targetTile, selectedTile);
	}
	
	@Override
	public String toString() {
		return "Fill";
	}
	
}
