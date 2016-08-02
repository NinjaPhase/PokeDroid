package com.pokedroid.scene.window;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.pokedroid.scene.window.MessageBox.MessageBoxSkin;

/**
 * <p>A {@code ChoiceBox} is a container which has
 * a number of options that can be controlled using
 * the arrow key.</p>
 * 
 * @author J. Kitchen
 * @version 17 March 2016
 *
 */
public class ChoiceBox {

	private List<OptionPair> options;
	private int cols, rows;
	private MessageBoxSkin skin;
	private float width, height, fixedWidth;
	private int paddingHor, paddingVer;
	private GlyphLayout prefix;
	private GlyphLayout suffix;
	private int sX, sY;

	/**
	 * <p>Constructs a new {@code ChoiceBox}.</p>
	 * 
	 * @param skin The box skin.
	 * @param cols The amount of columns.
	 * @param rows The amount of rows.
	 * @param choices The choices on the box.
	 */
	public ChoiceBox(MessageBoxSkin skin, int cols, int rows, Object... choices) {
		this.skin = skin;
		this.rows = rows;
		this.cols = cols;
		this.options = Collections.synchronizedList(new ArrayList<>());
		for(Object s : choices) {
			this.options.add(new OptionPair(skin.font, s));
		}
		this.fixedWidth = -1;
		this.paddingHor = 16;
		this.paddingVer = 24;
		this.prefix = new GlyphLayout(skin.font, "\u2666 ");
		this.suffix = new GlyphLayout(skin.font, " ");
	}

	/**
	 * <p>Renders this choice box at x and y.</p>
	 * 
	 * @param batch The batch.
	 * @param x The x position.
	 * @param y The y position.
	 */
	public void render(Batch batch, float x, float y) {
		width = 0.0f;
		if(fixedWidth == -1) {
			for(OptionPair g : options) {
				width = Math.max(width, g.layout.width);
			}
		} else width = fixedWidth;
		int w, h;
		w = (paddingHor*2);
		w += ((width+prefix.width) * cols);
		h = 32;
		h += (32.0f * (rows));
		skin.patch.draw(batch, x, y, w, h);
		int index = 0;
		for(int yy = 0; yy < rows; yy++) {
			for(int xx = 0; xx < cols; xx++) {
				Object o = options.get(index++).object;
				if(sX+(sY*cols) == index-1) {
					skin.font.draw(batch, "\u2666 " + (o == null ? "-" : o.toString()), x+paddingHor+(xx*(
							prefix.width+suffix.width+width)), 
							(y+h)-(yy*skin.font.getLineHeight())-paddingVer);
				} else {
					skin.font.draw(batch, (o == null ? "-" : o.toString()), prefix.width+x+paddingHor+(xx*(
							prefix.width+suffix.width+width)), 
							(y+h)-(yy*skin.font.getLineHeight())-paddingVer);
				}
			}
		}
		height = h;
	}

	/**
	 * <p>Renders this choice box at x and y.</p>
	 * 
	 * @param batch The batch.
	 * @param x The x position.
	 * @param y The y position.
	 * @param w The width of the box.
	 * @param h The height of the box.
	 */
	public void render(Batch batch, float x, float y, float w, float h) {
		width = 0.0f;
		if(fixedWidth == -1) {
			for(OptionPair g : options) {
				width = Math.max(width, g.layout.width);
			}
		} else width = fixedWidth;
		skin.patch.draw(batch, x, y, w, h);
		int index = 0;
		for(int yy = 0; yy < rows; yy++) {
			for(int xx = 0; xx < cols; xx++) {
				Object o = options.get(index++).object;
				if(sX+(sY*cols) == index-1) {
					skin.font.draw(batch, "\u2666 " + (o == null ? "-" : o.toString()), x+paddingHor+(xx*(
							prefix.width+suffix.width+width)), 
							(y+h)-(yy*skin.font.getLineHeight())-paddingVer);
				} else {
					skin.font.draw(batch, (o == null ? "-" : o.toString()), prefix.width+x+paddingHor+(xx*(
							prefix.width+suffix.width+width)), 
							(y+h)-(yy*skin.font.getLineHeight())-paddingVer);
				}
			}
		}
		height = h;
	}
	
	/**
	 * <p>Sets the text to a fixed width, set to -1 to
	 * calculate the width of each option.</p>
	 * 
	 * @param fixedWidth The fixed width.
	 */
	public void setFixedWidth(float fixedWidth) {
		this.fixedWidth = fixedWidth;
	}

	/**
	 * <p>Moves the cursor relative to the rows
	 * and columns.</p>
	 * 
	 * @param x The x position.
	 * @param y The y position.
	 */
	public void moveCursor(int x, int y) {
		sX += x;
		if(sX < 0)
			sX = rows-1;
		if(sX >= rows)
			sX = 0;
		sY += y;
		if(sY < 0)
			sY = cols-1;
		if(sY >= cols)
			sY = 0;
		if(getSelectedValue() == null)
			moveBack(-x, -y);
	}
	
	/**
	 * <p>Moves the cursor relative to the rows
	 * and columns.</p>
	 * 
	 * @param x The x position.
	 * @param y The y position.
	 */
	protected void moveBack(int x, int y) {
		sX += x;
		if(sX < 0)
			sX = rows-1;
		if(sX >= rows)
			sX = 0;
		sY += y;
		if(sY < 0)
			sY = cols-1;
		if(sY >= cols)
			sY = 0;
	}

	/**
	 * <p>Gets the selected index on the
	 * x axis.</p>
	 * 
	 * @return The selected index on the x axis.
	 */
	public int getSelectedX() {
		return this.sX;
	}

	/**
	 * <p>Gets the selected column.</p>
	 * 
	 * @return The selected column.
	 */
	public int getSelectedY() {
		return this.sY;
	}

	/**
	 * <p>Gets the selected value.</p>
	 * 
	 * @return The selected value.
	 */
	public Object getSelectedValue() {
		return this.options.get(getSelectedIndex()).object;
	}

	/**
	 * <p>Gets the selected index.</p>
	 * 
	 * @return The selected index.
	 */
	public int getSelectedIndex() {
		return this.sX+(this.sY*this.cols);
	}
	
	/**
	 * @return The width of the box.
	 */
	public float getWidth() {
		return this.width;
	}
	
	/**
	 * @return The height of the box.
	 */
	public float getHeight() {
		return this.height;
	}

	/**
	 * <p>An option pair is a string with a glyph layout
	 * for width and height.</p>
	 * 
	 * @author J. Kitchen
	 * @version 17 March 2016
	 *
	 */
	private static class OptionPair {
		public Object object;
		public GlyphLayout layout;

		/**
		 * <p>Constructs a new {@code OptionPair}.</p>
		 * 
		 * @param font The font.
		 * @param string The string.
		 */
		public OptionPair(BitmapFont font, Object string) {
			this.object = string;
			this.layout = new GlyphLayout(font, (string == null ? "-" : string.toString()));
		}
	}

}
