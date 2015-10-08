package com.pokedroid.input;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.utils.Disposable;

/**
 * <p>A {@code Control} is a component to be used with the {@link OnscreenControls}.</p>
 * 
 * @author PoketronHacker
 * @version v1.0
 * @since v1.0
 *
 */
public abstract class Control implements Disposable {
	
	/**
	 * <p>Renders the control onto a {@code Batch}.</p>
	 * 
	 * @param batch The {@code Batch} to render onto.
	 */
	public abstract void render(Batch batch);
	
	/**
	 * <p>Called when the screen is touched.</p>
	 * 
	 * @param x The x position.
	 * @param y The y position.
	 * @param pointer The pointer used.
	 */
	public abstract boolean onTouchDown(float x, float y, int pointer);
	
	/**
	 * <p>Called when the screen is released.</p>
	 * 
	 * @param x The x position.
	 * @param y The y position.
	 * @param pointer The pointer used.
	 */
	public abstract boolean onTouchUp(float x, float y, int pointer);
	
	/**
	 * <p>Called when the screen is dragged.</p>
	 * 
	 * @param x The x position.
	 * @param y The y position.
	 * @param pointer The pointer used.
	 */
	public abstract boolean onTouchDragged(float x, float y, int pointer);
	
	@Override
	public abstract void dispose();
	
}
