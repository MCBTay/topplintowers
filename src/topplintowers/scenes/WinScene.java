package topplintowers.scenes;

import java.util.ArrayList;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.menu.MenuScene;
import org.andengine.entity.scene.menu.MenuScene.IOnMenuItemClickListener;
import org.andengine.entity.scene.menu.item.IMenuItem;
import org.andengine.entity.scene.menu.item.SpriteMenuItem;
import org.andengine.entity.text.Text;

import com.topplintowers.R;

import topplintowers.resources.ResourceManager;
import topplintowers.scenes.SceneManager.SceneType;

public class WinScene extends BaseScene implements IOnMenuItemClickListener {
	private Rectangle mRectangle;
	private Text mText;
	
	private SpriteMenuItem mResumeButton, mRestartButton, mMainMenuButton, mLevelSelect;
	private ArrayList<SpriteMenuItem> mButtons;
	private MenuScene mMenuChildScene;
	
	public Camera getCamera() { return camera; }
	public ArrayList<SpriteMenuItem> getButtons() { return mButtons; }
	public Rectangle getRectangle() { return mRectangle; }
	public Text getText() { return mText; }
	public void setBackgroundPosition(float x, float y) { mRectangle.setPosition(x, y); }
	public void setTextPosition(float x, float y) { mText.setPosition(x, y); }
	
	@Override
	public void createScene() {
		setBackgroundEnabled(false);
		mRectangle = new Rectangle(0, 0, 800, 480, vbom);
		mRectangle.setColor(0,0,0,0);
		attachChild(mRectangle);
		
		mText = new Text(0, 0, ResourceManager.mFont140, activity.getString(R.string.you_win), vbom);
		float fontX = 25;
		float fontY = camera.getHeight()/2 - mText.getHeight()/2 - 40;
		mText.setPosition(fontX, fontY);
		attachChild(mText);
		
		Text mSubtext = new Text(0, 0, ResourceManager.mFont64, "You beat level 1.", vbom);
		fontX = 35;
		fontY = camera.getHeight()/2 - mSubtext.getHeight()/2 + 40;
		mSubtext.setPosition(fontX, fontY);
		attachChild(mSubtext);
		
		createMenuChildScene();
	}
	
	private void createMenuChildScene()
	{
		mMenuChildScene = new MenuScene(camera);
		mMenuChildScene.setPosition(0, 0);
		mMenuChildScene.setBackgroundEnabled(false);
		
		mButtons = new ArrayList<SpriteMenuItem>();
		
		mRestartButton = SceneCommon.createMenuButton(mMenuChildScene, MenuButtonsEnum.RESTART,  activity.getString(R.string.restart));
		mButtons.add(mRestartButton);
		
		mLevelSelect = SceneCommon.createMenuButton(mMenuChildScene, MenuButtonsEnum.LEVEL_SELECT,  activity.getString(R.string.levelselect));
		mButtons.add(mLevelSelect);
		
		mMainMenuButton = SceneCommon.createMenuButton(mMenuChildScene, MenuButtonsEnum.MAIN_MENU,  activity.getString(R.string.main_menu));
		mButtons.add(mMainMenuButton);
		
		SceneCommon.repositionButtons(mRestartButton.getWidth(), mRestartButton.getHeight(), mButtons);
		
		mMenuChildScene.setOnMenuItemClickListener(this);
		setChildScene(mMenuChildScene);
	}

	@Override
	public void onBackKeyPressed() { return; }
	
	@Override
	public void onMenuKeyPressed() { return; }

	@Override
	public SceneType getSceneType() { return SceneType.WIN; }

	@Override
	public void disposeScene() {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public boolean onMenuItemClicked(MenuScene pMenuScene, IMenuItem pMenuItem,
			float pMenuItemLocalX, float pMenuItemLocalY) {
		// TODO Auto-generated method stub
		return false;
	}
	
	public void fadeIn() {
		SceneCommon.applyFadeModifier(mRectangle, 0, 0.75f);
		SceneCommon.applyFadeModifier(mButtons, 0, 1);
		SceneCommon.applyFadeModifier(mText, 0, 1);
	}
	
	public void fadeOut() {
		SceneCommon.applyFadeModifier(mRectangle, 0.75f, 0);
		SceneCommon.applyFadeModifier(mButtons, 1, 0);
		SceneCommon.applyFadeModifier(mText, 1, 0);
	}

}
