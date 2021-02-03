package old.cny.gui.menu;

import old.cny.Main;
import old.cny.audio.Sound;
import old.cny.gui.GuiBackground;
import old.cny.gui.GuiButton;
import old.cny.level.FirstScene;
import old.cny.scenegraph.Scenegraph;
import org.joml.Vector2f;

import static old.cny.util.ResourceManager.MainMenu.*;

public class MainMenu extends Scenegraph
{

	private Sound backgroundAudio;

	public MainMenu() 
	{
		super(Main.GameState.MAIN_MENU);
	}
	
	@Override
	public void Initialize() 
	{
		GuiBackground background = new GuiBackground(BACKGROUND);
		backgroundAudio = new Sound(BACKGROUND_AUDIO);

		AddNode("background", new GuiBackground(BACKGROUND));
		AddNode("title", new GuiBackground(TITLE,  new Vector2f(-0.95f, 0.1f), new Vector2f(0.7f, 0.7f)));

		AddNode("play-button", new GuiButton(PLAY_BUTTON, new Vector2f(-0.9f, -0.3f),  new Vector2f(0.4f, 0.17f)));
		AddNode("settings-button", new GuiButton(SETTINGS_BUTTON, new Vector2f(-0.9f, -0.52f), new Vector2f(0.4f, 0.17f)));
		AddNode("quit-button", new GuiButton(QUIT_BUTTON, new Vector2f(-0.9f, -0.74f), new Vector2f(0.4f, 0.17f)));

		backgroundAudio.Play();
	}
	
	@Override
	public void Update(float delta)
	{	
		super.Update(delta);

		if (((GuiButton)GetNode("play-button")).IsPressed())
		{
			Main.cny.SetScenegraph(new FirstScene(), true);
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
