package com.pokedroid.desktop;

import java.io.IOException;
import java.io.InputStream;

import javax.sound.midi.*;

import com.badlogic.gdx.Files.FileType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.pokedroid.input.midi.MidiPlayer;

public class DesktopMidiPlayer implements MidiPlayer {

	private Sequence sequence;
	private Sequencer sequencer;

	public DesktopMidiPlayer() {

		try {
			sequencer = MidiSystem.getSequencer();
		} catch (MidiUnavailableException e) {
			e.printStackTrace();
		}

	}

	public void open(String fileName, FileType type) {
		FileHandle file = Gdx.files.getFileHandle(fileName, type);
		try {
			sequence = MidiSystem.getSequence(file.read());
			sequencer.open();
			sequencer.setSequence(sequence);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void open(InputStream is) throws IOException {
		try {
			sequence = MidiSystem.getSequence(is);
			sequencer.open();
			sequencer.setSequence(sequence);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public boolean isLooping() {
		if(sequencer != null){
			return sequencer.getLoopCount() != 0;
		}
		return false;
	}


	public void setLooping(boolean loop) {
		if(sequencer != null){
			if(!loop){
				sequencer.setLoopCount(0);
				return;
			}
			sequencer.setLoopCount(Sequencer.LOOP_CONTINUOUSLY);
		}
	}


	public void play() {
		if(sequencer != null){
			sequencer.start();
		}
	}


	public void pause() {
		stop();
	}


	public void stop() {
		if(sequencer != null){
			sequencer.stop();
		}
	}

	public void release() {
		if(sequencer != null){
			sequencer.close();
		}
	}

	public boolean isPlaying() {
		return sequencer.isRunning();
	}

	public void setVolume(float volume) {
		//Not implemented
	}
}