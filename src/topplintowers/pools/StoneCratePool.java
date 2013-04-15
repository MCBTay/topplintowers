package topplintowers.pools;

import org.andengine.entity.sprite.Sprite;
import org.andengine.util.adt.pool.GenericPool;

import topplintowers.ResourceManager;
import topplintowers.scenes.GameScene;

public class StoneCratePool extends GenericPool<Sprite> {

	public StoneCratePool() {
		super();
	}
	
	@Override
	protected Sprite onAllocatePoolItem() {
		return new Sprite(0, 0, ResourceManager.mStoneCrateTextureRegion, GameScene.getScene().getVBOM());
	}
	
	protected void recycle(Sprite cloud) {
		this.recyclePoolItem(cloud);
	}
}

