package topplintowers;

import java.util.ArrayList;

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

import android.graphics.Color;

public class ResourceManager
{
    private static final ResourceManager INSTANCE = new ResourceManager();
    
    public Engine mEngine;
    public MainActivity mActivity;
    public SmoothCamera mCamera;
    public VertexBufferObjectManager vbom;
    
	// Crate Textures
	private static BitmapTextureAtlas mWoodCrateTexture, mStoneCrateTexture, mMetalCrateTexture, mMagnetCrateTexture,
									  mElectromagnetCrateTexture, mStickyCrateTexture, mTransformerCrateTexture;
    public static TextureRegion mWoodCrateTextureRegion, mStoneCrateTextureRegion, mMetalCrateTextureRegion, mMagnetCrateTextureRegion,
    						    mElectromagnetCrateTextureRegion, mStickyCrateTextureRegion, mTransformerCrateTextureRegion;

    // HUD Textures
    public BuildableBitmapTextureAtlas mHUDTextureAtlas;
	public static TextureRegion mCrateContainerMiddleTextureRegion, mCrateContainerEndTextureRegion, mScrollBarTextureRegion, mScrollBarEndTextureRegion;
	public static Font mCrateCountFont;
    
    // Scene Textures
    public static BitmapTextureAtlas mBackgroundTexture;   //TODO delete
	public static TextureRegion mBackgroundTextureRegion;  //TODO delete
    
	private static BitmapTextureAtlas mPlatformTexture;
	public static TextureRegion mPlatformTextureRegion;
	
	// Cloud Textures
	public static ArrayList<TextureRegion> mCloudTextureRegions;
	
	private static Texture mCloudTexture1, mCloudTexture2, mCloudTexture3, mCloudTexture4, mCloudTexture5, mCloudTexture6, mCloudTexture7;
	public static TextureRegion mCloudTextureRegion1, mCloudTextureRegion2, mCloudTextureRegion3, mCloudTextureRegion4, mCloudTextureRegion5, 
								mCloudTextureRegion6, mCloudTextureRegion7;

	// Star Textures
	public static ArrayList<TextureRegion> mStarTextureRegions;
	
	private static Texture mStarTexture1, mStarTexture2, mStarTexture3;
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
	
	public static BitmapTextureAtlas mFontButtonTexture, mFontLevelSelectTexture;
	public static Font mFontButton, mFontLevelSelect;
	
	public BuildableBitmapTextureAtlas mLevelSelectCrateThumbnailsTextureAtlas;
	public static TextureRegion mLevelSelectWoodThumb, mLevelSelectStoneThumb, mLevelSelectMetalThumb, mLevelSelectMagnetThumb,
						 mLevelSelectElectromagnetThumb, mLevelSelectStickyThumb, mLevelSelectTransformerThumb;
    
    // Splash textures
	private static BitmapTextureAtlas mSplashTexture;
	public static TextureRegion mSplashTextureRegion;
   
    public static BitmapTextureAtlas mFontSplashTexture;
    public static Font mFontSplash;
    
    // Parallax textures
    public static BitmapTextureAtlas mParallaxTexture1, mParallaxTexture2, mParallaxTexture3;
    public static TextureRegion mParallaxTextureRegion1, mParallaxTextureRegion2, mParallaxTextureRegion3;
    
    // Global Font
    public static BitmapTextureAtlas mFontTitleTexture;
    public static Font mFontTitle;
    
    public static BitmapTextureAtlas mFontSmallTexture;
    public static BitmapTextureAtlas mGoalFontTexture;
    public static BitmapTextureAtlas mLoadingFontTexture;
    public static Font mFontSmall;
    public static Font mGoalFont;
    public static Font mLoadingFont;
    
    // GameScene Textures
    private BitmapTextureAtlas mGameBackgroundTexture;
    public static TextureRegion mGameBackgroundTextureRegion;
    
    //---------------------------------------------
    // CLASS LOGIC
    //---------------------------------------------

    public void loadMenuResources()
    {
        loadMenuGraphics();
        loadMenuFonts();
        loadMenuAudio();
        
    }
    
    public void loadGameResources()
    {
    	loadGameFonts();
    	loadGameTextures();  
        loadGameAudio();
        loadPauseTextures();
    }
    
    private void loadMenuGraphics()
    {
    	loadMainMenuGraphics();
    	loadLoadingSceneTextures();
    }
    
