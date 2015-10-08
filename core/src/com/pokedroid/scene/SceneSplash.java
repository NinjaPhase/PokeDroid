package com.pokedroid.scene;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.pokedroid.PokeDroid;

/**
 * <p>The {@code SceneSplash} will display a simple splash screen.</p>
 * 
 * @author PoketronHacker
 * @version v1.0
 * @since v1.0
 *
 */
public class SceneSplash implements Scene {
	private static final String[] SPLASH_TEXT = new String[]{
			"This game is a fan game and is no way affiliated with nintendo."
	};
	private static final float TIME_TO_MAP = 3f;

	private PokeDroid game;
	private BitmapFont font;
	private SpriteBatch batch;
	private String currentText;
	private GlyphLayout layout;
	private Camera camera;
	private float timer;

	@Override
	public void create(PokeDroid game) {
		this.game = game;
		camera = game.createCamera();
		System.out.println(camera.viewportWidth + ":" + camera.viewportHeight);
		font = new BitmapFont();
		batch = new SpriteBatch();
		batch.setProjectionMatrix(camera.combined);
		currentText = SPLASH_TEXT[0];
		layout = new GlyphLayout(font, currentText);
	}

	@Override
	public void update(float timeDelta) {
		timer += timeDelta;
		if(timer >= TIME_TO_MAP) {
			game.getSceneManager().remove(this);
			game.getSceneManager().push(new SceneMap());
		}
	}

	@Override
	public void render() {
		batch.begin();
		font.draw(batch, currentText,
				((camera.viewportWidth-layout.width)/2f),
				((camera.viewportHeight+layout.height)/2f));
		batch.end();
	}
	
	@Override
	public void resize(int width, int height) {
		camera = game.createCamera(width, height);
		batch.setProjectionMatrix(camera.combined);
	}

	@Override
	public void dispose() {
		font.dispose();
		batch.dispose();
	}

	@Override
	public boolean keyDown(int keycode) {
		if(keycode == Keys.Z ||
				keycode == Keys.ENTER) {
			timer = TIME_TO_MAP;
			return true;
		}
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		return false;
	}

}
