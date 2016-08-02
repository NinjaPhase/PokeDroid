package com.pokedroid.pokemon;

import java.util.List;

import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.JsonValue;
import com.pokedroid.PokeDroid;
import com.pokedroid.pokemon.Species.MovePair;

/**
 * <p>A {@code Pokemon} is a single instance of a {@link Species}.</p>
 * 
 * @author PoketronHacker
 * @version 26 October 2015
 *
 */
public class Pokemon {
	private static final int MAX_IV = 31, MAX_EV = 255;
	private static final float[] STAGE_MODIFIER = new float[]{
			3/9, 3/8, 3/7, 3/6, 3/5, 3/4,
			3/3,
			4/3, 5/3, 6/3, 7/3, 8/3, 9/3};

	private Species species;
	private String nickname;
	private int level;
	private int[] stats;
	private int[] ivs, evs;
	private int personalityValue;
	private Gender gender;
	private Move[] moveList;
	private int[] movePP;
	private StatusEffect statusEffect;

	/**
	 * <p>Constructor for a new {@code Pokemon} using the
	 * JSON format.</p>
	 * 
	 * @param v The json array.
	 */
	public Pokemon(JsonValue v) {
		this.moveList = new Move[4];
		this.movePP = new int[4];
		if(v.isArray()) {
			if(v.get(0).isString()) this.species = Species.forName(v.getString(0));
			else if(v.get(0).isLong()) this.species = Species.forId(v.getInt(0));
			else throw new GdxRuntimeException("Unable to determine species type in array");
			this.level = v.getInt(1);
			this.stats = new int[Stat.values().length];
			this.ivs = new int[Stat.values().length];
			this.evs = new int[Stat.values().length];
			generatePersonality();
			generateIVs();
			calculateStats();
		} else if(v.isObject()) {
			if(v.get("species").isString()) this.species = Species.forName(v.getString("species"));
			else if(v.get("species").isLong()) this.species = Species.forId(v.getInt("species"));
			else throw new GdxRuntimeException("Unable to determine species type in array");
			this.level = v.getInt(1);
			this.stats = new int[Stat.values().length];
			this.ivs = new int[Stat.values().length];
			this.evs = new int[Stat.values().length];
			if(v.get("ivs") != null) {
				for(int i = 0; i < Math.min(Stat.values().length, v.get("ivs").size); i++) {
					if(this.ivs[i] < 0 || this.ivs[i] > MAX_IV){
						this.ivs[i] = (int)Math.floor(Math.random() * (MAX_IV+1));
						continue;
					}
					this.ivs[i] = v.get("ivs").getInt(i);
				}
			} else generateIVs();
			if(v.get("evs") != null) {
				for(int i = 0; i < Math.min(Stat.values().length, v.get("evs").size); i++) {
					if(this.evs[i] < 0 || this.evs[i] > MAX_EV){
						this.evs[i] = (int)Math.floor(Math.random() * (MAX_EV+1));
						continue;
					}
					this.evs[i] = v.get("evs").getInt(i);
				}
			}
			generatePersonality();
			calculateStats();
		} else throw new GdxRuntimeException("Unable to determine json type");
	}

	/**
	 * <p>Constructor for a new {@code Pokemon}.</p>
	 * 
	 * @param species The species of the {@code Pokemon}.
	 * @param level The level of the {@code Pokemon}.
	 */
	public Pokemon(Species species, int level) {
		if(species == null)
			throw new NullPointerException("species given cannot be null");
		this.moveList = new Move[4];
		this.movePP = new int[4];
		this.species = species;
		this.level = level;
		this.stats = new int[Stat.values().length];
		this.ivs = new int[Stat.values().length];
		this.evs = new int[Stat.values().length];

		int index = 0;
		for(int i = 0; i <= level; i++) {
			List<MovePair> mp = species.getMovesAtLevel(i);
			while(!mp.isEmpty()) {
				int idx = ((index++)%4);
				moveList[idx] = mp.get(0).move;
				mp.remove(0);
			}
		}

		generatePersonality();
		generateIVs();
		calculateStats();
	}

	/**
	 * <p>Constructor for a new {@code Pokemon}.</p>
	 * 
	 * @param id The id of the {@code Species}.
	 * @param level The level of the {@code Pokemon}.
	 */
	public Pokemon(int id, int level) {
		this(Species.forId(id), level);
	}

	/**
	 * <p>Constructor for a new {@code Pokemon}.</p>
	 * 
	 * @param name The name of the {@code Species}.
	 * @param level The level of the {@code Pokemon}.
	 */
	public Pokemon(String name, int level) {
		this(Species.forName(name), level);
	}

