package topplintowers.crates;

import org.andengine.extension.physics.box2d.PhysicsFactory;

import topplintowers.pools.PoolManager;
import com.badlogic.gdx.physics.box2d.FixtureDef;

public class StickyCrate extends Crate {
	// FROM DERRICK: Weight: 4, Friction: 5, Elasticity: 3 sticks to any crate
	// TODO: customize this crate's density, elasticity, and friction
	private static final FixtureDef FIXTURE_DEF_STICKY = PhysicsFactory.createFixtureDef(1, 0.25f, 0.3f);  

	public StickyCrate() {
		super(CrateType.STICKY, PoolManager.getInstance().mStickyPool, FIXTURE_DEF_STICKY);
	}
}