    private void loadMainMenuGraphics() {
    	mParallaxTexture1 = new BitmapTextureAtlas(mActivity.getTextureManager(), 800, 400, TextureOptions.BILINEAR);
    	mParallaxTextureRegion1 = BitmapTextureAtlasTextureRegionFactory.createFromAsset(mParallaxTexture1, mActivity, "gfx/mainmenuscene/slide1.png", 0, 0);
    	mParallaxTexture1.load();
    	
    	mParallaxTexture2 = new BitmapTextureAtlas(mActivity.getTextureManager(), 800, 400, TextureOptions.BILINEAR);
    	mParallaxTextureRegion2 = BitmapTextureAtlasTextureRegionFactory.createFromAsset(mParallaxTexture2, mActivity, "gfx/mainmenuscene/slide2.png", 0, 0);
    	mParallaxTexture2.load();
    	
    	mParallaxTexture3 = new BitmapTextureAtlas(mActivity.getTextureManager(), 800, 400, TextureOptions.BILINEAR);
    	mParallaxTextureRegion3 = BitmapTextureAtlasTextureRegionFactory.createFromAsset(mParallaxTexture3, mActivity, "gfx/mainmenuscene/slide3.png", 0, 0);
    	mParallaxTexture3.load();
    	
    	mMenuButtonTexture = new BitmapTextureAtlas(mActivity.getTextureManager(), 256, 128, TextureOptions.BILINEAR);
    	mMenuButtonTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(mMenuButtonTexture, mActivity, "gfx/button.png", 0, 0);
    	mMenuButtonTexture.load();   
    	
    	mBackgroundTexture = new BitmapTextureAtlas(mActivity.getTextureManager(), 800, 480, TextureOptions.BILINEAR);
    	mBackgroundTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(mBackgroundTexture, mActivity, "gfx/background.png", 0, 0);
    	mBackgroundTexture.load();
    }
    