	/**
	 * <p>Converts this {@code Pokemon} into JSON format for saving/loading.</p>
	 * 
	 * @return This {@code Pokemon} into JSON format.
	 */
	public String toJSON() {
		StringBuilder str = new StringBuilder();
		str.append("{\n");
		str.append("  \"species\": \"" + species.getName() + "\",\n");
		str.append("  \"name\": \"" + this.nickname + "\",\n");
		str.append("  \"level\": " + this.level + ",\n");
		str.append("  \"stats\": [");
		for(int i = 0; i < stats.length; i++)
			str.append(stats[i] + (i == stats.length-1 ? "" : ","));
		str.append("],\n");
		str.append("  \"ivs\": [");
		for(int i = 0; i < ivs.length; i++)
			str.append(ivs[i] + (i == ivs.length-1 ? "" : ","));
		str.append("],\n");
		str.append("  \"evs\": [");
		for(int i = 0; i < evs.length; i++)
			str.append(evs[i] + (i == evs.length-1 ? "" : ","));
		str.append("],\n");
		str.append("  \"personality_value\": " + this.personalityValue + ",\n");
		str.append("  \"gender:\": \"" + this.gender.toString() + "\"\n");
		str.append("}");
		return str.toString();
	}

	/**
	 * <p>Generates the personality value.</p>
	 */
	protected void generatePersonality() {
		this.personalityValue = (int)(Math.random()*Integer.MAX_VALUE);
		if(species.getGenderRatio() >= 0.0f && species.getGenderRatio() <= 1.0f) {
			if(species.getGenderRatio() == 0.0f) {
				this.gender = Gender.FEMALE;
			} else if(species.getGenderRatio() == 1.0f) {
				this.gender = Gender.MALE;
			} else {
				int gender = this.personalityValue & 0xFF;
				if((gender/255.0f) >= species.getGenderRatio())
					this.gender = Gender.FEMALE;
				else this.gender = Gender.MALE;
			}
		} else this.gender = Gender.GENDERLESS;
	}

	/**
	 * <p>Generates the individual values for this {@code Pokemon}.</p>
	 */
	protected void generateIVs() {
		for(int i = 0; i < ivs.length; i++)
			ivs[i] = (int)Math.floor(Math.random() * (MAX_IV+1));
	}

	/**
	 * <p>Calculates the stats.</p>
	 */
	protected void calculateStats() {
		for(int i = 0; i < stats.length; i++) {
			double stat = 0.0;
			if(i == 0) {
				stat = Math.floor(((2.0 * species.getBaseStat(i) + ivs[i] + Math.floor(evs[i]/4)) * level)/100.0) + level + 10;
			} else {
				stat = Math.floor(((2.0 * species.getBaseStat(i) + ivs[i] + Math.floor(evs[i]/4)) * level)/100.0) + 5;
			}
			stats[i] = (int)Math.floor(stat);
		}
	}

	/**
	 * <p>Gets the effective stats of pokemon.</p>
	 * 
	 * @param stages The active stages.
	 * @return The effective stats.
	 */
	public float[] getEffectiveStats(int[] stages) {
		float[] s = new float[6];
		s[0] = stages[0];
		for(int i = 1; i < 6; i++) {
			s[i] = getStat(i)*STAGE_MODIFIER[(i+6)];
		}
		return s;
	}

	/**
	 * <p>Uses a move on this pokemon, and generates a move
	 * report so that the battle can know the outcome of the
	 * move.</p>
	 * 
	 * @param defendStats The player stats.
	 * @param attackerStats The enemy stats.
	 * @param attacker The enemy.
	 * @param move The move.
	 */
	public void useMoveOn(int[] defendStats, int[] attackerStats, Pokemon attacker, Move move) {
		if(move.getFunctionCode() == 1)
			return;
		float[] dEffectiveStats = getEffectiveStats(defendStats);
		float[] aEffectiveStats = attacker.getEffectiveStats(attackerStats);
		float damage = getDamage(attacker, move, dEffectiveStats, aEffectiveStats);
		switch(move.getFunctionCode()) {
		case 0x043:
			if(attackerStats[Stat.DEFENCE.ordinal()] > -6)
				attackerStats[Stat.DEFENCE.ordinal()] -= 1;
			break;
		case 0x026:
			if(defendStats[Stat.ATTACK.ordinal()] < 6)
				defendStats[Stat.ATTACK.ordinal()] += 1;
			if(defendStats[Stat.SPEED.ordinal()] < 6)
				defendStats[Stat.SPEED.ordinal()] += 1;
			break;
		case 0x003:
			if(PokeDroid.RANDOM.nextFloat() <= move.getEffectChance())
				setStatusEffect(StatusEffect.SLEEP);
			break;
		case 0x005:
			if(PokeDroid.RANDOM.nextFloat() <= move.getEffectChance())
				setStatusEffect(StatusEffect.POISONED);
			break;
		case 0x006:
			if(PokeDroid.RANDOM.nextFloat() <= move.getEffectChance())
				setStatusEffect(StatusEffect.POISONED);
			break;
		case 0x007:
			if(PokeDroid.RANDOM.nextFloat() <= move.getEffectChance())
				setStatusEffect(StatusEffect.PARALYSIS);
			defendStats[0] -= damage;
			if(defendStats[0] < 0)
				defendStats[0] = 0;
			break;
		case 0x000:
			defendStats[0] -= damage;
			if(defendStats[0] < 0)
				defendStats[0] = 0;
			break;
		}
	}

