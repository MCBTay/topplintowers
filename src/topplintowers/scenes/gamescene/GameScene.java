package topplintowers.scenes.gamescene;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedHashMap;
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
import org.andengine.extension.physics.box2d.FixedStepPhysicsWorld;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.extension.physics.box2d.util.constants.PhysicsConstants;
import org.andengine.input.touch.TouchEvent;
import org.andengine.input.touch.detector.ScrollDetector;
import org.andengine.input.touch.detector.ScrollDetector.IScrollDetectorListener;
import org.andengine.input.touch.detector.SurfaceScrollDetector;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.modifier.ease.EaseCubicOut;

import android.hardware.SensorManager;
import android.util.Log;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.joints.MouseJoint;
import com.badlogic.gdx.physics.box2d.joints.MouseJointDef;

import topplintowers.MainActivity;
import topplintowers.Platform;
import topplintowers.crates.Crate;
import topplintowers.crates.CrateType;
import topplintowers.crates.WoodCrate;
import topplintowers.levels.Level;
import topplintowers.levels.LevelManager.LevelType;
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
    public MainActivity getActivity() { return activity; }
    
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
	          		
	          		activity.getLevelMgr().unlockNextLevel();
	          	}
	          }
	  	}));
		
		registerUpdateHandler(new TimerHandler(0.1f, true, new ITimerCallback() {
	          @Override
	          public void onTimePassed(final TimerHandler pTimerHandler) {
	        	  for (int i = 0; i < activeCrates.get(CrateType.WOOD).size(); i++) {
	        		  final WoodCrate woodCrate = (WoodCrate) activeCrates.get(CrateType.WOOD).get(i);
	        		  if (woodCrate.toBeBroken() && !woodCrate.isBroken()) {
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
				Fixture fixture1 = pContact.getFixtureA();
				Fixture fixture2 = pContact.getFixtureB();
				
				if (fixture1.getUserData() == "wood" || fixture2.getUserData() == "wood") {
					
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
			    
				if (maxImpulse > 120) {
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

				boolean crateIsBeingMoved = currentCrate.getIsBeingMoved();
				if (crateIsHigher && currentCrate.getBox().isAwake() && !crateIsBeingMoved) {  
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
	
	Body groundBody;
	public void setGroundBody(Body groundBody) {
		this.groundBody = groundBody;
	}
	
	MouseJoint mjActive;
	public void setMJActive(MouseJoint mjActive) {
		this.mjActive = mjActive;
	}
	
	public MouseJoint createMouseJoint(Body box, float x, float y){
		Vector2 v = new Vector2(x, y);
		
		MouseJointDef mjd = new MouseJointDef();
		mjd.bodyA 				= groundBody; 
		mjd.bodyB 				= box;
		mjd.dampingRatio 		= 0f;
		mjd.frequencyHz 		= 100;
		mjd.maxForce 			= (float) 5000 * box.getMass();
		mjd.collideConnected	= true;
		mjd.target.set(v);
		return (MouseJoint) mPhysicsWorld.createJoint(mjd);
	}
	
	private Crate getCrateTouched(final TouchEvent pSceneTouchEvent) { 
		Crate crate = null;
		
		Enumeration<CrateType> crateTypes = activeCrates.keys();
		while (crateTypes.hasMoreElements()) {
			CrateType type = (CrateType) crateTypes.nextElement();
			ArrayList<Crate> currentList = activeCrates.get(type);
			int size = currentList.size();
			for (int j = 0; j < size; j++) {
				Crate currentCrate = currentList.get(j);
				
				float x = pSceneTouchEvent.getX();
				float y = pSceneTouchEvent.getY();
				

				
				if (currentCrate.getSprite().contains(x, y)) {
					crate = currentCrate;
					break;
				}
			}
		}
		
		return crate;
	}
	
	Crate crateBeingDragged = null;
	@Override
	public boolean onSceneTouchEvent(final Scene pScene, final TouchEvent pSceneTouchEvent) {	
		
		final Crate crate = getCrateTouched(pSceneTouchEvent);
		
		if (pSceneTouchEvent.isActionDown()) {
			Log.e("", "Clicked" + pSceneTouchEvent.getX() + ", " + pSceneTouchEvent.getY() + "...");
			Log.e("", "Clicked" + pSceneTouchEvent.getX()/32 + ", " + pSceneTouchEvent.getY()/32 + "...");
			if (crate != null) {
				float jointX = pSceneTouchEvent.getX() / PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT;
				float jointY = pSceneTouchEvent.getY() / PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT;
				Vector2 vector = new Vector2(jointX, jointY);
				
				BodyDef groundBodyDef = new BodyDef();
                groundBodyDef.position.set(vector);
                groundBody      = mPhysicsWorld.createBody(groundBodyDef);
				
				Log.e("", "Creating mouse joint at " + vector.toString() + "...");
				mjActive = createMouseJoint(crate.getBox(), jointX, jointY);
					
				crateBeingDragged = crate;
				return true;
			}
		}
		
		if (pSceneTouchEvent.isActionMove()) {
			if (mjActive != null) {
				float jointX = pSceneTouchEvent.getX() / PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT;
				float jointY = pSceneTouchEvent.getY() / PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT;
				Vector2 vector = new Vector2(jointX, jointY);
				mjActive.setTarget(vector);
				
				
				//Log.e("topplintowers", "Crate's angular velocity: " + crateBeingDragged.getBox().getAngularVelocity());
				
				return true;
			}
		}
		
		if (pSceneTouchEvent.isActionUp() || pSceneTouchEvent.isActionCancel()) {
			if (mjActive != null) {
				mPhysicsWorld.destroyJoint(mjActive);
				mPhysicsWorld.destroyBody(groundBody);
				mjActive = null;
				crateBeingDragged = null;
				
				return true;
			}
		}
		
				
		this.mScrollDetector.onTouchEvent(pSceneTouchEvent);
		return false;
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
