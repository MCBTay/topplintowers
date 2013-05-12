package topplintowers.resources;

import java.io.IOException;

import org.andengine.audio.music.Music;
import org.andengine.audio.music.MusicFactory;
import org.andengine.audio.sound.Sound;
import org.andengine.audio.sound.SoundFactory;
import org.andengine.engine.Engine;

import android.content.SharedPreferences;

import topplintowers.MainActivity;
import topplintowers.scenes.SceneManager;

public class SoundManager {
	private static final SoundManager INSTANCE = new SoundManager();
	private Engine mEngine = ResourceManager.getInstance().mEngine;
	private MainActivity mActivity = ResourceManager.getInstance().mActivity;
	
    public static Sound mCollisionSound;
    public static Music mBackgroundMusic;
    
    public static float mFXVolume;
    
    public static SoundManager getInstance() { return INSTANCE; }
    
	public void loadMusic() {
		try {  //forced to handle the IOException here but not for images?
    		mBackgroundMusic = MusicFactory.createMusicFromAsset(mEngine.getMusicManager(), mActivity, "snd/background.wav");
    		mBackgroundMusic.setLooping(true);
    	} catch (IOException e) {
    		e.printStackTrace();
    	}
	}
    
    public void loadGameAudio() {
    	try {  //forced to handle the IOException here but not for images?
    		mCollisionSound = SoundFactory.createSoundFromAsset(mEngine.getSoundManager(), mActivity.getApplicationContext(), "snd/collision.wav");
    		mCollisionSound.setVolume(mFXVolume);
    	} catch (IOException e) {
    		e.printStackTrace();
    	}
    }

    public void playBlockCollision() {
    	SharedPreferences options = mActivity.getOptions();
		float volume = options.getFloat("fxVolume", 50);
		setMusicVolume(volume);
		mCollisionSound.play();
    }
    
    public void playBackgroundMusic() {
		SharedPreferences options = mActivity.getOptions();
		float volume = options.getFloat("musicVolume", 50);
		mBackgroundMusic.setVolume(volume/100);
		mBackgroundMusic.play();
    }
    
    public void setMusicVolume(float value) {
    	mBackgroundMusic.setVolume(value);
    }
    
    public void setFXVolume(float value) {
    	// because you can set this value before the sounds have been loaded, we'll store off the value
    	// and set it when we load the sounds
    	mFXVolume = value;
    }
    
    public void saveSoundOptions(float musicVolume, float fxVolume) {
		SharedPreferences options = mActivity.getOptions();
		SharedPreferences.Editor editor = options.edit();

		editor.putFloat("musicVolume", musicVolume);
		editor.putFloat("fxVolume", fxVolume);
		
		editor.commit();
    }
}
