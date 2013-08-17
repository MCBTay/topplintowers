package topplintowers.crates;

import org.andengine.entity.sprite.Sprite;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.util.constants.PhysicsConstants;
import org.andengine.opengl.texture.region.TextureRegion;
import org.andengine.util.adt.pool.GenericPool;

import topplintowers.scenes.gamescene.GameScene;

import android.util.Log;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;


public abstract class Crate {
	protected CrateType type;
	protected Sprite sprite;
	protected Body box;
	protected TextureRegion texture;
	protected float weight;
	protected GenericPool<Sprite> spritePool;
	protected boolean isBeingMoved = false;
	
	private static float size = 65;
	
	public Crate(CrateType crateType, GenericPool<Sprite> pool) {
		type = crateType;
		spritePool = pool;
	}
	
    public Crate(CrateType crateType, GenericPool<Sprite> pool, FixtureDef fd) { 
    	type = crateType;
		spritePool = pool;
		sprite = spritePool.obtainPoolItem();
		
		box = PhysicsFactory.createBoxBody(GameScene.mPhysicsWorld, sprite, BodyType.DynamicBody, fd);
		// set bullet to true for CCD.  by default CCD is not enabled for dynamic/dynamic collisions.
		// since all of my crates are dynamic, they need to have setBullet set to true for CCD
		//box.setBullet(true);
		// allows body to sleep when at rest for less overhead
		box.setSleepingAllowed(true);
		box.setAwake(true);
		
		//box.setAngularDamping(10);
		//box.setAngularVelocity(5);
		
		sprite.setUserData(box);
		sprite.setVisible(true);
		
		GameScene.mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(sprite, box, true, true));
		GameScene.getScene().getContainer().attachChild(sprite);
		GameScene.activeCrates.get(type).add(this);		
    }
    
	public Body getBox() { return box; }
	public Sprite getSprite() { return sprite; }
	public CrateType getType() { return type; }
	public static float getCrateWidth() { return size; }
	public GenericPool<Sprite> getSpritePool() { return spritePool; }
	public boolean getIsBeingMoved() { return isBeingMoved; }
	public float getSize() { return size; }

	public void setSprite(Sprite sprite) { this.sprite = sprite; }
	public void setBox(Body box) { this.box = box; }
	
	public void setPosition(float x, float y) {
		
		final float widthD2 = sprite.getWidth() / 2;
		final float heightD2 = sprite.getHeight() / 2;
		final float angle = box.getAngle();
		float newX = (x + widthD2) / PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT;
		float newY = (y + heightD2) / PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT;
		
		Log.e("topplintowers", "Setting position to " + newX + ", " + newY);
		box.setTransform(newX, newY, angle);
	}	

	public void dispose() {
		PhysicsConnector physicsConnector = GameScene.mPhysicsWorld.getPhysicsConnectorManager().findPhysicsConnectorByShape(sprite);
		GameScene.mPhysicsWorld.unregisterPhysicsConnector(physicsConnector);
		
		sprite.detachSelf();
		spritePool.recyclePoolItem(sprite);
	}
}	

