package topplintowers.scenes;

import java.util.ArrayList;
import java.util.Iterator;

import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.AlphaModifier;
import org.andengine.entity.modifier.MoveXModifier;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.menu.MenuScene;
import org.andengine.entity.scene.menu.item.IMenuItem;
import org.andengine.entity.scene.menu.item.SpriteMenuItem;
import org.andengine.entity.scene.menu.item.TextMenuItem;
import org.andengine.entity.text.Text;

import topplintowers.MainActivity;
import topplintowers.crates.Crate;
import topplintowers.crates.CrateType;
import topplintowers.hud.MyHUD;
import topplintowers.resources.ResourceManager;

public class SceneCommon {

	private static MainActivity instance = MainActivity.getSharedInstance();
	
	public static SpriteMenuItem createMenuButton(MenuScene scene, MenuButtonsEnum button, String buttonText) {
		final SpriteMenuItem menuButton = new SpriteMenuItem(button.getValue(), ResourceManager.mMenuButtonTextureRegion, instance.getVertexBufferObjectManager());
		scene.addMenuItem(menuButton);
				
		TextMenuItem menuButtonText = new TextMenuItem(button.getValue(), ResourceManager.mFont48, buttonText, instance.getVertexBufferObjectManager());
		menuButtonText.setPosition(5, 0);
		menuButton.attachChild(menuButtonText);
		
		return menuButton;
	}
	
	public static void repositionButtons(float buttonWidth, float buttonHeight, ArrayList<SpriteMenuItem> buttons){
		float numberOfButtons = buttons.size();
		
		float heightOfButtons = (buttonHeight * numberOfButtons) + (numberOfButtons * 20);
			
		float newX = instance.mCamera.getWidth() - buttonWidth + 25;
		float newY = instance.mCamera.getHeight()/2 - heightOfButtons/2;
		
		for (int i = 0; i < numberOfButtons; i++) {
			SpriteMenuItem currentButton = buttons.get(i);
			currentButton.setPosition(newX, newY);
			newY += buttonHeight + 20;
		}
	}
	
	public static void reenableButton(IMenuItem pMenuItem) {
		if (pMenuItem.getX() == instance.mCamera.getWidth()) {
			MoveXModifier moveLeft = new MoveXModifier(0.25f, instance.mCamera.getWidth(), instance.mCamera.getWidth() - pMenuItem.getWidth() + 25);
		    moveLeft.setAutoUnregisterWhenFinished(true);
		    pMenuItem.registerEntityModifier(moveLeft);
		}
	}
	
	public static void fadeChildren(IEntity object, float start, float finish) {
		int childCount = object.getChildCount();
		for (int i = 0; i < childCount; i++) {
			IEntity child = object.getChildByIndex(i);
			applyFadeModifier(child, start, finish);
			int childChildCount = child.getChildCount();
			for (int j = 0; j < childChildCount; j++) {
				IEntity childsChild = child.getChildByIndex(j);
				applyFadeModifier(childsChild, start, finish);
			}
		}
	}
	
	public static void applyFadeModifier(ArrayList<SpriteMenuItem> buttons, float start, float finish) {
		int size = buttons.size();
		for (int i = 0; i < size; i++) {
			SpriteMenuItem current = buttons.get(i);
			applyFadeModifier(current, start, finish);
			fadeChildren(current, start, finish);
		}
	}
	
	public static void applyFadeModifier(IEntity object, float start, float finish) {
		AlphaModifier am = new AlphaModifier(0.2f, start, finish);
		am.setAutoUnregisterWhenFinished(true);
		object.registerEntityModifier(am);
	}
	
	
	
	public static Rectangle createBackground(Scene currentScene) {
		currentScene.setBackgroundEnabled(false);
		Rectangle mRectangle = new Rectangle(0, 0, 800, 480, instance.getVertexBufferObjectManager());
		mRectangle.setColor(0,0,0,0);
		currentScene.attachChild(mRectangle);
		return mRectangle;
	}
	
	public static Text createLargeText(Scene scene, String label) {
		Text mText = new Text(0, 0, ResourceManager.mFont140, label, instance.getVertexBufferObjectManager());
		float fontX = 25;
		float fontY = instance.mCamera.getHeight()/2 - mText.getHeight()/2;
		mText.setPosition(fontX, fontY);
		scene.attachChild(mText);
		return mText;
	}
	
	public static void deleteExistingCrates() {
		Iterator<CrateType> it = GameScene.activeCrates.keySet().iterator();
		while (it.hasNext()) {
			CrateType type = it.next();
			
			Iterator<Crate> crateIt = GameScene.activeCrates.get(type).iterator();
			while (crateIt.hasNext()) {
				Crate currentCrate = crateIt.next();
				currentCrate.dispose();
				MyHUD.mAvailableCrateCounts.put(type, MyHUD.mAvailableCrateCounts.get(type) + 1);
				GameScene.mPhysicsWorld.destroyBody(currentCrate.getBox());
				GameScene.activeCrates.get(type).remove(currentCrate);
			}
		}

		instance.mPhysicsWorld.clearPhysicsConnectors();
	}	
}
