package net.cny.gui.menu;

import net.cny.Main;
import net.cny.Main;
import net.cny.gui.GuiBackground;
import net.cny.audio.Sound;
import net.cny.gui.GuiButton;
import net.cny.scenegraph.Scenegraph;
import org.joml.Vector2f;

import static net.cny.util.ResourceManager.MainMenu.TITLE;
import static net.cny.util.ResourceManager.MainMenu.QUIT_BUTTON;
import static net.cny.util.ResourceManager.MainMenu.BACKGROUND_AUDIO;
import static net.cny.util.ResourceManager.MainMenu.SETTINGS_BUTTON;

import static net.cny.util.ResourceManager.PauseMenu.*;

public class PauseMenu extends Scenegraph
{

	private GuiButton[] buttons;
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
		super.Initialize();

		GuiBackground background = new GuiBackground(BACKGROUND);
		background.AddComponent(backgroundAudio = new Sound(BACKGROUND_AUDIO, true));

		AddNode(background);
		AddNode(new GuiBackground(TITLE, new Vector2f(-0.95f, 0.1f), new Vector2f(0.7f, 0.7f)));

		buttons = new GuiButton[3];
		buttons[0] = new GuiButton(RESUME_BUTTON, new Vector2f(-0.9f, -0.3f),  new Vector2f(0.4f, 0.17f));
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
			Main.cny.SetScenegraph(oldScene, false);
			oldScene.OnResume();
			Main.cny.SetState(oldScene.GetState());
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
