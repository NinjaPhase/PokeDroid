package com.pokedroid.input.midi;

import java.io.IOException;
import java.io.InputStream;

import com.badlogic.gdx.Files.FileType;

public interface MidiPlayer {
    public void open(String fileName, FileType type);
    public void open(InputStream is) throws IOException;
    public boolean isLooping();
    public void setLooping(boolean loop);
    public void play();
    public void pause();
    public void stop();
    public void release();
    public boolean isPlaying();
    public void setVolume(float volume);
}
