package com.pokedroid.scene;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.pokedroid.PokeDroid;
import com.pokedroid.desktop.DesktopMidiMusic;
import com.pokedroid.scene.battle.SceneBattle;
import com.pokedroid.scene.window.ChoiceBox;
import com.pokedroid.scene.window.MessageBox;
import com.pokedroid.scene.window.MessageBox.MessageBoxSkin;
import com.pokedroid.util.ResourceManager;

/**
 * <p>The {@code SceneTitleMenu} is used to get into
 * the game and select a story.</p>
 * 
 * @author J. Kitchen
 * @version 16 March 2016
 *
 */
public class SceneTitleMenu implements IScene {

	private PokeDroid game;
	private ResourceManager rm;
	private NinePatch window;
	private BitmapFont font;
	private Texture titlePokemon;
	private Music music;
	private int selected;
	private ChoiceBox mainMenu;
	private MessageBoxSkin messageBoxSkin;

	@Override
	public void create(PokeDroid game) {
		this.game = game;
		game.bgColor.set(0.6f, 0.85f, 0.92f, 1.0f);
		this.rm = game.getGlobalResources().createSubManager(getClass().getSimpleName());
		this.window = new NinePatch(
				new Texture("graphics/ui/windowskin.png"),
				16, 16, 16, 16);
		this.font = new BitmapFont(Gdx.files.internal("graphics/fonts/mainFont.fnt"));
		this.titlePokemon = rm.load("TitlePokemon", new Texture("graphics/ui/titlePokemon.png"));
		this.rm.load("Music", (music = new DesktopMidiMusic(Gdx.files.internal("music.mid"), 4.2f)));
		this.messageBoxSkin = new MessageBoxSkin(font, window);
		this.mainMenu = new ChoiceBox(messageBoxSkin, 1, 5,
				new Object[]{"NEW GAME", "LOAD GAME", "ACHIEVEMENTS", "STORY SELECT", "EXIT"});
		music.setLooping(true);
		music.play();
	}

	@Override
	public void update(float deltaTime) {

	}

	@Override
	public void render(Batch batch) {
		this.mainMenu.render(batch, 0.0f, Gdx.graphics.getHeight()-mainMenu.getHeight());
		batch.draw(titlePokemon,
				Gdx.graphics.getWidth()-titlePokemon.getWidth()-64.0f,
				(Gdx.graphics.getHeight()-titlePokemon.getHeight())/2);
	}

	@Override
	public void resize(int width, int height) {

	}

	@Override
	public void focusGained() {

	}

	@Override
	public void focusLost() {

	}

	@Override
	public void dispose() {
		this.rm.dispose();
	}

	@Override
	public boolean keyDown(int keycode) {
		if(keycode == Keys.DOWN) {
			mainMenu.moveCursor(0, 1);
			return true;
		} else if (keycode == Keys.UP) {
			mainMenu.moveCursor(0, -1);
			return true;
		} else if(keycode == Keys.Z) {
			if(selected == 0) {
				music.stop();
				game.getSceneManager().push(new SceneBattle());
			} else if(selected == 3) {
				
			} else if(selected == 4) {
				Gdx.app.exit();
			}
			return true;
		}
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		return false;
	}

}
