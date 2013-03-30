package topplintowers.scenes;

import java.util.ArrayList;
import java.util.Enumeration;

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
import topplintowers.ResourceManager;
import topplintowers.crates.Crate;
import topplintowers.crates.CrateType;
import topplintowers.hud.MyHUD;

public class SceneCommon {

	private static MainActivity instance = MainActivity.getSharedInstance();
	
	public static SpriteMenuItem createMenuButton(MenuScene scene, MenuButtonsEnum button, String buttonText) {
		final SpriteMenuItem menuButton = new SpriteMenuItem(button.getValue(), ResourceManager.mMenuButtonTextureRegion, instance.getVertexBufferObjectManager());
		scene.addMenuItem(menuButton);
				
		TextMenuItem menuButtonText = new TextMenuItem(button.getValue(), ResourceManager.mFontButton, buttonText, instance.getVertexBufferObjectManager());
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
	
	public static void fadeInBackground(Rectangle rectangle) {		
		applyFadeInModifier(rectangle, 0, 0.75f);
		fadeInChildren(rectangle);
	}
	
	private static void fadeInButtons(ArrayList<SpriteMenuItem> buttons) {
		int size = buttons.size();
		for (int i = 0; i < size; i++) {
			SpriteMenuItem current = buttons.get(i);
			applyFadeInModifier(current, 0, 1);
			fadeInChildren(current);
		}
	}
	
	private static void fadeInText(Text text) {
		applyFadeInModifier(text, 0, 1);
	}
	
	public static void fadeInChildren(IEntity object) {
		int childCount = object.getChildCount();
		for (int i = 0; i < childCount; i++) {
			IEntity child = object.getChildByIndex(i);
			applyFadeInModifier(child, 0, 1);
			int childChildCount = child.getChildCount();
			for (int j = 0; j < childChildCount; j++) {
				IEntity childsChild = child.getChildByIndex(j);
				applyFadeInModifier(childsChild, 0, 1);
			}
		}
	}
	
	private static void applyFadeInModifier(IEntity object, float start, float finish) {
		AlphaModifier am = new AlphaModifier(0.2f, start, finish);
		am.setAutoUnregisterWhenFinished(true);
		object.registerEntityModifier(am);
	}
	
	private static void fadeOutBackground(Rectangle rectangle) {
		applyFadeOutModifier(rectangle, 0.75f, 0);
		fadeOutChildren(rectangle);
	}
	
	private static void fadeOutButtons(ArrayList<SpriteMenuItem> buttons) {
		int size = buttons.size();
		for (int i = 0; i < size; i++) {
			SpriteMenuItem current = buttons.get(i);
			applyFadeOutModifier(current, 1, 0);
			fadeOutChildren(current);
		}
	}
	
	private static void fadeOutText(Text text) {
		applyFadeOutModifier(text, 1, 0);
	}
	
	public static void fadeOutChildren(IEntity object) {
		int childCount = object.getChildCount();
		for (int i = 0; i < childCount; i++) {
			IEntity child = object.getChildByIndex(i);
			applyFadeOutModifier(child, 1, 0);
			int childChildCount = child.getChildCount();
			for (int j = 0; j < childChildCount; j++) {
				IEntity childsChild = child.getChildByIndex(j);
				applyFadeOutModifier(childsChild, 1, 0);
			}
		}
	}
	
	public static void fadeIn(Rectangle rect, ArrayList<SpriteMenuItem> buttons) {
		SceneCommon.fadeInBackground(rect);
		SceneCommon.fadeInButtons(buttons);
	}
	
	public static void fadeIn(Rectangle rect, ArrayList<SpriteMenuItem> buttons, Text text) {
		SceneCommon.fadeInBackground(rect);
		SceneCommon.fadeInButtons(buttons);
		SceneCommon.fadeInText(text);
	}
	
	public static void fadeOut(Rectangle rect, ArrayList<SpriteMenuItem> buttons) {
		SceneCommon.fadeOutBackground(rect);
		SceneCommon.fadeOutButtons(buttons);
	}
	
	public static void fadeOut(Rectangle rect, ArrayList<SpriteMenuItem> buttons, Text text) {
		SceneCommon.fadeOutBackground(rect);
		SceneCommon.fadeOutButtons(buttons);
		SceneCommon.fadeOutText(text);
	}
	
	private static void applyFadeOutModifier(IEntity object, float start, float finish) {
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
		Text mText = new Text(0, 0, ResourceManager.mFontSplash, label, instance.getVertexBufferObjectManager());
		float fontX = 25;
		float fontY = instance.mCamera.getHeight()/2 - mText.getHeight()/2;
		mText.setPosition(fontX, fontY);
		scene.attachChild(mText);
		return mText;
	}
	
	public static void deleteExistingCrates() {
		Enumeration<CrateType> crateTypes = GameScene.activeCrates.keys();
		while (crateTypes.hasMoreElements()) {
			CrateType type = (CrateType) crateTypes.nextElement();
			ArrayList<Crate> currentList = GameScene.activeCrates.get(type);
			for (Crate currentCrate : currentList) {
				instance.mPhysicsWorld.destroyBody(currentCrate.getBox());
				currentCrate.getSprite().detachSelf();
				MyHUD.availableCrateCounts.put(type, MyHUD.availableCrateCounts.get(type) + 1);
			}
			currentList.clear();
		}
		instance.mPhysicsWorld.clearPhysicsConnectors();
	}	
}
