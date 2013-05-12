package topplintowers.scenes;

import org.andengine.engine.Engine;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.entity.scene.menu.MenuScene;
import org.andengine.entity.scene.menu.item.SpriteMenuItem;
import org.andengine.ui.IGameInterface.OnCreateSceneCallback;

import topplintowers.levels.Level;
import topplintowers.resources.ResourceManager;

public class SceneManager
{
    private BaseScene mSplashScene, mMenuScene, mGameScene, mPausedScene, mQuitPopupScene, mLevelSelectScene, mLoadingScene, mOptionsScene;
    
    private static final SceneManager INSTANCE = new SceneManager();
    private SceneType mCurrentSceneType = SceneType.SPLASH;
    private BaseScene mCurrentScene;
    private Engine mEngine = ResourceManager.getInstance().mEngine;
    
    public enum SceneType {
    	SPLASH,
    	MAIN_MENU,
    	LEVEL_SELECT,
    	PAUSED,
    	QUIT_POPUP,
    	LOADING,
    	GAME,
    	OPTIONS
    }
    
    public static SceneManager getInstance(){ return INSTANCE; }    
    public SceneType getCurrentSceneType()  { return mCurrentSceneType; }    
    public BaseScene getCurrentScene()		{ return mCurrentScene; }
    
    public void setScene(BaseScene scene)
    {
        mEngine.setScene(scene);
        mCurrentScene = scene;
        mCurrentSceneType = scene.getSceneType();
    }
    
    public void setScene(SceneType sceneType)
    {
        switch (sceneType)
        {
            case MAIN_MENU:  	setScene(mMenuScene); break;
            case GAME:       	setScene(mGameScene); break;
            case SPLASH:        setScene(mSplashScene); break;
            case LEVEL_SELECT:	setScene(mLevelSelectScene); break;
            case PAUSED:        setScene(mPausedScene); break;
            case QUIT_POPUP: 	setScene(mQuitPopupScene); break;
            case LOADING:       setScene(mLoadingScene); break;
            case OPTIONS:       setScene(mOptionsScene); break;
            default:                break;
        }
    }
    
    public void createSplashScene(OnCreateSceneCallback pOnCreateSceneCallback) {
    	ResourceManager.getInstance().loadSplashScreen();
    	mSplashScene = new SplashScene();
    	mCurrentScene = mSplashScene;
    	pOnCreateSceneCallback.onCreateSceneFinished(mSplashScene);
    }
    
    private void disposeSplashScene() {
    	ResourceManager.getInstance().unloadSplashScreen();
    	mSplashScene.disposeScene();
    	mSplashScene = null;
    }
    
    public void createMenuScene() {
    	ResourceManager.getInstance().loadMenuResources();
    	mMenuScene = new MainMenuScene();
    	mLoadingScene = new LoadingScene();
    	mQuitPopupScene = new QuitPopupScene();
    	mOptionsScene = new OptionsScene();
    	setScene(mMenuScene);
    	disposeSplashScene();
    }
    
    public void loadGameScene(final Engine mEngine, final Level level) {
    	((LoadingScene)mLoadingScene).setCrateTexture();
    	setScene(mLoadingScene);    	
    	ResourceManager.getInstance().unloadMenuTextures();
    	mEngine.registerUpdateHandler(new TimerHandler(0.1f, new ITimerCallback() {
    		public void onTimePassed(final TimerHandler pTimerHandler) {
    			mEngine.unregisterUpdateHandler(pTimerHandler);
    			ResourceManager.getInstance().loadGameResources();
    			mGameScene = new GameScene(level);
    			mPausedScene = new PauseMenuScene();
    			
    			setScene(mGameScene);
    		}
    	}));
    }
    
