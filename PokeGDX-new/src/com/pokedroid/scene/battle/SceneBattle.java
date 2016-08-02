package com.pokedroid.scene.battle;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.pokedroid.PokeDroid;
import com.pokedroid.desktop.DesktopMidiMusic;
import com.pokedroid.pokemon.Move;
import com.pokedroid.pokemon.Pokemon;
import com.pokedroid.pokemon.Stat;
import com.pokedroid.scene.IScene;
import com.pokedroid.scene.window.ChoiceBox;
import com.pokedroid.scene.window.EnemyStatus;
import com.pokedroid.scene.window.MessageBox;
import com.pokedroid.scene.window.PlayerStatus;
import com.pokedroid.scene.window.MessageBox.MessageBoxSkin;
import com.pokedroid.util.ResourceManager;

/**
 * <p>The {@code SceneBattle} is the scene for a 
 * pokémon battle.</p>
 * 
 * @author J. Kitchen
 * @version 10 March 2016
 *
 */
public class SceneBattle implements IScene {
	private static final float[] STAGE_MODIFIER = new float[]{
			3/9, 3/8, 3/7, 3/6, 3/5, 3/4,
			3/3,
			4/3, 5/3, 6/3, 7/3, 8/3, 9/3};

	private ResourceManager rm;
	private NinePatch window;
	private BitmapFont font, statusFont;
	private MessageBox messageBox;
	private ChoiceBox fightOptions, moveOptions;
	private Pokemon p, e;
	private PlayerStatus pStatus;
	private EnemyStatus eStatus;
	private Texture top, loop, statusEffects;
	private Music music;
	private FightState state;
	private boolean showPlayerPokemon, showEnemyPokemon;
	private float dist;
	private int[] pStats;
	private int[] eStats;
	private Move pMove, eMove;
	private boolean pAttackFirst;
	private boolean playerWon;

	@Override
	public void create(PokeDroid game) {
		// Setup Party
		p = new Pokemon("DRAGONITE", 55);
		e = new Pokemon(25, 5);

		// Setup stats
		pStats = new int[6];
		pStats[0] = p.getStat(0);
		eStats = new int[6];
		eStats[0] = e.getStat(0);

		// Load Resources
		this.rm = game.getGlobalResources().createSubManager(getClass().getSimpleName());
		this.music = this.rm.load("BattleMusic", new DesktopMidiMusic(Gdx.files.internal("wildBattle.mid")));
		this.music.play();
		this.top = this.rm.load("bgTop", new Texture("graphics/ui/battle/standardBGTop.png"));
		this.loop = this.rm.load("bgLoop", new Texture("graphics/ui/battle/standardBGLoop.png"));
		this.statusEffects = this.rm.load("statusEffects", new Texture("graphics/ui/statusEffects.png"));
		Texture bgPlayer = this.rm.load("battlePlayerBG", new Texture("graphics/ui/battlePlayerBG.png"));
		Texture bgEnemy = this.rm.load("battleEnemyBG", new Texture("graphics/ui/battleEnemyBG.png"));
		Texture healthbars = this.rm.load("healthbars", new Texture("graphics/ui/battle/healthbars.png"));
		this.window = new NinePatch(
				new Texture("graphics/ui/windowskin.png"),
				16, 16, 16, 16);
		this.font = new BitmapFont(Gdx.files.internal("graphics/fonts/mainFont.fnt"));
		this.statusFont = this.rm.load("battleFont", new BitmapFont(Gdx.files.internal("graphics/fonts/battleFont.fnt")));
		MessageBoxSkin skin = this.rm.load("WindowSkin", new MessageBoxSkin(font, window).setOwnsResources(true));
		messageBox = new MessageBox(skin, "A Wild " + e.getName() + " appeared.");
		fightOptions = new ChoiceBox(skin, 2, 2, "FIGHT", "BAG", "POKéMON", "RUN");
		moveOptions = new ChoiceBox(skin, 2, 2, (Object[])p.getMove());
		moveOptions.setFixedWidth(160.0f);
		pStatus = new PlayerStatus(p, statusFont, bgPlayer, healthbars);
		eStatus = new EnemyStatus(e, statusFont, bgEnemy, healthbars);
		state = FightState.FIGHT_BEGIN;
	}

	@Override
	public void update(float deltaTime) {
		messageBox.update(deltaTime);
		if(dist < 1.0f) {
			dist += deltaTime;
			if(dist > 1.0f)
				dist = 1.0f;
		}
		pStatus.update(deltaTime);
		eStatus.update(deltaTime);
	}

