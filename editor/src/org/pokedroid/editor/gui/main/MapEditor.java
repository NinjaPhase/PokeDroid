package org.pokedroid.editor.gui.main;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.Queue;
import java.util.Stack;

import javax.swing.JPanel;

import org.pokedroid.editor.asset.AssetFolder;
import org.pokedroid.editor.gui.MainGUI;
import org.pokedroid.editor.gui.brush.Brush;
import org.pokedroid.editor.gui.brush.PencilBrush;
import org.pokedroid.editor.map.TileMap;

/**
 * <p>The {@code MapEditor} renders the screen.</p>
 * 
 * @author PoketronHacker
 * @version v1.0
 *
 */
public class MapEditor extends JPanel implements MouseListener, MouseMotionListener {
	private static final long serialVersionUID = -8234414607314517777L;

	private final TileSelection selection = new TileSelection();

	private TileMap cur;
	private int mouseX, mouseY;
	private Brush brush = PencilBrush.PENCIL_BRUSH;
	private int currentLayer;
	private boolean dimmed;
	private Stack<Integer[][]> undoStack, redoStack;

	/**
	 * <p>Constructor for a new {@code MapEditor}.</p>
	 */
	public MapEditor(MainGUI mainGUI) {
		super();
		this.undoStack = new Stack<Integer[][]>();
		this.redoStack = new Stack<Integer[][]>();
		this.dimmed = true;
		this.selection.w = 2;
		this.selection.h = 2;
		this.selection.tiles = new int[]{7, 7, 15, 15};
		this.setBackground(Color.DARK_GRAY);
		this.cur = mainGUI.getAssetFolder().getMaps().get(0);
		this.addMouseListener(this);
		this.addMouseMotionListener(this);
		this.addKeyListener(mainGUI);
		this.setFocusable(true);
	}

	@Override
	public void paintComponent(Graphics gg) {
		super.paintComponent(gg);
		this.setPreferredSize(new Dimension(cur.getWidth()*cur.getTileset().getWidth(), cur.getHeight()*cur.getTileset().getHeight()));
		Graphics2D g = (Graphics2D) gg;
		for(int i = 0; i < cur.getLayerCount(); i++) {
			if(i == currentLayer) g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
			else if(dimmed) g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.44f));
			cur.render(g, i);
		}
		g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
		if(cur.isValid(mouseX, mouseY)) {
			g.setColor(Color.BLACK);
			brush.drawOverlay((Graphics2D)g, cur, selection, currentLayer, mouseX, mouseY);
		}
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		if(e.getX()/cur.getTileset().getWidth() == mouseX
				&& e.getY()/cur.getTileset().getHeight() == mouseY)
			return;
		mouseX = e.getX()/cur.getTileset().getWidth();
		mouseY = e.getY()/cur.getTileset().getHeight();
		brush.dragPaint(cur, selection, currentLayer, mouseX, mouseY);
		repaint();
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		if(e.getX()/cur.getTileset().getWidth() == mouseX
				&& e.getY()/cur.getTileset().getHeight() == mouseY)
			return;
		mouseX = e.getX()/cur.getTileset().getWidth();
		mouseY = e.getY()/cur.getTileset().getHeight();
		repaint();
	}

	@Override
	public void mouseClicked(MouseEvent e) {

	}

	@Override
	public void mousePressed(MouseEvent e) {
		int x = e.getX()/cur.getTileset().getWidth();
		int y = e.getY()/cur.getTileset().getHeight();
		Integer[][] snapshot = cur.createSnapshot();
		if(brush.startPaint(cur, selection, currentLayer, x, y)) {
			redoStack.clear();
			undoStack.push(snapshot);
		}
		repaint();
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		int x = e.getX()/cur.getTileset().getWidth();
		int y = e.getY()/cur.getTileset().getHeight();
		brush.stopPaint(cur, selection, currentLayer, x, y);
		repaint();
	}

	@Override
	public void mouseEntered(MouseEvent e) {

	}

	@Override
	public void mouseExited(MouseEvent e) {

	}

	/**
	 * <p>Sets the map to work on.</p>
	 * 
	 * @param cur The map.
	 */
	public void setCurrentMap(TileMap cur) {
		this.cur = cur;
		this.repaint();
	}

	/**
	 * <p>Sets the current layer of the map to work on.</p>
	 * 
	 * @param layer The new current layer.
	 */
	public void setCurrentLayer(int layer) {
		this.currentLayer = layer;
		this.repaint();
	}

	/**
	 * <p>Gets the tile map.</p>
	 * 
	 * @return The tile map.
	 */
	public TileMap getMap() {
		return this.cur;
	}

	/**
	 * <p>Gets the tile selection to paint onto the map.</p>
	 * 
	 * @return The tile selection to paint onto the map.
	 */
	public TileSelection getSelection() {
		return this.selection;
	}

	/**
	 * <p>Sets the new {@code Brush}.</p>
	 * 
	 * @param brush The new brush.
	 */
	public void setBrush(Brush brush) {
		this.brush = brush;
	}
	
	public void redo() {
		synchronized(redoStack) {
			if(redoStack.isEmpty())
				return;
			Integer[][] undo = cur.createSnapshot();
			Integer[][] i = redoStack.pop();
			undoStack.push(undo);
			cur.loadSnapshot(i);
			repaint();
		}
	}
	
	public void undo() {
		synchronized(undoStack) {
			if(undoStack.isEmpty())
				return;
			Integer[][] redo = cur.createSnapshot();
			redoStack.push(redo);
			Integer[][] i = undoStack.pop();
			cur.loadSnapshot(i);
			repaint();
		}
	}

}
