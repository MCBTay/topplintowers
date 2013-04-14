package topplintowers.pools;

public class PoolManager {
	public CloudPool mCloudPool; 	
	public WoodCratePool mWoodPool;
	public StoneCratePool mStonePool;
	public MetalCratePool mMetalPool;
	public MagnetCratePool mMagnetPool;
	public ElectromagnetCratePool mElectromagnetPool;
	public StickyCratePool mStickyPool; // lol
	public TransformerCratePool mTransformerPool;
	
	private static final PoolManager INSTANCE = new PoolManager();
	
	public static PoolManager getInstance(){ return INSTANCE; }   
	
	public PoolManager() { 
		mCloudPool = new CloudPool();
		mWoodPool = new WoodCratePool();
		mStonePool = new StoneCratePool();
		mMetalPool = new MetalCratePool();
		mMagnetPool = new MagnetCratePool();
		mElectromagnetPool = new ElectromagnetCratePool();
		mStickyPool = new StickyCratePool();
		mTransformerPool = new TransformerCratePool();
	}
}
