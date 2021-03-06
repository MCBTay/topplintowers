package topplintowers.resources;

import java.io.IOException;
import java.util.ArrayList;

import org.andengine.audio.music.Music;
import org.andengine.audio.music.MusicFactory;
import org.andengine.audio.sound.Sound;
import org.andengine.audio.sound.SoundFactory;
import org.andengine.engine.Engine;
import org.andengine.engine.camera.Camera;
import org.andengine.engine.camera.SmoothCamera;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.font.FontFactory;
import org.andengine.opengl.texture.Texture;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.atlas.bitmap.BuildableBitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.source.IBitmapTextureAtlasSource;
import org.andengine.opengl.texture.atlas.buildable.builder.BlackPawnTextureAtlasBuilder;
import org.andengine.opengl.texture.atlas.buildable.builder.ITextureAtlasBuilder.TextureAtlasBuilderException;
import org.andengine.opengl.texture.region.TextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.debug.Debug;

import topplintowers.MainActivity;

import android.graphics.Color;

public class ResourceManager
{
    private static final ResourceManager INSTANCE = new ResourceManager();
    
    public Engine mEngine;
    public MainActivity mActivity;
    public SmoothCamera mCamera;
    public VertexBufferObjectManager vbom;
    
	// Crate Textures
    public BuildableBitmapTextureAtlas mCrateTextureAtlas;
    public static TextureRegion mWoodCrateTextureRegion, mStoneCrateTextureRegion, mMetalCrateTextureRegion, mMagnetCrateTextureRegion,
    						    mElectromagnetCrateTextureRegion, mStickyCrateTextureRegion, mTransformerCrateTextureRegion;
    
    public BuildableBitmapTextureAtlas mWoodCrateTextureAtlas;
    public static TextureRegion mWoodCrateTopTextureRegion, mWoodCrateBottomTextureRegion, mWoodCrateLeftTextureRegion, 
    							mWoodCrateRightTextureRegion, mWoodCrateDiagonalTextureRegion;

    // HUD Textures
    public BuildableBitmapTextureAtlas mHUDTextureAtlas;
	public static TextureRegion mCrateContainerMiddleTextureRegion, mCrateContainerEndTextureRegion, mScrollBarTextureRegion, mScrollBarEndTextureRegion;
    
    // Scene Textures
    public static BitmapTextureAtlas mBackgroundTexture;   //TODO delete
	public static TextureRegion mBackgroundTextureRegion;  //TODO delete
    
	private static BitmapTextureAtlas mPlatformTexture;
	public static TextureRegion mPlatformTextureRegion;
	
	// Cloud Textures
	public static ArrayList<TextureRegion> mCloudTextureRegions;
	public BuildableBitmapTextureAtlas mCloudTextureAtlas;
	private static TextureRegion mCloudTextureRegion1, mCloudTextureRegion2, mCloudTextureRegion3, mCloudTextureRegion4, 
								 mCloudTextureRegion5, mCloudTextureRegion6, mCloudTextureRegion7;

	// Star Textures
	public static ArrayList<TextureRegion> mStarTextureRegions;
	public BuildableBitmapTextureAtlas mStarTextureAtlas;
	private static TextureRegion mStarTextureRegion1, mStarTextureRegion2, mStarTextureRegion3;
	
	// Loading Scene Textures
	public static ArrayList<TextureRegion> mLoadingCrateTextureRegions;
	
	public BuildableBitmapTextureAtlas mLoadingCratesTextureAtlas;
	public static TextureRegion mLoadingWoodTextureRegion, mLoadingStoneTextureRegion, mLoadingMetalTextureRegion, mLoadingMagnetTextureRegion,
								mLoadingElectromagnetTextureRegion, mLoadingStickyTextureRegion, mLoadingTransformerTextureRegion;

    // Menu Textures
	private static BitmapTextureAtlas mMenuButtonTexture;
	public static TextureRegion mMenuButtonTextureRegion;
	
	
	// Level Select Textures
	private static BitmapTextureAtlas mLevelSelectButtonTexture, mLockTexture;
	public static TextureRegion mLevelSelectButtonTextureRegion, mLockTextureRegion;
	
