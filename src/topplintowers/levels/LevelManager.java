package topplintowers.levels;

import java.util.LinkedHashMap;

import topplintowers.crates.CrateType;

public class LevelManager {
	private Level currentLevel;
	
	public enum LevelType {
		FREEMODE, ONE, TWO, THREE, FOUR, 
		FIVE, SIX, SEVEN, EIGHT, NINE,
		TEN, ELEVEN, TWELVE; 
    }
	
	public static LinkedHashMap<LevelType, LinkedHashMap<CrateType, Integer>> CountList = new LinkedHashMap<LevelType, LinkedHashMap<CrateType, Integer>>();
	public static LinkedHashMap<LevelType, Level> LevelList = new LinkedHashMap<LevelType, Level>();
	
	private Integer max = Integer.MAX_VALUE;
	private int[] free_mode = {max, max, max, max, max, max, max};
	private int[] one		= {3, 0, 0, 0, 0, 0, 0};
	private int[] two		= {3, 3, 0, 0, 0, 0, 0};
	private int[] three		= {3, 3, 3, 0, 0, 0, 0};
	private int[] four		= {4, 4, 4, 4, 0, 0, 0};
	private int[] five		= {5, 5, 5, 5, 5, 0, 0};
	private int[] six		= {6, 6, 6, 6, 6, 6, 0};
	private int[] seven		= {7, 7, 7, 7, 7, 7, 7};
	private int[] eight		= {8, 8, 8, 8, 8, 8, 8};
	private int[] nine		= {9, 9, 9, 9, 9, 9, 9};
	private int[] ten		= {10, 10, 10, 10, 10, 10, 10};
	private int[] eleven	= {11, 11, 11, 11, 11, 11, 11};
	private int[] twelve	= {11, 22, 33, 44, 55, 66, 77};
	
	public LevelManager() {
		SetupCountList();
		
		float goalHeight = 1;
		
		LevelType[] levels = LevelType.values();
		
		for (LevelType level : levels) {
        	LevelList.put(level, new Level(level, goalHeight++, CountList.get(level)));
		}
	}
	
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
