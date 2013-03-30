package topplintowers.hud;

import java.util.ArrayList;
import java.util.Timer;

import org.andengine.entity.modifier.MoveModifier;
import org.andengine.entity.modifier.ScaleModifier;
import org.andengine.entity.sprite.Sprite;
import topplintowers.MainActivity;
import topplintowers.ResourceManager;
import topplintowers.crates.CrateType;

public class CrateContainer {
	private static MainActivity instance = MainActivity.getSharedInstance();
	private Sprite sprite;
	public ArrayList<CrateThumbnail> thumbs = new ArrayList<CrateThumbnail>();

	private float totalContainerHeight = 0;
	private float containerEndHeight = 0;
	private String side = "";
	
	private ScaleModifier scale;
	private MoveModifier move;

	final Timer timer = new Timer();
	
	public CrateContainer(MyHUD HUD, String side, boolean freeMode) {	 
	    sprite = new Sprite(0, 0, ResourceManager.mCrateContainerMiddleTextureRegion, instance.getVertexBufferObjectManager());
	    
	    HUD.attachChild(sprite);
	    this.side = side;
	    
	    float scaledWidth;    
	    if (freeMode)
	    	scaledWidth = 60;
	    else   		  
	    	scaledWidth = 95;
	    sprite.setWidth(scaledWidth);
	    
	    if (side == "right") {
	    	float containerPosX = instance.mCamera.getWidth() - sprite.getWidth();
	    	sprite.setPosition(containerPosX, sprite.getY());
	    } 
	}
	
	private float createEnds(String side) {
		Sprite bottom = createEnd(false);
		Sprite top    = createEnd(true);
		
		if (side == "right") {
	    	bottom.setFlippedHorizontal(true);
	    	top.setFlippedHorizontal(true);
	    	top.setY(top.getY()+1);
		}
	
		return top.getHeight();
	}
	
	private Sprite createEnd(boolean top) {
		Sprite end = new Sprite(0, 0, ResourceManager.mCrateContainerEndTextureRegion, instance.getVertexBufferObjectManager());
		
		float ratio = sprite.getWidth() / end.getWidth();
		
		end.setScaleCenter(0, 0);
		end.setScale(ratio);
		
		float posY = 0;
		if (top) posY = -end.getHeightScaled();
		else     posY = sprite.getHeightScaled();
		
		end.setPosition(0, posY);
		
		end.setZIndex(5);
		
		if (top) end.setFlippedVertical(true);
		
		sprite.attachChild(end);
		return end;
	}
	
	public Sprite getSprite() { return sprite; }
	public String getSide() { return side; }
	
	private float calculateContainerHeight() {
		float totalHeight = 0;
		Sprite first = thumbs.get(0).getSprite();
		Sprite last = thumbs.get(thumbs.size() - 1).getSprite();
		float lastBottom = last.getY() + last.getHeight();
		totalHeight = lastBottom - first.getY() + 10;
		return totalHeight;
	}
	
	public void resizeContainer(float oldHeight) {
		ArrayList<CrateThumbnail> crates = new ArrayList<CrateThumbnail>();
		for (int i = 0; i < thumbs.size(); i++) {
			CrateThumbnail current = thumbs.get(i);
			if (!current.isHidden()) {
				crates.add(current);
			}
		}
		
		if (crates.size() > 0) {
			float crateHeight = (crates.get(0).getSprite().getHeight() * crates.size());
			crateHeight += ((crates.size() - 1) * 10) + 10;
			float ratio = crateHeight / oldHeight;
			
			scale = new ScaleModifier(0.25f, 1f, 1f, sprite.getScaleY(), ratio);
		    scale.setAutoUnregisterWhenFinished(true);
		    sprite.setScaleCenter(sprite.getWidth()/2, 0);
		    sprite.registerEntityModifier(scale);
		    
		    float startingCrateY = instance.mCamera.getHeight()/2f - crateHeight/2f;
			move = new MoveModifier(0.25f, sprite.getX(), sprite.getX(), sprite.getY(), startingCrateY);
			move.setAutoUnregisterWhenFinished(true);
			sprite.registerEntityModifier(move);
		} else {
			scale = new ScaleModifier(0.25f, 1f, 0f, sprite.getScaleY(), sprite.getScaleY());
		    scale.setAutoUnregisterWhenFinished(true);
		    if (side == "right") {
		    	sprite.setScaleCenter(sprite.getWidth(), 0);
		    } else {
		    	sprite.setScaleCenter(sprite.getX(), 0);
		    }
		    sprite.registerEntityModifier(scale);
		}
	}
	
	public int getThumbsCount() {
		int count = 0;
		for (int i = 0; i < thumbs.size(); i++) {
			CrateThumbnail current = thumbs.get(i);
			if (!current.isHidden()) { count++; }
		}
		return count;
	}
	
	public CrateThumbnail getThumbByType(CrateType type) {
		CrateThumbnail winner = null;
		for (int i = 0; i < thumbs.size(); i++) {
			CrateThumbnail currentCrate = thumbs.get(i);
			if (type == currentCrate.getType()) {
				winner = currentCrate;
			}
		}
		return winner;
	}
	
	public void initializeCratePositions() { 
		for (CrateThumbnail ct : thumbs) {
			Sprite ctSprite = ct.getSprite();
			ctSprite.setY(ctSprite.getY() + sprite.getY() + 5);
		}
	}
	
	public void repositionCrates () {
		ArrayList<CrateThumbnail> crates = new ArrayList<CrateThumbnail>();
		for (int i = 0; i < thumbs.size(); i++) {
			CrateThumbnail current = thumbs.get(i);
			if (!current.isHidden()) {
				crates.add(current);
			}
		}
				
		for (int i = 0; i < crates.size(); i++)  {
			CrateThumbnail current = crates.get(i);
			
			float crateHeight = ((current.getSprite().getHeight() + 10) * crates.size());
			float startingCrateY = instance.mCamera.getHeight()/2 - crateHeight/2 + 5;
			
			if (i != 0) {
				startingCrateY += (current.getSprite().getHeight() + 10) * i;
			}
			move = new MoveModifier(0.3f, current.getSprite().getX(), current.getSprite().getX(), current.getSprite().getY(), startingCrateY);
			move.setAutoUnregisterWhenFinished(true);
			crates.get(i).getSprite().registerEntityModifier(move);
		}
	}

	public void sizeContainer() {
		float height = calculateContainerHeight();
		sprite.setHeight(height);
		containerEndHeight = createEnds(side);
		
		totalContainerHeight = sprite.getHeight() + (containerEndHeight * 2);
	    float centerY = (instance.mCamera.getHeight() - totalContainerHeight) / 2;
	    // had to use containerEndHeight as an offset here to make it truly centered, otherwise it would be too high by the height of the container end
	    sprite.setPosition(sprite.getX(), centerY + containerEndHeight);
	}
}
