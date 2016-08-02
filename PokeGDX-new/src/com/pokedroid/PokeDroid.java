package com.pokedroid;

import java.security.SecureRandom;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.pokedroid.pokemon.Species;
import com.pokedroid.scene.SceneLoad;
import com.pokedroid.scene.SceneManager;
import com.pokedroid.story.Story;
import com.pokedroid.util.ResourceManager;

/**
 * <p>The {@code PokeDroid} is the main application
 * code.</p>
 * 
 * @author J. Kitchen
 * @version 10 March 2016
 *
 */
public class PokeDroid implements ApplicationListener {
	public static final Random RANDOM = new SecureRandom(
			new byte[]{
					((byte) (System.nanoTime() >> 56)),
					((byte) (System.nanoTime() >> 48)),
					((byte) (System.nanoTime() >> 32)),
					((byte) (System.nanoTime() >> 24)),
					((byte) (System.nanoTime() >> 16)),
					((byte) (System.nanoTime() >> 8)),
					((byte) (System.nanoTime()))
			});
	public final Color bgColor = new Color(0.0f, 0.0f, 0.0f, 1.0f);

	private SpriteBatch batch;
	private ResourceManager resourceManager;
	private SceneManager sceneManager;

	private Map<String, Story> story;

	@Override
	public void create() {
		this.story = Collections.synchronizedMap(new LinkedHashMap<>());

		Story story = new Story(Gdx.files.local("./Story/Pokemon GBA/"));
		this.story.put(story.getName(), story);

		this.batch = new SpriteBatch();
		this.resourceManager = new ResourceManager();
		this.sceneManager = new SceneManager(this);
		this.sceneManager.push(new SceneLoad());
		Gdx.input.setInputProcessor(sceneManager);
	}

	@Override
	public void resize(int width, int height) {
		sceneManager.resize(width, height);
	}

	@Override
	public void render() {
		sceneManager.update(Gdx.graphics.getDeltaTime());
		Gdx.gl.glClearColor(bgColor.r, bgColor.g, bgColor.b, bgColor.a);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.begin();
		sceneManager.render(batch);
		batch.end();
	}

	@Override
	public void pause() {

	}

	@Override
	public void resume() {

	}

	@Override
	public void dispose() {
		this.sceneManager.dispose();
		this.resourceManager.dispose();
		Species.clearRegistry();
	}

	/**
	 * <p>The Global SceneManager for state control.</p>
	 * 
	 * @return The {@code SceneManager}.
	 */
	public SceneManager getSceneManager() {
		return this.sceneManager;
	}

	/**
	 * <p>Global Resources are available to every single
	 * scene.</p>
	 * 
	 * @return The global resources.
	 */
	public ResourceManager getGlobalResources() {
		return this.resourceManager;
	}

	/**
	 * <p>The {@code Main} code.</p>
	 * 
	 * @param args The program arguments.
	 */
	public static void main(String[] args) {
		new LwjglApplication(new PokeDroid(), "PokeDroid - A Pokémon Simulator", 640, 360);
	}

}
