package com.pokedroid;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.pokedroid.input.OnscreenControls;
import com.pokedroid.registry.TilesetRegistry;
import com.pokedroid.scene.Scene;
import com.pokedroid.scene.SceneManager;
import com.pokedroid.scene.SceneSplash;
import com.pokedroid.story.Story;

/**
 * <p>This is the core application, this is where the game logic will occur.</p>
 * 
 * @author PoketronHacker
 * @version v1.0
 * @since v1.0
 *
 */
public class PokeDroid extends ApplicationAdapter implements InputProcessor {
	public static final int VIRTUAL_WIDTH = 640;

	public final Color clearColor = new Color(0f, 0f, 0f, 1f);

	private SceneManager sceneManager;
	private OnscreenControls controls;
	private Story story;
	private List<Story> storyList;
	private int storyIndex;

	@Override
	public void create() {
		if(Gdx.input.getInputProcessor() != null)
			Gdx.input.setInputProcessor(new InputMultiplexer(Gdx.input.getInputProcessor(), this));
		else Gdx.input.setInputProcessor(this);
		if(Gdx.app.getType() == ApplicationType.Android)
			controls = new OnscreenControls(this);
		this.storyList = Collections.synchronizedList(new ArrayList<Story>());
		JsonValue stories = new JsonReader().parse(Gdx.files.internal("storylist.json"));
		for(int i = 0; i < stories.get("stories").size; i++)
			this.storyList.add(new Story(Gdx.files.internal(stories.get("stories").getString(i))));
		for(FileHandle f : Gdx.files.external("PokeDroid/Story/").list()) {
			if(f.isDirectory())
				this.storyList.add(new Story(f));
			else if(f.toString().endsWith(".zip"))
				this.storyList.add(new Story(f));
		}
		System.out.println("Loaded " + storyList.size() + " stories.");
		this.story = storyList.get(storyIndex);
		sceneManager = new SceneManager(this);
		sceneManager.push(new SceneSplash());
	}

	@Override
	public void render() {
		sceneManager.update(Gdx.graphics.getDeltaTime());
		Gdx.gl.glClearColor(clearColor.r, clearColor.g, clearColor.b, clearColor.a);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		sceneManager.render();
		if(controls != null) controls.render();
	}

	@Override
	public void resize(int width, int height) {
		if(controls != null) controls.resize(width, height);
		Scene s = sceneManager.getCurrentScene();
		if(s != null) s.resize(width, height);
	}

	@Override
	public void dispose() {
		sceneManager.dispose();
		TilesetRegistry.dispose();
		story.dispose();
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
		float f = ((float)height/(float)width);
		OrthographicCamera camera = new OrthographicCamera(VIRTUAL_WIDTH, VIRTUAL_WIDTH*f);
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
		if(controls != null && controls.touchDown(screenX, screenY, pointer, button))
			return true;
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		if(controls != null && controls.touchUp(screenX, screenY, pointer, button))
			return true;
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		if(controls != null && controls.touchDragged(screenX, screenY, pointer))
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

	/**
	 * <p>Gets the {@code SceneManager} this game is using.</p>
	 * 
	 * @return The {@link SceneManager}.
	 */
	public SceneManager getSceneManager() {
		return this.sceneManager;
	}
	
	/**
	 * <p>Gets the currently loaded {@code Story}.</p>
	 * 
	 * @return The currently loaded {@code Story}.
	 */
	public Story getLoadedStory() {
		return this.story;
	}
	
	public List<Story> getStoryList() {
		return this.storyList;
	}
	
	public int getStoryIndex() {
		return this.storyIndex;
	}
	
	public void setStory(int index) {
		this.story = storyList.get(index);
		this.storyIndex = index;
	}

}
