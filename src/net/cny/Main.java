package net.cny;

import net.cny.core.CoreEngine;
import net.cny.core.Game;
import net.cny.core.RenderingEngine;
import net.cny.menu.MainMenu;
import net.cny.state.GameState;

public class Main implements Game
{
	
	private static GameState gameState;
	
	public Main()
	{
		CoreEngine.Start(this);
	}
	
	@Override
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
