package net.cny.gui.menu;

import static net.cny.util.ResourceManager.MainMenu.*;

import net.cny.Main;
import net.cny.level.FirstScene;
import net.cny.scenegraph.Scenegraph;
import net.cny.audio.Sound;
import net.cny.gui.GuiBackground;
import net.cny.gui.GuiButton;
import org.joml.Vector2f;

public class MainMenu extends Scenegraph
{

	private GuiButton[] buttons;
	private Sound backgroundAudio;

	public MainMenu() 
	{
		super(Main.GameState.MAIN_MENU);
	}
	
	@Override
	public void Initialize() 
	{
		super.Initialize();

		GuiBackground background = new GuiBackground(BACKGROUND);
		background.AddComponent(backgroundAudio = new Sound(BACKGROUND_AUDIO, true));

		AddNode(background);
		AddNode(new GuiBackground(TITLE,  new Vector2f(-0.95f, 0.1f), new Vector2f(0.7f, 0.7f)));

		buttons = new GuiButton[3];
		buttons[0] = new GuiButton(PLAY_BUTTON, new Vector2f(-0.9f, -0.3f),  new Vector2f(0.4f, 0.17f));
		buttons[1] = new GuiButton(SETTINGS_BUTTON, new Vector2f(-0.9f, -0.52f), new Vector2f(0.4f, 0.17f));
		buttons[2] = new GuiButton(QUIT_BUTTON, new Vector2f(-0.9f, -0.74f), new Vector2f(0.4f, 0.17f));

		for (GuiButton button : buttons)
			AddNode(button);

		backgroundAudio.Play();
	}
	
	@Override
	public void Update(float delta)
	{	
		super.Update(delta);

		if (buttons[0].IsPressed())
		{
			Main.cny.SetScenegraph(new FirstScene(), true);
		}

		if (buttons[2].IsPressed())
			Main.cny.ForceWindowToClose();

	}

	@Override
	public void CleanUp()
	{
		super.CleanUp();
	}
}
