package com.pokedroid.android;
import android.content.Context;
import java.io.IOException;
import java.io.InputStream;

import com.badlogic.gdx.LifecycleListener;
import com.badlogic.gdx.Files.FileType;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.files.FileHandle;
import com.pokedroid.input.midi.MidiPlayer;

public class AndroidMidiPlayer implements MidiPlayer {
	
	private Music m;
	private FileHandle file;

	public AndroidMidiPlayer(Context context) {
		
	}

	public void open(String fileName, FileType type) {
		
	}
	
	@Override
	public void open(InputStream is) throws IOException {
		
	}

	//TODO: This should probably be replaced with something better.
	//I had to reset the player to avoid error when
	//opening a second midi file.
	private void reset() {
		
	}
	
	public boolean isLooping() {
		return false;
	}

	public void setLooping(boolean loop) {
		
	}

	public void play() {
		
	}

	public void pause() {
		
	}

	public void stop() {
		
	}

	public void release() {
		
	}

	public boolean isPlaying() {
		return false;
	}

	public void setVolume(float volume) {
		
	}

}