package com.pokedroid.scene;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
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

	private BitmapFont font;
	private SpriteBatch batch;
	private String currentText;
	private GlyphLayout layout;

	@Override
	public void create(PokeDroid game) {
		font = new BitmapFont();
		batch = new SpriteBatch();
		currentText = SPLASH_TEXT[0];
		layout = new GlyphLayout(font, currentText);
	}

	@Override
	public void update(float timeDelta) {
		
	}

	@Override
	public void render() {
		batch.begin();
		font.draw(batch, currentText,
				(Gdx.graphics.getWidth()-layout.width)/2f, ((Gdx.graphics.getHeight()+layout.height)/2f));
		batch.end();
	}

	@Override
	public void dispose() {
		font.dispose();
		batch.dispose();
	}

}
