package topplintowers.pools;

import topplintowers.ResourceManager;

public class PoolManager {
	public CloudPool mCloudPool; 	
	public CratePool mWoodPool, mStonePool, mMetalPool, mMagnetPool,
					 mElectromagnetPool, mStickyPool, mTransformerPool;
	
	private static final PoolManager INSTANCE = new PoolManager();
	
	public static PoolManager getInstance(){ return INSTANCE; }   
	
	public PoolManager() { 
		mCloudPool = new CloudPool();
		
		mWoodPool 			= new CratePool(ResourceManager.mWoodCrateTextureRegion);
		mStonePool 			= new CratePool(ResourceManager.mStoneCrateTextureRegion);
		mMetalPool 			= new CratePool(ResourceManager.mMetalCrateTextureRegion);
		mMagnetPool 		= new CratePool(ResourceManager.mMagnetCrateTextureRegion);
		mElectromagnetPool 	= new CratePool(ResourceManager.mElectromagnetCrateTextureRegion);
		mStickyPool 		= new CratePool(ResourceManager.mStickyCrateTextureRegion);
		mTransformerPool 	= new CratePool(ResourceManager.mTransformerCrateTextureRegion);
	}
}
