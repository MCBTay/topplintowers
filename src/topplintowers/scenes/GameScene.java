package topplintowers.scenes;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.ListIterator;

import javax.microedition.khronos.opengles.GL10;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.entity.Entity;
import org.andengine.entity.modifier.AlphaModifier;
import org.andengine.entity.modifier.IEntityModifier;
import org.andengine.entity.modifier.MoveXModifier;
import org.andengine.entity.modifier.MoveYModifier;
import org.andengine.entity.modifier.SequenceEntityModifier;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.Background;
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
import org.andengine.util.color.Color;
import org.andengine.util.modifier.ease.EaseCubicOut;

import android.hardware.SensorManager;
import android.opengl.GLES20;

import com.badlogic.gdx.math.Vector2;

import topplintowers.Platform;
import topplintowers.ResourceManager;
import topplintowers.crates.Crate;
import topplintowers.crates.CrateType;
import topplintowers.crates.WoodCrate;
import topplintowers.hud.MyHUD;
import topplintowers.levels.Level;
import topplintowers.levels.Levels;
import topplintowers.pools.CloudPool;
import topplintowers.pools.PoolManager;
import topplintowers.scenes.SceneManager.SceneType;

public class GameScene extends BaseScene implements IOnSceneTouchListener, IScrollDetectorListener  {
	
	public static PhysicsWorld mPhysicsWorld;
	
	public Platform mPlatform;
	private Entity container;
		
	public static Hashtable<CrateType, ArrayList<Crate>> activeCrates = new Hashtable<CrateType, ArrayList<Crate>>();
    
    private static Sprite mBackgroundTop, mBackgroundMiddle, mBackgroundBottom;
    private static float mBackgroundHeight;
    private Color mBackgroundColor;
    
    public static MyHUD mHud;
    
    private Level level;
    
    //private boolean isInFreeMode = false;
    private ArrayList<Sprite> twinklingStars;
    
    // Scrolling
    private SurfaceScrollDetector mScrollDetector;
	private float mMinY = 0;
    private float mMaxY = 3200;
    private float mCurrentY = 0;
    private float lastMove;
    private static float INERTIA_DURATION = 0.5f;
    private static float INERTIA_COEF = 5;
    private MoveYModifier inertiaMove;
    
    private ArrayList<Sprite> mActiveCloudList;

	public Camera getCamera() { return camera; }
    public PhysicsWorld getPhysicsWorld() { return mPhysicsWorld; }
    public VertexBufferObjectManager getVBOM() { return vbom; }
    public static GameScene getScene() { return (GameScene)SceneManager.getInstance().getCurrentScene(); }
    public Entity getContainer() { return container; }
    
    public GameScene(Level level) {
		this.level = level;
		
		if (level.getLevelType() != Levels.FREEMODE) {
			level.setGoal(this);
		}
		
		initializeActiveCrateList();	
		createHUD();
	}
	
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
	
	private void createClouds() {	
		Sprite newCloud = PoolManager.getInstance().mCloudPool.obtainPoolItem();
		
		newCloud.setCullingEnabled(true);
		container.attachChild(newCloud);
		
		float startPosX = -newCloud.getWidth();
		float startPosY = ((float)Math.random() * -700) - 600;
		newCloud.setPosition(startPosX, startPosY);
		
		newCloud.setAlpha((float)Math.random());

		float randomSpeed = ((float)Math.random() * 20) + 40;
		MoveXModifier moveToRight = new MoveXModifier(randomSpeed, newCloud.getX(), camera.getWidth());
		moveToRight.setAutoUnregisterWhenFinished(true);
		newCloud.registerEntityModifier(moveToRight);
		
		mActiveCloudList.add(newCloud);
	}
	
