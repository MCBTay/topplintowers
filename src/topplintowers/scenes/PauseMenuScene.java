package topplintowers.scenes;

import java.util.ArrayList;
import org.andengine.engine.camera.Camera;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.MoveXModifier;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.menu.MenuScene;
import org.andengine.entity.scene.menu.MenuScene.IOnMenuItemClickListener;
import org.andengine.entity.scene.menu.item.IMenuItem;
import org.andengine.entity.scene.menu.item.SpriteMenuItem;
import org.andengine.entity.text.Text;
import topplintowers.scenes.SceneManager.SceneType;
import android.util.Log;

import com.topplintowers.R;

public class PauseMenuScene extends BaseScene implements IOnMenuItemClickListener {
	private SpriteMenuItem mResumeButton, mRestartButton, mMainMenuButton, mOptionsButton;
	private ArrayList<SpriteMenuItem> mButtons;
	private Rectangle mRectangle;
	private Text mText;
	private MenuScene mMenuChildScene;
	private PauseMenuScene instance;
	
	public ArrayList<SpriteMenuItem> getButtons() { return mButtons; }
	public Rectangle getRectangle() { return mRectangle; }
	public Text getText() { return mText; }
	public void setBackgroundPosition(float x, float y) { mRectangle.setPosition(x, y); }
	public void setTextPosition(float x, float y) { mText.setPosition(x, y); }

	@Override
	public void createScene() {	
		setBackgroundEnabled(false);
		mRectangle = new Rectangle(0, 0, 800, 480, vbom);
		mRectangle.setColor(0,0,0,0);
		attachChild(mRectangle);

		mText = SceneCommon.createLargeText(this, activity.getString(R.string.paused));		
		createMenuChildScene();
		
		instance = this;
	}
	
	public Camera getCamera() { return camera; }
	

	
	private void createMenuChildScene()
	{
		mMenuChildScene = new MenuScene(camera);
		mMenuChildScene.setPosition(0, 0);
		mMenuChildScene.setBackgroundEnabled(false);
		
		mButtons = new ArrayList<SpriteMenuItem>();
		
		mResumeButton = SceneCommon.createMenuButton(mMenuChildScene, MenuButtonsEnum.RESUME,  activity.getString(R.string.resume));
		mButtons.add(mResumeButton);
		
		mRestartButton = SceneCommon.createMenuButton(mMenuChildScene, MenuButtonsEnum.RESTART,  activity.getString(R.string.restart));
		mButtons.add(mRestartButton);
		
		mOptionsButton = SceneCommon.createMenuButton(mMenuChildScene, MenuButtonsEnum.OPTIONS,  activity.getString(R.string.option));
		mButtons.add(mOptionsButton);
		
		mMainMenuButton = SceneCommon.createMenuButton(mMenuChildScene, MenuButtonsEnum.MAIN_MENU,  activity.getString(R.string.main_menu));
		mButtons.add(mMainMenuButton);
		
		SceneCommon.repositionButtons(mResumeButton.getWidth(), mResumeButton.getHeight(), mButtons);
		
		mMenuChildScene.setOnMenuItemClickListener(this);
		setChildScene(mMenuChildScene);
	}

	@Override
	public void onBackKeyPressed() { SceneManager.getInstance().returnToGameScene(instance);}
	
	@Override
	public void onMenuKeyPressed() { SceneManager.getInstance().returnToGameScene(instance); }
	


	@Override
	public SceneType getSceneType() { return SceneType.PAUSED; }

	@Override
	public void disposeScene() { return; }
	
	@Override
	public boolean onMenuItemClicked(final MenuScene pMenuScene, final IMenuItem pMenuItem, float pMenuItemLocalX, float pMenuItemLocalY) {
		final MoveXModifier moveRight = new MoveXModifier(0.25f, pMenuItem.getX() - 25, camera.getWidth());
		moveRight.setAutoUnregisterWhenFinished(true);
		
		MoveXModifier moveLeft = new MoveXModifier(0.25f, pMenuItem.getX(), pMenuItem.getX() - 25) {
			@Override
			protected void onModifierFinished(IEntity pItem) {
				pItem.registerEntityModifier(moveRight);
			}
		};
	    moveLeft.setAutoUnregisterWhenFinished(true);
	    pMenuItem.registerEntityModifier(moveLeft);
	    
	    engine.registerUpdateHandler(new TimerHandler(0.55f, new ITimerCallback() {                      
            @Override
            public void onTimePassed(final TimerHandler pTimerHandler)
            {            	
            	MenuButtonsEnum button = MenuButtonsEnum.values()[pMenuItem.getID()];
            	switch (button) {
            	 	case RESUME:  
            	 		SceneManager.getInstance().returnToGameScene(instance);
	    	        	break;
	    	        case RESTART:
	    	        	SceneManager.getInstance().restartGameScene(instance);
	    	    		break;
	    	        case MAIN_MENU:
	    	        	SceneManager.getInstance().loadMenuScene(engine);
	    	        	break;
	    	        case OPTIONS:
	    	        	SceneManager.getInstance().loadOptionsScene(engine, false);
	    	        	break;
	    	        default:
						break;
            	}
            }
        }));
	    return true;
	}
	
	public void fadeIn() {
		Log.e("", "Fading in pause menu...");
		SceneCommon.applyFadeModifier(mRectangle, 0, 0.75f);
		SceneCommon.applyFadeModifier(mButtons, 0, 1);
		SceneCommon.applyFadeModifier(mText, 0, 1);
	}
	
	public void fadeOut() {
		Log.e("", "Fading out pause menu...");
		SceneCommon.applyFadeModifier(mRectangle, 0.75f, 0);
		SceneCommon.applyFadeModifier(mButtons, 1, 0);
		SceneCommon.applyFadeModifier(mText, 1, 0);
	}

}
