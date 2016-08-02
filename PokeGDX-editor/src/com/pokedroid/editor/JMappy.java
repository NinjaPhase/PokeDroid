package com.pokedroid.editor;

import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import com.pokedroid.map.TileMap;
import com.pokedroid.map.TileSet;
import com.pokedroid.story.Story;

/**
 * <p>Constructs a new {@code Mappy}.</p>
 * 
 * @author J. Kitchen
 * @version 11 March 2016
 *
 */
public class JMappy extends JFrame {
	private static final long serialVersionUID = 3539928907137458414L;

	private Story story;
	private TileMap openMap;
	private JSplitPane mapSplit, tileSplit;
	private JScrollPane tileScroll;
	private JMapViewer mapViewer;
	private JTileSelector tileSelector;
	private JMapTree mapTree;
	private JMappyMenu menuBar;

	/**
	 * <p>Constructs a new {@code Mappy}.</p>
	 */
	public JMappy() {
		mapSplit = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, true, null, null);
		{
			tileSplit = new JSplitPane(JSplitPane.VERTICAL_SPLIT, true, null, null);
			{
				tileSplit.setResizeWeight(0.5);
				tileSelector = new JTileSelector(this);
				mapTree = new JMapTree(this);
				tileSplit.setTopComponent(new JScrollPane(tileSelector, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED));
				tileSplit.setBottomComponent(new JScrollPane(mapTree, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED));
			}
			mapViewer = new JMapViewer(this);
			mapSplit.setLeftComponent(tileSplit);
			mapSplit.setRightComponent(mapViewer);
		}
		setContentPane(mapSplit);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setJMenuBar((menuBar = new JMappyMenu(this)));
		setTitle("Mappy - The Pokédroid Editor");
		setSize(
				(int)(Toolkit.getDefaultToolkit().getScreenSize().width*0.85),
				(int)(Toolkit.getDefaultToolkit().getScreenSize().height*0.85));
		setLocationRelativeTo(null);
		setVisible(true);
		mapSplit.setDividerLocation(0.33);
		tileSplit.setDividerLocation(0.65);
	}
	
	/**
	 * <p>Sets the currently open story.</p>
	 * 
	 * @param story The currently open story.
	 */
	public void setOpenStory(Story story) {
		this.story = story;
		this.mapTree.refresh();
		this.menuBar.refresh();
		this.repaint();
	}
	
	/**
	 * <p>Opens a map.</p>
	 * 
	 * @param map The map to open.
	 */
	public void setOpenMap(TileMap map) {
		this.openMap = map;
		this.repaint();
	}
	
	/**
	 * <p>Gets the currently open story.</p>
	 * 
	 * @return The currently open story.
	 */
	public Story getOpenStory() {
		return this.story;
	}

	/**
	 * <p>Gets the map.</p>
	 * 
	 * @return The map.
	 */
	public TileMap getMap() {
		return this.openMap;
	}
	
	/**
	 * @return The tileset currently open.
	 */
	public TileSet getTileSet() {
		return (getMap() == null ? null : getMap().getTileSet());
	}

	/**
	 * <p>Main-Method</p>
	 * 
	 * @param args The arguments.
	 */
	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
				| UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}
		new JMappy();
	}

}
