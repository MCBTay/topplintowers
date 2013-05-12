package topplintowers.scenes;

import java.util.ArrayList;

import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.MoveXModifier;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.menu.MenuScene;
import org.andengine.entity.scene.menu.MenuScene.IOnMenuItemClickListener;
import org.andengine.entity.scene.menu.item.IMenuItem;
import org.andengine.entity.scene.menu.item.SpriteMenuItem;
import org.andengine.entity.text.Text;
import org.andengine.input.touch.TouchEvent;

import topplintowers.scenes.SceneManager.SceneType;

import com.topplintowers.R;

public class QuitPopupScene extends BaseScene implements IOnSceneTouchListener, IOnMenuItemClickListener {
	private SpriteMenuItem mCancelButton, mQuitButton;
	private ArrayList<SpriteMenuItem> mButtons;
	private Rectangle mRectangle;
	private Text mText;
	
	private MenuScene mMenuChildScene;
		
	public Rectangle getRectangle() { return mRectangle; }
	public ArrayList<SpriteMenuItem> getButtons() { return mButtons; }
	public Text getText() { return mText; }
	
	@Override
	public boolean onSceneTouchEvent(final Scene pScene, final TouchEvent pSceneTouchEvent) {					
		return true;
	}

	@Override
	public void createScene() {
		mRectangle = SceneCommon.createBackground(this);
		//setBackgroundEnabled(false);
		mText = SceneCommon.createLargeText(this, activity.getString(R.string.Quit));
		createMenuChildScene();
		setOnSceneTouchListener(this);
	}

	@Override
	public void onBackKeyPressed() { returnToMainMenu(); }
	
	@Override
	public void onMenuKeyPressed() { return; }

	@Override
	public SceneType getSceneType() { return SceneType.SCENE_QUIT_POPUP; }

	@Override
	public void disposeScene() { return; }
	
	private void createMenuChildScene()
	{
		mMenuChildScene = new MenuScene(camera);
		mMenuChildScene.setPosition(0, 0);
		mMenuChildScene.setBackgroundEnabled(false);
		
		mButtons = new ArrayList<SpriteMenuItem>();
		
		mQuitButton = SceneCommon.createMenuButton(mMenuChildScene, MenuButtonsEnum.KILL_APP,  activity.getString(R.string.quit));
		mButtons.add(mQuitButton);
		
		mCancelButton = SceneCommon.createMenuButton(mMenuChildScene, MenuButtonsEnum.CANCEL,  activity.getString(R.string.cancel));
		mButtons.add(mCancelButton);
		
		SceneCommon.repositionButtons(mQuitButton.getWidth(), mQuitButton.getHeight(), mButtons);

		mMenuChildScene.setOnMenuItemClickListener(this);
		setChildScene(mMenuChildScene);
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
			        case KILL_APP:
		    			android.os.Process.killProcess(android.os.Process.myPid());
		    			break;
		        	case CANCEL:
		        		returnToMainMenu();
		        		break;
					
	    	        default:
						break;
            	}
            	
            }
        }));
	    return true;
	}
	
	private void returnToMainMenu() {
		final MainMenuScene mms = (MainMenuScene)SceneManager.getInstance().getCurrentScene();  
		SceneCommon.repositionButtons(mQuitButton.getWidth(), mQuitButton.getHeight(),  mms.getButtons());
		
		fadeOut();
		
		engine.registerUpdateHandler(new TimerHandler(0.2f, new ITimerCallback()
        {                      
            @Override
            public void onTimePassed(final TimerHandler pTimerHandler)
            {   
 	       		mms.setChildScene(mms.getMenuChildScene());
        		SceneCommon.repositionButtons(mQuitButton.getWidth(), mQuitButton.getHeight(), mButtons);
            }
        }));
	}
	
	public void fadeIn() {
		SceneCommon.applyFadeModifier(mRectangle, 0, 0.75f);
		SceneCommon.applyFadeModifier(mButtons, 0, 1);
		SceneCommon.applyFadeModifier(mText, 0, 1);
	}
	
	private void fadeOut() {
		SceneCommon.applyFadeModifier(mRectangle, 0.75f, 0);
		SceneCommon.applyFadeModifier(mButtons, 1, 0);
		SceneCommon.applyFadeModifier(mText, 1, 0);
	}
}
