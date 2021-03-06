package topplintowers.crates;

import org.andengine.extension.physics.box2d.PhysicsFactory;

import topplintowers.resources.PoolManager;

import com.badlogic.gdx.physics.box2d.FixtureDef;

public class MetalCrate extends Crate {
	// FROM DERRICK: Weight: 5, Friction: 3, Elasticity: 0 sticks to magnet and electromagnet
	// TODO: customize this crate's density, elasticity, and friction
	private static final FixtureDef FIXTURE_DEF_METAL = PhysicsFactory.createFixtureDef(1, 0.25f, 0.3f);  

	public MetalCrate() {
		super(CrateType.METAL, PoolManager.getInstance().mMetalPool, FIXTURE_DEF_METAL);
	}

}
