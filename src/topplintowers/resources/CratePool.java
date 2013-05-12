package topplintowers.resources;

import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.texture.region.TextureRegion;
import org.andengine.util.adt.pool.GenericPool;

import topplintowers.scenes.GameScene;

public class CratePool extends GenericPool<Sprite> {
	private TextureRegion texture;
	
	public CratePool(TextureRegion texture) {
		super();
		
		this.texture = texture;
	}
	
	@Override
	protected Sprite onAllocatePoolItem() {
		return new Sprite(0, 0, this.texture, GameScene.getScene().getVBOM());
	}
	
	protected void recycle(Sprite cloud) {
		this.recyclePoolItem(cloud);
	}
}
