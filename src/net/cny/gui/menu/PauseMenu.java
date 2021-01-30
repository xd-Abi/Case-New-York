package net.cny.gui.menu;

import net.cny.Main;
import net.cny.gui.GuiBackground;
import net.cny.GameState;
import net.cny.audio.Sound;
import net.cny.gui.GuiButton;
import net.cny.renderer.MasterRenderer;
import net.cny.scenegraph.Scenegraph;
import org.joml.Vector2f;

import static net.cny.util.ResourceManager.MainMenu.TITLE;
import static net.cny.util.ResourceManager.MainMenu.QUIT_BUTTON;
import static net.cny.util.ResourceManager.MainMenu.BACKGROUND_AUDIO;
import static net.cny.util.ResourceManager.MainMenu.SETTINGS_BUTTON;

import static net.cny.util.ResourceManager.PauseMenu.*;

import static org.lwjgl.opengl.GL11.*;

public class PauseMenu extends Scenegraph
{

	private GuiButton[] buttons;
	private Sound backgroundAudio;
	private final Scenegraph oldScene;

	public PauseMenu(Scenegraph oldScene)
	{
		this.oldScene = oldScene;
		GameState.SetState(GameState.PAUSE_MENU);
	}
	
	@Override
	public void Initialize() 
	{
		super.Initialize();
		AddNode(new GuiBackground(BACKGROUND));
		AddNode(new GuiBackground(TITLE, new Vector2f(-0.95f, 0.1f), new Vector2f(0.7f, 0.7f)));

		buttons = new GuiButton[3];
		buttons[0] = new GuiButton(RESUME_BUTTON, new Vector2f(-0.9f, -0.3f),  new Vector2f(0.4f, 0.17f));
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
			MasterRenderer.SetScene(oldScene);
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