	@Override
	public void render(Batch batch) {
		// Draw BG
		for(int i = 0; i < Gdx.graphics.getWidth()/this.top.getWidth(); i++) {
			batch.draw(top, top.getWidth()*i, Gdx.graphics.getHeight()-this.top.getHeight());
		}
		for(int y = 0; y < ((Gdx.graphics.getHeight()-top.getHeight())/loop.getHeight())+2; y++) {
			for(int x = 0; x < Gdx.graphics.getWidth()/this.loop.getHeight(); x++) {
				batch.draw(loop, loop.getWidth()*x,  Gdx.graphics.getHeight()-this.top.getHeight()-(loop.getHeight()*y));
			}
		}

		// Draw Pokemon
		if(showPlayerPokemon) {
			batch.draw(getPlayer(), 64.0f, 94.0f);
		}
		if(showEnemyPokemon) {
			batch.draw(getEnemy(),
					Gdx.graphics.getWidth()-getEnemy().getWidth()-48.0f,
					Gdx.graphics.getHeight()-getEnemy().getHeight()-0.0f);
		} else if(!showEnemyPokemon && state == FightState.FIGHT_BEGIN) {
			if(dist < 1.0f) {
				batch.setColor(0f, 0f, 0f, 0.65f);
			}
			batch.draw(getEnemy(),
					Gdx.graphics.getWidth()-(getEnemy().getWidth()+48.0f)*dist,
					Gdx.graphics.getHeight()-getEnemy().getHeight()-0.0f);
			batch.setColor(1.0f, 1.0f, 1.0f, 1.0f);
		}

		pStatus.render(batch, Gdx.graphics.getWidth()-pStatus.getWidth()-64.0f, 96.0f);
		eStatus.render(batch, 64.0f, Gdx.graphics.getHeight()-eStatus.getHeight()-48.0f);

		switch(state)
		{
		case FIGHT_BEGIN:
			messageBox.render(batch, 0.0f, 0.0f, Gdx.graphics.getWidth(), 96.0f);
			break;
		case FIGHT_SELECT_OPTION:
			messageBox.render(batch, 0.0f, 0.0f, Gdx.graphics.getWidth()-240.0f, 96.0f);
			fightOptions.render(batch, Gdx.graphics.getWidth()-240.0f, 0.0f);
			break;
		case FIGHT_SELECT_MOVE:
			moveOptions.render(batch, 0.0f, 0.0f, 426.0f, 96.0f);
			break;
		case FIGHT_ENEMY_MOVE:
			messageBox.render(batch, 0.0f, 0.0f, Gdx.graphics.getWidth(), 96.0f);
			break;
		case FIGHT_PLAYER_MOVE:
			messageBox.render(batch, 0.0f, 0.0f, Gdx.graphics.getWidth(), 96.0f);
			break;
		case FIGHT_END:
			messageBox.render(batch, 0.0f, 0.0f, Gdx.graphics.getWidth(), 96.0f);
			break;
		}

	}

	@Override
	public void resize(int width, int height) {

	}

	@Override
	public void focusGained() {

	}

	@Override
	public void focusLost() {
		music.stop();
	}

	@Override
	public void dispose() {
		this.rm.dispose();
	}

	/**
	 * <p>Changes the state.</p>
	 * 
	 * @param state The new state.
	 */
	private void changeState(FightState state) {
		this.state = state;
		switch(state) {
		case FIGHT_BEGIN:
			break;
		case FIGHT_SELECT_OPTION:
			messageBox.setText("What would you like " + p.getName() + "\nto do?");
			break;
		case FIGHT_SELECT_MOVE:
			break;
		case FIGHT_ENEMY_MOVE:
			messageBox.setText("Foe " + e.getName() + " used " + eMove.getName() + ".");
			break;
		case FIGHT_PLAYER_MOVE:
			messageBox.setText(p.getName() + " used " + pMove.getName() + ".");
			break;
		case FIGHT_END:
			if(eStats[0] <= 0) {
				messageBox.setText("Foe " + e.getName() + " has fainted.");
			} else if(pStats[0] <= 0) {
				messageBox.setText("Player's " + p.getName() + " has fainted.");
			}
			break;
		}
	}

