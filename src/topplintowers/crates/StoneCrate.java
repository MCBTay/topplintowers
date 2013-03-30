package topplintowers.crates;

import org.andengine.entity.sprite.Sprite;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.opengl.texture.region.TextureRegion;

import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;

import topplintowers.MainActivity;
import topplintowers.ResourceManager;

public class StoneCrate extends Crate {
	
	private static final FixtureDef FIXTURE_DEF = PhysicsFactory.createFixtureDef(1, 0.25f, 0.3f);
	private static TextureRegion texture = ResourceManager.mStoneCrateTextureRegion;
	
	public StoneCrate(float x, float y) {
		super(x, y, texture, FIXTURE_DEF);
		super.type = CrateType.STONE;
		super.weight = 100;
	}

}
