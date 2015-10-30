package com.pokedroid.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Texture;
import com.pokedroid.map.TileMap;

/**
 * <p>The {@code Player} is the {@link Entity} that the player will control.</p>
 * 
 * @author PoketronHacker
 * @version v1.0
 *
 */
public class Player extends HumanEntity {

	/**
	 * <p>Constructor for a new {@code Player}.</p>
	 * 
	 * @param map The map the player will start on.
	 * @param walk The walking texture.
	 */
	public Player(Texture walk, TileMap map, int x, int y) {
		super(walk, map, x, y);
	}
	
	/**
	 * <p>Applies a map change.</p>
	 */
	public void applyMapChange() {
		if(tileY < 0) {
			setMap(map.getConnection("south").map, tileX-map.getConnection("south").offset, map.getConnection("south").map.getHeight()-1, false);
		} else if(tileY >= map.getHeight()) {
			setMap(map.getConnection("north").map, tileX-map.getConnection("north").offset, 0, false);
		}
	}
	
	@Override
	protected boolean canMove(int x, int y) {
		if(Gdx.input.isKeyPressed(Keys.SHIFT_LEFT))
			return true;
		boolean b = super.canMove(x, y);
		if(b) return b;
		if(y < 0) {
			if(map.hasConnection("south")) {
				return map.getConnection("south").map.canMove(tileX-map.getConnection("south").offset, map.getConnection("south").map.getHeight()-1);
			}
		} else if(y >= map.getHeight()) {
			if(map.hasConnection("north"))
				return map.getConnection("north").map.canMove(tileX-map.getConnection("north").offset, 0);
		}
		return super.canMove(x, y);
	}

}
