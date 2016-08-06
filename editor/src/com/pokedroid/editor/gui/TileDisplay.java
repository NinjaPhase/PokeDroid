package com.pokedroid.editor.gui;

import java.awt.AlphaComposite;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import com.pokedroid.editor.map.TileSet;

import com.alee.laf.panel.WebPanel;

/**
 * <p>
 * This is used as a palette to show the tileset, with tiles that can be
 * selected.
 * </p>
 * 
 * @author PoketronHacker
 * @version 18 October 2015
 *
 */
public class TileDisplay extends WebPanel implements MouseListener, MouseMotionListener {
	private static final long serialVersionUID = 3251435398049278551L;
	private static final int COLS = 8;

	private MainGUI mainGUI;
	private int sX, sY;

	/**
	 * <p>
	 * Constructor for a new {@code TileDisplay}.
	 * </p>
	 * 
	 * @param mainGUI
	 *            The main gui.
	 */
	public TileDisplay(MainGUI mainGUI) {
		super();
		this.mainGUI = mainGUI;
		this.addMouseListener(this);
		this.addMouseMotionListener(this);
	}

	@Override
	public void paintComponent(Graphics gg) {
		super.paintComponent(gg);
		if (tileset() == null)
			return;
		Graphics2D g = (Graphics2D) gg;
		setPreferredWidth(tileset().getWidth() * COLS);
		setPreferredHeight(tileset().getHeight() * (tileset().length() / 8));
		for (int i = 0; i < tileset().length(); i++) {
			int x = i % COLS;
			int y = i / COLS;
			g.drawImage(tileset().getTile(i), x * tileset().getWidth(), y * tileset().getHeight(), null);
			if(mainGUI.getTileSelection() != null && mainGUI.getTileSelection().contains(i)) {
				g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.33f));
				g.fillRect(x * tileset().getWidth(), y * tileset().getHeight(),
						tileset().getWidth(), tileset().getHeight());
				g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
			}
		}
	}

	/**
	 * <p>
	 * Gets the tileset of the map we are working on.
	 * </p>
	 * 
	 * @return The tileset of the map we are working on.
	 */
	protected TileSet tileset() {
		return (mainGUI.getTileMap() == null ? null : mainGUI.getTileMap().getTileSet());
	}

	@Override
	public void mouseClicked(MouseEvent e) {

	}

	@Override
	public void mousePressed(MouseEvent e) {
		if(tileset() == null)
			return;
		int x = e.getX()/tileset().getWidth();
		int y = e.getY()/tileset().getHeight();
		int i = x+(y*COLS);
		if(x >= 0 && x < COLS) {
			sX = x;
			sY = y;
			this.mainGUI.getTileSelection().w = 1;
			this.mainGUI.getTileSelection().h = 1;
			this.mainGUI.getTileSelection().tiles = new int[]{i};
			repaint();
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		sX = -1;
		sY = -1;
	}

	@Override
	public void mouseEntered(MouseEvent e) {

	}

	@Override
	public void mouseExited(MouseEvent e) {

	}

	@Override
	public void mouseDragged(MouseEvent e) {
		if(tileset() == null || sX < 0 || sY < 0)
			return;
		int x = e.getX()/tileset().getWidth();
		int y = e.getY()/tileset().getHeight();
		int i = x+(y*COLS);
		if(x >= 0 && x < COLS) {
			int fX, fY, eX, eY;
			fX = sX < x ? sX : x;
			fY = sY < y ? sY : y;
			eX = sX > x ? sX : x;
			eY = sY > y ? sY : y;
			this.mainGUI.getTileSelection().w = (eX-fX)+1;
			this.mainGUI.getTileSelection().h = (eY-fY)+1;
			this.mainGUI.getTileSelection().tiles = new int[((eX-fX)+1)*((eY-fY)+1)];
			for(int yy = 0; yy < this.mainGUI.getTileSelection().h; yy++) {
				for(int xx = 0; xx < this.mainGUI.getTileSelection().w; xx++) {
					this.mainGUI.getTileSelection().tiles[xx + (yy * this.mainGUI.getTileSelection().w)] = (fX+xx)+((fY+yy)*COLS);
				}
			}
			repaint();
		}
	}

	@Override
	public void mouseMoved(MouseEvent e) {

	}
}
