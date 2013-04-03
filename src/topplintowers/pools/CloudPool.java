package topplintowers.pools;

import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.texture.region.TextureRegion;
import org.andengine.util.adt.pool.GenericPool;

import topplintowers.ResourceManager;
import topplintowers.scenes.SceneManager;

public class CloudPool extends GenericPool<Sprite> {

	public CloudPool() {
		super();
	}
	
	@Override
	protected Sprite onAllocatePoolItem() {
		int randomCloud = (int)((float)Math.random() * 6);	
		TextureRegion cloudTexture = ResourceManager.mCloudTextureRegions.get(randomCloud);
		Sprite newCloud = new Sprite(0, 0, cloudTexture, SceneManager.getInstance().getCurrentScene().getVbom());
		return newCloud;
	}
	
	protected void recycle(Sprite cloud) {
		this.recyclePoolItem(cloud);
	}
	
	/*int randomCloud = (int)((float)Math.random() * 6);	
	TextureRegion cloudTexture = ResourceManager.mCloudTextureRegions.get(randomCloud);
	
	//TODO: change this to use a sprite pool!
	Sprite newCloud = new Sprite(0, 0, cloudTexture, vbom);*/

}
