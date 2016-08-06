package com.pokedroid.editor.gui;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import com.pokedroid.editor.gui.brush.Brush;
import com.pokedroid.editor.gui.brush.BrushRegistry;
import com.pokedroid.editor.gui.brush.TileSelection;
import com.pokedroid.editor.map.TileMap;
import com.pokedroid.editor.map.TileSet;

import com.alee.laf.panel.WebPanel;

/**
 * <p>
 * This is used to display the map, it is also what we will click to edit the
 * map.
 * </p>
 * 
 * @author PoketronHacker
 * @version 18 October 2015
 *
 */
public class MapDisplay extends WebPanel implements MouseListener, MouseMotionListener {
	private static final long serialVersionUID = 1902568132011144629L;
	private static final float GRID_TRANSPARENCY = 0.3f, DIM_TRANSPARENCY = 0.76f;

	private MainGUI mainGUI;
	private Brush currentBrush;
	private boolean showGrid, dimOtherLayers;
	private int currentLayer;
	private int mouseX, mouseY;
	private boolean in;
	private int button;

	/**
	 * <p>
	 * Creates a new {@code MapDisplay.</p>
	 * 
	 * @param mainGUI
	 *            The main gui.
	 */
	public MapDisplay(MainGUI mainGUI) {
		super();
		this.addMouseListener(this);
		this.addMouseMotionListener(this);
		this.mainGUI = mainGUI;
		this.showGrid = true;
		this.dimOtherLayers = true;
		this.currentBrush = BrushRegistry.BRUSHES.get(0);
		this.button = -1;
		if (map() != null)
			setPreferredSize(new Dimension(map().getWidth() * set().getWidth(), map().getHeight() * set().getHeight()));
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		if (map() == null) {
			setPreferredSize(new Dimension(1, 1));
			return;
		}
		setPreferredSize(new Dimension(map().getWidth() * set().getWidth(), map().getHeight() * set().getHeight()));
		g.setColor(Color.darkGray);
		g.fillRect(0, 0, map().getWidth() * set().getWidth(), map().getHeight() * set().getHeight());
		for (int l = 0; l < map().getLayerCount(); l++) {
			for (int y = 0; y < map().getHeight(); y++) {
				for (int x = 0; x < map().getWidth(); x++) {
					Graphics2D g2d = (Graphics2D) g;
					if (showGrid) {
						g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, GRID_TRANSPARENCY));
						g2d.drawRect(x * set().getWidth(), y * set().getHeight(), set().getWidth(), set().getHeight());
						g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
					}
					if (map().getTile(l, x, y) == -1)
						continue;
					if (l != currentLayer && dimOtherLayers)
						g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, DIM_TRANSPARENCY));
					g2d.drawImage(set().getTile(map().getTile(l, x, y)), x * set().getWidth(), y * set().getHeight(),
							null);
					g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
				}
			}
		}
		g.setColor(Color.BLACK);
		if (map().isValid(mouseX, mouseY)) {
			if(in) {
				currentBrush.drawOverlay((Graphics2D) g, map(), selection(), mouseX, mouseY);
			}
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {

	}

	@Override
	public void mousePressed(MouseEvent e) {
		if(map() == null || button >= 0)
			return;
		this.button = e.getButton();
		if (e.getX() < 0 || e.getX() > getWidth() || e.getY() < 0 || e.getY() > getHeight()) {
			return;
		}
		int tX = e.getX() / set().getWidth();
		int tY = e.getY() / set().getHeight();
		if (map().isValid(tX, tY)) {
			button = e.getButton();
			if(e.getButton() == MouseEvent.BUTTON1) {
				currentBrush.startPaint(map(), selection(), currentLayer, tX, tY);
				repaint();
			} else if(e.getButton() == MouseEvent.BUTTON3) {
				map().setTile(currentLayer, tX, tY, -1);
				repaint();
			}
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if(map() == null)
			return;
		if (e.getX() < 0 || e.getX() > getWidth() || e.getY() < 0 || e.getY() > getHeight()) {
			return;
		}
		this.button = -1;
		int tX = e.getX() / set().getWidth();
		int tY = e.getY() / set().getHeight();
		if (map().isValid(tX, tY)) {
			currentBrush.stopPaint(map(), selection(), currentLayer, tX, tY);
			repaint();
		}
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		if(map() == null)
			return;
		in = true;
		repaint();
	}

	@Override
	public void mouseExited(MouseEvent e) {
		if(map() == null)
			return;
		in = false;
		repaint();
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		if(map() == null)
			return;
		mouseMoved(e);
		if (e.getX() < 0 || e.getX() > getWidth() || e.getY() < 0 || e.getY() > getHeight()) {
			return;
		}
		int tX = e.getX() / set().getWidth();
		int tY = e.getY() / set().getHeight();
		if (map().isValid(tX, tY)) {
			if(button == MouseEvent.BUTTON1) {
				currentBrush.dragPaint(map(), selection(), currentLayer, tX, tY);
				repaint();
			} else if(button == MouseEvent.BUTTON3) {
				map().setTile(currentLayer, tX, tY, -1);
				repaint();
			}
		}
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		if(map() == null)
			return;
		if (e.getX() < 0 || e.getX() > getWidth() || e.getY() < 0 || e.getY() > getHeight()) {
			mouseX = mouseY = -1;
			return;
		}
		int nX = e.getX() / set().getWidth();
		int nY = e.getY() / set().getHeight();
		if (nX != mouseX || nY != mouseY) {
			mouseX = nX;
			mouseY = nY;
			repaint();
		}
	}

	/**
	 * <p>Sets the current brush.</p>
	 *
	 * @param b The brush.
     */
	public void setCurrentBrush(Brush b) {
		this.currentBrush = b;
	}

	/**
	 * <p>
	 * Sets whether other layers should be dimmed.
	 * </p>
	 * 
	 * @param b
	 *            The new value.
	 */
	public void setDimOtherLayers(boolean b) {
		this.dimOtherLayers = b;
		repaint();
	}

	/**
	 * <p>
	 * Sets whether to show the grid.
	 * </p>
	 * 
	 * @param b
	 *            The new value.
	 */
	public void setShowGrid(boolean b) {
		this.showGrid = b;
		repaint();
	}

	/**
	 * Sets the currently used layer.
	 *
	 * @param currentLayer The currently used layer.
     */
	public void setCurrentLayer(int currentLayer) {
		this.currentLayer = currentLayer;
		repaint();
	}



	/**
	 * <p>
	 * Gets the current map being worked on.
	 * </p>
	 * 
	 * @return The current map being worked on.
	 */
	protected TileMap map() {
		return this.mainGUI.getTileMap();
	}

	/**
	 * <p>
	 * Gets the current tileset being worked on.
	 * </p>
	 * 
	 * @return The current tileset being worked on.
	 */
	protected TileSet set() {
		return map() == null ? null : map().getTileSet();
	}

	/**
	 * <p>
	 * Gets the tile selection.
	 * </p>
	 * 
	 * @return The tile selection.
	 */
	protected TileSelection selection() {
		return this.mainGUI.getTileSelection();
	}

}
