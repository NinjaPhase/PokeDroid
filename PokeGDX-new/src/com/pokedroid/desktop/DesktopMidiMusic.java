package com.pokedroid.desktop;

import java.io.IOException;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Sequence;
import javax.sound.midi.Sequencer;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.files.FileHandle;

/**
 * <p>Constructs a {@code DesktopMidi}.</p>
 * 
 * @author J. Kitchen
 * @version 16 March 2016
 *
 */
public class DesktopMidiMusic implements Music {
	private static final boolean PLAY_SOUND = false;

	private Sequence sequence;
	private Sequencer sequencer;

	/**
	 * <p>Constructs a DesktopMidi from the
	 * file handle.</p>
	 * 
	 * @param handle The file handle.
	 */
	public DesktopMidiMusic(FileHandle handle) {
		try {
			sequencer = MidiSystem.getSequencer();   
			sequence = MidiSystem.getSequence(handle.read());
			sequencer.open();
			sequencer.setSequence(sequence);
		} catch (MidiUnavailableException | InvalidMidiDataException | IOException e) {
			Gdx.app.error(getClass().getSimpleName(), "Unable to open midi", e);
		}
	}
	
	/**
	 * <p>Constructs a DesktopMidi from the
	 * file handle.</p>
	 * 
	 * @param handle The file handle.
	 */
	public DesktopMidiMusic(FileHandle handle, float startOfLoop) {
		try {
			sequencer = MidiSystem.getSequencer();   
			sequence = MidiSystem.getSequence(handle.read());
			sequencer.open();
			sequencer.setSequence(sequence);
			setLooping(true);
		} catch (MidiUnavailableException | InvalidMidiDataException | IOException e) {
			Gdx.app.error(getClass().getSimpleName(), "Unable to open midi", e);
		}
	}

	@Override
	public void play() {
		if(!PLAY_SOUND)
			return;
		if(sequencer != null) {
			sequencer.start();
		}
	}

	@Override
	public void pause() {
		stop();
	}

	@Override
	public void stop() {
		if(!PLAY_SOUND)
			return;
		if(sequencer != null) {
			sequencer.stop();
		}
	}

	@Override
	public boolean isPlaying() {
		return sequencer.isRunning();
	}

	@Override
	public void setLooping(boolean isLooping) {
		if(sequencer != null) {
			sequencer.setLoopCount(0);
		}
	}

	@Override
	public boolean isLooping() {
		if(sequencer != null){
			return sequencer.getLoopCount() != 0;
		}
		return false;
	}

	@Override
	public void setVolume(float volume) {
		
	}

	@Override
	public float getVolume() {
		return 0;
	}

	@Override
	public void setPan(float pan, float volume) {

	}

	@Override
	public void setPosition(float position) {

	}

	@Override
	public float getPosition() {
		return 0;
	}

	@Override
	public void dispose() {
		if(sequencer != null) {
			sequencer.close();
			sequencer = null;
		}
	}

	@Override
	public void setOnCompletionListener(OnCompletionListener listener) {

	}

}
