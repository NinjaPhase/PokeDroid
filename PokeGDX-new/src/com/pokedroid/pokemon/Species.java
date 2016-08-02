package com.pokedroid.pokemon;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.JsonValue;

/**
 * <p>A {@code Species} is a particular type of {@link com.pokedroid.pokemon.Pokemon Pokemon}.</p>
 * 
 * @author PoketronHacker
 * @version 26 October 2015
 *
 */
public class Species implements Disposable {

	private int id;
	private String name;
	private Type[] type;
	private List<MovePair> moveSet;
	private int[] baseStats, evYield;
	private int catchRate, baseExpYield,
	eggCycles, baseFriendship;
	private float genderRatio;
	private Texture frontTexture, backTexture,
	frontTextureS, backTextureS;

	/**
	 * <p>Constructor for a new {@code Species}.</p>
	 * 
	 * @param id The id of the {@code Species}.
	 * @param name The name of the {@code Species}.
	 * @param baseStats The base stats of the {@code Species}.
	 * @param evYield The evs this {@code Species} yields.
	 * @param catchRate The chance that this {@code Species} will be caught.
	 */
	public Species(int id, String name, Type[] types, int[] baseStats, int[] evYield,
			int catchRate, int baseExpYield, float genderRatio, int eggCycles,
			int baseFriendship) {
		this.moveSet = Collections.synchronizedList(new ArrayList<>());
		this.id = id;
		this.name = name;
		this.type = types;
		this.baseStats = baseStats;
		this.evYield = evYield;
		this.catchRate = catchRate;
		this.baseExpYield = baseExpYield;
		this.genderRatio = genderRatio;
		this.eggCycles = eggCycles;
		this.baseFriendship = baseFriendship;
	}

	/**
	 * <p>Constructor for a new {@code Species} through a JsonValue.</p>
	 * 
	 * @param v The JsonValue
	 */
	public Species(JsonValue v) {
		this(v.getInt("id"), v.getString("name"), null, v.get("base_stats").asIntArray(),
				v.get("ev_yield").asIntArray(), Math.min(255, v.getInt("catch_rate")), v.getInt("base_exp"),
				v.getFloat("gender_ratio"), v.getInt("egg_cycles"), v.getInt("base_friendship"));
		this.type = new Type[v.get("type").size];
		for(int i = 0; i < type.length; i++)
			this.type[i] = Type.forName(v.get("type").getString(i));
		if(v.has("move_set")) {
			for(JsonValue moveSets : v.get("move_set").iterator()) {
				int level = moveSets.getInt(0);
				Move m = Move.forName(moveSets.getString(1));
				if(m == null) continue;
				moveSet.add(new MovePair(level, m));
			}
		}
	}

	@Override
	public void dispose() {
		if(frontTexture != null) frontTexture.dispose();
		if(frontTextureS != null) frontTextureS.dispose();
		if(backTexture != null) backTexture.dispose();
		if(backTextureS != null) backTextureS.dispose();
		frontTexture = frontTextureS = backTexture = backTextureS = null;
	}

	/**
	 * <p>Gets the id of this {@code Species}.</p>
	 * 
	 * @return The id of this {@code Species}.
	 */
	public int getId() {
		return this.id;
	}

	/**
	 * <p>Gets the name of this {@code Species}.</p>
	 * 
	 * @return The name of this {@code Species}.
	 */
	public String getName() {
		return this.name.toUpperCase();
	}

	/**
	 * <p>Gets the base stat of this {@code Species}.</p>
	 * 
	 * @param index The stat index.
	 * @return The base stat of this {@code Species}.
	 */
	public int getBaseStat(int index) {
		if(index < 0 || index >= this.baseStats.length)
			throw new ArrayIndexOutOfBoundsException(index);
		return this.baseStats[index];
	}

	/**
	 * <p>Gets the base stat of this {@code Species}.</p>
	 * 
	 * @param stat The {@link Stat}.
	 * @returns The base stat of this {@code Species}.
	 */
	public int getBaseStat(Stat stat) {
		return this.baseStats[stat.ordinal()];
	}

