package net.cny.scenegraph;

import java.util.HashMap;
import java.util.Map;

import net.cny.Main;
import net.cny.core.audio.Audio;
import net.cny.core.scenegraph.Node;
import net.cny.core.scenegraph.Scenegraph;
import net.cny.gui.GuiBackground;

public class FirstLevelScene extends Scenegraph
{
	
	private Audio audio;
	private Audio car;
	private int time;
	
	private Main main;

	public FirstLevelScene(Main main) 
	{
		this.main = main;
	}
	
	@Override
	public Map<String, Node> CreateMap() 
	{
		
		
		Map<String, Node> map = new HashMap<>();
		map.put("background", new GuiBackground("level/one-scene/background.jpeg"));

		return map;
	}
	
	@Override
	public void Initialize() 
	{
		super.Initialize();
		
		car = new Audio("level/one-scene/car.wav");
		car.SetGain(0.6f);
		car.Play();
		
		audio = new Audio("level/one-scene/dialog.wav");
		audio.SetGain(1.5f);
		audio.Play();
		
		
		time = 16 * (int) Main.frameCap;
	}
	
	@Override
	public void Update() {
		super.Update();
		
		if (time != 0)
		{
			time--;
		}else
		{
			main.SetScenegraph(new SecondLevel(main));
				
		}
	}

	@Override
	public void CleanUp() {
		
		audio.CleanUp();
		car.CleanUp();
		super.CleanUp();
	}
}
