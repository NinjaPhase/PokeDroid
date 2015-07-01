package org.game;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.image.BufferedImage;
import javax.swing.JFrame;
import javax.swing.JPanel;

import org.game.input.SceneInput;
import org.game.scene.Scene;
import org.game.scene.SceneLoad;
import org.game.scene.SceneManager;

/**
 * <p>The Entry-Point for our game that will also hold the game loop.</p>
 * 
 * @author Joshua Adam Kitchen
 * @version v1.0
 * @since v1.0
 *
 */
public class Game extends JPanel implements Runnable, ComponentListener {
	private static final long serialVersionUID = 8285727839266387543L;
	private static final long OPTIMAL_TIME = 1000000000/GameConfig.TARGET_FPS;

	private final JFrame jframe;
	private boolean isRunning;
	private BufferedImage backBuffer;
	private int fps;
	private SceneManager sceneManager;

	/**
	 * <p>Constructor for a {@code Game}.</p>
	 */
	public Game(final JFrame jframe) {
		this.jframe = jframe;
		this.setPreferredSize(new Dimension(GameConfig.GAME_WIDTH, GameConfig.GAME_HEIGHT));
		this.addComponentListener(this);
		this.setFocusable(true);
	}

	/**
	 * <p>Runs our initialisation process and game loop.</p>
	 */
	@Override
	public void run() {
		init();
		loop();
	}

	/**
	 * <p>Initialises the game.</p>
	 */
	private void init() {
		jframe.setTitle("YAPJE");
		backBuffer = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);
		sceneManager = new SceneManager(this);
		sceneManager.pushScene(new SceneLoad());
		this.addKeyListener(new SceneInput(sceneManager));
	}

	/**
	 * <p>Our game loop.
	 */
	private void loop() {
		isRunning = true;
		long fpsCounter = 0L;
		long lastUpdate = System.nanoTime();
		int curFps = 0;
		while(isRunning) {
			long now = System.nanoTime();
			long updateLength = now-lastUpdate;
			lastUpdate = now;
			double timeDelta = updateLength/(double)OPTIMAL_TIME;

			fpsCounter += updateLength;
			curFps++;
			if(fpsCounter >= 1000000000) {
				fps = curFps;
				jframe.setTitle("YAPJE - FPS: " + fps);
				curFps = 0;
				fpsCounter = 0L;
			}
			isRunning = jframe.isVisible();

			onUpdate(timeDelta);
			Graphics2D g = backBuffer.createGraphics();
			onRender(g);
			g.dispose();
			getGraphics().drawImage(backBuffer, 0, 0, null);

			try {
				Thread.sleep(Math.abs(lastUpdate-System.nanoTime()+OPTIMAL_TIME)/1000000);
			} catch (InterruptedException e) {
				System.err.println("[Game] Unable to create game loop: " + e.getMessage());
			}
		}
	}
	
	/**
	 * <p>The delta time method.</p>
	 * 
	 * @param timeDelta The delta time.
	 */
	public void onUpdate(double timeDelta) {
		Scene cur = sceneManager.getCurrentScene();
		if(cur != null) cur.onUpdate(timeDelta);
	}

	/**
	 * <p>The render method.</p>
	 * 
	 * @param g The graphics to render onto.
	 */
	public void onRender(Graphics2D g) {
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, backBuffer.getWidth(), backBuffer.getHeight());
		g.setColor(Color.WHITE);
		sceneManager.renderScenes(g);
	}
	
	/**
	 * <p>Gets the {@code SceneManager} this game will
	 * be using to control different areas of the game.</p>
	 * 
	 * @return The scene manager.
	 */
	public SceneManager getSceneManager() {
		return this.sceneManager;
	}

	/**
	 * <p>Main Entry-Point.</p>
	 * 
	 * @param args The program arguments for our game.
	 */
	public static void main(String[] args) {
		JFrame frame = new JFrame();
		Game game = new Game(frame);
		frame.add(game);
		frame.pack();
		frame.setResizable(true);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		(new Thread(game)).start();
	}

	@Override
	public void componentHidden(ComponentEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void componentMoved(ComponentEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void componentResized(ComponentEvent e) {
		if(backBuffer != null) {
			synchronized(backBuffer) {
				backBuffer = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);
			}
		}
	}

	@Override
	public void componentShown(ComponentEvent e) {
		// TODO Auto-generated method stub

	}

}
