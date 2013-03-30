package topplintowers.crates;

import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.TextureRegion;

import topplintowers.ResourceManager;

public enum CrateType {
	WOOD(0), STONE(1), METAL(2), MAGNET(3), ELECTROMAGNET(4), STICKY(5), TRANSFORMER(6);
	
	private CrateType(int value) {}
	
	public static TextureRegion getTextureRegion(CrateType type) {
		TextureRegion tr = null;
		switch (type) {
			case WOOD: 			tr = ResourceManager.mWoodCrateTextureRegion; break;
			case STONE: 		tr = ResourceManager.mStoneCrateTextureRegion; break;
			case METAL: 		tr = ResourceManager.mMetalCrateTextureRegion; break;
			case MAGNET: 		tr = ResourceManager.mMagnetCrateTextureRegion; break;
			case ELECTROMAGNET: tr = ResourceManager.mElectromagnetCrateTextureRegion; break;
			case STICKY: 		tr = ResourceManager.mStickyCrateTextureRegion; break;
			case TRANSFORMER: 	tr = ResourceManager.mTransformerCrateTextureRegion; break;
			default: break;
		}
		return tr;
	}
	
	public static TextureRegion getLevelSelectTextureRegion(CrateType type) {
		TextureRegion tr = null;
		switch (type) {
			case WOOD: 			tr = ResourceManager.mLevelSelectWoodThumb; break;
			case STONE: 		tr = ResourceManager.mLevelSelectStoneThumb; break;
			case METAL: 		tr = ResourceManager.mLevelSelectMetalThumb; break;
			case MAGNET: 		tr = ResourceManager.mLevelSelectMagnetThumb; break;
			case ELECTROMAGNET:	tr = ResourceManager.mLevelSelectElectromagnetThumb; break;
			case STICKY: 		tr = ResourceManager.mLevelSelectStickyThumb; break;
			case TRANSFORMER: 	tr = ResourceManager.mLevelSelectTransformerThumb; break;
			default: break;
		}
		return tr;
	}
	
	public static CrateType getType(ITextureRegion texture) {
		CrateType type = null;
		if (texture == ResourceManager.mWoodCrateTextureRegion) {
			type = CrateType.WOOD;
		} else if (texture == ResourceManager.mStoneCrateTextureRegion) {
			type = CrateType.STONE;
		} else if (texture == ResourceManager.mMetalCrateTextureRegion) {
			type = CrateType.METAL;
		} else if (texture == ResourceManager.mMagnetCrateTextureRegion) {
			type = CrateType.MAGNET;
		} else if (texture == ResourceManager.mElectromagnetCrateTextureRegion) {
			type = CrateType.ELECTROMAGNET;
		} else if (texture == ResourceManager.mStickyCrateTextureRegion) {
			type = CrateType.STICKY;
		} else if (texture == ResourceManager.mTransformerCrateTextureRegion) {
			type = CrateType.TRANSFORMER;
		}
		return type;
	}
}

