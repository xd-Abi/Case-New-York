package net.cny;

public class GameState
{

	private static State state;

	public static State GetState() {
		return state;
	}

	public static void SetState(State state) {
		GameState.state = state;
	}

	public enum State {
		MAIN_MENU,
		SETTINGS_MENU,
		PAUSE_MENU,

		FIRST_SCENE,
		LEVEL_1,
	}
}
