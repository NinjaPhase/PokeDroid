package com.pokedroid.scene.window;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.utils.Disposable;

/**
 * <p>A {@code MessageBox} shows text onto the screen
 * with different effects.</p>
 * 
 * @author J. Kitchen
 * @version 10 March 2016
 *
 */
public class MessageBox {
	public static float TIME_TO_LETTER = 0.05f;

	private MessageBoxSkin skin;
	private String text, totalText;
	private float timer;

	/**
	 * <p>Constructs a new {@code MessageBox}.</p>
	 * 
	 * @param skin The skin of the message box.
	 * @param text The text.
	 * @param x The x position.
	 * @param y The y position.
	 * @param w The width.
	 * @param h The height.
	 */
	public MessageBox(MessageBoxSkin skin, String text) {
		this.skin = skin;
		this.totalText = text;
		this.text = "";
	}

	/**
	 * <p>Updates the box's text.</p>
	 * 
	 * @param deltaTime The delta time.
	 */
	public void update(float deltaTime) {
		if(text.length() != totalText.length()) {
			if(timer < TIME_TO_LETTER)
				timer += deltaTime;
			while(timer >= TIME_TO_LETTER) {
				if(text.length() == totalText.length())
					break;
				text = totalText.substring(0, text.length()+1);
				timer -= TIME_TO_LETTER;
				if(text.length() != totalText.length())
					break;
			}
		}
	}
	
	/**
	 * <p>Renders this {@code MessageBox} at a specific
	 * x and y with a fixed w and h.</p>
	 * 
	 * @param batch The batch to render onto.
	 * @param x The x position.
	 * @param y The y position.
	 * @param w The width.
	 * @param h The height.
	 */
	public void render(Batch batch, float x, float y, float w, float h) {
		skin.patch.draw(batch, x, y, w, h);
		skin.font.draw(batch, text, x+16, y+h-24);
	}
	
	/**
	 * <p>Sets the text of the message box.</p>
	 * 
	 * @param text The text.
	 */
	public void setText(String text) {
		this.text = "";
		this.totalText = text;
		this.timer = 0.0f;
	}
	
	/**
	 * <p>Appends another piece of text to the message box.</p>
	 * 
	 * @param text The new text.
	 */
	public void append(String text) {
		this.totalText += text;
	}
	
	/**
	 * @return Whether the text is finished.
	 */
	public boolean isFinished() {
		return this.text.length() == this.totalText.length();
	}
	
	/**
	 * <p>A {@code MessageBoxSkin} determines the message box's
	 * attributes.</p>
	 * 
	 * @author J. Kitchen
	 * @version 10 March 2016
	 *
	 */
	public static class MessageBoxSkin implements Disposable {

		public BitmapFont font;
		public NinePatch patch;
		private boolean ownsResources;

		/**
		 * <p>Constructs a new {@code MessageBoxSkin}.</p>
		 * 
		 * @param font The font.
		 * @param patch The patch.
		 */
		public MessageBoxSkin(BitmapFont font, NinePatch patch) {
			this.font = font;
			this.patch = patch;
		}

		/**
		 * <p>Sets whether this message box skin owns its own
		 * resources.</p>
		 * 
		 * @param ownsResources Whether the skin owns its own
		 * resources.
		 * @return The message box for chaining.
		 */
		public MessageBoxSkin setOwnsResources(boolean ownsResources) {
			this.ownsResources = ownsResources;
			return this;
		}

		@Override
		public void dispose() {
			if(this.ownsResources) {
				font.dispose();
				patch.getTexture().dispose();
			}
		}

	}

}