	public BuildableBitmapTextureAtlas mLevelSelectCrateThumbnailsTextureAtlas;
	public static TextureRegion mLevelSelectWoodThumb, mLevelSelectStoneThumb, mLevelSelectMetalThumb, mLevelSelectMagnetThumb,
						 mLevelSelectElectromagnetThumb, mLevelSelectStickyThumb, mLevelSelectTransformerThumb;
    
    // Splash textures
	private static BitmapTextureAtlas mSplashTexture;
	public static TextureRegion mSplashTextureRegion;
    
    // Parallax textures
    public static BitmapTextureAtlas mParallaxTexture1, mParallaxTexture2, mParallaxTexture3;
    public static TextureRegion mParallaxTextureRegion1, mParallaxTextureRegion2, mParallaxTextureRegion3;
    
    // Global Font
    public static BitmapTextureAtlas mFont32Texture, mFont48Texture, mFont64Texture, mFont115Texture, mFont140Texture;
    public static Font mFont32, mFont48, mFont64, mFont115, mFont140;
    
    // GameScene Textures
    public BuildableBitmapTextureAtlas mGameBackgroundTextureAtlas;
    public static TextureRegion mGameBackgroundTopTextureRegion, mGameBackgroundMiddleTextureRegion, mGameBackgroundBottomTextureRegion;
    
    // Options Textures
    private BitmapTextureAtlas mSliderTexture, mSliderThumbTexture;
    public static TextureRegion mSliderTextureRegion, mSliderThumbTextureRegion;
    

    public void loadMenuResources()
    {
        loadMenuGraphics();
        loadFonts();
        SoundManager.getInstance().loadMusic(); 
    }
    
    public void loadGameResources()
    {
    	loadGameTextures();  
        loadPauseTextures();
        
        SoundManager.getInstance().loadGameAudio();
    }
    
    private void loadMenuGraphics()
    {
    	loadMainMenuGraphics();
    	loadLoadingSceneTextures();
    	loadOptionsSceneTextures();
    }
    
    private void loadMainMenuGraphics() {
    	BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/mainmenuscene/");
    	mParallaxTexture1 = new BitmapTextureAtlas(mActivity.getTextureManager(), 800, 400, TextureOptions.BILINEAR);
    	mParallaxTextureRegion1 = BitmapTextureAtlasTextureRegionFactory.createFromAsset(mParallaxTexture1, mActivity, "slide1.png", 0, 0);
    	mParallaxTexture1.load();
    	
    	mParallaxTexture2 = new BitmapTextureAtlas(mActivity.getTextureManager(), 800, 400, TextureOptions.BILINEAR);
    	mParallaxTextureRegion2 = BitmapTextureAtlasTextureRegionFactory.createFromAsset(mParallaxTexture2, mActivity, "slide2.png", 0, 0);
    	mParallaxTexture2.load();
    	
    	mParallaxTexture3 = new BitmapTextureAtlas(mActivity.getTextureManager(), 800, 400, TextureOptions.BILINEAR);
    	mParallaxTextureRegion3 = BitmapTextureAtlasTextureRegionFactory.createFromAsset(mParallaxTexture3, mActivity, "slide3.png", 0, 0);
    	mParallaxTexture3.load();
    	
    	BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
    	
    	mMenuButtonTexture = new BitmapTextureAtlas(mActivity.getTextureManager(), 256, 128, TextureOptions.BILINEAR);
    	mMenuButtonTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(mMenuButtonTexture, mActivity, "button.png", 0, 0);
    	mMenuButtonTexture.load();   
    	
    	mBackgroundTexture = new BitmapTextureAtlas(mActivity.getTextureManager(), 800, 480, TextureOptions.BILINEAR);
    	mBackgroundTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(mBackgroundTexture, mActivity, "background.png", 0, 0);
    	mBackgroundTexture.load();
    	
    	BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/levelselect/");
    	mLevelSelectButtonTexture = new BitmapTextureAtlas(mActivity.getTextureManager(), 484, 75, TextureOptions.BILINEAR);
    	mLevelSelectButtonTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(mLevelSelectButtonTexture, mActivity, "levelSelectButton.png", 0, 0);
    	mLevelSelectButtonTexture.load();
    }
    
