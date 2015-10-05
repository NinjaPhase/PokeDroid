package com.pokedroid.scene;

import com.badlogic.gdx.utils.Disposable;
import com.pokedroid.PokeDroid;

/**
 * <p>A {@code Scene} will be used to update and render a certain portion of the game, there
 * will be one for each major aspect such as battling or walking around the map.</p>
 * 
 * @author PoketronHacker
 * @version v1.0
 * @since v1.0
 *
 */
public interface Scene extends Disposable {
	
	/**
	 * <p>Creates the {@code Scene}.</p>
	 * 
	 * @param game The game to bind to the {@code Scene}.
	 */
	public void create(PokeDroid game);
	
	/**
	 * <p>Updates the {@code Scene}.<p>
	 * 
	 * @param timeDelta The delta time.
	 */
	public void update(float timeDelta);
	
	/**
	 * <p>Renders the {@code Scene}.</p>
	 */
	public void render();
	
	@Override
	public void dispose();
	
}
