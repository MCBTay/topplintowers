package topplintowers;

import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.sprite.Sprite;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import topplintowers.resources.ResourceManager;
import topplintowers.scenes.GameScene;

import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

public class Platform {
	final Sprite sprite;
	public final Rectangle rectangle;
    private static final FixtureDef FIXTURE_DEF = PhysicsFactory.createFixtureDef(1, 0.1f, 0.5f);
	
	public Platform(GameScene gs) {	
		VertexBufferObjectManager vbom = gs.getVBOM();
		PhysicsWorld physicsWorld = gs.getPhysicsWorld();
			
	    sprite = new Sprite(100, 410, ResourceManager.mPlatformTextureRegion, vbom);
	    rectangle = new Rectangle(100, 410, 600, 80, vbom);
	    PhysicsFactory.createBoxBody(physicsWorld, rectangle, BodyType.StaticBody, FIXTURE_DEF);
	}
	
	public Sprite getSprite() { return sprite; }
}