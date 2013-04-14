package topplintowers.crates;

import org.andengine.entity.scene.IOnAreaTouchListener;
import org.andengine.entity.scene.ITouchArea;
import org.andengine.entity.shape.Shape;
import org.andengine.entity.sprite.Sprite;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.util.constants.PhysicsConstants;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.region.TextureRegion;
import org.andengine.util.adt.pool.GenericPool;

import topplintowers.MainActivity;
import topplintowers.pools.PoolManager;
import topplintowers.scenes.GameScene;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;


public class Crate implements IOnAreaTouchListener {
	private static MainActivity instance = MainActivity.getSharedInstance();
	
	protected CrateType type;
	protected Sprite sprite;
	protected Body box;
	protected TextureRegion texture;
	protected float weight;
	protected GenericPool<Sprite> spritePool;
	
	private boolean recycled = false;
	private static float size = 65;
    
    protected static final FixtureDef FIXTURE_DEF = PhysicsFactory.createFixtureDef(1, 0.25f, 0.3f);
    
    public Crate() { /* could probably move some of the duplicate code out of subclasses into this */ }
    
	public Body getBox() { return box; }
	public void setBox(Body box) { this.box = box; }
	
	public Sprite getSprite() { return sprite; }
	public void setSprite(Sprite sprite) { this.sprite = sprite; }

	public CrateType getType() { return type; }
	public static float getCrateWidth() { return size; }
	public boolean isRecycled() { return recycled; }
	public void setRecycled(boolean b) { recycled = b; }
	
	public GenericPool<Sprite> getSpritePool() { return spritePool; }
	
	public void setPosition(float x, float y) {
		final float widthD2 = sprite.getWidth() / 2;
		final float heightD2 = sprite.getHeight() / 2;
		final float angle = box.getAngle();
		float newX = (x + widthD2) / PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT;
		float newY = (y + heightD2) / PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT;
		box.setTransform(newX, newY, angle);
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

