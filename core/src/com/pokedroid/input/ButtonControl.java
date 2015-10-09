package com.pokedroid.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

/**
 * <p>The {@code ButtonControl} is an onscreen button that can be mapped to a key.</p>
 * 
 * @author PoketronHacker
 * @version v1.0
 * @since v1.0
 *
 */
public class ButtonControl extends Control {
	
	private Texture texture;
	private Vector2 position;
	private int keycode;
	private int pointer;
	private Rectangle bounds;
	
	/**
	 * <p>Constructor for a new {@code ButtonControl}.</p>
	 * 
	 * @param texture The texture to render.
	 * @param position The position.
	 * @param keycode The key to emulate.
	 */
	public ButtonControl(Texture texture, Vector2 position, int keycode) {
		this.texture = texture;
		this.position = position;
		this.keycode = keycode;
		this.bounds = new Rectangle(position.x, position.y, texture.getWidth(), texture.getHeight());
		this.pointer = -1;
	}
	
	@Override
	public void render(Batch batch) {
		batch.draw(texture, position.x, position.y);
	}

	@Override
	public boolean onTouchDown(float x, float y, int pointer) {
		if(bounds.contains(x, y) && this.pointer == -1) {
			Gdx.input.getInputProcessor().keyDown(keycode);
			this.pointer = pointer;
			return true;
		}
		return false;
	}

	@Override
	public boolean onTouchUp(float x, float y, int pointer) {
		if(pointer == this.pointer) {
			Gdx.input.getInputProcessor().keyUp(keycode);
			this.pointer = -1;
		}
		return false;
	}

	@Override
	public boolean onTouchDragged(float x, float y, int pointer) {
		return false;
	}

	@Override
	public void dispose() {
		texture.dispose();
	}
	
	
	
}
