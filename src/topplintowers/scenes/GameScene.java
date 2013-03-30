package topplintowers.scenes;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.ListIterator;

import javax.microedition.khronos.opengles.GL10;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.camera.hud.HUD;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.entity.modifier.AlphaModifier;
import org.andengine.entity.modifier.IEntityModifier;
import org.andengine.entity.modifier.MoveXModifier;
import org.andengine.entity.modifier.SequenceEntityModifier;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.Sprite;
import org.andengine.extension.physics.box2d.FixedStepPhysicsWorld;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.input.touch.TouchEvent;
import org.andengine.input.touch.detector.ScrollDetector;
import org.andengine.input.touch.detector.ScrollDetector.IScrollDetectorListener;
import org.andengine.input.touch.detector.SurfaceScrollDetector;
import org.andengine.opengl.texture.region.TextureRegion;
import org.andengine.opengl.util.GLState;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import android.hardware.SensorManager;

import com.badlogic.gdx.math.Vector2;

import topplintowers.MainActivity;
import topplintowers.Platform;
import topplintowers.ResourceManager;
import topplintowers.crates.Crate;
import topplintowers.crates.CrateType;
import topplintowers.hud.CrateContainer;
import topplintowers.hud.CrateThumbnail;
import topplintowers.hud.MyHUD;
import topplintowers.levels.Level;
import topplintowers.levels.LevelMgr;
import topplintowers.levels.Levels;
import topplintowers.scenes.SceneManager.SceneType;

public class GameScene extends BaseScene implements IOnSceneTouchListener, IScrollDetectorListener  {
	
	public static PhysicsWorld mPhysicsWorld;
	
	public Platform mPlatform;
		
	public static Hashtable<CrateType, ArrayList<Crate>> activeCrates = new Hashtable<CrateType, ArrayList<Crate>>();
    
    private SurfaceScrollDetector mScrollDetector;	
    
    private static Sprite mBottomGradient, mSky, mMidGradient, mSpace;
    private static float mBackgroundHeight;
    
    public static MyHUD mHud;
    
    private Level level;
    
    private boolean isInFreeMode = false;
    private ArrayList<Sprite> twinklingStars = new ArrayList<Sprite>();
    

	public Camera getCamera() { return camera; }
    public PhysicsWorld getPhysicsWorld() { return mPhysicsWorld; }
    public VertexBufferObjectManager getVBOM() { return vbom; }
    public static GameScene getScene() { return (GameScene)SceneManager.getInstance().getCurrentScene(); }
    
    public GameScene(Level level) {
		this.level = level;
		
		if (level.getLevelType() == Levels.FREEMODE) {
			isInFreeMode = true;
		}
		
		if (!isInFreeMode) {
			this.level.setGoal(this);
		}
		
		initializeActiveCrateList();
		
		createHUD();
	}
    
//	public GameScene(Level level) {
//		camera.setCenter(camera.getWidth()/2, camera.getHeight()/2);
//		
//		this.level = level;
//		
//		if (level.getLevelType() == Levels.FREEMODE) {
//			isInFreeMode = true;
//		}
//		
//		setOnSceneTouchListener(this);
//		this.mScrollDetector = new SurfaceScrollDetector(this);
//		
//		platform = new Platform();
//		createBackground();		
//		attachChild(platform.getSprite());
//		
//		initializeActiveCrateList();
//		
//		if (!isInFreeMode) {
//			this.level.setGoal(this);
//		}
//		
//		mHud = new MyHUD(this.isInFreeMode, this.level, this.backgroundHeight);
//		camera.setHUD(mHud);
//		
//		createStars();
//		
//		registerUpdateHandler(mActivity.mPhysicsWorld);
//		registerUpdateHandler(new TimerHandler(1f / 30.0f, true, new ITimerCallback() {
//            @Override
//            public void onTimePassed(final TimerHandler pTimerHandler) {
//            	cleaner();
//            	mHud.updateCounts();
//            }
//    	}));
//		
//		registerUpdateHandler(new TimerHandler(1.5f, true, new ITimerCallback() {
//            @Override
//            public void onTimePassed(final TimerHandler pTimerHandler) {
//            	createClouds();
//            }
//    	}));
//		
//		registerUpdateHandler(new TimerHandler(3f, true, new ITimerCallback() {
//			@Override
//			public void onTimePassed(final TimerHandler pTimerHandler) {
//				twinkleStars();
//			}
//		}));
//	}
	
