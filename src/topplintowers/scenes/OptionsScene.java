package topplintowers.scenes;

import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.menu.item.SpriteMenuItem;
import org.andengine.entity.sprite.Sprite;

import topplintowers.ResourceManager;
import topplintowers.scenes.SceneManager.SceneType;

public class OptionsScene extends BaseScene {
	private Rectangle mRectangle;
	
	public Rectangle getRectangle() { return mRectangle; }
	
	@Override
	public void createScene() {
		mRectangle = SceneCommon.createBackground(this);
		createBackgroundButton();
		//setOnSceneTouchListener(this);
	}
	
	private void createBackgroundButton() {
		Sprite buttonBackground = new Sprite(60, 60, ResourceManager.mLevelSelectButtonTextureRegion, vbom);
		buttonBackground.setSize(680, 360);
		attachChild(buttonBackground);
	}

	@Override
	public void onBackKeyPressed() { returnToMainMenu(); }

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
}
