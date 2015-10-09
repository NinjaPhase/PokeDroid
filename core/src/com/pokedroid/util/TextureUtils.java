package com.pokedroid.util;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * <p>A collection of Texture Utilities.</p>
 * 
 * @author PoketronHacker
 * @version v1.0
 *
 */
public final class TextureUtils {
	
	/** <p>Private Constructor.</p> */
	private TextureUtils() {}
	
	/**
	 * <p>Constructs a group of animations from a texture region in the format
	 * of an RPG Maker character, this means each grid should be the same size
	 * and be formatted to be:</p>
	 * 
	 * <pre>
	 *   DOWN,
	 *   LEFT,
	 *   RIGHT,
	 *   UP
	 * </pre>
	 * 
	 * @param t The texture region.
	 * @param speed The speed of the animations.
	 * @return The animations.
	 */
	public static Animation[] createRMAnimation(TextureRegion t, float speed) {
		return createRMAnimation(splitTexture(t, 4, 4), speed);
	}
	
	/**
	 * <p>Constructs a group of animations from a single texture in the format
	 * of an RPG Maker character, this means each grid should be the same size
	 * and be formatted to be:</p>
	 * 
	 * <pre>
	 *   DOWN,
	 *   LEFT,
	 *   RIGHT,
	 *   UP
	 * </pre>
	 * 
	 * @param t The texture.
	 * @param speed The speed of the animations.
	 * @return The animations.
	 */
	public static Animation[] createRMAnimation(Texture t, float speed) {
		return createRMAnimation(splitTexture(t, 4, 4), speed);
	}
	
	/**
	 * <p>Constructs a group of animations from texture regions in the format
	 * of an RPG Maker character, this means each grid should be the same size
	 * and be formatted to be:</p>
	 * 
	 * <pre>
	 *   DOWN,
	 *   LEFT,
	 *   RIGHT,
	 *   UP
	 * </pre>
	 * 
	 * @param regions The texture regions.
	 * @param speed The speed of the animations.
	 * @return The animations.
	 */
	public static Animation[] createRMAnimation(TextureRegion[] regions, float speed) {
		Animation[] anims = new Animation[4];
		for(int i = 0; i < anims.length; i++) {
			anims[i] = new Animation(speed, regions[i*4],
											regions[(i*4)+1],
											regions[(i*4)+2],
											regions[(i*4)+3]);
			anims[i].setPlayMode(PlayMode.LOOP);
		}
		return anims;
	}
	
	/**
	 * <p>Splits a {@code Texture} into a grid of texture regions.</p>
	 * 
	 * @param t The texture to split.
	 * @param cols The amount of columns.
	 * @param rows The amount of rows.
	 * @return An array of textures.
	 */
	public static TextureRegion[] splitTexture(Texture t, int cols, int rows) {
		int width = t.getWidth()/cols;
		int height = t.getHeight()/rows;
		TextureRegion[] regions = new TextureRegion[cols*rows];
		for(int y = 0; y < rows; y++) {
			for(int x = 0; x < cols; x++) {
				regions[x + (y * cols)] = new TextureRegion(t, x*width, y*height, width, height);
			}
		}
		return regions;
	}
	
	/**
	 * <p>Splits a {@code TextureRegion} into a grid of texture regions.</p>
	 * 
	 * @param tr The texture regions to split.
	 * @param cols The amount of columns.
	 * @param rows The amount of rows.
	 * @return An array of textures.
	 */
	public static TextureRegion[] splitTexture(TextureRegion tr, int cols, int rows) {
		int width = tr.getRegionWidth()/cols;
		int height = tr.getRegionHeight()/rows;
		TextureRegion[] regions = new TextureRegion[cols*rows];
		for(int y = 0; y < rows; y++) {
			for(int x = 0; x < cols; x++) {
				regions[x + (y * cols)] = new TextureRegion(tr, x*width, y*height, width, height);
			}
		}
		return regions;
	}
	
}
