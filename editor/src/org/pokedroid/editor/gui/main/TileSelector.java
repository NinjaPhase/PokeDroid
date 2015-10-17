package org.pokedroid.editor.gui.main;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JPanel;

import org.pokedroid.editor.gui.MainGUI;
import org.pokedroid.editor.map.Tileset;

/**
 * <p>A {@code TileSelector} is used to select tiles that are used within the map
 * editor.</p>
 * 
 * @author PoketronHacker
 * @version v1.0
 *
 */
public class TileSelector extends JPanel implements MouseListener {
	private static final long serialVersionUID = 1020638363824507651L;

	private MainGUI mainGUI;

	/**
	 * <p>Constructor for a new {@code TileSelector}.</p>
	 * 
	 * @param mainGUI The MainGUI.
	 */
	public TileSelector(MainGUI mainGUI) {
		this.mainGUI = mainGUI;
		this.addMouseListener(this);
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		final int tw = tileset().getWidth();
		final int th = tileset().getHeight();
		this.setPreferredSize(new Dimension(tileset().getWidth()*8, (tileset().length()/8)*tileset().getHeight()));
		for(int i = 0; i < tileset().length(); i++) {
			int x = i%8;
			int y = i/8;
			boolean selected = false;
			for(int j = 0; j < selection().tiles.length; j++) {
				if(selection().tiles[j] == i) {
					selected = true;
					break;
				}
			}
			g.drawImage(tileset().getTile(i), x*tw, y*th, null);
			if(selected) {
				Graphics2D g2d = (Graphics2D) g;
				g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.33f));
				g2d.setColor(new Color(0x4444AA));
				g2d.fillRect(x*tw, y*th, tw, th);
				g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
			}
		}
	}

	/**
	 * <p>Gets the tileset.</p>
	 * 
	 * @return The tileset.
	 */
	protected Tileset tileset() {
		return mainGUI.getMap().getTileset();
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mousePressed(MouseEvent e) {
		int tX = e.getX()/tileset().getWidth();
		int tY = e.getY()/tileset().getHeight();
		int tId = tX+(tY*8);
		if(tId >= 0 && tId < tileset().length()) {
			selection().w = 1;
			selection().h = 1;
			if(e.getButton() == MouseEvent.BUTTON1) {
				selection().tiles = new int[]{tId};
			} else if(e.getButton() == MouseEvent.BUTTON3) {
				selection().tiles = new int[]{-1};
			}
			repaint();
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	/**
	 * <p>Gets the selected tiles.</p>
	 * 
	 * @return The selected tiles.
	 */
	public TileSelection selection() {
		return mainGUI.getSelection();
	}

}
