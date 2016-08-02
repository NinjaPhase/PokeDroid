package com.pokedroid.story;

/**
 * <p>The {@code StoryLoadException} is thrown when a story
 * has missing files.</p>
 * 
 * @author J. Kitchen
 * @version 15 March 2016
 *
 */
public class StoryLoadException extends RuntimeException {
	private static final long serialVersionUID = 323673321034742710L;

	/**
	 * <p>Constructs an empty {@code StoryLoadException}.</p>
	 */
	public StoryLoadException() {
		super();
	}
	
	/**
	 * <p>Constructs a {@code StoryLoadException} with a message.</p>
	 * 
	 * @param message The message.
	 */
	public StoryLoadException(String message) {
		super(message);
	}
	
	/**
	 * <p>Constructs a {@code StoryLoadException} with an extended
	 * throwable.</p>
	 * 
	 * @param throwable The throwable.
	 */
	public StoryLoadException(Throwable throwable) {
		super(throwable);
	}
	
	/**
	 * <p>Constructs a {@code StoryLoadException} with an extended
	 * throwable and a message.</p>
	 * 
	 * @param message The message.
	 * @param throwable The throwable.
	 */
	public StoryLoadException(String message, Throwable throwable) {
		super(message, throwable);
	}
	
}
