package topplintowers.crates;

import org.andengine.extension.physics.box2d.PhysicsFactory;

import topplintowers.pools.PoolManager;
import com.badlogic.gdx.physics.box2d.FixtureDef;

public class MagnetCrate extends Crate {
	// TODO: customize this crate's density, elasticity, and friction
	private static final FixtureDef FIXTURE_DEF_MAGNET = PhysicsFactory.createFixtureDef(1, 0.25f, 0.3f);  

	public MagnetCrate() {
		super(CrateType.MAGNET, PoolManager.getInstance().mMagnetPool, FIXTURE_DEF_MAGNET);
	}
}