	private void createStars() {
		twinklingStars = new ArrayList<Sprite>();
		for (int i = 0; i < 300; i++) {
			int randomStar = (int)((float)Math.random() * 3);
			TextureRegion starTexture = ResourceManager.mStarTextureRegions.get(randomStar);
			Sprite newStar = new Sprite(0, 0, starTexture, vbom);
			newStar.setCullingEnabled(true);
			container.attachChild(newStar);
			
			newStar.setScaleCenter(0, 0);
			
			float randomScale = ((float)Math.random() * 0.3f) + 0.2f;
			newStar.setScale(randomScale);
			
			float starPosX = ((float)Math.random() * camera.getWidth());
			float starPosY = ((float)Math.random() * -2500) - 2000;
			newStar.setPosition(starPosX, starPosY);
			
			float randomAlpha = (float)Math.random();			
			newStar.setAlpha(randomAlpha);
			
			if (i % 5 == 0) {
				twinklingStars.add(newStar);
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
				if (currentCrate.getSprite().getY() > highest) {  
					highest = currentCrate.getSprite().getY(); 
				}
			}
		}
		return highest;
	}
	
	private void initializeActiveCrateList() {
		for (CrateType type : CrateType.values()) {
			activeCrates.put(type, new ArrayList<Crate>());
		}
	}
	
