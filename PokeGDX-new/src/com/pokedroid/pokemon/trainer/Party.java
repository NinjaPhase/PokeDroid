package com.pokedroid.pokemon.trainer;

import java.util.Iterator;

import com.pokedroid.pokemon.Pokemon;

/**
 * <p>A {@code Party} is a simple implementation of the {@link IParty}.</p>
 * 
 * @author PoketronHacker
 * @version 12 November 2015
 *
 */
public class Party implements IParty, Iterable<Pokemon> {
	
	private int size;
	private Pokemon[] pokemon;
	
	/**
	 * <p>Constructor for a new {@code Party}.</p>
	 * 
	 * @param size The maximum size of the party.
	 */
	public Party(int size) {
		this.size = size;
		this.pokemon = new Pokemon[size];
	}
	
	/**
	 * <p>Moves all {@code null} pokemon to the back.</p>
	 */
	public void clamp() {
		for(int i = 0; i < pokemon.length; i++) {
			if(pokemon[i] != null)
				continue;
			for(int j = i+1; j < pokemon.length; j++) {
				pokemon[j-1] = pokemon[j];
			}
			pokemon[pokemon.length-1] = null;
		}
	}

	@Override
	public boolean addPokemon(Pokemon pokemon) {
		if(isPartyFull())
			return false;
		for(int i = 0; i < size; i++) {
			if(this.pokemon[i] == null) {
				this.pokemon[i] = pokemon;
				clamp();
				return true;
			}
		}
		return false;
	}

	@Override
	public void insertPokemon(int index, Pokemon p) {
		if(index < 0 || index >= size)
			throw new ArrayIndexOutOfBoundsException(index);
		if(pokemon[index] == null) {
			addPokemon(p);
		} else {
			if(index == size-1) return;
			for(int i = size-1; i >= index; i--) {
				pokemon[i] = pokemon[i+1];
			}
			pokemon[index] = p;
		}
		clamp();
	}

	@Override
	public boolean removePokemon(int index) {
		if(index < 0 || index >= size)
			throw new ArrayIndexOutOfBoundsException(index);
		if(pokemon[index] == null)
			return false;
		pokemon[index] = null;
		clamp();
		return true;
	}

	@Override
	public boolean removePokemon(Pokemon p) {
		boolean contain = false;
		int index = 0;
		for(int i = 0; i < pokemon.length; i++) {
			if(pokemon[i] != p) continue;
			contain = true;
			index = i;
			break;
		}
		if(!contain) return false;
		pokemon[index] = null;
		clamp();
		return true;
	}
	
	@Override
	public void clear() {
		pokemon = new Pokemon[size];
	}
	
	@Override
	public Pokemon getPokemon(int index) {
		if(index < 0 || index >= size)
			throw new ArrayIndexOutOfBoundsException(index);
		return pokemon[index];
	}

	@Override
	public int partyCount() {
		int c = 0;
		for(int i = 0; i < size; i++) {
			if(pokemon[i] == null) continue;
			c++;
		}
		return c;
	}

	@Override
	public int partySize() {
		return size;
	}

	@Override
	public boolean isPartyFull() {
		for(int i = 0; i < size; i++) {
			if(pokemon[i] == null)
				return false;
		}
		return true;
	}

	@Override
	public Iterator<Pokemon> iterator() {
		Iterator<Pokemon> it = new Iterator<Pokemon>() {
			
			private int index;
			
			@Override
			public boolean hasNext() {
				return index < size;
			}

			@Override
			public Pokemon next() {
				return pokemon[index++];
			}
		};
		return it;
	}
	
	@Override
	public Pokemon[] getPokemon() { return this.pokemon; }
	
	@Override
	public String toString() {
		StringBuilder str = new StringBuilder();
		str.append("[");
		for(int i = 0; i < pokemon.length; i++)
			str.append(pokemon[i] + ", ");
		str.substring(0, str.toString().length()-2);
		str.append("]");
		return str.toString();
	}

}
