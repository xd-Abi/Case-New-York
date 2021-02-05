package net.cny.state;

public enum GameState
{

    MAIN_MENU,
    FIRST_SCENE,
    LEVEL_1;

    public static GameState state;

    public static void SetState(GameState state)
    {
        GameState.state = state;
    }

    public static GameState SetState()
    {
        return state;
    }
}
