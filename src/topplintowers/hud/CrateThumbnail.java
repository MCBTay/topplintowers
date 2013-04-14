package topplintowers.hud;

import org.andengine.entity.modifier.MoveModifier;
import org.andengine.entity.modifier.ScaleModifier;
import org.andengine.entity.scene.IOnAreaTouchListener;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.ITouchArea;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.input.touch.TouchEvent;
import org.andengine.input.touch.detector.ClickDetector;
import org.andengine.input.touch.detector.ClickDetector.IClickDetectorListener;
import org.andengine.opengl.texture.region.TextureRegion;

import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

import topplintowers.MainActivity;
import topplintowers.ResourceManager;
import topplintowers.crates.*;

public class CrateThumbnail implements IOnSceneTouchListener, IClickDetectorListener, IOnAreaTouchListener {
	private static MainActivity instance = MainActivity.getSharedInstance();
	
	private boolean hidden = false;
	private Sprite sprite;
	private String side;
	private CrateContainer parent;
	private CrateType type;
	private Text countText;
	
	final ScaleModifier zoomOut;
	final ScaleModifier zoomIn;
	MoveModifier move;
	
	private ClickDetector mClickDetector;
	
	public CrateThumbnail(MyHUD HUD, CrateContainer parent, CrateType type) {
		this.mClickDetector = new ClickDetector(this);	
		this.side = parent.getSide();
		this.parent = parent;
		this.type = type;
		
	    zoomOut = new ScaleModifier(0.3f, 1f, 0f);
	    zoomOut.setAutoUnregisterWhenFinished(true);
	    zoomIn = new ScaleModifier(0.3f, 0f, 1f);
	    zoomIn.setAutoUnregisterWhenFinished(true);
	    	
		TextureRegion textureRegion = CrateType.getTextureRegion(type);
		sprite = new Sprite(0, 0, textureRegion, instance.getVertexBufferObjectManager());
		sprite.setSize(45, 45);
		
		sprite.setScale(1f);
		
		parent.thumbs.add(this);
		setThumbPosition(parent, sprite);
		
		Integer count = MyHUD.availableCrateCounts.get(type);
		if (count < 100)
			createCounter(sprite, side, count.toString());
	
		HUD.registerTouchArea(sprite);
		HUD.setOnSceneTouchListenerBindingOnActionDownEnabled(true);
		HUD.attachChild(sprite);
	}
	
	public String getSide() { return side; }
	public Sprite getSprite() { return sprite; }
	public CrateType getType() { return type; }
	public Text getCounterText() { return countText; }
	public CrateContainer getParent() { return parent; }
	public void setHidden(boolean b) { hidden = b; }
	public boolean isHidden() {
		if (hidden == true) return true;
		return false;
	}
	
	private void setThumbPosition(CrateContainer parent, Sprite thumb) {
		Sprite parentSprite = parent.getSprite();
		float thumbPosX = parentSprite.getX() + 7.5f;
		if (parent.getSide() == "right")
			thumbPosX = instance.mCamera.getWidth() - thumb.getWidth() - 7.5f;
		float thumbPosY = 0;
		float offsetY = 10;
		Sprite lastThumb = null;
		
		if (parent.thumbs.size() > 0) {
			if (parent.thumbs.size() != 1) {
				lastThumb = parent.thumbs.get(parent.thumbs.size() - 2).sprite;
				thumbPosY = lastThumb.getY() + lastThumb.getHeight() + offsetY;
			}
		}
    	thumb.setPosition(thumbPosX, thumbPosY);
	}
	
	private void createCounter(Sprite parent, String side, String crateCount) {
		float posX = 0;
		float crateEdge = 0, spaceToFill = 0, textWidth = 0;
		
		countText = new Text(0, 0, ResourceManager.mFontLevelSelect, crateCount, instance.getVertexBufferObjectManager());
		if (side == "left") {
			crateEdge = parent.getX() + parent.getWidthScaled();
			spaceToFill = this.parent.getSprite().getWidthScaled() - crateEdge;
			textWidth = countText.getWidthScaled();
			posX = crateEdge + (spaceToFill - textWidth)/2 - 5; //5 is just an offset
		} else {
			crateEdge = parent.getX();
			spaceToFill = crateEdge - this.parent.getSprite().getX();
			textWidth = countText.getWidth();
			posX = -(spaceToFill/2) - (textWidth/2);
		}
		float textPosY = parent.getHeight()/2 - countText.getHeight()/2;
		countText.setPosition(posX, textPosY);
		parent.attachChild(countText);
	}

