package pokejava;

import java.awt.Color;
import java.awt.Graphics;

import pokejava.api.Game;
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

	private boolean isRunning;

	/**
	 * Constructor for the game object.
	 */
	public PokeGame() {
		this.display = new Display(TITLE, WIDTH, HEIGHT);
		this.scene = new SceneMap(this);
		this.isRunning = true;
		updateLoop();
	}
	
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
	 * Entry-Point
	 * 
	 * @param args Arguments
	 */
	public static void main(String[] args) {
		new PokeGame();
	}

}
