package net.cny.gui.menu;

import net.cny.CoreEngine;
import net.cny.GameState;
import net.cny.Main;
import net.cny.RenderingEngine;
import net.cny.audio.Sound;
import net.cny.gui.GuiButton;
import net.cny.gui.GuiMenu;
import net.cny.level.FirstLevel;
import net.cny.math.Vec2f;
import net.cny.scenegraph.Scene;

import static net.cny.util.ResourceManager.MainMenu.*;
import static net.cny.util.ResourceManager.PauseMenu.*;

import static org.lwjgl.opengl.GL11.*;

public class PauseMenu extends GuiMenu
{

	private GuiButton[] buttons;
	private Sound backgroundAudio;
	private Scene oldScene;

	public PauseMenu(Scene oldScene)
	{
		super(BACKGROUND, BACKGROUND_AUDIO);
		this.oldScene = oldScene;
		Main.setGameState(GameState.MAIN_MENU);
	}
	
	@Override
	public void Initialize() 
	{
		super.Initialize();
		super.SetTitle(TITLE, new Vec2f(-0.95f, 0.1f), new Vec2f(0.7f, 0.7f));

		buttons = new GuiButton[3];
		buttons[0] = new GuiButton(RESUME_BUTTON, new Vec2f(-0.9f, -0.3f),  new Vec2f(0.4f, 0.17f));
		buttons[1] = new GuiButton(SETTINGS_BUTTON, new Vec2f(-0.9f, -0.52f), new Vec2f(0.4f, 0.17f));
		buttons[2] = new GuiButton(QUIT_BUTTON, new Vec2f(-0.9f, -0.74f), new Vec2f(0.4f, 0.17f));

		for (GuiButton button : buttons)
			AddObject(button);
	}
	
	@Override
	public void Update() 
	{	
		super.Update();

		if (buttons[0].IsPressed())
		{
			RenderingEngine.SetScene(oldScene);
		}

		if (buttons[2].IsPressed())
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
}
