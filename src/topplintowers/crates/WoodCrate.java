package topplintowers.crates;

import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;

import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;

import topplintowers.pools.PoolManager;
import topplintowers.scenes.GameScene;

public class WoodCrate extends Crate {
	// TODO: customize this crate's density, elasticity, and friction
	private static final FixtureDef FIXTURE_DEF_WOOD = PhysicsFactory.createFixtureDef(1, 0.25f, 0.3f);  

	public WoodCrate(float x, float y) {
		type = CrateType.WOOD;
		spritePool = PoolManager.getInstance().mWoodPool;
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