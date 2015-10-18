package org.pokedroid.editor.gui;

import java.awt.BorderLayout;
import java.awt.Toolkit;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JScrollPane;

import org.pokedroid.editor.Config;
import org.pokedroid.editor.asset.GameFolder;
import org.pokedroid.editor.gui.brush.BrushPencil;
import org.pokedroid.editor.gui.brush.BrushRegistry;
import org.pokedroid.editor.gui.brush.TileSelection;
import org.pokedroid.editor.map.TileMap;

import com.alee.extended.filechooser.WebDirectoryChooser;
import com.alee.laf.WebLookAndFeel;
import com.alee.laf.scroll.WebScrollPane;
import com.alee.laf.splitpane.WebSplitPane;

/**
 * <p>
 * This will contain the main frame we are working from.
 * </p>
 * 
 * @author PoketronHacker
 * @version 10 October 2015
 * 
 */
public class MainGUI extends JFrame {
	private static final long serialVersionUID = -2719719472469080364L;

	private GameFolder gameFolder;
	private WebDirectoryChooser chooseGameFolder;
	private Properties props;
	private TileMap map;
	private MapDisplay mapDisplay;
	private TileDisplay tileDisplay;
	private TileSelection tileSelection;

	/**
	 * <p>
	 * Constructor for a new {@code MainFrame}.
	 * </p>
	 */
	public MainGUI() {
		super();
		this.map = null;
		this.tileSelection = new TileSelection();
		this.tileSelection.w = 1;
		this.tileSelection.h = 1;
		this.tileSelection.tiles = new int[] { 2 };
		this.chooseGameFolder = new WebDirectoryChooser(this);
		this.loadProperties();
		this.addComponents();
		this.setupFrame();
	}

	/**
	 * <p>
	 * Loads the properties and initialises the asset folder.
	 * </p>
	 */
	private void loadProperties() {
		this.props = new Properties();
		try {
			this.props.load(new FileReader(Config.CONFIG_FILE));
		} catch (IOException e) {
			System.err.println("[MainGUI] Unable to read config file.");
			e.printStackTrace();
		}
		if (!this.props.containsKey("GameDirectory")) {
			this.chooseGameFolder.setTitle("Select PokeDroid Game Folder");
			this.chooseGameFolder.setSelectedDirectory(new File("./"));
			if (this.chooseGameFolder.showDialog() == JFileChooser.APPROVE_OPTION) {
				this.props.setProperty("GameDirectory", this.chooseGameFolder.getSelectedDirectory().getPath());
			}
		}
		this.gameFolder = new GameFolder(new File(props.getProperty("GameDirectory")));
		this.map = gameFolder.getMaps().get(0);
	}

	/**
	 * <p>
	 * Adds the components to the {@code MainGUI}
	 * </p>
	 */
	private void addComponents() {
		this.mapDisplay = new MapDisplay(this);
		this.getContentPane().setLayout(new BorderLayout());
		this.getContentPane().add(new EditorToolbar(this), BorderLayout.NORTH);
		WebScrollPane mapPane = new WebScrollPane(mapDisplay);
		mapPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		mapPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		WebSplitPane splPane = new WebSplitPane(WebSplitPane.HORIZONTAL_SPLIT, true,
				new JScrollPane(tileDisplay = new TileDisplay(this), JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
						JScrollPane.HORIZONTAL_SCROLLBAR_NEVER),
				mapPane);
		tileDisplay.repaint();
		splPane.setDividerLocation(
				(getTileMap().getTileset().getWidth() * 8) + mapPane.getVerticalScrollBar().getWidth());
		this.getContentPane().add(splPane, BorderLayout.CENTER);
		this.setJMenuBar(new EditorMenubar(this));
	}

	/**
	 * <p>
	 * Sets up the game frame.
	 * </p>
	 */
	private void setupFrame() {
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setTitle("Mappy - The PokeDroid Editor");
		setSize((int) (Toolkit.getDefaultToolkit().getScreenSize().width * 0.75),
				(int) (Toolkit.getDefaultToolkit().getScreenSize().height * 0.75));
		setLocationRelativeTo(null);
		setVisible(true);
	}

	/**
	 * <p>
	 * Gets the currently working on game folder.
	 * </p>
	 * 
	 * @return The currently being worked on game folder.
	 */
	public GameFolder getGameFolder() {
		return this.gameFolder;
	}

	/**
	 * <p>
	 * Sets the current tilemap being worked on.
	 * </p>
	 * 
	 * @param t
	 *            The tilemap to work on.
	 */
	public void setTileMap(TileMap t) {
		this.map = t;
		this.tileSelection.w = 0;
		this.tileSelection.h = 0;
		this.tileSelection.tiles = null;
		repaint();
	}

	/**
	 * <p>
	 * Gets the current tilemap that is being worked on, this can be
	 * {@code null} if there is no map being worked on.
	 * </p>
	 * 
	 * @return The current {@code TileMap} being worked on.
	 */
	public TileMap getTileMap() {
		return this.map;
	}

	/**
	 * <p>
	 * Gets the display we will display the map onto.
	 * </p>
	 * 
	 * @return The display we will display the map onto.
	 */
	public MapDisplay getMapDisplay() {
		return this.mapDisplay;
	}

	/**
	 * <p>
	 * Gets the current tileselection.
	 * </p>
	 * 
	 * @return The current tileselection.
	 */
	public TileSelection getTileSelection() {
		return this.tileSelection;
	}

	/**
	 * <p>
	 * Static main method.
	 * </p>
	 * 
	 * @param args
	 *            The program arguments.
	 */
	public static void main(String[] args) {
		WebLookAndFeel.install();
		BrushRegistry.BRUSHES.add(new BrushPencil());
		new MainGUI();
	}

}