	/**
	 * <p>Gets the ev yield of this {@code Species}.</p>
	 * 
	 * @param index The stat index.
	 * @return The ev yield of this {@code Species}.
	 */
	public int getEvYield(int index) {
		if(index < 0 || index >= this.evYield.length)
			throw new ArrayIndexOutOfBoundsException(index);
		return this.evYield[index];
	}

	/**
	 * <p>Gets the catch rate of this {@code Species} as a value
	 * between 0-255.</p>
	 * 
	 * @return The catch rate of this {@code Species}.
	 */
	public int getCatchRate() {
		return this.catchRate;
	}

	/**
	 * <p>Gets the primary type.</p>
	 * 
	 * @return The primary type.
	 */
	public Type getPrimaryType() {
		return this.type[0];
	}

	/**
	 * <p>Gets the secondary type.</p>
	 * 
	 * @return The secondary type.
	 */
	public Type getSecondaryType() {
		return (this.type.length < 2 ? type[0] : type[1]);
	}

	/**
	 * <p>Gets the base experience of this {@code Species}.</p>
	 *
	 * @return The base experience.
	 */
	public int getBaseExp() {
		return this.baseExpYield;
	}

	/**
	 * <p>Gets the amount of egg cycles in this {@code Species}.</p>
	 * 
	 * @return The amount of egg cycles.
	 */
	public int getEggCycles() {
		return this.eggCycles;
	}

	/**
	 * <p>Gets the base friendship of this {@code Species}.</p>
	 * 
	 * @return The base friendship.
	 */
	public int getBaseFriendship() {
		return this.baseFriendship;
	}

	/**
	 * <p>Gets the gender ratio of this {@code Species}.</p>
	 * 
	 * @return The gender ratio.
	 */
	public float getGenderRatio() {
		return this.genderRatio;
	}

	/**
	 * <p>Gets the front sprite of this {@code Species}.</p>
	 * 
	 * @param shiny Whether the sprite is shiny.
	 * @return The back sprite of this {@code Species}.
	 */
	public Texture getFrontSprite(boolean shiny) {
		String format = String.valueOf("000").substring(
				String.valueOf(this.id).length(), String.valueOf("000").length())
				.concat(String.valueOf(this.id));
		if(shiny && frontTextureS == null) {
			frontTextureS = new Texture("graphics/pokemon/" + format + "s.png");
		} else if(!shiny && frontTexture == null)
			frontTexture = new Texture("graphics/pokemon/" + format + ".png");
		return (shiny) ? frontTextureS : frontTexture;
	}

	/**
	 * <p>Gets the back sprite of this {@code Species}.</p>
	 * 
	 * @param shiny Whether the sprite is shiny.
	 * @return The back sprite of this {@code Species}.
	 */
	public Texture getBackSprite(boolean shiny) {
		String format = String.valueOf("000").substring(
				String.valueOf(this.id).length(), String.valueOf("000").length())
				.concat(String.valueOf(this.id));
		if(shiny && backTextureS == null) {
			backTextureS = new Texture("graphics/pokemon/" + format + "sb.png");
		} else if(!shiny && backTexture == null)
			backTexture = new Texture("graphics/pokemon/" + format + "b.png");
		return (shiny) ? backTextureS : backTexture;
	}

	/**
	 * <p>Gets moves at level i.</p>
	 * 
	 * @param i The level.
	 * @return The move pair list.
	 */
	public List<MovePair> getMovesAtLevel(int i) {
		List<MovePair> p = new ArrayList<>();
		for(MovePair pair : moveSet) {
			if(pair.level != i)
				continue;
			p.add(pair);
		}
		return p;
	}

	@Override
	public String toString() {
		return this.id + ": " + this.name;
	}

