package com.pokedroid.editor;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.JPanel;

/**
 * <p>The {@code JTileSelector} for selecting the
 * different tiles for editing the map.</p>
 * 
 * @author J. Kitchen
 * @version 16 March 2016
 *
 */
public class JTileSelector extends JPanel {
	private static final long serialVersionUID = 8574126077527333222L;
	
	private JMappy mappy;
	
	/**
	 * <p>Constructs a new {@code JTileSelector}.</p>
	 * 
	 * @param mappy The parent.
	 */
	public JTileSelector(JMappy mappy) {
		this.mappy = mappy;
	}
	
	@Override
	public void paintComponent(Graphics g) {
		g.setColor(Color.GRAY);
		g.fillRect(0, 0, getWidth(), getHeight());
		if(mappy.getTileSet() == null)
			return;
		setPreferredSize(new Dimension(
				mappy.getTileSet().getTileWidth()*8,
				mappy.getTileSet().getTileHeight()*(mappy.getTileSet().getTileCount()/8)
				));
		for(int y = 0; y < mappy.getTileSet().getTileCount()/8; y++) {
			for(int x = 0; x < 8; x++) {
				Image i = mappy.getTileSet().getTile(x + (y * 8));
				g.drawImage(i, x*mappy.getTileSet().getTileWidth(), y*mappy.getTileSet().getTileHeight(), null);
			}
		}
	}
	
}
