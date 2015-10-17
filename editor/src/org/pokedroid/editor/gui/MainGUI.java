package org.pokedroid.editor.gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import org.pokedroid.editor.asset.AssetFolder;
import org.pokedroid.editor.gui.brush.Brush;
import org.pokedroid.editor.gui.main.EditorToolbar;
import org.pokedroid.editor.gui.main.MapEditor;
import org.pokedroid.editor.gui.main.MapTree;
import org.pokedroid.editor.gui.main.TileSelection;
import org.pokedroid.editor.gui.main.TileSelector;
import org.pokedroid.editor.map.TileMap;

/**
 * <p>This will contain the main frame we are working from.</p>
 * 
 * @author PoketronHacker
 * @version 10 October 2015
 * 
 */
public class MainGUI extends JFrame implements KeyListener {
	private static final long serialVersionUID = -2719719472469080364L;
	
	private AssetFolder folder;
	private JSplitPane splTileList, splTileMap;
	private TileSelector tileSelector;
	private MapEditor mapEditor;
	
	/**
	 * <p>Constructor for a new {@code MainFrame}.</p>
	 */
	public MainGUI() {
		super();
		this.folder = new AssetFolder(new File("../android/assets/"));
		this.mapEditor = new MapEditor(this);
		this.tileSelector = new TileSelector(this);
		this.splTileList = new JSplitPane(JSplitPane.VERTICAL_SPLIT, createScrollPane(tileSelector), new MapTree(this));
		this.splTileMap = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, splTileList, createScrollPane(mapEditor));
		this.getContentPane().setLayout(new BorderLayout());
		this.getContentPane().add(splTileMap);
		this.getContentPane().add(BorderLayout.NORTH, new EditorToolbar(this));
		setTitle("PokeDroid - The Editor");
		setSize((int)(Toolkit.getDefaultToolkit().getScreenSize().width*0.75),
				(int)(Toolkit.getDefaultToolkit().getScreenSize().height*0.75));
		setLocationRelativeTo(null);
		setVisible(true);
		this.splTileList.setDividerLocation(0.5);
		this.getContentPane().addKeyListener(this);
		this.addKeyListener(this);
		this.setFocusable(true);
		this.setFocusTraversalKeysEnabled(false);
	}
	
	private JScrollPane createScrollPane(Component view) {
		JScrollPane newPane = new JScrollPane(view);
		return newPane;
	}
	
	/**
	 * <p>Sets the brush to a new brush.</p>
	 * 
	 * @param brush The new brush.
	 */
	public void setBrush(Brush brush) {
		this.mapEditor.setBrush(brush);
	}
	
	/**
	 * <p>Gets the current asset folder.</p>
	 * 
	 * @return The current asset folder.
	 */
	public AssetFolder getAssetFolder() {
		return this.folder;
	}
	
	/**
	 * <p>Sets the current {@code TileMap} that is being worked on.</p>
	 * 
	 * @param map The {@code TileMap} being worked on.
	 */
	public void setMap(TileMap map) {
		this.mapEditor.setCurrentMap(map);
	}
	
	/**
	 * <p>Sets the currently edited layer.</p>
	 * 
	 * @param layerId The new layer id.
	 */
	public void setLayer(int layerId) {
		if(layerId > getMap().getLayerCount())
			layerId = getMap().getLayerCount()-1;
		this.mapEditor.setCurrentLayer(layerId);
	}
	
	/**
	 * <p>Gets the tile map.</p>
	 * 
	 * @return The tile map.
	 */
	public TileMap getMap() {
		return this.mapEditor.getMap();
	}
	
	/**
	 * <p>Gets the {@code TileSelection}.</p>
	 * 
	 * @return The {@code TileSelection}
	 */
	public TileSelection getSelection() {
		return this.mapEditor.getSelection();
	}
	
	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getInstalledLookAndFeels()[1].getClassName());
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
				| UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}
		new MainGUI();
	}

	@Override
	public void keyTyped(KeyEvent e) {
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if((e.getModifiers() & KeyEvent.CTRL_MASK) > 0) {
			if(e.getKeyCode() == KeyEvent.VK_Z) {
				this.mapEditor.undo();
			} else if(e.getKeyCode() == KeyEvent.VK_Y) {
				this.mapEditor.redo();
			}
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		
	}

}