package topplintowers.crates;

import org.andengine.extension.physics.box2d.PhysicsFactory;
import com.badlogic.gdx.physics.box2d.FixtureDef;

import topplintowers.pools.PoolManager;

public class StoneCrate extends Crate {
	// TODO: customize this crate's density, elasticity, and friction
	private static final FixtureDef FIXTURE_DEF_STONE = PhysicsFactory.createFixtureDef(1, 0.25f, 0.3f);  

	public StoneCrate() {
		super(CrateType.STONE, PoolManager.getInstance().mStonePool, FIXTURE_DEF_STONE);
	}
}
