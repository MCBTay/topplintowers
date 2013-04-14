package topplintowers.crates;

import org.andengine.entity.scene.IOnAreaTouchListener;
import org.andengine.entity.scene.ITouchArea;
import org.andengine.entity.sprite.Sprite;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.util.constants.PhysicsConstants;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.region.TextureRegion;
import org.andengine.util.adt.pool.GenericPool;

import topplintowers.pools.PoolManager;
import topplintowers.scenes.GameScene;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;


public abstract class Crate implements IOnAreaTouchListener {
	protected CrateType type;
	protected Sprite sprite;
	protected Body box;
	protected TextureRegion texture;
	protected float weight;
	protected GenericPool<Sprite> spritePool;
	
	private static float size = 65;
    
    public Crate(CrateType crateType, GenericPool<Sprite> pool, FixtureDef fd) { 
    	/* could probably move some of the duplicate code out of subclasses into this */ 
    	type = crateType;
		spritePool = pool;
		sprite = spritePool.obtainPoolItem();
		
		box = PhysicsFactory.createBoxBody(GameScene.mPhysicsWorld, sprite, BodyType.DynamicBody, fd);
		box.setBullet(true);
		
		sprite.setUserData(box);
		sprite.setVisible(true);
		
		GameScene.mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(sprite, box, true, true));
		GameScene.getScene().getContainer().attachChild(sprite);
		GameScene.activeCrates.get(type).add(this);		
    }
    
	public Body getBox() { return box; }
	public void setBox(Body box) { this.box = box; }
	
	public Sprite getSprite() { return sprite; }
	public void setSprite(Sprite sprite) { this.sprite = sprite; }

	public CrateType getType() { return type; }
	public static float getCrateWidth() { return size; }
	
	public GenericPool<Sprite> getSpritePool() { return spritePool; }
	
	public void setPosition(float x, float y) {
		final float widthD2 = sprite.getWidth() / 2;
		final float heightD2 = sprite.getHeight() / 2;
		final float angle = box.getAngle();
		float newX = (x + widthD2) / PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT;
		float newY = (y + heightD2) / PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT;
		box.setTransform(newX, newY, angle);
	}	

	public void dispose() {
		PhysicsConnector physicsConnector = GameScene.mPhysicsWorld.getPhysicsConnectorManager().findPhysicsConnectorByShape(sprite);
		GameScene.mPhysicsWorld.unregisterPhysicsConnector(physicsConnector);
		
		sprite.detachSelf();
		spritePool.recyclePoolItem(sprite);
	}
	
	@Override
	public boolean onAreaTouched(TouchEvent pSceneTouchEvent, ITouchArea pTouchArea, float pTouchAreaLocalX, float pTouchAreaLocalY) {
		float curX = pSceneTouchEvent.getX() - 5;
        float curY = pSceneTouchEvent.getY() - 5;
        float dX = 0, dY = 0, lastX = 0, lastY = 0;
        
		if (pSceneTouchEvent.isActionDown()) {
			box.setType(BodyType.KinematicBody);
			dX = curX;
			dY = curY;
			box.setTransform(curX/32,  curY/32, 0);
		
		} else if (pSceneTouchEvent.isActionMove()) {
			dX = curX - lastX;
			dY = curY - lastY;
			box.setTransform(curX/32, curY/32, 0);
			lastX = curX;
			lastX = curY;
			
		} else if (pSceneTouchEvent.isActionUp()) { 
			box.setType(BodyType.DynamicBody);
			box.setLinearVelocity(dX, dY);
		}
		return false;
	}
}	