	private void twinkleStars() {
		for (Sprite current : this.twinklingStars) {
			AlphaModifier am_1, am_2;
			
			float oldAlpha = current.getAlpha();
			float newAlpha = 0;
			
			if (oldAlpha >= 0.5f) {
				newAlpha = oldAlpha - 0.5f;
			} else {
				newAlpha = oldAlpha + 0.5f;
			}
			
			am_1 = new AlphaModifier(1.5f, oldAlpha, newAlpha);
			am_2 = new AlphaModifier(1.5f, newAlpha, oldAlpha);
			
			am_1.setAutoUnregisterWhenFinished(true);
			am_2.setAutoUnregisterWhenFinished(true);
			
			SequenceEntityModifier sem = new SequenceEntityModifier(am_1, am_2);
			sem.setAutoUnregisterWhenFinished(true);
			current.clearEntityModifiers();
			current.registerEntityModifier(sem);
			
		}
	}
	

	
	private Sprite drawGradient(TextureRegion tr, Sprite previous, float height){
		Sprite sprite = new Sprite(0, 0, tr, vbom) {
			@Override
		     protected void preDraw(GLState pGLState, Camera pCamera)
		     {
		            super.preDraw(pGLState, pCamera);
		            pGLState.enableDither();
		     }
		};
		
		if (height != 0) sprite.setHeight(height);
		
		float posY = 0;
		if (previous == null) { 
			posY = camera.getHeight() - sprite.getHeight();
		} else { 
			posY = previous.getY() - sprite.getHeight();// + 2;
		}
		
		sprite.setPosition(0, posY);
		
		attachChild(sprite);
		return sprite;
	}
	
	private void createClouds() {	
		int randomCloud = (int)((float)Math.random() * 6);	
		TextureRegion cloudTexture = ResourceManager.mCloudTextureRegions.get(randomCloud);
		
		//TODO: change this to use a sprite pool!
		Sprite newCloud = new Sprite(0, 0, cloudTexture, vbom);
		newCloud.setCullingEnabled(true);
		attachChild(newCloud);
		float startPosX = -newCloud.getWidth();
		float startPosY = ((float)Math.random() * -700) - 250;
		newCloud.setPosition(startPosX, startPosY);
		newCloud.setAlpha((float)Math.random());

		float randomSpeed = ((float)Math.random() * 20) + 40;
		MoveXModifier moveToRight = new MoveXModifier(randomSpeed, newCloud.getX(), camera.getWidth());
		moveToRight.setAutoUnregisterWhenFinished(true);
		newCloud.registerEntityModifier(moveToRight);
	}
	
	private void createStars() {
		for (int i = 0; i < 300; i++) {
			int randomStar = (int)((float)Math.random() * 3);
			TextureRegion starTexture = ResourceManager.mStarTextureRegions.get(randomStar);
			//TODO: change this to use a sprite pool!
			Sprite newStar = new Sprite(0, 0, starTexture, vbom);
			newStar.setCullingEnabled(true);
			attachChild(newStar);
			
			newStar.setScaleCenter(0, 0);
			
			float randomScale = ((float)Math.random() * 0.3f) + 0.2f;
			newStar.setScale(randomScale);
			
			float starPosX = ((float)Math.random() * camera.getWidth());
			float starPosY = ((float)Math.random() * -1000) - 800;
			newStar.setPosition(starPosX, starPosY);
			
			float randomAlpha = (float)Math.random();			
			newStar.setAlpha(randomAlpha);
			
			if (i % 5 == 0) {
				this.twinklingStars.add(newStar);
			}
		}
	}
	
