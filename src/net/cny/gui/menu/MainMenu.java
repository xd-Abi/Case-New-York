package net.cny.gui.menu;

import static net.cny.util.ResourceManager.MainMenu.*;

import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.GL_DEPTH_TEST;
import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.glBlendFunc;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;

import net.cny.Main;
import net.cny.renderer.MasterRenderer;
import net.cny.scenegraph.Scenegraph;
import net.cny.GameState;
import net.cny.audio.Sound;
import net.cny.gui.GuiBackground;
import net.cny.level.FirstScene;
import net.cny.gui.GuiButton;
import org.joml.Vector2f;

public class MainMenu extends Scenegraph
{

	private GuiButton[] buttons;
	private Sound backgroundAudio;

	public MainMenu() 
	{
		GameState.SetState(GameState.MAIN_MENU);
	}
	
	@Override
	public void Initialize() 
	{
		super.Initialize();

		AddNode(new GuiBackground(BACKGROUND));
		AddNode(new GuiBackground(TITLE, new Vector2f(-0.97f, 0.1f), new Vector2f(0.7f, 0.7f)));

		buttons = new GuiButton[3];
		buttons[0] = new GuiButton(PLAY_BUTTON, new Vector2f(-0.9f, -0.3f),  new Vector2f(0.4f, 0.17f));
		buttons[1] = new GuiButton(SETTINGS_BUTTON, new Vector2f(-0.9f, -0.52f), new Vector2f(0.4f, 0.17f));
		buttons[2] = new GuiButton(QUIT_BUTTON, new Vector2f(-0.9f, -0.74f), new Vector2f(0.4f, 0.17f));

		for (GuiButton button : buttons)
			AddNode(button);

		backgroundAudio = new Sound(BACKGROUND_AUDIO);
		backgroundAudio.Play();
	}
	
	@Override
	public void Update(float delta)
	{	
		super.Update(delta);

		if (buttons[0].IsPressed())
		{
			MasterRenderer.SetScene(new FirstScene());
		}

		if (buttons[2].IsPressed())
			Main.cny.Stop();

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
	public void CleanUp()
	{
		backgroundAudio.Stop();
		super.CleanUp();
	}
}
