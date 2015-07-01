package org.game;

/**
 * <p>Our Game configurations.</p>
 * 
 * @author Joshua Adam Kitchen
 * @version v1.0
 * @since v1.0
 *
 */
public class GameConfig {
	
	private GameConfig() {}
	
	/** The dimensions of the game. */
	public static final int GAME_WIDTH = 480, GAME_HEIGHT = 320;
	
	/** The tile dimensions for our game. */
	public static final int TILE_WIDTH = 32, TILE_HEIGHT = 32;
	
	/** The fps we are targeting. */
	public static final int TARGET_FPS = 60;
	
}
