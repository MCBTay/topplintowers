package topplintowers.scenes;

import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.menu.item.SpriteMenuItem;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;

import android.content.SharedPreferences;

import com.topplintowers.R;

import topplintowers.ResourceManager;
import topplintowers.Slider;
import topplintowers.Slider.OnSliderValueChangeListener;
import topplintowers.scenes.SceneManager.SceneType;

public class OptionsScene extends BaseScene implements OnSliderValueChangeListener {
	private Rectangle mRectangle;
	private Text mTitleText, mMusicVolumeText, mFXVolumeText;
	private Slider mMusicSlider, mFXSlider;
	private Sprite mButtonBackground;
	private SharedPreferences mOptions;
	
	
	public Slider getMusicSlider() { return mMusicSlider; }
	public Slider getFXSlider() { return mFXSlider; }
	public Rectangle getRectangle() { return mRectangle; }
	
	@Override
	public void createScene() {		
		mRectangle = SceneCommon.createBackground(this);
		createBackgroundButton();
		createTitleText();
		createMusicVolumeOptions();
		createFXVolumeOption();
		//setOnSceneTouchListener(this);
	}
	
	private void createBackgroundButton() {
		mButtonBackground = new Sprite(60, 60, ResourceManager.mLevelSelectButtonTextureRegion, vbom);
		mButtonBackground.setSize(680, 360);
		attachChild(mButtonBackground);
	}
	
	private void createTitleText() {
		mTitleText = new Text(0, 0, ResourceManager.mFont64, activity.getString(R.string.option), vbom);
		float fontX = mButtonBackground.getX() + 15;
		float fontY = mButtonBackground.getY() + 5;
		mTitleText.setPosition(fontX, fontY);
		attachChild(mTitleText);
	}

	private void createMusicVolumeOptions() {
		mMusicVolumeText = new Text(0, 0, ResourceManager.mFont48, activity.getString(R.string.music_volume), vbom);
		float fontX = mButtonBackground.getX() + 40;
		float fontY = mTitleText.getY() + mTitleText.getHeight() + 5;
		mMusicVolumeText.setPosition(fontX, fontY);
		attachChild(mMusicVolumeText);
		
		mMusicSlider = createSlider(mMusicVolumeText);
	}
	
	private void createFXVolumeOption() {
		mFXVolumeText = new Text(0, 0, ResourceManager.mFont48, activity.getString(R.string.fx_volume), vbom);
		float fontX = mMusicVolumeText.getX() + mMusicVolumeText.getWidth() - mFXVolumeText.getWidth();
		float fontY = mMusicVolumeText.getY() + mMusicVolumeText.getHeight() + 10;
		mFXVolumeText.setPosition(fontX, fontY);
		attachChild(mFXVolumeText);
		
		mFXSlider = createSlider(mFXVolumeText);
	}
	
	private Slider createSlider(Text siblingText) {
		Slider slider = new Slider(vbom);
		float sliderX = siblingText.getX() + siblingText.getWidth() + 50;
		float sliderY = siblingText.getY() + siblingText.getHeight()/2 - slider.getHeight()/2;
		slider.setPosition(sliderX, sliderY);
		attachChild(slider);
		registerTouchArea(slider.getmThumb());
		setTouchAreaBindingOnActionDownEnabled(true);
		slider.setOnSliderValueChangeListener(this);
		return slider;
	}
	
	private void saveOptions() {
		SharedPreferences.Editor editor = mOptions.edit();
		
		float musicVolume = mMusicSlider.getValue();
		float fxVolume = mFXSlider.getValue();
		
		editor.putFloat("musicVolume", musicVolume);
		editor.putFloat("fxVolume", fxVolume);
		
		editor.commit();
	}
	
	@Override
	public void onBackKeyPressed() { 
		//saveOptions();
		returnToMainMenu(); 
	}

	@Override
	public void onMenuKeyPressed() { return; }

	@Override
	public SceneType getSceneType() { return SceneType.SCENE_OPTIONS; }

	@Override
	public void disposeScene() {
	}
	
	private void returnToMainMenu() {
		final MainMenuScene mms = (MainMenuScene)SceneManager.getInstance().getCurrentScene();  
		SpriteMenuItem mainMenuButton = mms.getButtons().get(0);
		SceneCommon.repositionButtons(mainMenuButton.getWidth(), mainMenuButton.getHeight(),  mms.getButtons());
		
		SceneCommon.fadeOutBackground(mRectangle);
		engine.registerUpdateHandler(new TimerHandler(0.2f, new ITimerCallback()
        {                      
            @Override
            public void onTimePassed(final TimerHandler pTimerHandler) { mms.setChildScene(mms.getMenuChildScene()); }
        }));
	}

	@Override
	public void onSliderValueChanged(float value) {
		
	}
}
