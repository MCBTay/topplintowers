package topplintowers.levels;

import java.util.LinkedHashMap;

import org.andengine.entity.primitive.Line;
import org.andengine.entity.text.Text;
import org.andengine.util.color.Color;

import com.topplintowers.R;

import topplintowers.crates.CrateType;
import topplintowers.levels.LevelManager.LevelType;
import topplintowers.resources.ResourceManager;
import topplintowers.scenes.gamescene.GameScene;

public class Level {
	private float goalHeight;
	private LinkedHashMap<CrateType, Integer> crateCounts;
	private LevelType level;
	private boolean isLocked = true;
	private Line goalLine;

	public Level(LevelType level, float goalHeight, LinkedHashMap<CrateType, Integer> crateCounts, boolean locked) {
		this.level = level;
		this.isLocked = locked;
		this.crateCounts = crateCounts;
		this.goalHeight = goalHeight;	
	}
	
	public boolean getLocked() 	{ return this.isLocked; }
	public float getGoal() 		{ return this.goalHeight; }
	public float getGoalLineHeight() { return this.goalLine.getY1(); }
	public LevelType getLevelType() { return this.level; }
	
	public void setLocked(boolean lock) { this.isLocked = lock; }
	
	public void setGoal(GameScene currentScene) {
		float goalScaled = 65 * goalHeight;	
		
		float lineLength = 480;
		float linePoxX = 400 - (lineLength/2);
		float linePosY = currentScene.mPlatform.rectangle.getY() - goalScaled;
		
		goalLine = new Line(linePoxX, linePosY, linePoxX+lineLength, linePosY, 3, currentScene.getVBOM());
		goalLine.setColor(Color.WHITE);
		currentScene.getContainer().attachChild(goalLine);
		
		Text goalText = new Text(0, 0, ResourceManager.mFont48, "Goal", currentScene.getVBOM());
		float textPosX = goalLine.getX2() - goalText.getWidth();
		float textPosY = goalLine.getY2() - goalText.getHeight();
		goalText.setPosition(textPosX, textPosY);
		currentScene.getContainer().attachChild(goalText);
	}
		
	public LinkedHashMap<CrateType, Integer> getCounts() { return crateCounts; }
}