	private static final Map<Integer, Species> SPECIES_BY_ID = Collections.synchronizedMap(new HashMap<Integer, Species>());
	private static final Map<String, Species> SPECIES_BY_NAME = Collections.synchronizedMap(new HashMap<String, Species>());

	private static JsonValue flyLoad;

	/**
	 * <p>Registers a {@code Species} for indexing.</p>
	 * 
	 * @param species The species to register.
	 */
	public static void registerSpecies(Species species) {
		if(species == null)
			throw new NullPointerException("trying to register null species");
		if(SPECIES_BY_ID.containsKey(species.getId())) {
			SPECIES_BY_ID.remove(species.getId());
		}
		if(SPECIES_BY_NAME.containsKey(species.getName().toUpperCase())) {
			SPECIES_BY_NAME.remove(species.getName().toUpperCase());
		}
		SPECIES_BY_ID.put(species.getId(), species);
		SPECIES_BY_NAME.put(species.getName().toUpperCase(), species);
	}

	/**
	 * <p>Unregisters a {@code Species}.</p>
	 * 
	 * @param species The species to unregister.
	 */
	public static void unregisterSpecies(Species species) {
		if(species == null)
			throw new NullPointerException("trying to unregister null species");
		if(!SPECIES_BY_NAME.containsKey(species.getName().toUpperCase()) && !SPECIES_BY_ID.containsKey(species.getId()))
			return;
		SPECIES_BY_ID.remove(species.getId());
		SPECIES_BY_NAME.remove(species.getName().toUpperCase());
	}

	/**
	 * <p>Sets the json value to load pokemon on the fly.</p>
	 * 
	 * @param v The value.
	 */
	public static void setFlyLoader(JsonValue v) {
		Species.flyLoad = v;
	}

	/**
	 * <p>Gets a {@code Species} for a name.</p>
	 * 
	 * @param name The name of the {@code Species}.
	 * @return The species with the name, if it doesn't exist then {@code null}.
	 */
	public static Species forName(String name) {
		if(!SPECIES_BY_NAME.containsKey(name) && flyLoad != null) {
			if(flyLoad.has(name.toLowerCase())) {
				registerSpecies(new Species(flyLoad.get(name.toLowerCase())));
			}
		}
		return SPECIES_BY_NAME.get(name.toUpperCase());
	}

	/**
	 * <p>Gets a {@code Species} for a certain id.</p>
	 * 
	 * @param id The id of the {@code Species}.
	 * @return The species with the id, if it doesnt exist then {@code null}.
	 */
	public static Species forId(int id) {
		if(!SPECIES_BY_ID.containsKey(id) && flyLoad != null) {
			for(JsonValue v : flyLoad.iterator()) {
				if(v.isObject() && v.getInt("id") == id) {
					registerSpecies(new Species(v));
					break;
				}
			}
		}
		return SPECIES_BY_ID.get(id);
	}

	/**
	 * <p>Disposes of all {@code Species} in the registry.</p>
	 */
	public static void clearRegistry() {
		for(Species s : SPECIES_BY_NAME.values()) s.dispose();
		for(Species s : SPECIES_BY_ID.values()) s.dispose();
		SPECIES_BY_NAME.clear();
		SPECIES_BY_ID.clear();
	}

	/**
	 * <p>Creates a unmodifiable list of species.</p>
	 * 
	 * @return An unmodifiable list of species.
	 */
	public static List<Species> createSpeciesList() {
		return Collections.unmodifiableList(new ArrayList<Species>(SPECIES_BY_NAME.values()));
	}

	/**
	 * <p>The {@code MovePair} is a move listing for learning moves.</p>
	 * 
	 * @author J. Kitchen
	 * @version 21 March 2016
	 *
	 */
	public static class MovePair {
		public int level;
		public Move move;

		/**
		 * <p>Constructs a new {@code MovePair}.</p>
		 * 
		 * @param level The level.
		 * @param move The move.
		 */
		public MovePair(int level, Move move) {
			this.level = level;
			this.move = move;
		}
	}

}
