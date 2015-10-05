package com.pokedroid.scene;

import com.pokedroid.PokeDroid;
import com.pokedroid.map.TileMap;

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
	
	@Override
	public void create(PokeDroid game) {
		this.game = game;
	}

	@Override
	public void update(float timeDelta) {
		
	}

	@Override
	public void render() {
		
	}

	@Override
	public void dispose() {
		
	}

}
