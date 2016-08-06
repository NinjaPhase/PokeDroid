package com.pokedroid.editor.gui.brush;

/**
 * <p>
 * A {@code TileSelection} is a group of tiles that is then drawn by a brush.
 * </p>
 * 
 * @author PoketronHacker
 * @version 18 October 2015
 *
 */
public class TileSelection {

	public int[] tiles;
	public int w, h;

	/**
	 * <p>Checks if the selection contains a certain
	 * tile.</p>
	 *
	 * @param tile The tile to check.
	 * @return {@code true} if the array contains the tile.
     */
	public boolean contains(int tile) {
		if(tiles == null)
			return false;
		for(int i : tiles)
			if (i == tile)
				return true;
		return false;
	}

}
