package topplintowers.levels;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map.Entry;

import android.content.SharedPreferences;
import android.util.Log;

import topplintowers.MainActivity;
import topplintowers.crates.CrateType;
import topplintowers.resources.ResourceManager;
import topplintowers.scenes.SceneManager;

public class LevelManager {
	private Level currentLevel;
	private MainActivity mActivity = ResourceManager.getInstance().mActivity;
	
	public enum LevelType {
		FREEMODE, ONE, TWO, THREE, FOUR, FIVE, SIX, SEVEN, EIGHT, NINE, TEN, ELEVEN, TWELVE; 
		
		public LevelType getNext() {
			int ordinal = this.ordinal();

			return ordinal < LevelType.values().length - 1 ? LevelType.values()[ordinal + 1] : null;
		}
    }
	
	public static LinkedHashMap<LevelType, LinkedHashMap<CrateType, Integer>> CountList = new LinkedHashMap<LevelType, LinkedHashMap<CrateType, Integer>>();
	public static LinkedHashMap<LevelType, Level> LevelList = new LinkedHashMap<LevelType, Level>();
	public static LinkedHashMap<LevelType, Boolean> LevelLocks = new LinkedHashMap<LevelType, Boolean>();
	
	private Integer max = Integer.MAX_VALUE;
	private int[] free_mode = {max, max, max, max, max, max, max};
	private int[] one		= { 3,  0,  0,  0,  0,  0,  0};
	private int[] two		= { 3,  3,  0,  0,  0,  0,  0};
	private int[] three		= { 3,  3,  3,  0,  0,  0,  0};
	private int[] four		= { 4,  4,  4,  4,  0,  0,  0};
	private int[] five		= { 5,  5,  5,  5,  5,  0,  0};
	private int[] six		= { 6,  6,  6,  6,  6,  6,  0};
	private int[] seven		= { 7,  7,  7,  7,  7,  7,  7};
	private int[] eight		= { 8,  8,  8,  8,  8,  8,  8};
	private int[] nine		= { 9,  9,  9,  9,  9,  9,  9};
	private int[] ten		= {10, 10, 10, 10, 10, 10, 10};
	private int[] eleven	= {11, 11, 11, 11, 11, 11, 11};
	private int[] twelve	= {11, 22, 33, 44, 55, 66, 77};
	
	public LevelManager() {
		SetupCountList();
		
		float goalHeight = 1.5f;
		
		LevelType[] levels = LevelType.values();
		
		loadLevelState();
		
		for (LevelType level : levels) {
        	LevelList.put(level, new Level(level, goalHeight++, CountList.get(level), LevelLocks.get(level)));
		}
	}
	
    public void loadLevelState() {
		SharedPreferences options = MainActivity.getOptions();
		
		for (LevelType level : LevelType.values()) {
			Boolean isLocked = true;
			
			if (level == LevelType.FREEMODE) {
				isLocked = false;
			} else {
				boolean isLockedByDefault = true;
				if (level == LevelType.ONE) {
					isLockedByDefault = false;
				}
				
				isLocked = options.getBoolean(level.toString(), isLockedByDefault);
			}
			
			LevelLocks.put(level, isLocked);
		}
    }
    
    public void writeLevelState() {	
		SharedPreferences options = mActivity.getOptions();
		SharedPreferences.Editor editor = options.edit();

		for (LevelType level : LevelType.values()) {
			editor.putBoolean(level.toString(), LevelLocks.get(level));
		}
		
		editor.commit();
    }
    
    public void unlockNextLevel() {
    	LevelType nextLevel = getCurrentLevel().getLevelType().getNext();
    	if (nextLevel != null) {
    		LevelLocks.put(nextLevel, false);
    	}
    	
    	writeLevelState();
    }

	
    public void setCurrentLevel(Level level) { currentLevel = level; }
	public Level getCurrentLevel()      { return currentLevel; }
	public Level getLevel(LevelType level) { return LevelList.get(level); }
	
	private void SetupCountList() {
		CountList.put(LevelType.FREEMODE, 	getCrateCount(free_mode));
		CountList.put(LevelType.ONE, 		getCrateCount(one));
		CountList.put(LevelType.TWO, 		getCrateCount(two));
		CountList.put(LevelType.THREE, 	getCrateCount(three));
		CountList.put(LevelType.FOUR, 		getCrateCount(four));
		CountList.put(LevelType.FIVE, 		getCrateCount(five));
		CountList.put(LevelType.SIX, 		getCrateCount(six));
		CountList.put(LevelType.SEVEN, 	getCrateCount(seven));
		CountList.put(LevelType.EIGHT, 	getCrateCount(eight));
		CountList.put(LevelType.NINE, 		getCrateCount(nine));
		CountList.put(LevelType.TEN, 		getCrateCount(ten));
		CountList.put(LevelType.ELEVEN,	getCrateCount(eleven));
		CountList.put(LevelType.TWELVE, 	getCrateCount(twelve));
	}
	
	private LinkedHashMap<CrateType, Integer> getCrateCount(int[] count) {
		LinkedHashMap<CrateType, Integer> counts = new LinkedHashMap<CrateType, Integer>();
		counts.put(CrateType.WOOD, 			count[0]);
		counts.put(CrateType.STONE, 		count[1]);
		counts.put(CrateType.METAL, 		count[2]);
		counts.put(CrateType.MAGNET, 		count[3]);
		counts.put(CrateType.ELECTROMAGNET, count[4]);
		counts.put(CrateType.STICKY, 		count[5]);
		counts.put(CrateType.TRANSFORMER, 	count[6]);
		return counts;
	}
}