	/**
	 * <p>Gets the amount of damage.</p>
	 * 
	 * @param attacker The move user.
	 * @param move The move.
	 * @param pEStats This pokemons effective stat.
	 * @param eEStats The other pokemons effective stat.
	 * @return The damage.
	 */
	private int getDamage(Pokemon attacker, Move move, float pEStats[], float eEStats[]) {
		float modifier = 0.85f+(PokeDroid.RANDOM.nextFloat()*0.15f);
		if(attacker.getSpecies().getPrimaryType() == move.getType()
				|| attacker.getSpecies().getSecondaryType() == move.getType())
			modifier *= 1.5;
		modifier *= move.getType().getEffectiveness(species.getPrimaryType(), species.getSecondaryType());
		float damage = ((2*level+10)/250.0f*(eEStats[Stat.ATTACK.ordinal()]/pEStats[Stat.DEFENCE.ordinal()])*move.getBasePower() + 2) * modifier;
		return (int)(damage);
	}

	/**
	 * <p>Sets the status effect.</p>
	 * 
	 * @param effect The status effect.
	 */
	public void setStatusEffect(StatusEffect effect) {
		this.statusEffect = effect;
	}

	/**
	 * <p>Gets the status effect.</p>
	 * 
	 * @return The status effect.
	 */
	public StatusEffect getStatusEffect() {
		return this.statusEffect;
	}

	/**
	 * <p>Sets the nickname of this {@code Pokemon}.</p>
	 * 
	 * @param nickname The new nickname of this {@code Pokemon}.
	 */
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	/**
	 * <p>Gets the name of this {@code Pokemon}, this is the
	 * species name if the nickname is {@code null}, other
	 * wise it is the nickname.</p>
	 * 
	 * @return The name of this {@code Pokemon}.
	 */
	public String getName() {
		return (this.getNickname() == null ? species.getName() : this.getNickname());
	}

	/**
	 * <p>Gets the nickname of this {@code Pokemon}.</p>
	 * 
	 * @return The nickname of this {@code Pokemon}.
	 */
	public String getNickname() {
		return this.nickname;
	}

	/**
	 * <p>Gets the {@link Species} of the {@code Pokemon}.</p>
	 * 
	 * @return The {@link Species} of this {@code Pokemon}.
	 */
	public Species getSpecies() {
		return this.species;
	}

	/**
	 * <p>Gets the current level of the {@code Pokemon}.</p>
	 * 
	 * @return The level of the {@code Pokemon}.
	 */
	public int getLevel() {
		return this.level;
	}

	/**
	 * <p>Gets the stat at index i.</p>
	 * 
	 * @param i The index of the stat.
	 * @return The stat value.
	 */
	public int getStat(int i) {
		if(i < 0 || i >= this.stats.length)
			throw new ArrayIndexOutOfBoundsException(i);
		return this.stats[i];
	}

	/**
	 * <p>Gets the IV at index i.</p>
	 * 
	 * @param i The index of the stat.
	 * @return The IV value.
	 */
	public int getIV(int i) {
		if(i < 0 || i >= this.stats.length)
			throw new ArrayIndexOutOfBoundsException(i);
		return this.ivs[i];
	}

	/**
	 * <p>Gets the EV at index i.</p>
	 * 
	 * @param i The EV of the stat.
	 * @return The EV value.
	 */
	public int getEV(int i) {
		return this.evs[i];
	}

	/**
	 * <p>Gets the personality value of this {@code Pokemon}.</p>
	 * 
	 * @return The personality value of this {@code Pokemon}.
	 */
	public int getPersonalityValue() {
		return this.personalityValue;
	}

	/**
	 * <p>Gets the gender of this {@code Pokemon}.</p>
	 * 
	 * @return The gender of this {@code Pokemon}.
	 */
	public Gender getGender() {
		return this.gender;
	}

	/**
	 * <p>Gets the move list.</p>
	 * 
	 * @return The move list.
	 */
	public Move[] getMove() {
		return this.moveList;
	}
	
	/**
	 * @return The move pp.
	 */
	public int[] getPP() {
		return this.movePP;
	}

	@Override
	public String toString() {
		return species.getName().toUpperCase();
	}

}
