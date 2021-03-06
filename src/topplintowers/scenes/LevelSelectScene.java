package topplintowers.scenes;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;

import org.andengine.engine.camera.hud.HUD;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.entity.Entity;
import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.MoveYModifier;
import org.andengine.entity.primitive.Line;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.menu.MenuScene;
import org.andengine.entity.scene.menu.item.TextMenuItem;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.input.touch.TouchEvent;
import org.andengine.input.touch.detector.ClickDetector;
import org.andengine.input.touch.detector.ClickDetector.IClickDetectorListener;
import org.andengine.input.touch.detector.ScrollDetector;
import org.andengine.input.touch.detector.SurfaceScrollDetector;
import org.andengine.input.touch.detector.ScrollDetector.IScrollDetectorListener;
import org.andengine.opengl.texture.region.TextureRegion;
import org.andengine.util.color.Color;
import org.andengine.util.modifier.ease.EaseCubicOut;

import topplintowers.crates.CrateType;
import topplintowers.levels.Level;
import topplintowers.levels.LevelManager;
import topplintowers.levels.LevelManager.LevelType;
import topplintowers.resources.ResourceManager;
import topplintowers.scenes.SceneManager.SceneType;

public class LevelSelectScene extends BaseScene implements IClickDetectorListener, IScrollDetectorListener, IOnSceneTouchListener {
	
	
	private ArrayList<Sprite> mButtons; 	
	public HUD mHud;
	
	private Entity container;
	protected MoveYModifier inertiaMove;
	
    // Scrolling
    private SurfaceScrollDetector mScrollDetector;
    private ClickDetector mClickDetector;
	private float mMinY = -1100;
    private float mMaxY = 75;
    private float mCurrentY = 0;
    private int iItemClicked = -1;
    private float lastMove;
    private static float INERTIA_DURATION = 0.5f;
    private static float INERTIA_COEF = 5;
    
    private static Color dimmed = new Color(0.2f, 0.2f, 0.2f, 0.2f);

    public ArrayList<Sprite> getButtons() { return mButtons; }
    public HUD getHUD() { return mHud; }

	public void createLevelButtons() {
		container = new Entity();
		for (LevelType level : LevelType.values()) {
			if (level != LevelType.FREEMODE) {
				Sprite button = createButton(level);
				container.attachChild(button);
			}
			
		}
		createSelectedLines();
		attachChild(container);
	}
	
	public Sprite createButton(LevelType level) {
		Sprite button = createButtonBackground(level);
		createLevelText(level, button);
		createGoalText(level, button);
		createCrateThumbnailsAndCounts(level, button);
		
		if (LevelManager.LevelLocks.get(level)) {
			button.setColor(dimmed);
			Sprite lock = new Sprite(0, 0, ResourceManager.mLockTextureRegion, vbom);
			
			int childCount = button.getChildCount();
			for (int i = 0; i < childCount; i++) {
				IEntity currentChild = button.getChildByIndex(i);
				currentChild.setColor(dimmed);
			}
			
			float lockX = button.getWidth()/2 - lock.getWidth()/2;
			float lockY = button.getHeight()/2 - lock.getHeight()/2;
			lock.setPosition(lockX, lockY);
			button.attachChild(lock);
		}
		return button;
	}
	
	private Sprite createButtonBackground(final LevelType level) {
		Sprite menuButton = new Sprite(0, 0, ResourceManager.mLevelSelectButtonTextureRegion, vbom) { 
			@Override
			public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {
				//SceneCommon.deleteExistingCrates();
				mScrollDetector.onTouchEvent(pSceneTouchEvent);
				iItemClicked = level.ordinal();
				return false;
			}
		};
		registerTouchArea(menuButton);
		
		float posX = 158;
		float posY = 202;
		
		if (level != LevelType.ONE) {
			posY += 95 * (level.ordinal() - 1);
		}
		
		menuButton.setPosition(posX, posY);
		menuButton.setCullingEnabled(true);
		mButtons.add(menuButton);
		return menuButton;
	}
	
	public void createSelectedLines() {
		mHud = new HUD();
		float linePosX = mButtons.get(0).getX() - 30;
		float linePosY = camera.getHeight()/2;
		createLine(linePosX, linePosY, -75);
		
		linePosX = mButtons.get(0).getX() + mButtons.get(0).getWidthScaled() + 30;
		linePosY = camera.getHeight()/2;
		createLine(linePosX, linePosY, 75);
		camera.setHUD(mHud);
	}
	
	private void createLine(float posX, float posY, float offset) {
		Line line = new Line(0, 0, 0, 0, 5, vbom);
		line.setPosition(posX, posY, posX + offset, posY);
		line.setColor(Color.WHITE);
		mHud.attachChild(line);
	}
	
	private void createLevelText(LevelType level, Sprite button) {
		TextMenuItem text = new TextMenuItem(level.ordinal(), ResourceManager.mFont32, "Level "+((Integer)level.ordinal()).toString(), vbom);	
		button.attachChild(text);
		text.setPosition(text.getX() + 5, text.getY());
	}
	