    public void loadMenuScene(final Engine mEngine) {
    	((LoadingScene)mLoadingScene).setCrateTexture(); 
    	BaseScene previousScene = mCurrentScene;
    	setScene(mLoadingScene);
    	
    	if (previousScene instanceof GameScene || previousScene instanceof PauseMenuScene) {
    		mGameScene.disposeScene();
    		ResourceManager.getInstance().unloadGameTextures();
    	} else if (previousScene instanceof LevelSelectScene) {
    		mLevelSelectScene.disposeScene();
    		ResourceManager.getInstance().unloadLevelSelectGraphics();
    	}    	
    	
    	mEngine.registerUpdateHandler(new TimerHandler(0.1f, new ITimerCallback() {
    		public void onTimePassed(final TimerHandler pTimerHandler) {
    			mEngine.unregisterUpdateHandler(pTimerHandler);
    			ResourceManager.getInstance().loadMenuTextures();
    			
    			for (SpriteMenuItem button : ((MainMenuScene)mMenuScene).getButtons()) {
    				if (button.getX() >= mMenuScene.camera.getWidth())
    					SceneCommon.reenableButton(button);
    			}
    			
    			MenuScene childScene = ((MainMenuScene)mMenuScene).getMenuChildScene();
    			mMenuScene.setChildScene(childScene);		
    			setScene(mMenuScene);
    		}
    	}));
    }
    
    public void loadLevelSelect(final Engine mEngine) {
    	((LoadingScene)mLoadingScene).setCrateTexture();
    	setScene(mLoadingScene);
    	mMenuScene.disposeScene();
    	ResourceManager.getInstance().unloadMenuTextures();
    	mEngine.registerUpdateHandler(new TimerHandler(0.1f, new ITimerCallback() {
    		public void onTimePassed(final TimerHandler pTimerHandler) {
    			mEngine.unregisterUpdateHandler(pTimerHandler);
    			
    			ResourceManager.getInstance().loadLevelSelectGraphics();
    			
    			mLevelSelectScene = new LevelSelectScene();
    			setScene(mLevelSelectScene);
    		}
    	}));
    }
    
    public void loadQuitPopup(final Engine mEngine) {
    	mCurrentScene.setChildScene(mQuitPopupScene);
    	QuitPopupScene qps = (QuitPopupScene) mQuitPopupScene;
    	qps.fadeIn();
    }
    
    public void loadOptionsScene(final Engine mEngine) { loadOptionsScene(mEngine, true); }
    
    public void loadOptionsScene(final Engine mEngine, boolean background) { 	
    	OptionsScene os = (OptionsScene) mOptionsScene;
    	
    	float musicVolume = os.activity.getOptions().getFloat("musicVolume", 50);
    	float fxVolume = os.activity.getOptions().getFloat("fxVolume", 50);
    	os.getMusicSlider().setValue(musicVolume);
    	os.getFXSlider().setValue(fxVolume);
    	
    	mCurrentScene.setChildScene(os);
    	os.fadeIn(background);    	
    }
    
    public void loadPauseScene(final Engine mEngine) {
    	GameScene currentScene = (GameScene)mCurrentScene;
    	currentScene.mHud.setVisible(false);
    	
    	PauseMenuScene pms = (PauseMenuScene) mPausedScene;
    	
    	pms.setBackgroundPosition(0, pms.getCamera().getCenterY() - 240);
    	pms.setTextPosition(10, pms.getCamera().getCenterY() - pms.getText().getHeight()/2);
    	
    	currentScene.setChildScene(mPausedScene, false, true, true);
    	
    	pms.fadeIn();
    }
    
	public void returnToPauseMenu(OptionsScene os) {
		final GameScene gs = (GameScene) getCurrentScene();
		PauseMenuScene ps = (PauseMenuScene) mPausedScene;
		SpriteMenuItem pauseButton = ps.getButtons().get(0);
		SceneCommon.repositionButtons(pauseButton.getWidth(), pauseButton.getHeight(),  ps.getButtons());

		os.engine.registerUpdateHandler(new TimerHandler(0.2f, new ITimerCallback()
        {                      
            @Override
            public void onTimePassed(final TimerHandler pTimerHandler) { 
            	gs.setChildScene(mPausedScene, false, true, true);
            	//fade in menu child scene?
        	}
        }));
	}
}
