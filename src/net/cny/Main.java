package net.cny;

import net.cny.gui.menu.MainMenu;

public class Main
{
	
	private static GameState gameState;
	
	public Main()
	{
		CoreEngine.Start(this);
	}
	
	public void Initialize()
	{
		RenderingEngine.SetScene(new MainMenu());
	}

	public static GameState getGameState()
	{
		return gameState;
	}
	
	public static void setGameState(GameState gameState) 
	{
		Main.gameState = gameState;
	}
}
