package com.pokedroid.editor;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JPanel;

/**
 * <p>The {@code JMapViewer} is the map editor that
 * will be used to edit most of the map.</p>
 * 
 * @author J. Kitchen
 * @version 16 March 2016
 *
 */
public class JMapViewer extends JPanel {
	private static final long serialVersionUID = 5078742056900462799L;
	
	private JMappy mappy;
	
	/**
	 * <p>Constructs a new {@code JMapViewer}.</p>
	 * 
	 * @param mappy The parent.
	 */
	public JMapViewer(JMappy mappy) {
		this.mappy = mappy;
	}
	
	@Override
	public void paintComponent(Graphics g) {
		g.setColor(Color.GRAY);
		g.fillRect(0, 0, getWidth(), getHeight());
		if(this.mappy.getMap() == null)
			return;
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, mappy.getMap().getRenderWidth(), mappy.getMap().getRenderHeight());
		this.mappy.getMap().render(g, 0, 0, 0);
	}
	
}