	public float getHighestCrate() {
		float highest = 0;
		Enumeration<CrateType> crateTypes = activeCrates.keys();
		while (crateTypes.hasMoreElements()) {
			CrateType type = (CrateType) crateTypes.nextElement();
			ArrayList<Crate> currentList = activeCrates.get(type);
			int size = currentList.size();
			for (int j = 0; j < size; j++) {
				Crate currentCrate = currentList.get(j);
				if (currentCrate.getSprite().getY() > highest) { // && currentCrate.getBox().getLinearVelocity().y == 0) { 
					highest = currentCrate.getSprite().getY(); 
				}
			}
		}
		return highest;
	}
	
	private void initializeActiveCrateList() {
		//SceneCommon.deleteExistingCrates();
		for (CrateType type : CrateType.values()) {
			activeCrates.put(type, new ArrayList<Crate>());
		}
	}
	

	
	@Override
	public boolean onSceneTouchEvent(final Scene pScene, final TouchEvent pSceneTouchEvent) {	
		//right.onSceneTouchEvent(pScene, pSceneTouchEvent);
		//left.onSceneTouchEvent(pScene, pSceneTouchEvent);
		
		Enumeration<CrateType> crateTypes = activeCrates.keys();
		while (crateTypes.hasMoreElements()) {
			CrateType type = (CrateType) crateTypes.nextElement();
			ArrayList<Crate> currentList = activeCrates.get(type);
			int size = currentList.size();
			for (int j = 0; j < size; j++) {
				Crate currentCrate = currentList.get(j);
				if (currentCrate.getSprite().contains(pSceneTouchEvent.getX(), pSceneTouchEvent.getY())) {
					//currentCrate.onSceneTouchEvent(pScene, pSceneTouchEvent);
					currentCrate.onAreaTouched(pSceneTouchEvent, currentCrate.getSprite(), pSceneTouchEvent.getX(), pSceneTouchEvent.getY());
					return false;
				}
			}
		}
				
		this.mScrollDetector.onTouchEvent(pSceneTouchEvent);
		return true;
	}

