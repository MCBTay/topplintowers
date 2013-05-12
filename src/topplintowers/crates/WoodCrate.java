package topplintowers.crates;

import org.andengine.extension.physics.box2d.PhysicsFactory;

import com.badlogic.gdx.physics.box2d.FixtureDef;

import topplintowers.pools.PoolManager;

public class WoodCrate extends Crate {
	// FROM DERRICK: Weight: 2, Friction: 5, Elasticity: 1, breaks under weight 15
	// TODO: customize this crate's density, elasticity, and friction
	private static final FixtureDef FIXTURE_DEF_WOOD = PhysicsFactory.createFixtureDef(1, 0.25f, 0.3f);  

	public WoodCrate() {
		super(CrateType.WOOD, PoolManager.getInstance().mWoodPool, FIXTURE_DEF_WOOD);
	}
}
