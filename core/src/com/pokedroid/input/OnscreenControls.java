package com.pokedroid.input;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Disposable;
import com.pokedroid.PokeDroid;

/**
 * <p>The {@code OnscreenControls} determines the different controls that can
 * be used on a touch screen device.</p>
 * 
 * @author PoketronHacker
 * @version v1.0
 * @since v1.0
 *
 */
public class OnscreenControls implements InputProcessor, Disposable {
	private static final float ALPHA = 0.66f;
	
	private PokeDroid game;
	private Camera camera;
	private SpriteBatch batch;
	private List<Control> controls;
	private boolean isVisible;
	
	/**
	 * <p>Constructor for {@code OnscreenControls}.</p>
	 * 
	 * @param game The game to attach to.
	 */
	public OnscreenControls(PokeDroid game) {
		this.camera = game.createCamera();
		this.controls = Collections.synchronizedList(new ArrayList<Control>());
		this.controls.add(new ButtonControl(new Texture(Gdx.files.internal("graphics/ui/a_button.png")),new Vector2(32f, 32f),Keys.Z));
		this.controls.add(new ButtonControl(new Texture(Gdx.files.internal("graphics/ui/b_button.png")),new Vector2(100f, 32f),Keys.X));
		Texture startTexture = new Texture(Gdx.files.internal("graphics/ui/start_button.png")),
				selectTexture = new Texture(Gdx.files.internal("graphics/ui/select_button.png"));
		this.controls.add(new ButtonControl(startTexture,
				new Vector2((camera.viewportWidth/2f)-startTexture.getWidth()-4f, 32f),Keys.ENTER));
		this.controls.add(new ButtonControl(selectTexture,
				new Vector2((camera.viewportWidth/2f)+4f, 32f),Keys.BACKSPACE));
		this.controls.add(new DPADControl(new Texture(Gdx.files.internal("graphics/ui/dpad.png")), new Vector2(camera.viewportWidth-156f, 32f)));
		this.game = game;
		this.batch = new SpriteBatch();
		this.batch.setProjectionMatrix(camera.combined);
		batch.setColor(1f, 1f, 1f, ALPHA);
		this.isVisible = true;
	}
	
	/**
	 * <p>Renders the onscreen controls.</p>
	 */
	public void render() {
		if(!isVisible)
			return;
		batch.begin();
		for(Control c : controls) {
			c.render(batch);
		}
		batch.end();
	}
	
	/**
	 * <p>Resizes the camera to fit the new screen dimensions.</p>
	 * 
	 * @param width The width of the viewport.
	 * @param height The height of the viewport.
	 */
	public void resize(int width, int height) {
		this.camera = game.createCamera(width, height);
		this.batch.setProjectionMatrix(camera.combined);
	}
	
	/**
	 * <p>Sets the {@code OnscreenControls} to visible or not.</p>
	 * 
	 * @param isVisible The visibility.
	 */
	public void setVisible(boolean isVisible) {
		this.isVisible = isVisible;
	}
	
	/**
	 * <p>Whether the {@code OnscreenControls} are visible.</p>
	 * 
	 * @return Whether the {@code OnscreenControls} are visible.
	 */
	public boolean isVisible() {
		return this.isVisible;
	}

	@Override
	public boolean keyDown(int keycode) {
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		Vector3 v = camera.unproject(new Vector3(screenX, screenY, 0f));
		for(Control c : controls)
			if(c.onTouchDown(v.x, v.y, pointer))
				return true;
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		Vector3 v = camera.unproject(new Vector3(screenX, screenY, 0f));
		for(Control c : controls)
			if(c.onTouchUp(v.x, v.y, pointer))
				return true;
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		Vector3 v = camera.unproject(new Vector3(screenX, screenY, 0f));
		for(Control c : controls)
			if(c.onTouchDragged(v.x, v.y, pointer))
				return true;
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		return false;
	}

	@Override
	public void dispose() {
		for(Control c : controls) {
			c.dispose();
		}
	}
	
}