	@Override
	public boolean onSceneTouchEvent(Scene pScene, TouchEvent pSceneTouchEvent) {
		this.mClickDetector.onTouchEvent(pSceneTouchEvent);
		return true;
	}
	
	@Override
	public void onClick(ClickDetector pClickDetector, int pPointerID, float pSceneX, float pSceneY) {
//		int crateCount = GameScene.availableCrateCounts.get(type);
//		if (crateCount > 0) {
//			Crate newCrate = new Crate(100, 0, type); 
//			instance.mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(newCrate.getSprite(), newCrate.getBox(), true, true));
//		    
//			//newCrate.setPosition(instance.mCamera.getWidth()/2, pSceneY);
//			//newCrate.setPosition(pSceneX, pSceneY);
//			if (newCrate != null) {
//				int newCrateCount = crateCount - 1;
//				if (newCrateCount == 0) {
//					shrinkThumbnail();
//					parent.resizeContainer(parent.getSprite().getHeight());
//					parent.repositionCrates();
//				}
//				GameScene.availableCrateCounts.put(type, newCrateCount);
//			}
//		}
		
		return;
	}
	
	public void shrinkThumbnail() {
		sprite.registerEntityModifier(zoomOut.deepCopy());
		hidden = true;	
		countText.setVisible(false);
	}
	
	public void expandThumbnail() {
		// TODO: there is a bug where if two hit at exactly the same time they aren't positioned correctly, 
		//may need to look into how to make sure this method is only executed once at a time
		//low priority
		sprite.registerEntityModifier(zoomIn.deepCopy());	
		hidden = false;
		countText.setVisible(true);
	}
	
	Crate newCrate;
	@Override
	public boolean onAreaTouched(TouchEvent pSceneTouchEvent, ITouchArea pTouchArea, float pTouchAreaLocalX, float pTouchAreaLocalY) {
		float curX = pSceneTouchEvent.getX() - 5;
        float curY = pSceneTouchEvent.getY() - 5;
        float offset = instance.mCamera.getCenterY() - 240;
        curY += offset;
        float dX = 0, dY = 0, lastX = 0, lastY = 0;
        
		if (pSceneTouchEvent.isActionDown()) {
			
			int crateCount = MyHUD.availableCrateCounts.get(type);
			if (crateCount > 0) {
				
				newCrate = createCrate(type);
				newCrate.setPosition(curX, curY);
				if (newCrate != null) {
					int newCrateCount = crateCount - 1;
					if (newCrateCount == 0) {
						shrinkThumbnail();
						parent.resizeContainer(parent.getSprite().getHeight());
						parent.repositionCrates();
					}
					MyHUD.availableCrateCounts.put(type, newCrateCount);
				}
			}
			
			newCrate.getBox().setType(BodyType.KinematicBody);
			dX = curX;
			dY = curY;
			newCrate.getBox().setTransform(curX/32,  curY/32, 0);
		
		} else if (pSceneTouchEvent.isActionMove()) {
			dX = curX - lastX;
			dY = curY - lastY;
			newCrate.getBox().setTransform(curX/32, curY/32, 0);
			lastX = curX;
			lastX = curY;
			
		} else if (pSceneTouchEvent.isActionUp()) { 
			newCrate.getBox().setType(BodyType.DynamicBody);
			newCrate.getBox().setLinearVelocity(dX, dY);
		}
		return false;
	}
	
	private Crate createCrate(CrateType type) {
		Crate crate;
		
		switch (type) {
			case WOOD:
				crate = new WoodCrate(0, 0);
				break;
			case STONE:
				crate = new StoneCrate(0, 0);
				break;
			case METAL:
				crate = new MetalCrate(0, 0);
				break;
			case MAGNET:
				crate = new MagnetCrate(0, 0);
				break;
			case ELECTROMAGNET:
				crate = new ElectromagnetCrate(0, 0);
				break;
			case STICKY:
				crate = new StickyCrate(0, 0);
				break;
			case TRANSFORMER:
				crate = new TransformerCrate(0, 0);
				break;
			default:
				crate = new WoodCrate(0, 0);
				break;
		}
		
		return crate;
	}
}
