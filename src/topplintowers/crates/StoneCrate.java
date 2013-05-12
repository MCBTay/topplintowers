package topplintowers.crates;

import org.andengine.extension.physics.box2d.PhysicsFactory;
import com.badlogic.gdx.physics.box2d.FixtureDef;

import topplintowers.resources.PoolManager;

public class StoneCrate extends Crate {
	// FROM DERRICK: Weight: 8, Friction: 5, Elasticity: 0 (stone heavy stone smash?)
	// TODO: customize this crate's density, elasticity, and friction
	private static final FixtureDef FIXTURE_DEF_STONE = PhysicsFactory.createFixtureDef(200, 0.25f, 0);  

	public StoneCrate() {
		super(CrateType.STONE, PoolManager.getInstance().mStonePool, FIXTURE_DEF_STONE);
	}
}
