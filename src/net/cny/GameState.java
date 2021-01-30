package net.cny;

public enum GameState
{

	MAIN_MENU,
	SETTINGS_MENU,
	PAUSE_MENU,

	FIRST_SCENE,
	LEVEL_1;

	private static GameState state;

	public static GameState GetState() {
		return state;
	}

	public static void SetState(GameState state) {
		GameState.state = state;
	}

}
