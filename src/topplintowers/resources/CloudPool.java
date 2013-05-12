package topplintowers.resources;

import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.texture.region.TextureRegion;
import org.andengine.util.adt.pool.GenericPool;

import topplintowers.scenes.GameScene;

public class CloudPool extends GenericPool<Sprite> {

	public CloudPool() {
		super();
	}
	
	@Override
	protected Sprite onAllocatePoolItem() {
		int randomCloud = (int)((float)Math.random() * 6);	
		TextureRegion cloudTexture = ResourceManager.mCloudTextureRegions.get(randomCloud);
		Sprite newCloud = new Sprite(0, 0, cloudTexture, GameScene.getScene().getVBOM());
		return newCloud;
	}
	
	protected void recycle(Sprite cloud) {
		this.recyclePoolItem(cloud);
	}
}
