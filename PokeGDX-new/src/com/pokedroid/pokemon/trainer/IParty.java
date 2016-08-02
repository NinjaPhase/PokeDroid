package com.pokedroid.pokemon.trainer;

import com.pokedroid.pokemon.Pokemon;

/**
 * <p>The {@code IParty} is the interface for a party of
 * pokemon.</p>
 * 
 * @author PoketronHacker
 * @version 12 November 2015
 *
 */
public interface IParty extends Iterable<Pokemon> {
	
	/**
	 * <p>Adds a {@link Pokemon} to the party.</p>
	 * 
	 * @param pokemon The pokemon.
	 * @return {@code true} if the pokemon was added.
	 */
	public boolean addPokemon(Pokemon pokemon);
	
	/**
	 * <p>Inserts a {@link Pokemon} into the party.</p>
	 * 
	 * @param index The index of the pokemon.
	 * @param pokemon The pokemon.
	 */
	public void insertPokemon(int index, Pokemon pokemon);
	
	/**
	 * <p>Removes the {@link Pokemon} at a certain index.</p>
	 * 
	 * @param index The index.
	 * @return {@code true} if the pokemon was removed.
	 */
	public boolean removePokemon(int index);
	
	/**
	 * <p>Removes the {@link Pokemon}.</p>
	 * 
	 * @param pokemon The pokemon.
	 * @return {@code true} if the pokemon was removed.
	 */
	public boolean removePokemon(Pokemon pokemon);
	
	/**
	 * <p>Clears this party.</p>
	 */
	public void clear();
	
	/**
	 * <p>Gets the {@link Pokemon} at the index.</p>
	 * 
	 * @param index The index of the {@link Pokemon}.
	 * @return The {@link Pokemon} if its found, otherwise {@code null}.
	 */
	public Pokemon getPokemon(int index);
	
	/**
	 * <p>Gets the amount of {@link Pokemon} actually in
	 * the party.</p>
	 * 
	 * @return The amount of {@link Pokemon}.
	 */
	public int partyCount();
	
	/**
	 * <p>Gets the maximum size of the {@code IParty}.</p>
	 * 
	 * @return The maximum size of the {@code IParty}.
	 */
	public int partySize();
	
	/**
	 * <p>Gets whether the {@code IParty} is full.</p>
	 * 
	 * @return Whether the {@code IParty} is full.
	 */
	public boolean isPartyFull();
	
	/**
	 * <p>Gets an array of pokemon within the {@code IParty}.</p>
	 * 
	 * @return An array of pokemon.
	 */
	public Pokemon[] getPokemon();
	
}
