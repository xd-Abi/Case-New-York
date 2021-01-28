package net.cny.gui.menu;

import static net.cny.util.ResourceManager.MainMenu.*;

import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.GL_DEPTH_TEST;
import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.glBlendFunc;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;

import net.cny.CoreEngine;
import net.cny.GameState;
import net.cny.Main;
import net.cny.math.Vec2f;
import net.cny.scenegraph.Scene;
import net.cny.gui.GuiBackground;
import net.cny.gui.GuiButton;
import net.cny.gui.GuiClickListener;

public class MainMenu extends Scene
{

	private GuiButton[] buttons;

	public MainMenu() 
	{
		Main.setGameState(GameState.MAIN_MENU);
	}
	
	@Override
	public void Initialize() 
	{
		AddObject(new GuiBackground(BACKGROUND));

		GuiBackground title = new GuiBackground(TITLE);
		title.SetTransformation(new Vec2f(-0.165f, 0.1f), new Vec2f(0.7f, 0.7f));
		title.AddComponent(new GuiClickListener(new Vec2f(-0.95f, 0.1f), new Vec2f(0.7f, 0.7f)));

		AddObject(title);

		buttons = new GuiButton[3];

		buttons[0] = new GuiButton(PLAY_BUTTON, new Vec2f(-0.9f, -0.3f),  new Vec2f(0.4f, 0.17f));
		buttons[1] = new GuiButton(SETTINGS_BUTTON, new Vec2f(-0.9f, -0.52f), new Vec2f(0.4f, 0.17f));
		buttons[2] = new GuiButton(QUIT_BUTTON, new Vec2f(-0.9f, -0.74f), new Vec2f(0.4f, 0.17f));

		for (GuiButton button : buttons)
			AddObject(button);
	}
	
	@Override
	public void Update() 
	{	
		super.Update();

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
