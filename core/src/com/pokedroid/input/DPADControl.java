package com.pokedroid.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

/**
 * <p>The {@code DPADControl} is a virtual DPAD used for movement on 4 axis.</p>
 * 
 * @author PoketronHacker
 * @version v1.0
 *
 */
public class DPADControl extends Control {
	private static final float WHITE_SPACE = 40f, BUTTON_SPACE = 46f;
	private static final int UP_KEY = Keys.UP, DOWN_KEY = Keys.DOWN, LEFT_KEY = Keys.LEFT, RIGHT_KEY = Keys.RIGHT;
	private static final int[] KEYS = new int[]{UP_KEY,DOWN_KEY,LEFT_KEY,RIGHT_KEY};
	
	private Texture texture;
	private Vector2 position;
	private Rectangle[] bounds;
	private int[] pointer;
	
	/**
	 * <p>Constructor for a new {@code DPADControl}.</p>
	 * 
	 * @param texture The texture of the dpad.
	 * @param position The position of the control.
	 */
	public DPADControl(Texture texture, Vector2 position) {
		this.texture = texture;
		this.position = position;
		this.bounds = new Rectangle[]{
			new Rectangle(position.x+WHITE_SPACE, position.y+texture.getHeight()-BUTTON_SPACE, BUTTON_SPACE, BUTTON_SPACE),
			new Rectangle(position.x+WHITE_SPACE, position.y, BUTTON_SPACE, BUTTON_SPACE),
			new Rectangle(position.x, position.y+WHITE_SPACE, BUTTON_SPACE, BUTTON_SPACE),
			new Rectangle(position.x+texture.getWidth()-BUTTON_SPACE, position.y+WHITE_SPACE, BUTTON_SPACE, BUTTON_SPACE)
		};
		this.pointer = new int[]{-1,-1,-1,-1};
	}

	@Override
	public void render(Batch batch) {
		batch.draw(texture, position.x, position.y);
	}

	@Override
	public boolean onTouchDown(float x, float y, int pointer) {
		for(int i = 0; i < bounds.length; i++) {
			if(bounds[i].contains(x, y) && this.pointer[i] == -1) {
				Gdx.input.getInputProcessor().keyDown(KEYS[i]);
				this.pointer[i] = pointer;
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean onTouchUp(float x, float y, int pointer) {
		for(int i = 0; i < this.pointer.length; i++) {
			if(this.pointer[i] == pointer) {
				Gdx.input.getInputProcessor().keyUp(KEYS[i]);
				this.pointer[i] = -1;
			}
		}
		return false;
	}

	@Override
	public boolean onTouchDragged(float x, float y, int pointer) {
		for(int i = 0; i < bounds.length; i++) {
			if(this.pointer[i] == pointer && !bounds[i].contains(x, y)) {
				Gdx.input.getInputProcessor().keyUp(KEYS[i]);
				this.pointer[i] = -1;
			} else if(this.pointer[i] == -1 && bounds[i].contains(x, y)) {
				Gdx.input.getInputProcessor().keyDown(KEYS[i]);
				this.pointer[i] = pointer;
				
			}
		}
		return false;
	}

	@Override
	public void dispose() {
		
	}
	
}
