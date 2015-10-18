package org.pokedroid.editor.gui;

import java.awt.Graphics;

import org.pokedroid.editor.map.Tileset;

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
public class TileDisplay extends WebPanel {
	private static final long serialVersionUID = 3251435398049278551L;
	private static final int COLS = 8;

	private MainGUI mainGUI;

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
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		if (tileset() == null)
			return;
		setPreferredWidth(tileset().getWidth() * COLS);
		setPreferredHeight(tileset().getHeight() * (tileset().length() / 8));
		for (int i = 0; i < tileset().length(); i++) {
			int x = i % COLS;
			int y = i / COLS;
			g.drawImage(tileset().getTile(i), x * tileset().getWidth(), y * tileset().getHeight(), null);
		}
	}

	/**
	 * <p>
	 * Gets the tileset of the map we are working on.
	 * </p>
	 * 
	 * @return The tileset of the map we are working on.
	 */
	protected Tileset tileset() {
		return (mainGUI.getTileMap() == null ? null : mainGUI.getTileMap().getTileset());
	}

}
