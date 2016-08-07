package com.pokedroid.editor.gui;

import java.awt.BorderLayout;
import java.awt.Toolkit;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Properties;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.WindowConstants;

import com.pokedroid.editor.Config;
import com.pokedroid.editor.asset.Story;
import com.pokedroid.editor.gui.brush.BrushFill;
import com.pokedroid.editor.gui.brush.BrushPencil;
import com.pokedroid.editor.gui.brush.BrushRegistry;
import com.pokedroid.editor.gui.brush.TileSelection;
import com.pokedroid.editor.map.TileMap;

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

	private Story story;
	private WebDirectoryChooser chooseGameFolder;
	private Properties props;
	private TileMap map;
	private MapDisplay mapDisplay;
	private TileDisplay tileDisplay;
	private TileSelection tileSelection;
	private MapList mapList;

	/**
	 * <p>
	 * Constructor for a new {@code MainFrame}.
	 * </p>
	 */
	public MainGUI() {
		super();
		setSize((int) (Toolkit.getDefaultToolkit().getScreenSize().width * 0.75),
				(int) (Toolkit.getDefaultToolkit().getScreenSize().height * 0.75));
		this.map = null;
		this.tileSelection = new TileSelection();
		this.tileSelection.w = 1;
		this.tileSelection.h = 1;
		this.tileSelection.tiles = new int[] { 2 };
		this.chooseGameFolder = new WebDirectoryChooser(this);
		this.loadProperties();
		this.addComponents();
		this.setupFrame();
		this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
	}

	/**
	 * <p>
	 * Loads the properties and initialises the asset folder.
	 * </p>
	 */
	private void loadProperties() {
		this.props = new Properties();
		try {
			if(Config.CONFIG_FILE.exists())
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
		// this.story = new Story(new File(props.getProperty("GameDirectory")));
		if(this.story != null && this.story.getMaps().size() > 0) {
			this.map = story.getMaps().entrySet().iterator().next().getValue();
		}
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
		mapPane.getVerticalScrollBar().setUnitIncrement(32);
		WebScrollPane tilePane = new WebScrollPane(tileDisplay = new TileDisplay(this),
				JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		tilePane.getVerticalScrollBar().setUnitIncrement(32);
		WebSplitPane tileSplit = new WebSplitPane(WebSplitPane.VERTICAL_SPLIT, true,
				tilePane,
				(mapList = new MapList(this)));
		WebSplitPane splPane = new WebSplitPane(WebSplitPane.HORIZONTAL_SPLIT, true,
				tileSplit,
				mapPane);
		tileDisplay.repaint();
		if(getTileMap() != null) {
			splPane.setDividerLocation(
					(getTileMap().getTileSet().getWidth() * 8) + mapPane.getVerticalScrollBar().getWidth());
			tileSplit.setDividerLocation((int)(getHeight()*0.7));
		} else {
			splPane.setDividerLocation((32 * 8) + mapPane.getVerticalScrollBar().getWidth());
			tileSplit.setDividerLocation((int)(getHeight()*0.7));
		}
		this.getContentPane().add(splPane, BorderLayout.CENTER);
		this.setJMenuBar(new EditorMenuBar(this));
	}

	/**
	 * <p>
	 * Sets up the game frame.
	 * </p>
	 */
	private void setupFrame() {
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setTitle("Mappy - The PokeDroid Editor");
		setLocationRelativeTo(null);
		setVisible(true);
	}

	/**
	 * <p>Sets the story currently being worked on.</p>
	 *
	 * @param s The new story.
     */
	public void setStory(Story s) {
		this.story = s;
		if(s == null) {

		} else {
			if (this.story.getMaps().size() > 0) {
				this.setTileMap(story.getMaps().entrySet().iterator().next().getValue());
			}
			this.getMapList().buildTree();
		}
		this.repaint();
	}

	/**
	 * <p>
	 * Gets the currently working on game folder.
	 * </p>
	 * 
	 * @return The currently being worked on game folder.
	 */
	public Story getStory() {
		return this.story;
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
	 * @return The map list/tree.
     */
	public MapList getMapList() {
		return this.mapList;
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
		Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
			@Override
			public void uncaughtException(Thread t, Throwable e) {
				StringWriter sw = new StringWriter();
				e.printStackTrace(new PrintWriter(sw));
				String stacktrace = sw.toString();
				System.err.println(stacktrace);
				JOptionPane.showMessageDialog(null,
						"An uncaught exception has occurred:\n\n" + stacktrace,
						"An error has occurred.",
						JOptionPane.ERROR_MESSAGE);
				if (!(e instanceof RuntimeException)) {
					System.exit(-1);
				}
			}
		});
		WebLookAndFeel.install();
		BrushRegistry.BRUSHES.add(new BrushPencil());
		BrushRegistry.BRUSHES.add(new BrushFill());
		new MainGUI();
	}

}