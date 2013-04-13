package topplintowers.crates;

import org.andengine.entity.sprite.Sprite;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.opengl.texture.region.TextureRegion;

import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;

import topplintowers.MainActivity;
import topplintowers.ResourceManager;
import topplintowers.pools.PoolManager;
import topplintowers.scenes.GameScene;

public class StoneCrate extends Crate {
	// TODO: customize this crate's density, elasticity, and friction
	private static final FixtureDef FIXTURE_DEF_WOOD = PhysicsFactory.createFixtureDef(1, 0.25f, 0.3f);  

	public StoneCrate(float x, float y) {
		type = CrateType.STONE;
		spritePool = PoolManager.getInstance().mStonePool;
		sprite = spritePool.obtainPoolItem();
		
		box = PhysicsFactory.createBoxBody(GameScene.mPhysicsWorld, sprite, BodyType.DynamicBody, FIXTURE_DEF_WOOD);
		box.setBullet(true);
		
		sprite.setUserData(box);
		sprite.setVisible(true);
		
		GameScene.mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(sprite, box, true, true));
		GameScene.getScene().getContainer().attachChild(sprite);
		GameScene.activeCrates.get(type).add(this);		
	}
}
