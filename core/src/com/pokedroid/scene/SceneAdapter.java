package com.pokedroid.scene;

import com.pokedroid.PokeDroid;

/**
 * <p>A {@code SceneAdapter} is an abstract {@link Scene}, this is so
 * that the {@link Scene} methods can be overridden as needed.</p>
 * 
 * @author PoketronHacker
 * @version v1.0
 * @since v1.0
 *
 */
public abstract class SceneAdapter implements Scene {
	
	protected PokeDroid game;
	
	@Override
	public void create(PokeDroid game) {
		this.game = game;
	}

	@Override
	public void update(float timeDelta) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void render() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean keyDown(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

}
