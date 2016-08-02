package com.pokedroid.scene.window;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.pokedroid.pokemon.Gender;
import com.pokedroid.pokemon.Pokemon;

/**
 * <p>The {@code PlayerStatus} displays the player's
 * information and battle status.</p>
 * 
 * @author J. Kitchen
 * @version 11 March 2016
 *
 */
public class PlayerStatus {

	private Pokemon pokemon;
	private BitmapFont font;
	private Texture background;
	private TextureRegion lowHealth, midHealth, highHealth;
	private int barHealth;
	private float shownHealth;
	private GlyphLayout levelLayout;
	
	/**
	 * <p>Constructs a new {@code PlayerStatus}.</p>
	 * 
	 * @param pokemon The pokemon.
	 * @param font The bitmap font.
	 * @param background The background.
	 */
	public PlayerStatus(Pokemon pokemon, BitmapFont font, Texture background, Texture healthBars) {
		this.pokemon = pokemon;
		this.font = font;
		this.background = background;
		this.highHealth = new TextureRegion(healthBars, 0, 0, 1, 6);
		this.midHealth = new TextureRegion(healthBars, 0, 6, 1, 6);
		this.lowHealth = new TextureRegion(healthBars, 0, 12, 1, 6);
		this.shownHealth = this.barHealth = pokemon.getStat(0);
		this.levelLayout = new GlyphLayout(font, "\u2665" + pokemon.getLevel());
	}
	
	/**
	 * <p>Updates the {@code PlayerStatus}.</p>
	 * 
	 * @param deltaTime Timer.
	 */
	public void update(float deltaTime) {
		if(barHealth != shownHealth) {
			if(barHealth < shownHealth) {
				shownHealth -= deltaTime*32.0f;
				if(barHealth > shownHealth)
					shownHealth = barHealth;
			} else {
				shownHealth += deltaTime*32.0f;
				if(barHealth < shownHealth)
					shownHealth = barHealth;
			}
		}
	}
	
	/**
	 * <p>Renders the {@code PlayerStatus}.</p>
	 * 
	 * @param batch The batch.
	 * @param x The x position.
	 * @param y The y position.
	 */
	public void render(Batch batch, float x, float y) {
		float perc = (shownHealth/(float)pokemon.getStat(0));
		batch.draw(background, x, y);
		if(perc > 0.5f) {
			batch.draw(highHealth, x+96.0f, y+34.0f, 96.0f*perc, 6.0f);
		} else if(perc > 0.2 && perc <= 0.5f) {
			batch.draw(midHealth, x+96.0f, y+34.0f, 96.0f*perc, 6.0f);
		} else {
			batch.draw(lowHealth, x+96.0f, y+34.0f, 96.0f*perc, 6.0f);
		}
		font.draw(batch, pokemon.getName()
				+ (pokemon.getGender() != Gender.GENDERLESS ?
				(pokemon.getGender() == Gender.MALE ? "\u2642" : "\u2640") : ""),
				x+32.0f, y+60.0f);
		font.draw(batch, levelLayout, x+getWidth()-levelLayout.width-16.0f, y+60.0f);
	}
	
	/**
	 * <p>Sets the player status pokemon.</p>
	 * 
	 * @param pokemon The pokemon.
	 */
	public void setPokemon(Pokemon pokemon) {
		this.pokemon = pokemon;
		this.levelLayout = new GlyphLayout(font, "\u2665" + pokemon.getLevel());
	}
	
	/**
	 * <p>Sets the health bar.</p>
	 * 
	 * @param health The health.
	 */
	public void setHealth(int health) {
		this.barHealth = health;
	}
	
	/**
	 * <p>Gets the width of the background.</p>
	 * 
	 * @return The width of the background.
	 */
	public float getWidth() {
		return background.getWidth();
	}
	
	/**
	 * <p>Gets the height of the background.</p>
	 * 
	 * @return The height of the background.
	 */
	public float getHeight() {
		return background.getHeight();
	}
	
}
