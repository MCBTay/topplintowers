package topplintowers.levels;

import java.util.LinkedHashMap;

import topplintowers.crates.CrateType;

public enum Levels {
	FREEMODE(0), 
	ONE(1),	TWO(2), THREE(3),	FOUR(4), 
	FIVE(5),SIX(6),	SEVEN(7),	EIGHT(8), 
	NINE(9),TEN(10),ELEVEN(11),	TWELVE(12); 
	
	private int value;

	
	private Levels(int value) { 
		this.value = value;
	}
}
