package topplintowers;

import org.andengine.engine.camera.SmoothCamera;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.modifier.AlphaModifier;
import org.andengine.entity.scene.Scene;
import org.andengine.extension.physics.box2d.FixedStepPhysicsWorld;
import org.andengine.ui.activity.BaseGameActivity;
import topplintowers.levels.LevelMgr;
import topplintowers.scenes.BaseScene;
import topplintowers.scenes.GameScene;
import topplintowers.scenes.PauseMenuScene;
import topplintowers.scenes.SceneCommon;
import topplintowers.scenes.SceneManager;
import android.view.KeyEvent;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.joints.MouseJoint;

public class MainActivity extends BaseGameActivity {
	private static MainActivity instance;

	public SmoothCamera mCamera;

	public Scene mCurrentScene;

	public FixedStepPhysicsWorld mPhysicsWorld;
	public Body mGroundBody;
	public MouseJoint mMouseJointActive;

	public static LevelMgr mLevelManager;
	
	private ResourceManager mResourceManager;

	@SuppressWarnings("deprecation")
	@Override
	public EngineOptions onCreateEngineOptions() {
		instance = this;
		float cameraWidth = instance.getWindowManager().getDefaultDisplay().getWidth();   // deprecated
		float cameraHeight = instance.getWindowManager().getDefaultDisplay().getHeight(); // deprecated
		
		mCamera = new SmoothCamera(0, 0, 800, 480, 0, 1500, 0);
		mLevelManager = new LevelMgr();
		

		return new EngineOptions(true, ScreenOrientation.LANDSCAPE_SENSOR, new RatioResolutionPolicy(cameraWidth, cameraHeight), mCamera);
	}

	@Override
	public boolean onKeyDown(final int pKeyCode, final KeyEvent pEvent) {
		if (pEvent.getAction() == KeyEvent.ACTION_DOWN)
		{
			switch (pKeyCode) {
				case KeyEvent.KEYCODE_BACK:
					SceneManager.getInstance().getCurrentScene().onBackKeyPressed();
					break;
				case KeyEvent.KEYCODE_MENU:
					SceneManager.getInstance().getCurrentScene().onMenuKeyPressed();
					break;
				default:
					break;
			}
		}
	    return false; 
	}

	public static MainActivity getSharedInstance() {
		return instance;
	}

	public void setCurrentScene(Scene scene) {
		mCurrentScene = scene;
		getEngine().setScene(mCurrentScene);
	}

	public void onResumeGame() {
		super.onResumeGame();
		
		final BaseScene scene = SceneManager.getInstance().getCurrentScene();
		if (scene != null) {
			if (scene instanceof GameScene && scene.hasChildScene()) {
				PauseMenuScene pms = (PauseMenuScene) scene.getChildScene();
				SceneCommon.fadeOut(pms.getRectangle(), pms.getButtons(), pms.getText());
			}
			mEngine.registerUpdateHandler(new TimerHandler(0.25f, new ITimerCallback()
	        {                      
	            @Override
	            public void onTimePassed(final TimerHandler pTimerHandler)
	            {     	
	            	if (scene.hasChildScene()) 	scene.clearChildScene();
	            }
	        }));
			
			
			
			if (GameScene.mHud != null)
				showHUD();
		}
	}

	public void onPauseGame() {
//		super.onPause();  // causes crash when pressing homebutton?
		
		if (mCurrentScene instanceof GameScene) {
			final PauseMenuScene pms = new PauseMenuScene();
			SceneCommon.fadeIn(pms.getRectangle(), pms.getButtons(), pms.getText());
			instance.getEngine().registerUpdateHandler(new TimerHandler(0.25f, new ITimerCallback() {
				@Override
				public void onTimePassed(final TimerHandler pTimerHandler) {
					mCurrentScene.setChildScene(pms, false, true, true);
					hideHUD();
				}
			}));
		}
	}
	
	private void showHUD() {
		instance.getEngine().registerUpdateHandler(new TimerHandler(0.15f, new ITimerCallback() {
			@Override
			public void onTimePassed(final TimerHandler pTimerHandler) {
				AlphaModifier am = new AlphaModifier(0.2f, 0, 1);
				am.setAutoUnregisterWhenFinished(true);
				GameScene.mHud.registerEntityModifier(am);
				GameScene.mHud.setVisible(true);
				SceneCommon.fadeInChildren(GameScene.mHud);
			}
		}));
	}
	
	private void hideHUD() {
		AlphaModifier am = new AlphaModifier(0.2f, 1, 0);
		am.setAutoUnregisterWhenFinished(true);
		GameScene.mHud.registerEntityModifier(am);
		SceneCommon.fadeOutChildren(GameScene.mHud);
		instance.getEngine().registerUpdateHandler(new TimerHandler(0.25f, new ITimerCallback() {
			@Override
			public void onTimePassed(final TimerHandler pTimerHandler) {
				GameScene.mHud.setVisible(false);
				
			}
		}));
	}

	@Override
	public void onCreateResources(OnCreateResourcesCallback pOnCreateResourcesCallback) throws Exception {
		ResourceManager.prepareManager(mEngine, this, mCamera, getVertexBufferObjectManager());
		mResourceManager = ResourceManager.getInstance();
		pOnCreateResourcesCallback.onCreateResourcesFinished();
	}
	@Override
	public void onCreateScene(OnCreateSceneCallback pOnCreateSceneCallback) throws Exception {
		SceneManager.getInstance().createSplashScene(pOnCreateSceneCallback);
	}

	@Override
	public void onPopulateScene(Scene pScene, OnPopulateSceneCallback pOnPopulateSceneCallback) throws Exception {
		mEngine.registerUpdateHandler(new TimerHandler(3f, new ITimerCallback() 
	    {
            public void onTimePassed(final TimerHandler pTimerHandler) 
            {
                mEngine.unregisterUpdateHandler(pTimerHandler);
                SceneManager.getInstance().createMenuScene();
            }
	    }));
		mLevelManager = new LevelMgr();		
	    pOnPopulateSceneCallback.onPopulateSceneFinished();   
	}
	
	
	public LevelMgr getLevelMgr() { return mLevelManager; }
	
	@Override
	protected void onDestroy() {
	    super.onDestroy();
	    //System.exit(0);
	}
	
//	@Override
//	public boolean onKeyDown(int keyCode, KeyEvent event) 
//	{  
//	    if (keyCode == KeyEvent.KEYCODE_BACK)
//	    {
//	        SceneManager.getInstance().getCurrentScene().onBackKeyPressed();
//	    }
//	    return false; 
//	}
}
