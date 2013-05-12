package topplintowers.scenes;

import java.util.ArrayList;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.MoveXModifier;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.AutoParallaxBackground;
import org.andengine.entity.scene.background.ParallaxBackground.ParallaxEntity;
import org.andengine.entity.scene.menu.MenuScene;
import org.andengine.entity.scene.menu.MenuScene.IOnMenuItemClickListener;
import org.andengine.entity.scene.menu.item.IMenuItem;
import org.andengine.entity.scene.menu.item.SpriteMenuItem;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.opengl.texture.region.TextureRegion;
import org.andengine.opengl.util.GLState;
import topplintowers.ResourceManager;
import topplintowers.levels.LevelMgr;
import topplintowers.levels.Levels;
import topplintowers.scenes.SceneManager.SceneType;

import com.topplintowers.R;

public class MainMenuScene extends BaseScene implements IOnMenuItemClickListener {
	private GameScene callingScene;
    private SpriteMenuItem mLevelSelectButton, mFreeModeButton, mQuitButton, mResumeButton, mOptionsButton;
    
    private ArrayList<SpriteMenuItem> mButtons;
    
    private MenuScene mMenuChildScene;
	
	public SpriteMenuItem getQuitButton() { return mQuitButton; }
	public SpriteMenuItem getLevelsButton() { return mLevelSelectButton; }
	public ArrayList<SpriteMenuItem> getButtons() { return mButtons; }
	public MenuScene getMenuChildScene() { return mMenuChildScene; }
	
	@Override
	public void createScene() {
		createBackground();
		createTitle();
		createMenuChildScene();
	}
	
	private void createBackground() {        
		Sprite backgroundSprite = new Sprite(0, 0, ResourceManager.mBackgroundTextureRegion, vbom) {
			@Override
		     protected void preDraw(GLState pGLState, Camera pCamera)
		     {
		            super.preDraw(pGLState, pCamera);
		            pGLState.enableDither();
		     }
		};
		
		final AutoParallaxBackground autoParallaxBackground = new AutoParallaxBackground(0, 0, 0, 5);
		
		TextureRegion one 	= ResourceManager.mParallaxTextureRegion1;
		TextureRegion two 	= ResourceManager.mParallaxTextureRegion2;
		TextureRegion three = ResourceManager.mParallaxTextureRegion3;
		
		Sprite oneSprite = new Sprite(0, 0, one, vbom);
		Sprite twoSprite = new Sprite(0, 0, two, vbom);
		Sprite threeSprite = new Sprite(0, 0, three, vbom);
		
		oneSprite.setPosition(0, 80);
		twoSprite.setPosition(0, 80);
		threeSprite.setPosition(0, 80);
		
		autoParallaxBackground.attachParallaxEntity(new ParallaxEntity(0.0f, backgroundSprite));
		autoParallaxBackground.attachParallaxEntity(new ParallaxEntity(-0.5f, threeSprite));
		autoParallaxBackground.attachParallaxEntity(new ParallaxEntity(-1.5f, twoSprite));
		autoParallaxBackground.attachParallaxEntity(new ParallaxEntity(-2.5f, oneSprite));
		setBackground(autoParallaxBackground);
	}
	
	private void createTitle() {		
		Text topplinText = new Text(25, 0, ResourceManager.mFont115, activity.getString(R.string.topplin), vbom);
		attachChild(topplinText);
		
		Text towersText = new Text(0, 0, ResourceManager.mFont115, activity.getString(R.string.towers), vbom);
		float newX = topplinText.getX() + topplinText.getWidth()/3;
		float newY = topplinText.getY() + topplinText.getHeight()/2 + 15;
		towersText.setPosition(newX, newY);
		attachChild(towersText);
	}
	
	private void createMenuChildScene()
	{
		mMenuChildScene = new MenuScene(camera);
		mMenuChildScene.setPosition(0, 0);
		mMenuChildScene.setBackgroundEnabled(false);
		
		mButtons = new ArrayList<SpriteMenuItem>();
		
		if (callingScene != null) {
			mResumeButton = SceneCommon.createMenuButton(mMenuChildScene, MenuButtonsEnum.RESUME, activity.getString(R.string.resume));
			mButtons.add(mResumeButton);
		}
		mLevelSelectButton = SceneCommon.createMenuButton(mMenuChildScene, MenuButtonsEnum.LEVEL_SELECT, activity.getString(R.string.levelselect));
		mButtons.add(mLevelSelectButton);
		mFreeModeButton = SceneCommon.createMenuButton(mMenuChildScene, MenuButtonsEnum.FREE_MODE, activity.getString(R.string.freemode));
		mButtons.add(mFreeModeButton);
		mOptionsButton = SceneCommon.createMenuButton(mMenuChildScene, MenuButtonsEnum.OPTIONS, activity.getString(R.string.option));
		mButtons.add(mOptionsButton);
		mQuitButton = SceneCommon.createMenuButton(mMenuChildScene, MenuButtonsEnum.QUIT, activity.getString(R.string.quit));
		mButtons.add(mQuitButton);
		
		SceneCommon.repositionButtons(mQuitButton.getWidth(), mQuitButton.getHeight(), mButtons);

		mMenuChildScene.setOnMenuItemClickListener(this);
		setChildScene(mMenuChildScene);
	}
	
	@Override
	public void onBackKeyPressed() {
		Scene scene = SceneManager.getInstance().getCurrentScene();
		if (scene.hasChildScene()) {
			Scene childScene = scene.getChildScene();
			if (childScene instanceof QuitPopupScene) {
				QuitPopupScene qps = (QuitPopupScene) childScene;
				qps.onBackKeyPressed();
			} else if (childScene instanceof OptionsScene) {
				OptionsScene os = (OptionsScene) childScene;
				os.onBackKeyPressed();
			}
		} else {
			SceneManager.getInstance().loadQuitPopup(engine);
		}
	}
	
	@Override
	public SceneType getSceneType() { return SceneType.SCENE_MAIN_MENU; }
	
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
	    	        case LEVEL_SELECT:  
	    	        	SceneManager.getInstance().loadLevelSelect(engine);
	    	        	break;
	    	        case FREE_MODE:
	            		SceneManager.getInstance().loadGameScene(engine, LevelMgr.LevelList.get(Levels.FREEMODE));
	    	        	break;
	    	        case QUIT:
	    	        	SceneManager.getInstance().loadQuitPopup(engine);    	
	    	        	break;
	    	        case OPTIONS:
	    	        	SceneManager.getInstance().loadOptionsScene(engine);
	    	        	break;
	    	        default:
						break;
            	}
            }
        }));
	    return true;
	}
	@Override
	public void onMenuKeyPressed() {

	}
}
