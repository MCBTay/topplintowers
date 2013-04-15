package topplintowers.levels;

import java.util.LinkedHashMap;

import topplintowers.crates.CrateType;

public class LevelMgr {
	private Level currentLevel;
	
	public static LinkedHashMap<Levels, LinkedHashMap<CrateType, Integer>> CountList = new LinkedHashMap<Levels, LinkedHashMap<CrateType, Integer>>();
	public static LinkedHashMap<Levels, Level> LevelList = new LinkedHashMap<Levels, Level>();
	
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
	
	public LevelMgr() {
		SetupCountList();
		
		float goalHeight = 1;
		
		Levels[] levels = Levels.values();
		
		for (Levels level : levels) {
        	LevelList.put(level, new Level(level, goalHeight++, CountList.get(level)));
		}
	}
	
	public Level getCurrentLevel()      { return currentLevel; }
	public Level getLevel(Levels level) { return LevelList.get(level); }
	
	private void SetupCountList() {
		CountList.put(Levels.FREEMODE, 	getCrateCount(free_mode));
		CountList.put(Levels.ONE, 		getCrateCount(one));
		CountList.put(Levels.TWO, 		getCrateCount(two));
		CountList.put(Levels.THREE, 	getCrateCount(three));
		CountList.put(Levels.FOUR, 		getCrateCount(four));
		CountList.put(Levels.FIVE, 		getCrateCount(five));
		CountList.put(Levels.SIX, 		getCrateCount(six));
		CountList.put(Levels.SEVEN, 	getCrateCount(seven));
		CountList.put(Levels.EIGHT, 	getCrateCount(eight));
		CountList.put(Levels.NINE, 		getCrateCount(nine));
		CountList.put(Levels.TEN, 		getCrateCount(ten));
		CountList.put(Levels.ELEVEN,	getCrateCount(eleven));
		CountList.put(Levels.TWELVE, 	getCrateCount(twelve));
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
