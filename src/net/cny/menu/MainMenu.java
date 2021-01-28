package net.cny.menu;

import static net.cny.util.ResourceManager.MainMenu.*;
import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.GL_DEPTH_TEST;
import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.glBlendFunc;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;

import net.cny.core.CoreEngine;
import net.cny.core.math.Vec2f;
import net.cny.core.scenegraph.Scene;
import net.cny.gui.GuiBackground;
import net.cny.gui.GuiButton;
import net.cny.gui.GuiClickListener;

public class MainMenu extends Scene
{

	private GuiBackground title;
	private GuiButton playButton;
	private GuiButton settingsButton;
	private GuiButton quitButton;
	
	public MainMenu() 
	{
		
	}
	
	@Override
	public void Initialize() 
	{
		AddObject(new GuiBackground(BACKGROUND));
		AddObject(title = new GuiBackground(TITLE, new Vec2f(-0.165f, 0.1f), new Vec2f(0.7f, 0.7f)));
		AddObject(playButton = new GuiButton(PLAY_BUTTON, new Vec2f(-0.9f, -0.3f),  new Vec2f(0.4f, 0.17f)));
		AddObject(settingsButton = new GuiButton(SETTINGS_BUTTON, new Vec2f(-0.9f, -0.52f), new Vec2f(0.4f, 0.17f)));
		AddObject(quitButton = new GuiButton(QUIT_BUTTON, new Vec2f(-0.9f, -0.74f), new Vec2f(0.4f, 0.17f)));
	
		title.AddComponent(new GuiClickListener(new Vec2f(-0.95f, 0.1f), new Vec2f(0.7f, 0.7f)));
	}
	
	@Override
	public void Update() 
	{	
		super.Update();


		if (playButton.IsHover()) {
			System.out.println("TEST");
		}
		if (settingsButton.IsHover()) {
			System.out.println("TESTSTTST");
		}
		if (quitButton.IsPressed())
			CoreEngine.Stop();
	}
	
	@Override
	public void Render() 
	{
		
		glDisable(GL_DEPTH_TEST);
		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		
		super.Render();
		
		glEnable(GL_DEPTH_TEST);
		glDisable(GL_BLEND);
	}
	
	@Override
	public void CleanUp() {
		super.CleanUp();
	}
}
