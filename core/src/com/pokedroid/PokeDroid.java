package com.pokedroid;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
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
public class PokeDroid extends ApplicationAdapter {
	public static final int VIRTUAL_WIDTH = 768;
	
	private SceneManager sceneManager;
	
	@Override
	public void create() {
		sceneManager = new SceneManager(this);
		sceneManager.push(new SceneSplash());
	}

	@Override
	public void render() {
		sceneManager.update(Gdx.graphics.getDeltaTime());
		Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		sceneManager.render();
	}
	
	@Override
	public void dispose() {
		sceneManager.dispose();
	}
	
	
}
