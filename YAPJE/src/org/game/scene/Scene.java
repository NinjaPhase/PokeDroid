package org.game.scene;

import java.awt.Graphics2D;

import org.game.Game;

/**
 * <p>A {@code Scene} is a part of the game.</p>
 * 
 * @author Joshua Adam Kitchen
 * @version v1.0
 * @since v1.0
 *
 */
public abstract class Scene {
	
	protected Game game;
	
	/**
	 * <p>Called when the initialised.</p>
	 */
	public abstract void init();
	
	/**
	 * <p>Called each tick to update the game.</p>
	 * 
	 * @param timeDelta The time delta for timer values.
	 */
	public abstract void onUpdate(double timeDelta);
	
	/**
	 * <p>Called on the render tick.</p>
	 * 
	 * @param g The graphics to render onto.
	 */
	public abstract void onRender(Graphics2D g);
	
	/**
	 * <p>Called when the scene is due to be destroyed.</p>
	 */
	public abstract void cleanup();
	
	/**
	 * <p>Called when focus is gained.</p>
	 */
	public abstract void focusGained();
	
	/**
	 * <p>Called when focus is lost.</p>
	 */
	public abstract void focusLost();
	
}
