package topplintowers.crates;

import org.andengine.entity.sprite.Sprite;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.opengl.texture.region.TextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import topplintowers.resources.ResourceManager;
import topplintowers.scenes.gamescene.GameScene;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

public class WoodCratePiece {
	protected Sprite sprite;
	protected Body body;
	protected VertexBufferObjectManager vbom = GameScene.getScene().getVBOM();
	
	private static final FixtureDef FIXTURE_DEF_WOOD = PhysicsFactory.createFixtureDef(1, 0.05f, 0.3f);  
	
	public WoodCratePiece(TextureRegion tr, Vector2 position, float angle) {
		this(tr, position, angle, false, null);
	}
	
	public WoodCratePiece(TextureRegion tr, Vector2 position, float angle, boolean isDiagonal, Vector2[] diagonalVertices) {
		sprite = new Sprite(0, 0, tr, vbom);
		
		if (isDiagonal)
			createDiagonalBody(sprite, position, angle, diagonalVertices);
		else
			createBoxBody(sprite, position, angle);
	}
	
	public Body getBody() { return body; }
	public Sprite getSprite() { return sprite; }

	private void createBoxBody(Sprite sprite, Vector2 position, float angle) {
		body = PhysicsFactory.createBoxBody(GameScene.mPhysicsWorld, sprite, BodyType.DynamicBody, FIXTURE_DEF_WOOD);
        GameScene.mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(sprite, body, true, true));
        body.setTransform(position, angle);
        GameScene.getScene().attachChild(sprite);
	}
	
	private void createDiagonalBody(Sprite sprite, Vector2 position, float angle, Vector2[] diagonalVertices) {
		body = PhysicsFactory.createPolygonBody(GameScene.mPhysicsWorld, sprite, diagonalVertices, BodyType.DynamicBody, FIXTURE_DEF_WOOD);
        GameScene.mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(sprite, body, true, true));
        body.setTransform(position, angle);
        GameScene.getScene().attachChild(sprite);
	}
}
