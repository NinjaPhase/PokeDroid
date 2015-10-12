package com.pokedroid.scene;

import java.util.ArrayDeque;
import java.util.Queue;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.pokedroid.PokeDroid;
import com.pokedroid.map.TileMap;
import com.pokedroid.map.TileSet;
import com.pokedroid.registry.TileMapRegistry;
import com.pokedroid.registry.TilesetRegistry;

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
	private Queue<FileHandle> maps, tilesets;
	private boolean finished;

	@Override
	public void create(PokeDroid game) {
		this.maps = new ArrayDeque<FileHandle>();
		this.tilesets = new ArrayDeque<FileHandle>();
		this.game = game;
		camera = game.createCamera();
		font = new BitmapFont();
		batch = new SpriteBatch();
		batch.setProjectionMatrix(camera.combined);
		currentText = SPLASH_TEXT[0];
		layout = new GlyphLayout(font, currentText);
		FileHandle assetDir;
		if(Gdx.app.getType() == ApplicationType.Android) {
			assetDir = Gdx.files.internal("maps/");
		} else assetDir = Gdx.files.internal("./bin/maps/");
		loadDirectory(assetDir);
	}

	@Override
	public void update(float timeDelta) {
		if(!tilesets.isEmpty()) {
			FileHandle f = tilesets.poll();
			JsonValue v = new JsonReader().parse(f);
			TileSet set = new TileSet(v);
			TilesetRegistry.registerTileset(v.getString("name"), set);
		} else if(!maps.isEmpty()) {
			FileHandle f = maps.poll();
			JsonValue v = new JsonReader().parse(f);
			TileMap map = new TileMap(new JsonReader().parse(f), TilesetRegistry.getTileset(v.getString("tileset")));
			TileMapRegistry.registerMap(v.getString("name"), map);
		}
		timer += timeDelta;
		if(timer >= TIME_TO_MAP) {
			finished = (maps.isEmpty() && tilesets.isEmpty());
			if(finished) {
				game.getSceneManager().remove(this);
				game.getSceneManager().push(new SceneMap());
			}
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

	private void loadDirectory(FileHandle dir) {
		if(!dir.isDirectory())
			return;
		for(FileHandle f : dir.list()) {
			if(f.isDirectory()) {
				loadDirectory(f);
			} else {
				if(!f.name().contains("_"))
					continue;
				String tag = f.name().substring(0, f.name().indexOf("_"));
				if(tag.equals("map")) {
					maps.add(f);
				} else if(tag.equals("set")) {
					tilesets.add(f);
				}
			}
		}
	}

}