	@Override
	public boolean keyDown(int keycode) {
		if(keycode == Keys.UP) {
			fightOptions.moveCursor(0, -1);
			moveOptions.moveCursor(0, -1);
			return true;
		} else if(keycode == Keys.DOWN) {
			fightOptions.moveCursor(0, 1);
			moveOptions.moveCursor(0, 1);
			return true;
		} else if(keycode == Keys.LEFT) {
			fightOptions.moveCursor(-1, 0);
			moveOptions.moveCursor(-1, 0);
			return true;
		} else if(keycode == Keys.RIGHT) {
			fightOptions.moveCursor(1, 0);
			moveOptions.moveCursor(1, 0);
			return true;
		} else if(keycode == Keys.Z) {
			switch(state) {
			case FIGHT_BEGIN:
				if(messageBox.isFinished() && dist >= 1.0f && !showEnemyPokemon) {
					showEnemyPokemon = true;
					messageBox.append("\n$global.player sent out " + p.getName() + ".");
					dist = 1.0f;
				} else if(messageBox.isFinished() && showEnemyPokemon) {
					showPlayerPokemon = true;
					changeState(FightState.FIGHT_SELECT_OPTION);
				}
				break;
			case FIGHT_SELECT_OPTION:
				if(fightOptions.getSelectedIndex() == 0) {
					changeState(FightState.FIGHT_SELECT_MOVE);
				}
				break;
			case FIGHT_SELECT_MOVE:
				if(moveOptions.getSelectedValue() != null) {
					pMove = (Move)moveOptions.getSelectedValue();
					while(eMove == null) {
						eMove = e.getMove()[PokeDroid.RANDOM.nextInt(4)];
					}
					if(pMove.getPriority() != eMove.getPriority()) {

					} else {
						float pEffectiveSpeed = p.getStat(Stat.SPEED.ordinal())*STAGE_MODIFIER[(pStats[Stat.SPEED.ordinal()]+6)];
						float eEffectiveSpeed = e.getStat(Stat.SPEED.ordinal())*STAGE_MODIFIER[(eStats[Stat.SPEED.ordinal()]+6)];
						if(pEffectiveSpeed > eEffectiveSpeed) {
							pAttackFirst = true;
						} else if(pEffectiveSpeed < eEffectiveSpeed) {
							pAttackFirst = false;
						} else {
							if(PokeDroid.RANDOM.nextBoolean()) {
								pAttackFirst = true;
							} else {
								pAttackFirst = false;
							}
						}
						changeState(pAttackFirst ? FightState.FIGHT_PLAYER_MOVE : FightState.FIGHT_ENEMY_MOVE);
					}
				}
				break;
			case FIGHT_ENEMY_MOVE:
				if(messageBox.isFinished() && eMove != null) {
					p.useMoveOn(pStats, eStats, e, eMove);
					pStatus.setHealth(pStats[0]);
					eMove = null;
					if(pStats[0] <= 0) {
						playerWon = true;
						changeState(FightState.FIGHT_END);
					} else if(eStats[0] <= 0) {
						playerWon = false;
						changeState(FightState.FIGHT_END);
					}
				} else if(messageBox.isFinished() && eMove == null) {
					changeState(pAttackFirst ? FightState.FIGHT_SELECT_OPTION : FightState.FIGHT_PLAYER_MOVE);
				}
				break;
			case FIGHT_PLAYER_MOVE:
				if(messageBox.isFinished() && pMove != null) {
					e.useMoveOn(eStats, pStats, p, pMove);
					eStatus.setHealth(eStats[0]);
					pMove = null;
					if(pStats[0] <= 0) {
						playerWon = true;
						changeState(FightState.FIGHT_END);
					} else if(eStats[0] <= 0) {
						playerWon = false;
						changeState(FightState.FIGHT_END);
					}
				} else if(messageBox.isFinished() && pMove == null) {
					changeState(!pAttackFirst ? FightState.FIGHT_SELECT_OPTION : FightState.FIGHT_ENEMY_MOVE);
				}
				break;
			case FIGHT_END:
				break;
			}
			return true;
		} else if(keycode == Keys.X) {
			switch(state) {
			case FIGHT_BEGIN:
				break;
			case FIGHT_SELECT_OPTION:
				break;
			case FIGHT_SELECT_MOVE:
				changeState(FightState.FIGHT_SELECT_OPTION);
				break;
			case FIGHT_ENEMY_MOVE:
				break;
			case FIGHT_PLAYER_MOVE:
				break;
			case FIGHT_END:
				break;
			}
			return true;
		} else if(keycode == Keys.R) {
			this.rm.unload(BitmapFont.class, "battleFont");
			this.statusFont = new BitmapFont(Gdx.files.internal("graphics/fonts/battleFont.fnt"));
			pStatus = new PlayerStatus(p, statusFont,
					this.rm.get(Texture.class, "battlePlayerBG"), this.rm.get(Texture.class, "healthbars"));
			eStatus = new EnemyStatus(e, statusFont,
					this.rm.get(Texture.class, "battleEnemyBG"), this.rm.get(Texture.class, "healthbars"));
			System.out.println("Font reloaded");
			return true;
		}
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		return false;
	}

	/**
	 * <p>Gets the players texture.</p>
	 * 
	 * @return The players texture.
	 */
	protected Texture getPlayer() {
		return this.p.getSpecies().getBackSprite(false);
	}

	/**
	 * <p>Gets the enemy texture.</p>
	 * 
	 * @return The enemy texture.
	 */
	protected Texture getEnemy() {
		return this.e.getSpecies().getFrontSprite(false);
	}

	/**
	 * <p>A group of states used for determining which GUI to show,
	 * and what course of action to take place.</p>
	 * 
	 * @author J. Kitchen
	 * @version 18 March 2016
	 *
	 */
	public static enum FightState {
		FIGHT_BEGIN,
		FIGHT_SELECT_OPTION,
		FIGHT_SELECT_MOVE,
		FIGHT_PLAYER_MOVE,
		FIGHT_ENEMY_MOVE,
		FIGHT_END;
	}

}
