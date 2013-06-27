package topplintowers.scenes.gamescene;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Vector;

import javax.microedition.khronos.opengles.GL10;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.entity.Entity;
import org.andengine.entity.modifier.AlphaModifier;
import org.andengine.entity.modifier.IEntityModifier;
import org.andengine.entity.modifier.MoveYModifier;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.Sprite;
import org.andengine.extension.debugdraw.DebugRenderer;
import org.andengine.extension.physics.box2d.FixedStepPhysicsWorld;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.input.touch.TouchEvent;
import org.andengine.input.touch.detector.ScrollDetector;
import org.andengine.input.touch.detector.ScrollDetector.IScrollDetectorListener;
import org.andengine.input.touch.detector.SurfaceScrollDetector;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.modifier.ease.EaseCubicOut;

import android.hardware.SensorManager;
import android.os.Vibrator;
import android.util.Log;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.PolygonShape;

import topplintowers.Platform;
import topplintowers.crates.Crate;
import topplintowers.crates.CrateType;
import topplintowers.crates.WoodCrate;
import topplintowers.levels.Level;
import topplintowers.levels.LevelManager.LevelType;
import topplintowers.resources.SoundManager;
import topplintowers.scenes.BaseScene;
import topplintowers.scenes.SceneManager;
import topplintowers.scenes.SceneManager.SceneType;
import topplintowers.scenes.gamescene.hud.MyHUD;

public class GameScene extends BaseScene implements IOnSceneTouchListener, IScrollDetectorListener  {
	
	public static FixedStepPhysicsWorld mPhysicsWorld;
	
	public Platform mPlatform;
	private Entity container;
		
	public static Hashtable<CrateType, ArrayList<Crate>> activeCrates = new Hashtable<CrateType, ArrayList<Crate>>();
    
    public static MyHUD mHud;
    private GameSceneBackground mBackground;
    
    private Level level;

    // Scrolling
    private SurfaceScrollDetector mScrollDetector;
	private float mMinY = 0;
    private float mMaxY = 3200;
    private float mCurrentY = 0;
    private float lastMove;
    private static float INERTIA_DURATION = 0.5f;
    private static float INERTIA_COEF = 5;
    private MoveYModifier inertiaMove;
    
    private int completeCount = 0;
    

	public Camera getCamera() { return camera; }
    public PhysicsWorld getPhysicsWorld() { return mPhysicsWorld; }
    public VertexBufferObjectManager getVBOM() { return vbom; }
    public static GameScene getScene() { return (GameScene)SceneManager.getInstance().getCurrentScene(); }
    public Entity getContainer() { return container; }
    
	@Override
	public void createScene() {
		breakCrate = false;
		container = new Entity();
		createPhysics();
		mBackground = new GameSceneBackground(this);
		createPlatform();
		
    	setOnSceneTouchListener(this);
		this.mScrollDetector = new SurfaceScrollDetector(this);
		
		
		
		registerUpdateHandler(new TimerHandler(0.5f, true, new ITimerCallback() {
	          @Override
	          public void onTimePassed(final TimerHandler pTimerHandler) {
	          	cleaner();
	          	
	          	if (level.getLevelType() != LevelType.FREEMODE) {
	          		boolean complete = checkForLevelCompletion();
	          		
	          		if (complete) completeCount++;
	          		else completeCount = 0;
	          	}
	          	
	          	if (completeCount >= 6) {
	          		SceneManager.getInstance().loadWinScene(engine);
	          		
	          	}
	          }
	  	}));
		
		registerUpdateHandler(new TimerHandler(0.1f, true, new ITimerCallback() {
	          @Override
	          public void onTimePassed(final TimerHandler pTimerHandler) {
	        	  for (int i = 0; i < activeCrates.get(CrateType.WOOD).size(); i++) {
	        		  final WoodCrate woodCrate = (WoodCrate) activeCrates.get(CrateType.WOOD).get(i);
	        		  if (woodCrate.toBeBroken()) {
	        			  engine.runOnUpdateThread(new Runnable() {
								@Override
								public void run() {
									woodCrate.breakApart();
								}
	        			  });
	        		  }
	        	  }
	          }
		}));
		
		registerUpdateHandler(new IUpdateHandler() {

			@Override
			public void onUpdate(float pSecondsElapsed) {
				mHud.updateCounts();
				
				
			}

			@Override
			public void reset() { }
			
		});
		
		
		
		mPhysicsWorld.setContactListener(new ContactListener() {
			float maxImpulse;
			
			@Override
			public void beginContact(final Contact pContact) {
				if (pContact.isTouching()) {
					SoundManager.getInstance().playBlockCollision();
				}
			}

			@Override
			public void endContact(Contact contact) { 

			}
			
			@Override
			public void preSolve(Contact contact, Manifold oldManifold) { 
				
			}

			@Override
			public void postSolve(Contact contact, ContactImpulse impulse) {
				maxImpulse = impulse.getNormalImpulses()[0];
				for(int i = 1; i < impulse.getNormalImpulses().length; i++) {
					maxImpulse = Math.max(impulse.getNormalImpulses()[i], maxImpulse);
				}
			    
				if (maxImpulse > 80) {
					for (Crate crate : activeCrates.get(CrateType.WOOD)) {
						WoodCrate wc = (WoodCrate) crate;
						wc.setToBeBroken(true);
					}
				}
			}
			
			
			
		});
	
		attachChild(container);
		//attachChild(new DebugRenderer(mPhysicsWorld, vbom));
	}
	
