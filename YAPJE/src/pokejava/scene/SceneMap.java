package pokejava.scene;

import java.awt.Graphics;

import pokejava.PokeGame;

/**
 * The map scene of the game, used to control the players coordinates and the players cell.
 * 
 * @author PoketronHacker
 * @version 1.0
 * 
 * @see Scene
 */
public class SceneMap extends Scene {

	/**
	 * Constructor for a new SceneMap.
	 * 
	 * @param game The game this scene map belongs to.
	 */
	public SceneMap(PokeGame game) {
		super(game);
	}

	@Override
	public void onUpdate(double timeDelta, double timeTotal) {
		
	}

	@Override
	public void onRender(Graphics g) {
		g.fillRect(32, 32, 32, 32);
	}
	
}
