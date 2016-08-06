package com.pokedroid.editor.map.exception;

/**
 * <p>
 * Thrown if the tile is out of the maps width or height.
 * </p>
 * 
 * @author PoketronHacker
 * @version 10 Occober 2015
 *
 */
public class InvalidTileException extends RuntimeException {
	private static final long serialVersionUID = -4344990397480294277L;

	/**
	 * <p>
	 * Constructs a {@code InvalidTileException} with a message.
	 * </p>
	 * 
	 * @param s
	 *            The string.
	 */
	public InvalidTileException(String s) {
		super(s);
	}

	/**
	 * <p>
	 * Construct a {@code InvalidTileException} with a pre-defined message.
	 * </p>
	 * 
	 * @param x
	 *            The x location.
	 * @param y
	 *            The y location.
	 */
	public InvalidTileException(int x, int y) {
		super("Tile at (" + x + ", " + y + ") was out of bounds");
	}

}