	boolean breakCrate;
	
	private boolean checkForLevelCompletion() {
		float crateHeight = getHighestCrate();
		float goalHeight = this.level.getGoalLineHeight();

		if (crateHeight < goalHeight) {
			return true;
		}
		return false;
	}
	
	public float getHighestCrate() {
		float highest = camera.getHeight();
		Enumeration<CrateType> crateTypes = activeCrates.keys();
		while (crateTypes.hasMoreElements()) {
			CrateType type = (CrateType) crateTypes.nextElement();
			ArrayList<Crate> currentList = activeCrates.get(type);
			int size = currentList.size();
			for (int j = 0; j < size; j++) {
				Crate currentCrate = currentList.get(j);
				
				boolean crateIsHigher = currentCrate.getSprite().getY() < highest;
				
				Vector2 linearVelocity = currentCrate.getBox().getLinearVelocity();
				float velocityX = Math.round(linearVelocity.x);
				float velocityY = Math.round(linearVelocity.y);
				boolean crateIsMovingX = velocityX != 0.0;
				boolean crateIsMovingY = velocityY != 0.0;
				boolean crateIsMoving = crateIsMovingX && crateIsMovingY;
				//String velocityString = "["+velocityX+":"+velocityY+"]";
				//Log.i("TopplinTowers", velocityString);
				boolean crateIsBeingMoved = currentCrate.getIsBeingMoved();
				if (crateIsHigher && !crateIsMovingX && !crateIsMovingY && !crateIsBeingMoved) {  
					highest = currentCrate.getSprite().getY(); 
				}
			}
		}
		return highest;
	}
	
	private void createPlatform() {
		mPlatform = new Platform(this);
		container.attachChild(mPlatform.getSprite());
	}
	
	private void createHUD() {
		boolean freeMode = this.level.getLevelType() == LevelType.FREEMODE;
		mHud = new MyHUD(this, freeMode, this.level, mBackground.getHeight());
		camera.setHUD(mHud);
	}
	
	private void createPhysics() {
		mPhysicsWorld = new FixedStepPhysicsWorld(60, new Vector2(0, SensorManager.GRAVITY_EARTH), false);
		registerUpdateHandler(mPhysicsWorld);
	}

	@Override
	public void onBackKeyPressed() { handleKeyPress(); }
	
	@Override
	public void onMenuKeyPressed() { handleKeyPress(); }
	
	private void handleKeyPress() {
		if (hasChildScene()) {
			BaseScene childScene = (BaseScene) getChildScene();
			childScene.onBackKeyPressed();
		} else {
			SceneManager.getInstance().loadPauseScene(engine);
		}
	}

	@Override
	public SceneType getSceneType() { return SceneType.GAME; }

	@Override
	public void disposeScene() {
		camera.setHUD(null);
		camera.setCenter(400, 240);
		
		mBackground.cleanClouds(true);
		mBackground.cleanStars();
		cleanCrates(true);
	}
    
    public GameScene(Level level) {
		this.level = level;
		
		if (level.getLevelType() != LevelType.FREEMODE) {
			level.setGoal(this);
		}
		
		initializeActiveCrateList();	
		createHUD();
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
			screenTop - pDistanceY > camera.getHeight() - mBackground.getHeight())
		{	
			camera.offsetCenter(0, -pDistanceY);
			mHud.updateWithScroll(pDistanceY, mBackground.getHeight());
		}
		
		lastMove = pDistanceY;
		
        //Return if ends are reached
		float next = mCurrentY + pDistanceY;
		
		if (next < mMinY) 	  
			next = mMinY;  
		else if(next > mMaxY) 
			next = mMaxY;
        
		mCurrentY = next;
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
		//container.registerEntityModifier(inertiaMove);
	}
	
	public void cleaner() {
	    synchronized (this) {
	    	mBackground.cleanClouds();
	    	cleanCrates();
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
					crateIt.remove();
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
}
