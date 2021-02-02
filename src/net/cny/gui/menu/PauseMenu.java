package net.cny.gui.menu;

import net.cny.Main;
import net.cny.audio.Sound;
import net.cny.gui.GuiBackground;
import net.cny.gui.GuiButton;
import net.cny.scenegraph.Scenegraph;
import org.joml.Vector2f;

import static net.cny.util.ResourceManager.MainMenu.*;
import static net.cny.util.ResourceManager.PauseMenu.BACKGROUND;
import static net.cny.util.ResourceManager.PauseMenu.RESUME_BUTTON;

public class PauseMenu extends Scenegraph
{

	private Sound backgroundAudio;
	private final Scenegraph oldScene;

	public PauseMenu(Scenegraph oldScene)
	{
		super(Main.GameState.PAUSE_MENU);
		this.oldScene = oldScene;
	}
	
	@Override
	public void Initialize() 
	{
		AddNode("background", new GuiBackground(BACKGROUND));
		AddNode("title", new GuiBackground(TITLE, new Vector2f(-0.95f, 0.1f), new Vector2f(0.7f, 0.7f)));

		AddNode("settings-button", new GuiButton(SETTINGS_BUTTON, new Vector2f(-0.9f, -0.52f), new Vector2f(0.4f, 0.17f)));
		AddNode("quit-button", new GuiButton(QUIT_BUTTON, new Vector2f(-0.9f, -0.74f), new Vector2f(0.4f, 0.17f)));
		AddNode("resume-button", new GuiButton(SETTINGS_BUTTON, new Vector2f(-0.9f, -0.3f),  new Vector2f(0.4f, 0.17f)));

		backgroundAudio = new Sound(BACKGROUND_AUDIO);
		backgroundAudio.Play();

	}
	
	@Override
	public void Update(float delta)
	{	
		super.Update(delta);

		if (((GuiButton)GetNode("resume-button")).IsPressed())
		{
			Main.cny.SetScenegraph(oldScene, false);
			oldScene.OnResume();
			Main.cny.SetState(oldScene.GetState());
		}

		if (((GuiButton)GetNode("quit-button")).IsPressed())
			Main.cny.ForceWindowToClose();
	}

	@Override
	public void CleanUp()
	{
		backgroundAudio.CleanUp();
		super.CleanUp();
	}
}
