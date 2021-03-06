package topplintowers.crates;

import org.andengine.extension.physics.box2d.PhysicsFactory;

import topplintowers.resources.PoolManager;

import com.badlogic.gdx.physics.box2d.FixtureDef;

public class ElectromagnetCrate extends Crate {
	// FROM DERRICK: Weight: 5, Friction: 3, Elasticity: 1, sticks to metal, magnets and electromagnets
	// TODO: customize this crate's density, elasticity, and friction
	private static final FixtureDef FIXTURE_DEF_ELECTROMAGNET = PhysicsFactory.createFixtureDef(1, 0.25f, 0.3f);  

	public ElectromagnetCrate() {
		super(CrateType.ELECTROMAGNET, PoolManager.getInstance().mElectromagnetPool, FIXTURE_DEF_ELECTROMAGNET);
	}
}
