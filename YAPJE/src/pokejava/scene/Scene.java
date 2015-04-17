package pokejava.scene;

import java.awt.Graphics;

import pokejava.PokeGame;

/**
 * The scene system which will be used to separate different areas of the game.
 * 
 * @author PoketronHacker
 * @version 1.0
 *
 */
public abstract class Scene {
	
	protected PokeGame game;
	
	/**
	 * Constructor for a new Scene.
	 * 
	 * @param game The game this scene belongs to.
	 */
	public Scene(PokeGame game) {
		this.game = game;
	}
	
	/**
	 * The update method of the scene.
	 * 
	 * @param timeDelta The delta time.
	 * @param timeTotal The total time elapsed.
	 */
	public abstract void onUpdate(double timeDelta, double timeTotal);
	
	/**
	 * The render method of the scene.
	 * 
	 * @param g The graphics to render onto.
	 */
	public abstract void onRender(Graphics g);
	
}
