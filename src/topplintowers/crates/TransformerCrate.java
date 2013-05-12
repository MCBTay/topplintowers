package topplintowers.crates;

import org.andengine.extension.physics.box2d.PhysicsFactory;

import topplintowers.resources.PoolManager;

import com.badlogic.gdx.physics.box2d.FixtureDef;

public class TransformerCrate extends Crate {
	// FROM DERRICK: Weight: 3, Friction: 4, Elasticity: 2, opens up
	// TODO: customize this crate's density, elasticity, and friction
	private static final FixtureDef FIXTURE_DEF_TRANSFORMER = PhysicsFactory.createFixtureDef(1, 0.25f, 0.3f);  

	public TransformerCrate() {
		super(CrateType.TRANSFORMER, PoolManager.getInstance().mTransformerPool, FIXTURE_DEF_TRANSFORMER);
	}

}