    public void loadLevelSelectGraphics() { 
    	BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/levelselect/");
    	mLevelSelectButtonTexture = new BitmapTextureAtlas(mActivity.getTextureManager(), 484, 75, TextureOptions.BILINEAR);
    	mLevelSelectButtonTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(mLevelSelectButtonTexture, mActivity, "levelSelectButton.png", 0, 0);
    	mLevelSelectButtonTexture.load();
    	
    	mLockTexture = new BitmapTextureAtlas(mActivity.getTextureManager(), 50, 50, TextureOptions.BILINEAR);
    	mLockTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(mLockTexture, mActivity, "lock.png", 0, 0);
    	mLockTexture.load();
    	
    	BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/levelselect/cratethumbnails/");
    	mLevelSelectCrateThumbnailsTextureAtlas = new BuildableBitmapTextureAtlas(mActivity.getTextureManager(), 128, 256, TextureOptions.NEAREST);

    	mLevelSelectWoodThumb 			= BitmapTextureAtlasTextureRegionFactory.createFromAsset(mLevelSelectCrateThumbnailsTextureAtlas, mActivity, "wood.png");
    	mLevelSelectStoneThumb 			= BitmapTextureAtlasTextureRegionFactory.createFromAsset(mLevelSelectCrateThumbnailsTextureAtlas, mActivity, "stone.png");
    	mLevelSelectMetalThumb 			= BitmapTextureAtlasTextureRegionFactory.createFromAsset(mLevelSelectCrateThumbnailsTextureAtlas, mActivity, "metal.png");
    	mLevelSelectMagnetThumb 		= BitmapTextureAtlasTextureRegionFactory.createFromAsset(mLevelSelectCrateThumbnailsTextureAtlas, mActivity, "magnet.png");
    	mLevelSelectElectromagnetThumb 	= BitmapTextureAtlasTextureRegionFactory.createFromAsset(mLevelSelectCrateThumbnailsTextureAtlas, mActivity, "electromagnet.png");
    	mLevelSelectStickyThumb 		= BitmapTextureAtlasTextureRegionFactory.createFromAsset(mLevelSelectCrateThumbnailsTextureAtlas, mActivity, "sticky.png");
    	mLevelSelectTransformerThumb 	= BitmapTextureAtlasTextureRegionFactory.createFromAsset(mLevelSelectCrateThumbnailsTextureAtlas, mActivity, "transformer.png");

    	try {
    		mLevelSelectCrateThumbnailsTextureAtlas.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0, 1, 1));
    		mLevelSelectCrateThumbnailsTextureAtlas.load();
    	} catch (final TextureAtlasBuilderException e) {
    		Debug.e(e);
    	}
    }
    
    public void unloadLevelSelectGraphics() {
    	mLevelSelectButtonTexture.unload();
    	mLevelSelectButtonTextureRegion = null;
    	
    	mLevelSelectCrateThumbnailsTextureAtlas.unload();
    	mLevelSelectWoodThumb = null;
    	mLevelSelectStoneThumb = null;
    	mLevelSelectMetalThumb = null;
    	mLevelSelectMagnetThumb = null;
    	mLevelSelectElectromagnetThumb = null;
    	mLevelSelectStickyThumb = null;
    	mLevelSelectTransformerThumb = null;
    	
    	mLockTexture.unload();
    	mLockTextureRegion = null; 
    }
    
    private void loadOptionsSceneTextures() {
    	BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
    	mSliderTexture = new BitmapTextureAtlas(mActivity.getTextureManager(), 256, 32, TextureOptions.BILINEAR);
    	mSliderTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(mSliderTexture, mActivity, "slider.png", 0, 0);
    	mSliderTexture.load();
    	
    	mSliderThumbTexture = new BitmapTextureAtlas(mActivity.getTextureManager(), 32, 32, TextureOptions.BILINEAR);
    	mSliderThumbTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(mSliderThumbTexture, mActivity, "sliderThumb.png", 0, 0);
    	mSliderThumbTexture.load();
    }
    
    private void loadFonts() {
		mFont32Texture = new BitmapTextureAtlas(mActivity.getTextureManager(), 256, 256, TextureOptions.BILINEAR);
		mFont32 = FontFactory.createFromAsset(mActivity.getFontManager(), mFont32Texture, mActivity.getAssets(), "LeagueGothic-Regular.otf", 32, true, Color.WHITE);
		mFont32.load();
		
    	mFont48Texture = new BitmapTextureAtlas(mActivity.getTextureManager(), 256, 256, TextureOptions.BILINEAR);
		mFont48 = FontFactory.createFromAsset(mActivity.getFontManager(), mFont48Texture, mActivity.getAssets(), "LeagueGothic-Regular.otf", 48, true, Color.WHITE);
		mFont48.load();
		
		mFont64Texture = new BitmapTextureAtlas(mActivity.getTextureManager(), 256, 256, TextureOptions.BILINEAR);
		mFont64 = FontFactory.createFromAsset(mActivity.getFontManager(),  mFont64Texture, mActivity.getAssets(), "LeagueGothic-Regular.otf",  64,  true, Color.WHITE);
		mFont64.load();
		
		mFont115Texture = new BitmapTextureAtlas(mActivity.getTextureManager(), 256, 256, TextureOptions.BILINEAR);
		mFont115 = FontFactory.createFromAsset(mActivity.getFontManager(), mFont115Texture, mActivity.getAssets(), "LeagueGothic-Regular.otf", 115, true, Color.WHITE);
		mFont115.load();
		
		mFont140Texture = new BitmapTextureAtlas(mActivity.getTextureManager(), 512, 512, TextureOptions.BILINEAR);
		mFont140 = FontFactory.createFromAsset(mActivity.getFontManager(), mFont140Texture, mActivity.getAssets(), "LeagueGothic-Regular.otf", 140, true, Color.WHITE);
		mFont140.load();
    }
    
    public void unloadMenuTextures() {
    	mParallaxTexture1.unload();
    	mParallaxTexture2.unload();
    	mParallaxTexture3.unload();
    }
    
    public void loadMenuTextures() {
    	mBackgroundTexture.load();
    	mMenuButtonTexture.load();
    	
    	mParallaxTexture1.load();
    	mParallaxTexture2.load();
    	mParallaxTexture3.load();
    }
    
    private void loadMenuAudio() {
        
    }
    
    private void loadGameTextures() {
    	loadBackgroundTextures();
    	loadPlatformTexture();
    	loadCrateTextures();
    	loadHUDTextures();
    	loadStarTextures();
    	loadCloudTextures();
    }
    
    private void loadBackgroundTextures() {
    	BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/background/");
    	
    	mGameBackgroundTextureAtlas = new BuildableBitmapTextureAtlas(mActivity.getTextureManager(), 64, 1024, TextureOptions.NEAREST);
    	
    	mGameBackgroundTopTextureRegion		= BitmapTextureAtlasTextureRegionFactory.createFromAsset(mGameBackgroundTextureAtlas, mActivity, "background_top.png");
    	mGameBackgroundMiddleTextureRegion	= BitmapTextureAtlasTextureRegionFactory.createFromAsset(mGameBackgroundTextureAtlas, mActivity, "background_middle.png");
    	mGameBackgroundBottomTextureRegion  = BitmapTextureAtlasTextureRegionFactory.createFromAsset(mGameBackgroundTextureAtlas, mActivity, "background_bottom.png");

    	try {
    		mGameBackgroundTextureAtlas.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0, 1, 0));
    		mGameBackgroundTextureAtlas.load();
    	} catch (final TextureAtlasBuilderException e) {
    		Debug.e(e);
    	}
    }
    
    private void loadPlatformTexture() {
    	BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
    	
    	mPlatformTexture = new BitmapTextureAtlas(mActivity.getTextureManager(), 1024, 256, TextureOptions.BILINEAR);
    	mPlatformTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(mPlatformTexture, mActivity, "platform.png", 0, 0);
    	mPlatformTexture.load();
    }
    
    private void loadCrateTextures() { 
    	BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/crates/");
    	
    	mCrateTextureAtlas = new BuildableBitmapTextureAtlas(mActivity.getTextureManager(), 256, 512, TextureOptions.NEAREST);
    	
    	
    	
    	mWoodCrateTextureRegion 			= BitmapTextureAtlasTextureRegionFactory.createFromAsset(mCrateTextureAtlas, mActivity, "wood.png");
    	mStoneCrateTextureRegion 			= BitmapTextureAtlasTextureRegionFactory.createFromAsset(mCrateTextureAtlas, mActivity, "stone.png");
    	mMetalCrateTextureRegion 			= BitmapTextureAtlasTextureRegionFactory.createFromAsset(mCrateTextureAtlas, mActivity, "metal.png");
    	mMagnetCrateTextureRegion 			= BitmapTextureAtlasTextureRegionFactory.createFromAsset(mCrateTextureAtlas, mActivity, "magnet.png");
    	mElectromagnetCrateTextureRegion 	= BitmapTextureAtlasTextureRegionFactory.createFromAsset(mCrateTextureAtlas, mActivity, "electromagnet.png");
    	mStickyCrateTextureRegion 			= BitmapTextureAtlasTextureRegionFactory.createFromAsset(mCrateTextureAtlas, mActivity, "sticky.png");
    	mTransformerCrateTextureRegion 		= BitmapTextureAtlasTextureRegionFactory.createFromAsset(mCrateTextureAtlas, mActivity, "transformer.png");

    	try {
    		mCrateTextureAtlas.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0, 1, 1));
    		mCrateTextureAtlas.load();
    	} catch (final TextureAtlasBuilderException e) {
    		Debug.e(e);
    	}
    	
    	loadWoodCrateTextures();
    }
    
    private void loadWoodCrateTextures() {
    	BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/crates/woodpieces/");
    	
    	mWoodCrateTextureAtlas = new BuildableBitmapTextureAtlas(mActivity.getTextureManager(), 128, 128);
    	
    	mWoodCrateTopTextureRegion 		= BitmapTextureAtlasTextureRegionFactory.createFromAsset(mWoodCrateTextureAtlas, mActivity, "top.png");
    	mWoodCrateBottomTextureRegion  	= BitmapTextureAtlasTextureRegionFactory.createFromAsset(mWoodCrateTextureAtlas, mActivity, "bottom.png");
    	mWoodCrateLeftTextureRegion  	= BitmapTextureAtlasTextureRegionFactory.createFromAsset(mWoodCrateTextureAtlas, mActivity, "left.png");
    	mWoodCrateRightTextureRegion  	= BitmapTextureAtlasTextureRegionFactory.createFromAsset(mWoodCrateTextureAtlas, mActivity, "right.png");
    	mWoodCrateDiagonalTextureRegion	= BitmapTextureAtlasTextureRegionFactory.createFromAsset(mWoodCrateTextureAtlas, mActivity, "diagonal.png");
    	
    	try {
    		mWoodCrateTextureAtlas.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0, 1, 1));
    		mWoodCrateTextureAtlas.load();
    	} catch (final TextureAtlasBuilderException e) {
    		Debug.e(e);
    	}
    }
    
    private void loadHUDTextures() { 
    	BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
    	mHUDTextureAtlas = new BuildableBitmapTextureAtlas(mActivity.getTextureManager(), 560, 120, TextureOptions.NEAREST);
        
        mCrateContainerMiddleTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(mHUDTextureAtlas, mActivity, "containerMiddle.png");
    	mCrateContainerEndTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(mHUDTextureAtlas, mActivity, "containerEnd.png");
    	
    	mScrollBarTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(mHUDTextureAtlas, mActivity, "scrollMiddle.png");
    	mScrollBarEndTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(mHUDTextureAtlas, mActivity, "scrollTop.png");
    	
    	try {
    		mHUDTextureAtlas.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0, 1, 1));
    		mHUDTextureAtlas.load();
    	} catch (final TextureAtlasBuilderException e) {
    		Debug.e(e);
    	}
    }
    
    private void loadStarTextures() {
    	BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/background/stars/");
    	mStarTextureRegions = new ArrayList<TextureRegion>();
    	mStarTextureAtlas = new BuildableBitmapTextureAtlas(mActivity.getTextureManager(), 200, 70);
    	
    	mStarTextureRegion1	= BitmapTextureAtlasTextureRegionFactory.createFromAsset(mStarTextureAtlas, mActivity, "blue_star.png");
    	mStarTextureRegion2	= BitmapTextureAtlasTextureRegionFactory.createFromAsset(mStarTextureAtlas, mActivity, "purple_star.png");
    	mStarTextureRegion3	= BitmapTextureAtlasTextureRegionFactory.createFromAsset(mStarTextureAtlas, mActivity, "yellow_star.png");
    	
    	mStarTextureRegions.add(mStarTextureRegion1);
    	mStarTextureRegions.add(mStarTextureRegion2);
    	mStarTextureRegions.add(mStarTextureRegion3);
    	
    	try {
    		mStarTextureAtlas.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0, 1, 1));
    		mStarTextureAtlas.load();
    	} catch (final TextureAtlasBuilderException e) {
    		Debug.e(e);
    	}
    }
    
    private void loadCloudTextures() { 
    	BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/background/clouds/");
    	mCloudTextureRegions = new ArrayList<TextureRegion>();
    	mCloudTextureAtlas = new BuildableBitmapTextureAtlas(mActivity.getTextureManager(), 1032, 1032);
    	
    	mCloudTextureRegion1 = BitmapTextureAtlasTextureRegionFactory.createFromAsset(mCloudTextureAtlas, mActivity, "cloud1.png");
    	mCloudTextureRegion2 = BitmapTextureAtlasTextureRegionFactory.createFromAsset(mCloudTextureAtlas, mActivity, "cloud2.png");
    	mCloudTextureRegion3 = BitmapTextureAtlasTextureRegionFactory.createFromAsset(mCloudTextureAtlas, mActivity, "cloud3.png");
    	mCloudTextureRegion4 = BitmapTextureAtlasTextureRegionFactory.createFromAsset(mCloudTextureAtlas, mActivity, "cloud4.png");
    	mCloudTextureRegion5 = BitmapTextureAtlasTextureRegionFactory.createFromAsset(mCloudTextureAtlas, mActivity, "cloud5.png");
    	mCloudTextureRegion6 = BitmapTextureAtlasTextureRegionFactory.createFromAsset(mCloudTextureAtlas, mActivity, "cloud6.png");
    	mCloudTextureRegion7 = BitmapTextureAtlasTextureRegionFactory.createFromAsset(mCloudTextureAtlas, mActivity, "cloud7.png");
   	
    	mCloudTextureRegions.add(mCloudTextureRegion1);
    	mCloudTextureRegions.add(mCloudTextureRegion2);
    	mCloudTextureRegions.add(mCloudTextureRegion3);
    	mCloudTextureRegions.add(mCloudTextureRegion4);
    	mCloudTextureRegions.add(mCloudTextureRegion5);
    	mCloudTextureRegions.add(mCloudTextureRegion6);
    	mCloudTextureRegions.add(mCloudTextureRegion7);
    	
    	try {
    		mCloudTextureAtlas.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0, 1, 1));
    		mCloudTextureAtlas.load();
    	} catch (final TextureAtlasBuilderException e) {
    		Debug.e(e);
    	}
    }
    
    private void loadLoadingSceneTextures() {
    	BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/loadingscene/");
    	mLoadingCrateTextureRegions = new ArrayList<TextureRegion>();
    	mLoadingCratesTextureAtlas = new BuildableBitmapTextureAtlas(mActivity.getTextureManager(), 175, 350);
    	
    	mLoadingWoodTextureRegion 			= BitmapTextureAtlasTextureRegionFactory.createFromAsset(mLoadingCratesTextureAtlas, mActivity, "wood.png");
    	mLoadingStoneTextureRegion 			= BitmapTextureAtlasTextureRegionFactory.createFromAsset(mLoadingCratesTextureAtlas, mActivity, "stone.png");
    	mLoadingMetalTextureRegion 			= BitmapTextureAtlasTextureRegionFactory.createFromAsset(mLoadingCratesTextureAtlas, mActivity, "metal.png");
    	mLoadingMagnetTextureRegion 		= BitmapTextureAtlasTextureRegionFactory.createFromAsset(mLoadingCratesTextureAtlas, mActivity, "magnet.png");
    	mLoadingElectromagnetTextureRegion 	= BitmapTextureAtlasTextureRegionFactory.createFromAsset(mLoadingCratesTextureAtlas, mActivity, "electromagnet.png");
    	mLoadingStickyTextureRegion 		= BitmapTextureAtlasTextureRegionFactory.createFromAsset(mLoadingCratesTextureAtlas, mActivity, "sticky.png");
    	mLoadingTransformerTextureRegion 	= BitmapTextureAtlasTextureRegionFactory.createFromAsset(mLoadingCratesTextureAtlas, mActivity, "transformer.png");
    	
    	mLoadingCrateTextureRegions.add(mLoadingWoodTextureRegion);
    	mLoadingCrateTextureRegions.add(mLoadingStoneTextureRegion);
    	mLoadingCrateTextureRegions.add(mLoadingMetalTextureRegion);
    	mLoadingCrateTextureRegions.add(mLoadingMagnetTextureRegion);
    	mLoadingCrateTextureRegions.add(mLoadingElectromagnetTextureRegion);
    	mLoadingCrateTextureRegions.add(mLoadingStickyTextureRegion);
    	mLoadingCrateTextureRegions.add(mLoadingTransformerTextureRegion);
    	
    	try {
    		mLoadingCratesTextureAtlas.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0, 1, 1));
    		mLoadingCratesTextureAtlas.load();
    	} catch (final TextureAtlasBuilderException e) {
    		Debug.e(e);
    	}
    }
    
    public void unloadLoadingSceneTextures() {
    	mLoadingCrateTextureRegions = null;
    	
    	mLoadingCratesTextureAtlas.unload();
    	mLoadingStoneTextureRegion = null;
    	mLoadingMetalTextureRegion = null;
    	mLoadingMagnetTextureRegion = null;
    	mLoadingElectromagnetTextureRegion = null;
    	mLoadingStickyTextureRegion = null;
    	mLoadingTransformerTextureRegion = null;
    }
    
    public void unloadGameTextures() {
    	mGameBackgroundTextureAtlas.unload();
    	mGameBackgroundTopTextureRegion 	= null;
    	mGameBackgroundMiddleTextureRegion 	= null;
    	mGameBackgroundBottomTextureRegion 	= null;
    	
    	mPlatformTexture.unload();
    	mPlatformTextureRegion = null;
    	
    	mWoodCrateTextureRegion 			= null;
    	mStoneCrateTextureRegion 			= null;
    	mMetalCrateTextureRegion 			= null;
    	mMagnetCrateTextureRegion 			= null;
    	mElectromagnetCrateTextureRegion 	= null;
    	mStickyCrateTextureRegion 			= null;
    	mTransformerCrateTextureRegion 		= null;
    	
    	mHUDTextureAtlas.unload();
    	mCrateContainerMiddleTextureRegion 	= null;
    	mCrateContainerEndTextureRegion 	= null;
    	mScrollBarTextureRegion 			= null;
    	mScrollBarEndTextureRegion 			= null;
    	
    	mStarTextureAtlas.unload();
    	mStarTextureRegions = null;
    	mStarTextureRegion1 = null;
    	mStarTextureRegion2 = null;
    	mStarTextureRegion3 = null;
    	
    	//mCloudTextureAtlas.unload();
    	mCloudTextureRegions = null;
    	mCloudTextureRegion1 = null;
    	mCloudTextureRegion2 = null;
    	mCloudTextureRegion3 = null;
    	mCloudTextureRegion4 = null;
    	mCloudTextureRegion5 = null;
    	mCloudTextureRegion6 = null;
    	mCloudTextureRegion7 = null;
    }
    
    public void loadPauseTextures() {
    	// atm there really aren't pause textures that aren't already loaded
    }
    
    public void unloadPauseTextures() {
    	// atm there really aren't pause textures that aren't already loaded
    }
    
    
    public void loadSplashScreen() {
    	BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
    	mSplashTexture = new BitmapTextureAtlas(mActivity.getTextureManager(), 800, 480, TextureOptions.BILINEAR);
    	mSplashTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(mSplashTexture, mActivity, "splash.png", 0, 0);
    	mSplashTexture.load();
    }
    
    public void unloadSplashScreen() {
    	mSplashTexture.unload();
    	mSplashTextureRegion = null;
    }
    
    public static void prepareManager(Engine engine, MainActivity activity, SmoothCamera camera, VertexBufferObjectManager vbom) {
        getInstance().mEngine = engine;
        getInstance().mActivity = activity;
        getInstance().mCamera = camera;
        getInstance().vbom = vbom;
    }
       
    public static ResourceManager getInstance() { return INSTANCE; }
}
