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

import topplintowers.crates.CrateType;
import topplintowers.crates.WoodCrate;
import topplintowers.levels.LevelManager;
import topplintowers.resources.ResourceManager;
import topplintowers.scenes.BaseScene;
import topplintowers.scenes.PauseMenuScene;
import topplintowers.scenes.SceneCommon;
import topplintowers.scenes.SceneManager;
import topplintowers.scenes.WinScene;
import topplintowers.scenes.gamescene.GameScene;
import android.content.SharedPreferences;
import android.util.Log;
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

	public static LevelManager mLevelManager;
	
	private ResourceManager mResourceManager;
	
	
	private static SharedPreferences mOptions;

	@Override
	public EngineOptions onCreateEngineOptions() {
		instance = this;		
		mCamera = new SmoothCamera(0, 0, 800, 480, 0, 1500, 0);
		
		EngineOptions options = new EngineOptions(true, ScreenOrientation.LANDSCAPE_SENSOR, new RatioResolutionPolicy(800, 480), mCamera);
		options.getAudioOptions().setNeedsSound(true);
		options.getAudioOptions().setNeedsMusic(true);
		return options;
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
	
	@Override
	public boolean onKeyLongPress(final int pKeyCode, final KeyEvent pEvent) {
		switch (pKeyCode) {
			case KeyEvent.KEYCODE_BACK:
			case KeyEvent.KEYCODE_MENU:
				break;
			default:
				onKeyLongPress(pKeyCode, pEvent);
				break;
		}
		return false;
	}
	

	public static MainActivity getSharedInstance() { return instance; }
	
	public static SharedPreferences getOptions() { return mOptions; }

	public void setCurrentScene(Scene scene) {
		mCurrentScene = scene;
		getEngine().setScene(mCurrentScene);
	}

	public void onResumeGame() {
		Log.e("topplintowers", "resuming game");
		super.onResumeGame();
	}

	public void onPauseGame() {		
		if (SceneManager.getInstance().getCurrentScene() instanceof GameScene) {
			SceneManager.getInstance().loadPauseScene(mEngine);	
		}
		
		super.onPauseGame();
	}
	
	private void showHUD() {
		instance.getEngine().registerUpdateHandler(new TimerHandler(0.15f, new ITimerCallback() {
			@Override
			public void onTimePassed(final TimerHandler pTimerHandler) {
				AlphaModifier am = new AlphaModifier(0.2f, 0, 1);
				am.setAutoUnregisterWhenFinished(true);
				GameScene.mHud.registerEntityModifier(am);
				GameScene.mHud.setVisible(true);
				SceneCommon.fadeChildren(GameScene.mHud, 0, 1);
			}
		}));
	}
	
	private void hideHUD() {
		AlphaModifier am = new AlphaModifier(0.2f, 1, 0);
		am.setAutoUnregisterWhenFinished(true);
		GameScene.mHud.registerEntityModifier(am);
		SceneCommon.fadeChildren(GameScene.mHud, 1, 0);
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
		mOptions = getSharedPreferences("OptionsFile", MODE_PRIVATE);
		mLevelManager = new LevelManager();		
	    pOnPopulateSceneCallback.onPopulateSceneFinished();   
	}
	
	
	public LevelManager getLevelMgr() { return mLevelManager; }
	
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