	private void createGoalText(LevelType level, Sprite button) {
	    Level currentLevel = activity.mLevelManager.getLevel(level);
	    
	    float goalHeight = currentLevel.getGoal();
	    CharSequence goalHeightString = "Goal: " + goalHeight + " ft";
		Text text = new Text(0, 0, ResourceManager.mFont32, goalHeightString, vbom);
			
		text.setPosition(button.getWidth()- text.getWidth() - 5, 0);
		button.attachChild(text);
	}
	
	private void createCrateThumbnailsAndCounts(LevelType level, Sprite button) {
		Level currentLevel = activity.mLevelManager.getLevel(level);
		
		LinkedHashMap<CrateType, Integer> crateCounts = currentLevel.getCounts();
		Iterator<CrateType> it = crateCounts.keySet().iterator();
		Sprite previousCrate = null;
		Text previousText = null;
		
		while (it.hasNext()) {
			CrateType type = it.next();
			int count = crateCounts.get(type);
			if (count > 0) {
				TextureRegion tr = CrateType.getLevelSelectTextureRegion(type);
				Sprite crate = new Sprite(0, 0, tr, vbom);
				
				float crateX = 10;
				if (previousCrate != null && previousText != null) 
					crateX = previousCrate.getX() + previousCrate.getWidthScaled() + previousText.getWidthScaled() + 12;
				
				float crateY = button.getHeightScaled() - crate.getHeightScaled() - 3;
				crate.setPosition(crateX, crateY);
				button.attachChild(crate);
				previousCrate = crate;
				
				
				CharSequence countString = ((Integer)count).toString();
				Text countText = new Text(0, 0, ResourceManager.mFont32, countString, vbom);
				float countX = crate.getX() + crate.getWidthScaled() + 5;
				float countY = crate.getY() + crate.getHeightScaled()/2 - countText.getHeightScaled()/2;	
				countText.setPosition(countX, countY);
				button.attachChild(countText);
				previousText = countText;
			}
		}
	}

	@Override
	public void createScene() {
		Sprite newSprite = new Sprite(0, 0, ResourceManager.mBackgroundTextureRegion, vbom);
		attachChild(newSprite);
		
		Rectangle rect = new Rectangle(0, 0, 800, 480, vbom);
		rect.setColor(0,0,0,0.75f);
		attachChild(rect);
		
		mButtons = new ArrayList<Sprite>();
		createLevelButtons();
		
		this.mScrollDetector = new SurfaceScrollDetector(this);
		this.mClickDetector = new ClickDetector(this);
		this.setOnSceneTouchListener(this);
	}

	@Override
	public void onBackKeyPressed() {
		engine.registerUpdateHandler(new TimerHandler(0.2f, new ITimerCallback()
        {                      
            @Override
            public void onTimePassed(final TimerHandler pTimerHandler)
            {            	            
        	     mHud.setVisible(false);
        	     clearChildScene();
        	     SceneManager.getInstance().loadMenuScene(engine);
            }
        }));
	}
	
	@Override
	public void onMenuKeyPressed() { return; }	
	
    @Override
	public SceneType getSceneType() { return SceneType.LEVEL_SELECT; }

	@Override
	public void disposeScene() { 
		resourceManager.unloadLevelSelectGraphics();
	}

	@Override
	public boolean onSceneTouchEvent(Scene pScene, TouchEvent pSceneTouchEvent) {
		this.mScrollDetector.onTouchEvent(pSceneTouchEvent);
		this.mClickDetector.onTouchEvent(pSceneTouchEvent);
        return true;
	}

	@Override
	public void onScrollStarted(ScrollDetector pScollDetector, int pPointerID, float pDistanceX, float pDistanceY) {
		container.unregisterEntityModifier(inertiaMove);
		inertiaMove = null;
		mCurrentY = container.getY();
	}

	@Override
	public void onScroll(ScrollDetector pScollDetector, int pPointerID, float pDistanceX, float pDistanceY) {
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
		float next = mCurrentY + lastMove*INERTIA_COEF;
		if ( (next < mMinY)  ){    
			next = mMinY;  
	    }else if(next > mMaxY){  
			next = mMaxY;
	    }
		
		final float next_final = next;
				 
		inertiaMove = new MoveYModifier(INERTIA_DURATION, mCurrentY, next, EaseCubicOut.getInstance());
		inertiaMove.setAutoUnregisterWhenFinished(true);
		container.registerEntityModifier(inertiaMove);
		
		registerUpdateHandler(new TimerHandler(INERTIA_DURATION + 0.1f, new ITimerCallback() {
    		public void onTimePassed(final TimerHandler pTimerHandler) {
    			float lowest = Float.MAX_VALUE;
    			for (Sprite button : mButtons) {
    				float middle = camera.getCenterY() - next_final;
    				float current = middle - (button.getY() + button.getHeightScaled()/2);

    				if (Math.abs(current) < lowest) lowest = current;
    			}

    			container.registerEntityModifier(new MoveYModifier(0.1f, container.getY(), container.getY()+lowest));
    		}
    	}));
	}

	@Override
	public void onClick(ClickDetector pClickDetector, int pPointerID, float pSceneX, float pSceneY) {
		if (iItemClicked != -1) {
			Level newLevel = activity.mLevelManager.getLevel(LevelType.values()[iItemClicked]);
			mHud.setVisible(false);
			activity.mLevelManager.setCurrentLevel(newLevel);
			SceneManager.getInstance().loadGameScene(engine, newLevel);
		}
	}

}
