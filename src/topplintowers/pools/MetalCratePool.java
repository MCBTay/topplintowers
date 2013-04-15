package topplintowers.pools;

import org.andengine.entity.sprite.Sprite;
import org.andengine.util.adt.pool.GenericPool;

import topplintowers.ResourceManager;
import topplintowers.scenes.GameScene;

public class MetalCratePool extends GenericPool<Sprite> {

	public MetalCratePool() {
		super();
	}
	
	@Override
	protected Sprite onAllocatePoolItem() {
		return new Sprite(0, 0, ResourceManager.mMetalCrateTextureRegion, GameScene.getScene().getVBOM());
	}
	
	protected void recycle(Sprite cloud) {
		this.recyclePoolItem(cloud);
	}
}

