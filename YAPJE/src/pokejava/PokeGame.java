package pokejava;

import java.awt.Color;
import java.awt.Graphics;
import java.util.HashMap;
import java.util.Map;

import pokejava.api.Game;
import pokejava.api.pokemon.ExperienceType;
import pokejava.plugin.PluginManager;
import pokejava.scene.Scene;
import pokejava.scene.SceneMap;

/**
 * The game class will bind all the features of the game together
 * and will be the main access point for the game.
 * 
 * @author PoketronHacker
 * @version 1.0
 *
 */
public class PokeGame implements Game {
	private static final String[] SPLASH_TITLES = new String[]{
		"Random Experiences",
		"Ambition at it's finest",
		"Vark... What're you doing?"
		};
	
	public static final String TITLE = "YAPJE - " +
			SPLASH_TITLES[(int)(Math.random()*SPLASH_TITLES.length)];
	
	public static final int WIDTH = 480, HEIGHT = (WIDTH / 4) * 3;
	private static final int TARGET_FPS = 60;
	private static final long OPTIMAL_TIME = 1000000000/TARGET_FPS;

	private Display display;
	private Scene scene;
	
	private PluginManager pluginManager;
	
	private Map<String, ExperienceType> expPools;

	private boolean isRunning;

	/**
	 * Constructor for the game object.
	 */
	public PokeGame() {
		this.pluginManager = new PluginManager(this);
		this.initSystems();
		this.display = new Display(TITLE, WIDTH, HEIGHT);
		this.scene = new SceneMap(this);
		this.isRunning = true;
		updateLoop();
	}
	
	/**
	 * Initialises the different pokemon types.
	 */
	private void initSystems() {
		this.expPools = new HashMap<String, ExperienceType>();
		this.expPools.put(ExperienceType.ERRATIC.getName(), ExperienceType.ERRATIC);
		this.expPools.put(ExperienceType.FAST.getName(), ExperienceType.FAST);
		this.expPools.put(ExperienceType.FLUCTUATING.getName(), ExperienceType.FLUCTUATING);
		this.expPools.put(ExperienceType.MEDIUM_FAST.getName(), ExperienceType.MEDIUM_FAST);
		this.expPools.put(ExperienceType.MEDIUM_SLOW.getName(), ExperienceType.MEDIUM_SLOW);
		this.expPools.put(ExperienceType.SLOW.getName(), ExperienceType.SLOW);
	}
	
	/**
	 * The update loop of the game.
	 */
	private void updateLoop() {
		double timeDelta = 1.0, timeElapsed = 0.0;
		long lastUpdate = System.nanoTime();
		while(isRunning) {
			long now = System.nanoTime();
			long updateLength = now-lastUpdate;
			lastUpdate = now;
			timeDelta = updateLength/((double)OPTIMAL_TIME);
			timeElapsed += timeDelta;
			onUpdate(timeDelta, timeElapsed);
			onRender(this.display.getGameBuffer().getGraphics());
			this.display.paintBackBuffer();
			try {
				Thread.sleep(Math.abs((lastUpdate-System.nanoTime() + OPTIMAL_TIME)/1000000));
			} catch (InterruptedException e) {
				System.err.println("[Game] Unable to control game loop time.");
			}
		}
	}

	/**
	 * The update method of the game.
	 * 
	 * @param timeDelta The delta time.
	 * @param timeTotal The time elapsed since the start.
	 */
	private void onUpdate(double timeDelta, double timeTotal) {
		this.scene.onUpdate(timeDelta, timeTotal);
	}

	/**
	 * The render method of the game.
	 * 
	 * @param g The graphics object.
	 */
	private void onRender(Graphics g) {
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, WIDTH, HEIGHT);
		g.setColor(Color.WHITE);
		this.scene.onRender(g);
	}
	
	/**
	 * @return The plugin manager of this game
	 */
	public PluginManager getPluginManager() {
		return this.pluginManager;
	}
	
	@Override
	public Map<String, ExperienceType> getExpTypes() {
		return this.expPools;
	}

	/**
	 * Entry-Point
	 * 
	 * @param args Arguments
	 */
	public static void main(String[] args) {
		new PokeGame();
	}

}
