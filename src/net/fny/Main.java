package net.fny;

import net.fny.core.CoreEngine;
import net.fny.core.Game;
import net.fny.core.RenderingEngine;
import net.fny.menu.MainMenu;
import net.fny.state.GameState;

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
