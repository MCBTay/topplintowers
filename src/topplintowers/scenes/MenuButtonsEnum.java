package topplintowers.scenes;

public enum MenuButtonsEnum {
	QUIT(0), 
	KILL_APP(1),
	CANCEL(2), 
	RESUME(3), 
	RESTART(4),
	MAIN_MENU(5), 
	LEVEL_SELECT(6), 
	FREE_MODE(7);
	
	private int value;

	private MenuButtonsEnum(int value) { 
		this.value = value;
	}
	
	public int getValue() {
		return this.value;
	}
	
	public MenuButtonsEnum getEnum(int value) {
		return this;
	}
}
