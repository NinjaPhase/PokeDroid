package com.pokedroid.scene;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.JsonReader;
import com.pokedroid.PokeDroid;
import com.pokedroid.entity.Entity;
import com.pokedroid.entity.EntityDirection;
import com.pokedroid.entity.Player;
import com.pokedroid.map.TileMap;
import com.pokedroid.map.TileSet;

/**
 * <p>This is the map scene, where the {@link TileMap}'s will be rendered and all
 * entities.</p>
 * 
 * @author PoketronHacker
 * @version v1.0
 * @since v1.0
 *
 */
public class SceneMap implements Scene {

	private PokeDroid game;
	private TileSet johto;
	private TileMap firstMap;
	private SpriteBatch batch;
	private Camera camera;
	private Player player;
	private boolean up, down, left, right;

	@Override
	public void create(PokeDroid game) {
		this.game = game;
		this.game.clearColor.set(Color.BLACK);
		this.johto = new TileSet(new JsonReader().parse(Gdx.files.internal("maps/set_johto.json")));
		this.firstMap = new TileMap(new JsonReader().parse(Gdx.files.internal("maps/map_firstMap.json")), this.johto);
		this.player = new Player(firstMap);
		this.camera = game.createCamera();
		this.camera.position.setZero();
		this.camera.update();
		this.batch = new SpriteBatch();
		this.batch.setProjectionMatrix(camera.combined);
	}

	@Override
	public void update(float timeDelta) {
		for(Entity e : firstMap.getEntityList()) {
			e.update(timeDelta);
		}
		camera.position.set(player.getPosition(), 0f);
		camera.update();
		batch.setProjectionMatrix(camera.combined);
	}

	@Override
	public void render() {
		batch.begin();
		firstMap.render(batch, 0, 0f, 0f);
		for(Entity e : firstMap.getEntityList()) {
			e.render(batch);
		}
		batch.end();
	}

	@Override
	public void resize(int width, int height) {
		camera = game.createCamera(width, height);
		camera.position.setZero();
		camera.update();
		batch.setProjectionMatrix(camera.combined);
	}

	@Override
	public void dispose() {
		batch.dispose();
	}

	@Override
	public boolean keyDown(int keycode) {
		if(keycode == Keys.UP) {
			up = true;
			player.move(EntityDirection.DIRECTION_UP);
			return true;
		} else if(keycode == Keys.DOWN) {
			down = true;
			player.move(EntityDirection.DIRECTION_DOWN);
		} else if(keycode == Keys.LEFT) {
			left = true;
			player.move(EntityDirection.DIRECTION_LEFT);
		} else if(keycode == Keys.RIGHT) {
			right = true;
			player.move(EntityDirection.DIRECTION_RIGHT);
		}
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		if(keycode == Keys.UP) up = false;
		else if(keycode == Keys.DOWN) down = false;
		else if(keycode == Keys.LEFT) left = false;
		else if(keycode == Keys.RIGHT) right = false;
		if(keycode == Keys.UP || keycode == Keys.DOWN
				|| keycode == Keys.LEFT || keycode == Keys.RIGHT) {
			if(up) {
				player.move(EntityDirection.DIRECTION_UP);
			} else if(down) {
				player.move(EntityDirection.DIRECTION_DOWN);
			} else if(left) {
				player.move(EntityDirection.DIRECTION_LEFT);
			} else if(right) {
				player.move(EntityDirection.DIRECTION_RIGHT);
			} else player.move(null);
		}
		return false;
	}

}