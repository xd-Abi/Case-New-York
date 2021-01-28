package net.cny.core;

import net.cny.core.audio.SoundManager;
import net.cny.core.platform.Keyboard;
import net.cny.core.platform.Mouse;
import net.cny.core.platform.Window;
import net.cny.core.util.Time;

public class CoreEngine 
{

	private static boolean isRunning;
	private static double frameTime = 1.0 / 60;
	private static Game game;
	
	public static void Start(Game game)
	{
		if (isRunning)
			isRunning = true;
		
		CoreEngine.game = game;
		Run();
	}
	
	public static void Stop()
	{
		if (!isRunning)
			return;
		
		isRunning = false;
	}
	
	private static void Initialize()
	{
		Window.CreateWindow();
		Keyboard.Create();
		Mouse.Create();

		SoundManager.Initialize();
		RenderingEngine.Initialize();


		game.Initialize();
	}
	
	private static void Run()
	{	
		isRunning = true;
		
		Initialize();
		
		int frames = 0;
		double frameCounter = 0;
		double lastTime = Time.GetTime();
		double unprocessedTime = 0;
		
		while (isRunning)
		{
			boolean render = false;
			
			double startTime = Time.GetTime();
			double passedTime = startTime - lastTime;
			lastTime = startTime;
			
			unprocessedTime += passedTime;
			frameCounter += passedTime;
			
			while (unprocessedTime > frameTime)
			{
				render = true;
				
				unprocessedTime -= frameTime;
				
				if (Window.IsCloseRequested())
					Stop();
				
				Time.setDelta(frameTime);
				Update();
				
				if (frameCounter >= 1.0)
				{
					System.out.println(frames);
					frames = 0;
					frameCounter = 0;
				}
			}
			
			if (render)
			{
				Render();
				frames++;
			}
			else
			{
				try 
				{
					Thread.sleep(1);
				} 
				catch (InterruptedException e) 
				{
					e.printStackTrace();
				}
			}
		}
		
		CleanUp();
	}
	
	private static void Update()
	{
		RenderingEngine.Update();
		Keyboard.Update();
		Mouse.Update();
		Window.Update();
	}
	
	private static void Render()
	{
		RenderingEngine.Render();
	}
	
	private static void CleanUp()
	{
		RenderingEngine.CleanUp();
		SoundManager.CleanUp();
		Keyboard.Destroy();
		Mouse.Destroy();
		Window.Dispose();
	}
}