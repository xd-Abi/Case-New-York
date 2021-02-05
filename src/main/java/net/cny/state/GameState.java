package net.cny.state;

public enum GameState
{

    MAIN_MENU,
    FIRST_SCENE;

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
