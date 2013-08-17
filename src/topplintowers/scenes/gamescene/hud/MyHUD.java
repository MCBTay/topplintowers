package topplintowers.scenes.gamescene.hud;

import java.util.Iterator;
import java.util.LinkedHashMap;

import org.andengine.engine.camera.hud.HUD;
import org.andengine.entity.scene.IOnAreaTouchListener;
import org.andengine.entity.scene.ITouchArea;
import org.andengine.entity.sprite.Sprite;
import org.andengine.input.touch.TouchEvent;

import topplintowers.crates.CrateType;
import topplintowers.levels.Level;
import topplintowers.resources.ResourceManager;
import topplintowers.scenes.gamescene.GameScene;

public class MyHUD extends HUD implements IOnAreaTouchListener {
    public static HUD mHud;
    private Sprite mScrollBar;
    private CrateContainer mLeftContainer;
    private CrateContainer mRightContainer;  
    private GameScene mGameScene; 
    
    private Level mLevel;
    
    public static float mCameraOffset;
    
    public static LinkedHashMap<CrateType, Integer> mAvailableCrateCounts = new LinkedHashMap<CrateType, Integer>();
	
	public MyHUD(GameScene gameScene, boolean isInFreeMode, Level level, float backgroundHeight) {
		super();
		
		mGameScene = gameScene;
		mLevel = level;
		initializeAvailableCrateCounts(mLevel);
		
		createContainers(isInFreeMode);
		createScrollBar(backgroundHeight);
		
		
		setOnAreaTouchListener(this);
	}
	
    public CrateContainer getLeft() { return mLeftContainer; }
    public CrateContainer getRight() { return mRightContainer; }
    public Sprite getScrollBar() { return mScrollBar; }
	
    private void createScrollBar(float backgroundHeight) {
    	mScrollBar = new Sprite(0, 0, ResourceManager.mScrollBarTextureRegion, mGameScene.getVBOM());
    	
    	Sprite top = new Sprite(0, 0, ResourceManager.mScrollBarEndTextureRegion, mGameScene.getVBOM());
		Sprite bottom = new Sprite(0, 0, ResourceManager.mScrollBarEndTextureRegion, mGameScene.getVBOM());
		
		bottom.setFlippedVertical(true);	
		mScrollBar.attachChild(top);
		mScrollBar.attachChild(bottom);
		top.setPosition(top.getX(), top.getY()-3);
		bottom.setPosition(top.getX(), mScrollBar.getHeight());
	
		mScrollBar.setPosition(790, 480 - (mScrollBar.getHeight() + top.getHeight() + bottom.getHeight()));
		
		mScrollBar.setVisible(false);
		attachChild(mScrollBar);
    }
    
	private void createContainers(boolean isInFreeMode) {
		int totalThumbs = 0;
		mLeftContainer  = new CrateContainer(this, "left", isInFreeMode);
		mRightContainer = new CrateContainer(this, "right", isInFreeMode);
		mRightContainer.getSprite().setVisible(false);
		
		Iterator<CrateType> it = mAvailableCrateCounts.keySet().iterator();
		while (it.hasNext()) {
			CrateType type = it.next();
			int count = mAvailableCrateCounts.get(type);
			if (count > 0) {
				if (totalThumbs < 4)
					new CrateThumbnail(this, mLeftContainer, type);
				else {
					mRightContainer.getSprite().setVisible(true);
					new CrateThumbnail(this, mRightContainer, type);
				}
				totalThumbs++;
			}
		}
		mLeftContainer.sizeContainer();
		mLeftContainer.initializeCratePositions();
		if (totalThumbs > 4) {
			mRightContainer.sizeContainer();
			mRightContainer.initializeCratePositions();
		}	
	}
	
	public void updateCounts() {
		updateSingleContainerCounts(mLeftContainer);
		updateSingleContainerCounts(mRightContainer);
	}
	
	private void updateSingleContainerCounts(CrateContainer container) {
		for (CrateThumbnail currentThumb : container.thumbs) {
			CrateType type = currentThumb.getType();
			String newCount = mAvailableCrateCounts.get(type).toString();
			if (currentThumb.getCounterText() != null) {
				currentThumb.getCounterText().setText(newCount);
			}
		}
	}
	
	public void updateScrollBar(float distance, float backgroundHeight) {
		float scaledDistance = distance * (480/backgroundHeight);
		mScrollBar.setPosition(mScrollBar.getX(), mScrollBar.getY() - scaledDistance);		
	}
	
	
	public void updateWithScroll(float distance, float backgroundHeight) {
		updateScrollBar(distance, backgroundHeight);
		
		mCameraOffset += distance;
	}

	
	public void updateHUDWithReturningBlock(CrateType crate) {
		CrateThumbnail crateThumb = getThumbByType(crate);
		crateThumb.expandThumbnail();
		crateThumb.getParent().resizeContainer(crateThumb.getParent().getSprite().getHeight());
		crateThumb.getParent().repositionCrates();
	}
	
	private CrateThumbnail getThumbByType(CrateType crate) {
		CrateThumbnail lt = mLeftContainer.getThumbByType(crate);
		CrateThumbnail rt = mRightContainer.getThumbByType(crate);
		
		if (lt != null) return lt;
		else return rt;
	}
	
	private void initializeAvailableCrateCounts(Level level) {
		mAvailableCrateCounts = new LinkedHashMap<CrateType, Integer>();
		for (CrateType type : CrateType.values()) {
			mAvailableCrateCounts.put(type, level.getCounts().get(type));
		}
	}

	@Override
	public boolean onAreaTouched(TouchEvent pSceneTouchEvent, ITouchArea pTouchArea, float pTouchAreaLocalX, float pTouchAreaLocalY) {
		if (isVisible()) {
			// TODO:  somehow get rid of this tiny bit of dupli-code
			float pSceneTouchEventX = pSceneTouchEvent.getX();
			float pSceneTouchEventY = pSceneTouchEvent.getY();
			
			for (CrateThumbnail ct : mLeftContainer.thumbs) {
				if (ct.getSprite().contains(pSceneTouchEventX, pSceneTouchEventY)) {
					ct.onAreaTouched(pSceneTouchEvent, pTouchArea, pTouchAreaLocalX, pTouchAreaLocalY);
					return true;
				}
			}
			for (CrateThumbnail ct : mRightContainer.thumbs) {
				if (ct.getSprite().contains(pSceneTouchEventX, pSceneTouchEventY)) {
					ct.onAreaTouched(pSceneTouchEvent, pTouchArea, pTouchAreaLocalX, pTouchAreaLocalY);
					return true;
				}
			}	
		}
		return false;
	}
}