    public void loadLevelSelectGraphics() { 
    	mLevelSelectButtonTexture = new BitmapTextureAtlas(mActivity.getTextureManager(), 484, 75, TextureOptions.BILINEAR);
    	mLevelSelectButtonTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(mLevelSelectButtonTexture, mActivity, "gfx/levelselect/levelSelectButton.png", 0, 0);
    	mLevelSelectButtonTexture.load();
    	
    	mLockTexture = new BitmapTextureAtlas(mActivity.getTextureManager(), 50, 50, TextureOptions.BILINEAR);
    	mLockTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(mLockTexture, mActivity, "gfx/levelselect/lock.png", 0, 0);
    	mLockTexture.load();
    	
    	mLevelSelectCrateThumbnailsTextureAtlas = new BuildableBitmapTextureAtlas(mActivity.getTextureManager(), 2056, 2056, TextureOptions.NEAREST);  // TODO resize this atlas

    	mLevelSelectWoodThumb = BitmapTextureAtlasTextureRegionFactory.createFromAsset(mLevelSelectCrateThumbnailsTextureAtlas, mActivity, "gfx/levelselect/cratethumbnails/wood.png");
    	mLevelSelectStoneThumb = BitmapTextureAtlasTextureRegionFactory.createFromAsset(mLevelSelectCrateThumbnailsTextureAtlas, mActivity, "gfx/levelselect/cratethumbnails/stone.png");
    	mLevelSelectMetalThumb = BitmapTextureAtlasTextureRegionFactory.createFromAsset(mLevelSelectCrateThumbnailsTextureAtlas, mActivity, "gfx/levelselect/cratethumbnails/metal.png");
    	mLevelSelectMagnetThumb = BitmapTextureAtlasTextureRegionFactory.createFromAsset(mLevelSelectCrateThumbnailsTextureAtlas, mActivity, "gfx/levelselect/cratethumbnails/magnet.png");
    	mLevelSelectElectromagnetThumb = BitmapTextureAtlasTextureRegionFactory.createFromAsset(mLevelSelectCrateThumbnailsTextureAtlas, mActivity, "gfx/levelselect/cratethumbnails/electromagnet.png");
    	mLevelSelectStickyThumb = BitmapTextureAtlasTextureRegionFactory.createFromAsset(mLevelSelectCrateThumbnailsTextureAtlas, mActivity, "gfx/levelselect/cratethumbnails/sticky.png");
    	mLevelSelectTransformerThumb = BitmapTextureAtlasTextureRegionFactory.createFromAsset(mLevelSelectCrateThumbnailsTextureAtlas, mActivity, "gfx/levelselect/cratethumbnails/transformer.png");

    	try {
    		mLevelSelectCrateThumbnailsTextureAtlas.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0, 1, 1));
    		mLevelSelectCrateThumbnailsTextureAtlas.load();
    	} catch (final TextureAtlasBuilderException e) {
    		Debug.e(e);
    	}
    }
    
    public void unloadLevelSelectGraphics() {
    	mLevelSelectButtonTexture.unload();
    	mLockTexture.unload();
    	mLevelSelectCrateThumbnailsTextureAtlas.unload();
    }
    
    private void loadMenuFonts() {
    	mFontButtonTexture = new BitmapTextureAtlas(mActivity.getTextureManager(), 256, 256, TextureOptions.BILINEAR);
		mFontButton = FontFactory.createFromAsset(mActivity.getFontManager(), mFontButtonTexture, mActivity.getAssets(), "LeagueGothic-Regular.otf", 48, true, Color.WHITE);
		mFontButton.load();
		
		mFontTitleTexture = new BitmapTextureAtlas(mActivity.getTextureManager(), 256, 256, TextureOptions.BILINEAR);
		mFontTitle = FontFactory.createFromAsset(mActivity.getFontManager(), mFontTitleTexture, mActivity.getAssets(), "LeagueGothic-Regular.otf", 115, true, Color.WHITE);
		mFontTitle.load();
		
		mLoadingFontTexture = new BitmapTextureAtlas(mActivity.getTextureManager(), 256, 256, TextureOptions.BILINEAR);
		mLoadingFont = FontFactory.createFromAsset(mActivity.getFontManager(),  mLoadingFontTexture, mActivity.getAssets(), "LeagueGothic-Regular.otf",  64,  true, Color.WHITE);
		mLoadingFont.load();
		
		mFontSplashTexture = new BitmapTextureAtlas(mActivity.getTextureManager(), 512, 512, TextureOptions.BILINEAR);
		mFontSplash = FontFactory.createFromAsset(mActivity.getFontManager(), mFontSplashTexture, mActivity.getAssets(), "LeagueGothic-Regular.otf", 140, true, Color.WHITE);
		mFontSplash.load();
		
		mFontLevelSelectTexture = new BitmapTextureAtlas(mActivity.getTextureManager(), 256, 256, TextureOptions.BILINEAR);
		mFontLevelSelect = FontFactory.createFromAsset(mActivity.getFontManager(), mFontLevelSelectTexture, mActivity.getAssets(), "LeagueGothic-Regular.otf", 32, true, Color.WHITE);
		mFontLevelSelect.load();
    }
    
    public void unloadMenuTextures() {
    	//mBackgroundTexture.unload();
    	mParallaxTexture1.unload();
    	mParallaxTexture2.unload();
    	mParallaxTexture3.unload();
    	// don't unload button texture as it's used in the pause scene
    }
    
    public void loadMenuTextures() {
    	mBackgroundTexture.load();
    	mParallaxTexture1.load();
    	mParallaxTexture2.load();
    	mParallaxTexture3.load();
    	mMenuButtonTexture.load();
    }
    
    private void loadMenuAudio() {
        
    }
    
    private void loadGameTextures() {
    	loadBackgroundTextures();
    	loadPlatformTexture();
    	loadCrateTextures();
    	loadHUDTextures();
    }
    
    private void loadBackgroundTextures() {
    	mGameBackgroundTexture = new BitmapTextureAtlas(mActivity.getTextureManager(), 800, 1600, TextureOptions.BILINEAR);
    	mGameBackgroundTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(mGameBackgroundTexture, mActivity, "gfx/background/background.png", 0, 0);
    	mGameBackgroundTexture.load();
    }
    
    private void loadPlatformTexture() {
    	mPlatformTexture = new BitmapTextureAtlas(mActivity.getTextureManager(), 1024, 256, TextureOptions.BILINEAR);
    	mPlatformTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(mPlatformTexture, mActivity, "gfx/platform.png", 0, 0);
    	mPlatformTexture.load();
    }
    
    private void loadCrateTextures() { 
    	mWoodCrateTexture = new BitmapTextureAtlas(mActivity.getTextureManager(), 128, 128, TextureOptions.BILINEAR);
    	mWoodCrateTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(mWoodCrateTexture, mActivity, "gfx/crates/wood.png", 0, 0);
        mWoodCrateTexture.load();
    	
        mStoneCrateTexture = new BitmapTextureAtlas(mActivity.getTextureManager(), 128, 128, TextureOptions.BILINEAR);
        mStoneCrateTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(mStoneCrateTexture, mActivity, "gfx/crates/stone.png", 0, 0);
        mStoneCrateTexture.load();
    	
        mMetalCrateTexture = new BitmapTextureAtlas(mActivity.getTextureManager(), 128, 128, TextureOptions.BILINEAR);
        mMetalCrateTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(mMetalCrateTexture, mActivity, "gfx/crates/metal.png", 0, 0);
        mMetalCrateTexture.load();
    	
        mMagnetCrateTexture = new BitmapTextureAtlas(mActivity.getTextureManager(), 128, 128, TextureOptions.BILINEAR);
        mMagnetCrateTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(mMagnetCrateTexture, mActivity, "gfx/crates/magnet.png", 0, 0);
        mMagnetCrateTexture.load();
    	
        mElectromagnetCrateTexture = new BitmapTextureAtlas(mActivity.getTextureManager(), 128, 128, TextureOptions.BILINEAR);
        mElectromagnetCrateTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(mElectromagnetCrateTexture, mActivity, "gfx/crates/electromagnet.png", 0, 0);
        mElectromagnetCrateTexture.load();
    	
        mStickyCrateTexture = new BitmapTextureAtlas(mActivity.getTextureManager(), 128, 128, TextureOptions.BILINEAR);
        mStickyCrateTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(mStickyCrateTexture, mActivity, "gfx/crates/sticky.png", 0, 0);
        mStickyCrateTexture.load();
    	
        mTransformerCrateTexture = new BitmapTextureAtlas(mActivity.getTextureManager(), 128, 128, TextureOptions.BILINEAR);
        mTransformerCrateTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(mTransformerCrateTexture, mActivity, "gfx/crates/transformer.png", 0, 0);
        mTransformerCrateTexture.load();
    }
    
    private void loadHUDTextures() { 
    	mHUDTextureAtlas = new BuildableBitmapTextureAtlas(mActivity.getTextureManager(), 560, 120, TextureOptions.NEAREST);
        
        mCrateContainerMiddleTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(mHUDTextureAtlas, mActivity, "gfx/containerMiddle.png");
    	mCrateContainerEndTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(mHUDTextureAtlas, mActivity, "gfx/containerEnd.png");
    	
    	mScrollBarTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(mHUDTextureAtlas, mActivity, "gfx/scrollMiddle.png");
    	mScrollBarEndTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(mHUDTextureAtlas, mActivity, "gfx/scrollTop.png");
    	
    	try {
    		mHUDTextureAtlas.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0, 1, 1));
    		mHUDTextureAtlas.load();
    	} catch (final TextureAtlasBuilderException e) {
    		Debug.e(e);
    	}
    }
    
    private void loadLoadingSceneTextures() {
    	mLoadingCrateTextureRegions = new ArrayList<TextureRegion>();
    	mLoadingCratesTextureAtlas = new BuildableBitmapTextureAtlas(mActivity.getTextureManager(), 175, 350);
    	
    	mLoadingWoodTextureRegion 			= BitmapTextureAtlasTextureRegionFactory.createFromAsset(mLoadingCratesTextureAtlas, mActivity, "gfx/loadingscene/wood.png");
    	mLoadingStoneTextureRegion 			= BitmapTextureAtlasTextureRegionFactory.createFromAsset(mLoadingCratesTextureAtlas, mActivity, "gfx/loadingscene/stone.png");
    	mLoadingMetalTextureRegion 			= BitmapTextureAtlasTextureRegionFactory.createFromAsset(mLoadingCratesTextureAtlas, mActivity, "gfx/loadingscene/metal.png");
    	mLoadingMagnetTextureRegion 		= BitmapTextureAtlasTextureRegionFactory.createFromAsset(mLoadingCratesTextureAtlas, mActivity, "gfx/loadingscene/magnet.png");
    	mLoadingElectromagnetTextureRegion 	= BitmapTextureAtlasTextureRegionFactory.createFromAsset(mLoadingCratesTextureAtlas, mActivity, "gfx/loadingscene/electromagnet.png");
    	mLoadingStickyTextureRegion 		= BitmapTextureAtlasTextureRegionFactory.createFromAsset(mLoadingCratesTextureAtlas, mActivity, "gfx/loadingscene/sticky.png");
    	mLoadingTransformerTextureRegion 	= BitmapTextureAtlasTextureRegionFactory.createFromAsset(mLoadingCratesTextureAtlas, mActivity, "gfx/loadingscene/transformer.png");
    	
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
    
    public void unloadGameTextures() {
    	mGameBackgroundTexture.unload();
    }
    
    public void loadPauseTextures() {
    	
    }
    
    public void unloadPauseTextures() {
    	
    }
    
    private void loadGameFonts() {
    	mFontLevelSelectTexture = new BitmapTextureAtlas(mActivity.getTextureManager(), 512, 512, TextureOptions.BILINEAR);
    	mFontLevelSelect = FontFactory.createFromAsset(mActivity.getFontManager(), mFontLevelSelectTexture, mActivity.getAssets(), "LeagueGothic-Regular.otf", 36, true, Color.WHITE);
    	mFontLevelSelect.load();
    }
    
    private void loadGameAudio() {
        
    }
    
    public void loadSplashScreen() {
    	mSplashTexture = new BitmapTextureAtlas(mActivity.getTextureManager(), 800, 480, TextureOptions.BILINEAR);
    	mSplashTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(mSplashTexture, mActivity, "gfx/splash.png", 0, 0);
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
