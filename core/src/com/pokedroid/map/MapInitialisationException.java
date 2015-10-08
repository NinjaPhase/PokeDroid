package com.pokedroid.map;

/**
 * <p>A {@code MapInitialisationException} is called when a problem occurs during
 * initialisation of a map.</p>
 * 
 * @author PoketronHacker
 * @version v1.0
 * @since v1.0
 *
 */
public class MapInitialisationException extends RuntimeException {
	private static final long serialVersionUID = 8781274439038934344L;

	/**
	 * <p>Constructor for a new {@code MapInitialisationException}.</p>
	 * 
	 * @param message The exception message.
	 */
	public MapInitialisationException(String message) {
		super(message);
	}
	
}