	@Override
	public boolean onSceneTouchEvent(final Scene pScene, final TouchEvent pSceneTouchEvent) {	
		Enumeration<CrateType> crateTypes = activeCrates.keys();
		while (crateTypes.hasMoreElements()) {
			CrateType type = (CrateType) crateTypes.nextElement();
			ArrayList<Crate> currentList = activeCrates.get(type);
			int size = currentList.size();
			for (int j = 0; j < size; j++) {
				Crate currentCrate = currentList.get(j);
				if (currentCrate.getSprite().contains(pSceneTouchEvent.getX(), pSceneTouchEvent.getY())) {
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
		
		container.unregisterEntityModifier(inertiaMove);
		inertiaMove = null;
		mCurrentY = container.getY();
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
		
		lastMove = pDistanceY;
		
        //Return if ends are reached
		float next = mCurrentY + pDistanceY;
		
		if (next < mMinY) 	  next = mMinY;  
		else if(next > mMaxY) next = mMaxY;
        
        //Center camera to the current point
		mCurrentY = next;
        container.setPosition(0, mCurrentY);
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
		
		float next = mCurrentY + lastMove*INERTIA_COEF;
		if ( (next < mMinY)  ){    
			next = mMinY;  
	    }else if(next > mMaxY){  
			next = mMaxY;
	    }
		
		inertiaMove = new MoveYModifier(INERTIA_DURATION, mCurrentY, next, EaseCubicOut.getInstance());
		inertiaMove.setAutoUnregisterWhenFinished(true);
		container.registerEntityModifier(inertiaMove);
	}
	
	public void cleaner() {
	    synchronized (this) {
	    	cleanClouds();
	    	cleanCrates();
	    }
	}
	
	private void cleanClouds() {
		cleanClouds(false);
	}
	
	private void cleanClouds(boolean dispose) {
    	Iterator<Sprite> it = mActiveCloudList.iterator();
    	
    	while (it.hasNext()) {
    		Sprite currentCloud = it.next();
    		
    		if (dispose || currentCloud.getX() >= 800) {
    			container.detachChild(currentCloud);
    			PoolManager.getInstance().mCloudPool.recyclePoolItem(currentCloud);
    			mActiveCloudList.remove(currentCloud);
    		}
    	}
	}

	private void cleanCrates() {
		cleanCrates(false);
	}
	
	private void cleanCrates(boolean dispose) {
		LinkedHashMap<CrateType, Integer> crateCounts = MyHUD.mAvailableCrateCounts;
		
		Iterator<CrateType> it = crateCounts.keySet().iterator();
		
		while (it.hasNext()) {
			CrateType type = it.next();
			
			Iterator<Crate> crateIt = activeCrates.get(type).iterator();
			while (crateIt.hasNext()) {
				Crate currentCrate = crateIt.next();
				Sprite currentSprite = currentCrate.getSprite();
				
				if (dispose || currentSprite.getY() > camera.getHeight()) {
					activeCrates.get(type).remove(currentCrate);
					
					int crateCount = crateCounts.get(type);
					if (crateCount == 0) {
						mHud.updateHUDWithReturningBlock(type);
					}
					
					crateCounts.put(type, crateCount + 1);
					currentCrate.dispose();
				}
			}
		}
	}
	
	@Override
	public void createScene() {
		container = new Entity();
		createPhysics();
		createBackground();
		createPlatform();
		
    	setOnSceneTouchListener(this);
		this.mScrollDetector = new SurfaceScrollDetector(this);
		
		createStars();
		
		registerUpdateHandler(new TimerHandler(1f / 10.0f, true, new ITimerCallback() {
	          @Override
	          public void onTimePassed(final TimerHandler pTimerHandler) {
	          	cleaner();
	          	mHud.updateCounts();
	          }
	  	}));
		
		mActiveCloudList = new ArrayList<Sprite>();
		registerUpdateHandler(new TimerHandler(1.5f, true, new ITimerCallback() {
			@Override
			public void onTimePassed(final TimerHandler pTimerHandler) {
				//createClouds();
			}
		}));
		
		registerUpdateHandler(new TimerHandler(3f, true, new ITimerCallback() {
			@Override
			public void onTimePassed(final TimerHandler pTimerHandler) {
				twinkleStars();
			}
		}));
		attachChild(container);
	}
	
	private void createBackground() {
		mBackgroundColor = new Color(0.06667f, 0.07059f, 0.18823f, 1);
		setBackground(new Background(mBackgroundColor));
		
		mBackgroundTop = new Sprite(0, 0, ResourceManager.mGameBackgroundTopTextureRegion, vbom) {
	    	@Override
		     protected void preDraw(GLState pGLState, Camera pCamera)
		     {
		            super.preDraw(pGLState, pCamera);
		            pGLState.enableDither();
		     }
	    };
		mBackgroundMiddle = new Sprite(0, 0, ResourceManager.mGameBackgroundMiddleTextureRegion, vbom) {
	    	@Override
		     protected void preDraw(GLState pGLState, Camera pCamera)
		     {
		            super.preDraw(pGLState, pCamera);
		            pGLState.enableDither();
		     }
	    };
		mBackgroundBottom = new Sprite(0, 0, ResourceManager.mGameBackgroundBottomTextureRegion, vbom) {
	    	@Override
		     protected void preDraw(GLState pGLState, Camera pCamera)
		     {
		            super.preDraw(pGLState, pCamera);
		            pGLState.enableDither();
		     }
	    };
	    
	    mBackgroundBottom.setWidth(800);
	    mBackgroundMiddle.setWidth(800);
	    mBackgroundTop.setWidth(800);
	    
	    mBackgroundBottom.setPosition(0, -544);
	    mBackgroundMiddle.setPosition(0, mBackgroundBottom.getY() - 1024);
	    mBackgroundTop.setPosition(0, mBackgroundMiddle.getY() - 1024);
	    
	    container.attachChild(mBackgroundTop);
	    container.attachChild(mBackgroundMiddle);
	    container.attachChild(mBackgroundBottom);

		mBackgroundHeight = 3072;
	}
	
	private void createPlatform() {
		mPlatform = new Platform(this);
		container.attachChild(mPlatform.getSprite());
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
		if (hasChildScene()) {
			BaseScene childScene = (BaseScene) getChildScene();
			childScene.onBackKeyPressed();
		} else {
			SceneManager.getInstance().loadPauseScene(engine);
		}
	}
	
	@Override
	public void onMenuKeyPressed() { }

	@Override
	public SceneType getSceneType() { return SceneType.SCENE_GAME; }

	@Override
	public void disposeScene() {
		camera.setHUD(null);
		camera.setCenter(400, 240);
		
		cleanClouds(true);
		cleanCrates(true);
		//remove all game scene objs
	}


}
