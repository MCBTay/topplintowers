package topplintowers.scenes;

import java.util.ArrayList;
import java.util.Enumeration;

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

import com.topplintowers.R;

import topplintowers.MainActivity;
import topplintowers.crates.Crate;
import topplintowers.crates.CrateType;
import topplintowers.resources.ResourceManager;
import topplintowers.scenes.SceneManager.SceneType;
import topplintowers.scenes.gamescene.GameScene;
import topplintowers.scenes.gamescene.hud.CrateContainer;
import topplintowers.scenes.gamescene.hud.CrateThumbnail;
import topplintowers.scenes.gamescene.hud.MyHUD;

public class WinScene extends BaseScene implements IOnMenuItemClickListener {
	private Rectangle mRectangle;
	private Text mText;
	private WinScene instance;  //ugly hack
	
	private SpriteMenuItem mResumeButton, mRestartButton, mMainMenuButton, mLevelSelect;
	private ArrayList<SpriteMenuItem> mButtons;
	private MenuScene mMenuChildScene;
	
	public Camera getCamera() { return camera; }
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
		
		mText = new Text(0, 0, ResourceManager.mFont140, activity.getString(R.string.you_win), vbom);
		float fontX = 25;
		float fontY = camera.getHeight()/2 - mText.getHeight()/2 - 40;
		mText.setPosition(fontX, fontY);
		attachChild(mText);
		
		Text mSubtext = new Text(0, 0, ResourceManager.mFont64, "You beat level 1.", vbom);
		fontX = 35;
		fontY = camera.getHeight()/2 - mSubtext.getHeight()/2 + 40;
		mSubtext.setPosition(fontX, fontY);
		attachChild(mSubtext);
		
		createMenuChildScene();
		
		instance = this;
	}
	
	private void createMenuChildScene()
	{
		mMenuChildScene = new MenuScene(camera);
		mMenuChildScene.setPosition(0, 0);
		mMenuChildScene.setBackgroundEnabled(false);
		
		mButtons = new ArrayList<SpriteMenuItem>();
		
		mRestartButton = SceneCommon.createMenuButton(mMenuChildScene, MenuButtonsEnum.RESTART,  activity.getString(R.string.restart));
		mButtons.add(mRestartButton);
		
		mLevelSelect = SceneCommon.createMenuButton(mMenuChildScene, MenuButtonsEnum.LEVEL_SELECT,  activity.getString(R.string.levelselect));
		mButtons.add(mLevelSelect);
		
		mMainMenuButton = SceneCommon.createMenuButton(mMenuChildScene, MenuButtonsEnum.MAIN_MENU,  activity.getString(R.string.main_menu));
		mButtons.add(mMainMenuButton);
		
		SceneCommon.repositionButtons(mRestartButton.getWidth(), mRestartButton.getHeight(), mButtons);
		
		mMenuChildScene.setOnMenuItemClickListener(this);
		setChildScene(mMenuChildScene);
	}

	@Override
	public void onBackKeyPressed() { return; }
	
	@Override
	public void onMenuKeyPressed() { return; }

	@Override
	public SceneType getSceneType() { return SceneType.WIN; }

	@Override
	public void disposeScene() {
		// TODO Auto-generated method stub
		
	}
	
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
	    	        case RESTART:
	    	        	SceneManager.getInstance().restartGameScene(instance);
	    	    		break;
	    	        case LEVEL_SELECT:
	    	        	SceneManager.getInstance().loadLevelSelect(engine);
	    	        	break;
	    	        case MAIN_MENU:
	    	        	SceneManager.getInstance().loadMenuScene(engine);
	    	        	break;
	    	        default:
						break;
            	}
            }
        }));
	    return true;
	}
		
	public void fadeIn() {
		SceneCommon.applyFadeModifier(mRectangle, 0, 0.75f);
		SceneCommon.applyFadeModifier(mButtons, 0, 1);
		SceneCommon.applyFadeModifier(mText, 0, 1);
	}
	
	public void fadeOut() {
		SceneCommon.applyFadeModifier(mRectangle, 0.75f, 0);
		SceneCommon.applyFadeModifier(mButtons, 1, 0);
		SceneCommon.applyFadeModifier(mText, 1, 0);
	}

}
