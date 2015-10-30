package com.pokedroid.scene;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.pokedroid.PokeDroid;
import com.pokedroid.entity.Entity;
import com.pokedroid.entity.EntityDirection;
import com.pokedroid.entity.Player;
import com.pokedroid.map.TileMap;
import com.pokedroid.story.Story;

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
	private SpriteBatch batch;
	private Camera camera;
	private Player player;
	private boolean up, down, left, right;
	private Story story;

	@Override
	public void create(PokeDroid game) {
		this.game = game;
		this.story = game.getLoadedStory();
		this.game.clearColor.set(Color.BLACK);
		this.player = new Player(game.getLoadedStory().getPlayerTexture(), game.getLoadedStory().getStartMap(),
				game.getLoadedStory().getStartX(), game.getLoadedStory().getStartY());
		this.camera = game.createCamera();
		this.camera.position.setZero();
		this.camera.update();
		this.batch = new SpriteBatch();
		this.batch.setProjectionMatrix(camera.combined);
		this.player.getMap().onEnter();
	}

	@Override
	public void update(float timeDelta) {
		if(this.story != game.getLoadedStory()) {
			this.player.setSprite(game.getLoadedStory().getPlayerTexture());
			this.player.setMap(game.getLoadedStory().getStartMap(),
					game.getLoadedStory().getStartX(),
					game.getLoadedStory().getStartY());
			this.story = game.getLoadedStory();
		}
		for(Entity e : player.getMap().getEntityList()) {
			e.update(timeDelta);
		}
		player.applyMapChange();
		camera.position.set(player.getX(), player.getY(), 0f);
		camera.update();
		batch.setProjectionMatrix(camera.combined);
	}

	@Override
	public void render() {
		batch.begin();
		for(int i = 0; i < player.getMap().getLayerCount()-1; i++) {
			player.getMap().render(batch, i, 0f, 0f);
			player.getMap().renderConnections(batch, i, 0f, 0f);
		}
		for(Entity e : player.getMap().getEntityList()) {
			e.render(batch);
		}
		player.getMap().render(batch, player.getMap().getLayerCount()-1, 0f, 0f);
		player.getMap().renderConnections(batch, player.getMap().getLayerCount()-1, 0f, 0f);
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
		} else if(keycode == Keys.ENTER) {
			int index = game.getStoryIndex();
			index++;
			if(index >= game.getStoryList().size())
				index = 0;
			game.setStory(index);
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
