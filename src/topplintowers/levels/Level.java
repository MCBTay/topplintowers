package topplintowers.levels;

import java.util.LinkedHashMap;

import org.andengine.entity.primitive.Line;
import org.andengine.entity.text.Text;
import org.andengine.util.color.Color;

import topplintowers.MainActivity;
import topplintowers.ResourceManager;
import topplintowers.crates.Crate;
import topplintowers.crates.CrateType;
import topplintowers.scenes.GameScene;

import com.topplintowers.R;


public class Level {
	private static MainActivity instance = MainActivity.getSharedInstance();
	
	private float goalHeight;
	private LinkedHashMap<CrateType, Integer> crateCounts;
	private Levels level;
	private boolean isLocked = true;

	public Level(Levels level, float goalHeight, LinkedHashMap<CrateType, Integer> crateCounts) {
		this.level = level;
		
		if (this.level == Levels.ONE) {
			this.isLocked = false;
		}
		
		this.crateCounts = crateCounts;
		this.goalHeight = goalHeight;	
	}
	
	public boolean getLocked() 	{ return this.isLocked; }
	public float getGoal() 		{ return this.goalHeight; }
	public Levels getLevelType() { return this.level; }
	
	public void setGoal(GameScene currentScene) {
//		float goalScaled = Crate.getCrateWidth()  * goalHeight;	
//		float lineLength = instance.mCamera.getWidth() * 0.6f;
//		float linePoxX = instance.mCamera.getWidth()/2 - (lineLength/2);
//		//float linePosY = instance.mCamera.getHeight() - currentScene.mPlatform.rectangle.getHeight() - goalScaled;
//		//Line goalLine = new Line(linePoxX, linePosY, linePoxX+lineLength, linePosY, 3, instance.getVertexBufferObjectManager());
//		goalLine.setColor(Color.WHITE);
//		currentScene.attachChild(goalLine);
//		Text goalText = new Text(0, 0, ResourceManager.mGoalFont, instance.getString(R.string.goal_caps), instance.getVertexBufferObjectManager());
//		float textPosX = goalLine.getX2() - goalText.getWidth();
//		float textPosY = goalLine.getY2() - goalText.getHeight();
//		goalText.setPosition(textPosX, textPosY);
//		currentScene.attachChild(goalText);
	}
	
	
	public LinkedHashMap<CrateType, Integer> getCounts() { return crateCounts; }
}