	@Override
	public void onScrollStarted(ScrollDetector pScollDetector, int pPointerID, float pDistanceX, float pDistanceY) {
		Sprite scrollBar = mHud.getScrollBar();
		scrollBar.clearUpdateHandlers();
		scrollBar.clearEntityModifiers();
		scrollBar.setVisible(true);
		scrollBar.setBlendFunction(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
		IEntityModifier alphaMod = new AlphaModifier(0.1f, 1, 1);	
		int childCount = scrollBar.getChildCount();
		for (int i = 0; i < childCount; i++) {
			Sprite current = (Sprite) scrollBar.getChildByIndex(i);
			current.setBlendFunction(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
			current.registerEntityModifier(alphaMod);
		}
		scrollBar.registerEntityModifier(alphaMod);
	}

	@Override
	public void onScroll(ScrollDetector pScollDetector, int pPointerID,	float pDistanceX, float pDistanceY) {
		float screenBottom = camera.getCenterY() + camera.getHeight()/2;
		float screenTop = camera.getCenterY() - camera.getHeight()/2;
		if (screenBottom - pDistanceY < camera.getHeight() &&
			screenTop - pDistanceY > camera.getHeight() - mBackgroundHeight)
		{	
			camera.offsetCenter(0, -pDistanceY);
			mHud.updateWithScroll(pDistanceY, mBackgroundHeight);
//			mHud.updateScrollBar(pDistanceY, backgroundHeight);
		}
	}

	@Override
	public void onScrollFinished(ScrollDetector pScollDetector, int pPointerID, float pDistanceX, float pDistanceY) {
		final Sprite scrollBar = mHud.getScrollBar();
		scrollBar.registerUpdateHandler(new TimerHandler(1, false, new ITimerCallback()
		{
	        @Override
	        public void onTimePassed(final TimerHandler pTimerHandler)
	        { 	
	    		IEntityModifier alphaMod = new AlphaModifier(0.5f, 1, 0);	
	    		int childCount = scrollBar.getChildCount();
	    		for (int i = 0; i < childCount; i++) {
	    			Sprite current = (Sprite) scrollBar.getChildByIndex(i);
	    			current.registerEntityModifier(alphaMod);
	    		}
	    		scrollBar.registerEntityModifier(alphaMod);
	        }
		}));
	}
	
	public void cleaner() {
	    synchronized (this) {
	    	Enumeration<CrateType> crateTypes = activeCrates.keys();
    		while (crateTypes.hasMoreElements()) {
    			CrateType type = (CrateType) crateTypes.nextElement();
    			
    			ArrayList<Crate> currentList = activeCrates.get(type);
    			ListIterator<Crate> it = currentList.listIterator();
    			if (it.hasNext()) {
    				Crate currentCrate = it.next();
    				if (currentCrate.getSprite().getY() > camera.getHeight()) {
    					currentList.remove(currentCrate);
    					
    					int crateCount = mHud.availableCrateCounts.get(type);
    					if (crateCount == 0) {
							mHud.updateHUDWithReturningBlock(type);
    					}
    					mHud.availableCrateCounts.put(type, crateCount + 1);
    					PhysicsConnector physicsConnector = mPhysicsWorld.getPhysicsConnectorManager().findPhysicsConnectorByShape(currentCrate.getSprite());
    					mPhysicsWorld.unregisterPhysicsConnector(physicsConnector);
    					continue;
    				}
    			}
    		}
	    }
	}

	@Override
	public void createScene() {
		createPhysics();
		createBackground();
		createPlatform();
		
    	setOnSceneTouchListener(this);
		this.mScrollDetector = new SurfaceScrollDetector(this);
		
		registerUpdateHandler(new TimerHandler(1f / 30.0f, true, new ITimerCallback() {
	          @Override
	          public void onTimePassed(final TimerHandler pTimerHandler) {
	          	cleaner();
	          	mHud.updateCounts();
	          }
	  	}));
	}
	
	private void createBackground() {
	    mSky = new Sprite(0, -1120, ResourceManager.mGameBackgroundTextureRegion, vbom);
	    attachChild(mSky);
		mBackgroundHeight = 1600;
	}
	
	private void createPlatform() {
		mPlatform = new Platform(this);
		attachChild(mPlatform.getSprite());
	}
	
	private void createHUD() {
		boolean freeMode = this.level.getLevelType() == Levels.FREEMODE;
		mHud = new MyHUD(this, freeMode, this.level, mBackgroundHeight);
		camera.setHUD(mHud);
	}
	
	private void createPhysics() {
		mPhysicsWorld = new FixedStepPhysicsWorld(60, new Vector2(0, SensorManager.GRAVITY_EARTH), false);
		registerUpdateHandler(mPhysicsWorld);
	}

	@Override
	public void onBackKeyPressed() {
		Scene scene = SceneManager.getInstance().getCurrentScene();
		if (scene.hasChildScene()) {
			Scene childScene = scene.getChildScene();
			if (childScene instanceof PauseMenuScene) {
				PauseMenuScene pms = (PauseMenuScene) childScene;
				pms.onBackKeyPressed();
			}
		} else {
			SceneManager.getInstance().loadPauseScene(engine);
		}
	}

	@Override
	public SceneType getSceneType() { return SceneType.SCENE_GAME; }

	@Override
	public void disposeScene() {
		camera.setHUD(null);
		camera.setCenter(400, 240);
		//remove all game scene objs
	}

}
