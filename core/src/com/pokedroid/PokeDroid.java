package com.pokedroid;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.pokedroid.input.OnscreenControls;
import com.pokedroid.scene.Scene;
import com.pokedroid.scene.SceneManager;
import com.pokedroid.scene.SceneSplash;

/**
 * <p>This is the core application, this is where the game logic will occur.</p>
 * 
 * @author PoketronHacker
 * @version v1.0
 * @since v1.0
 *
 */
public class PokeDroid extends ApplicationAdapter implements InputProcessor {
	public static final int VIRTUAL_WIDTH = 768;
	
	public final Color clearColor = new Color(0f, 0f, 0f, 1f);
	
	private SceneManager sceneManager;
	private OnscreenControls controls;
	
	@Override
	public void create() {
		if(Gdx.input.getInputProcessor() != null)
			Gdx.input.setInputProcessor(new InputMultiplexer(Gdx.input.getInputProcessor(), this));
		else Gdx.input.setInputProcessor(this);
		controls = new OnscreenControls(this);
		sceneManager = new SceneManager(this);
		sceneManager.push(new SceneSplash());
	}

	@Override
	public void render() {
		sceneManager.update(Gdx.graphics.getDeltaTime());
		Gdx.gl.glClearColor(clearColor.r, clearColor.g, clearColor.b, clearColor.a);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		sceneManager.render();
		controls.render();
	}
	
	@Override
	public void resize(int width, int height) {
		Scene s = sceneManager.getCurrentScene();
		if(s != null) s.resize(width, height);
	}
	
	@Override
	public void dispose() {
		sceneManager.dispose();
	}
	
	/**
	 * <p>Creates a camera using the {@link PokeDroid#VIRTUAL_WIDTH} and using
	 * the screens width and height.</p>
	 * 
	 * @return The camera.
	 */
	public OrthographicCamera createCamera() {
		return createCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
	}
	
	/**
	 * <p>Creates a camera using the {@link PokeDroid#VIRTUAL_WIDTH}.</p>
	 * 
	 * @param width The width of the screen.
	 * @param height The height of the screen.
	 * @return The camera just created.
	 */
	public OrthographicCamera createCamera(int width, int height) {
		OrthographicCamera camera = new OrthographicCamera(VIRTUAL_WIDTH, VIRTUAL_WIDTH*((float)height/(float)width));
		camera.translate(camera.viewportWidth/2f, camera.viewportHeight/2f);
		camera.update();
		return camera;
	}

	@Override
	public boolean keyDown(int keycode) {
		Scene s = sceneManager.getCurrentScene();
		if(s != null)
			return s.keyDown(keycode);
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		Scene s = sceneManager.getCurrentScene();
		if(s != null)
			return s.keyUp(keycode);
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		if(controls.touchDown(screenX, screenY, pointer, button))
			return true;
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		if(controls.touchUp(screenX, screenY, pointer, button))
			return true;
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		if(controls.touchDragged(screenX, screenY, pointer))
			return true;
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		// TODO Auto-generated method stub
		return false;
	}
	
	/**
	 * <p>Gets the {@code SceneManager} this game is using.</p>
	 * 
	 * @return The {@link SceneManager}.
	 */
	public SceneManager getSceneManager() {
		return this.sceneManager;
	}
	
	
}
