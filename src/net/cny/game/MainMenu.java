package net.cny.game;

import net.cny.Main;
import net.cny.audio.Audio;
import net.cny.gui.GuiBackground;
import net.cny.gui.GuiButton;
import net.cny.scenegraph.Scenegraph;
import net.cny.state.GameState;
import org.joml.Vector2f;

public class MainMenu extends Scenegraph
{

    private Audio backgroundAudio;
    private Main main;

    public MainMenu(Main main)
    {
        this.main = main;
    }

    @Override
    public void Initialize()
    {

        GameState.SetState(GameState.MAIN_MENU);

        Add("background", new GuiBackground("menu/main/background.png"));
        Add("title", new GuiBackground("menu/main/title.png",  new Vector2f(-0.95f, 0.1f), new Vector2f(0.7f, 0.7f)));

        Add("play-button", new GuiButton("menu/main/play-button.png", new Vector2f(-0.9f, -0.3f),  new Vector2f(0.4f, 0.17f)));
        Add("settings-button", new GuiButton("menu/main/settings-button.png", new Vector2f(-0.9f, -0.52f), new Vector2f(0.4f, 0.17f)));
        Add("quit-button", new GuiButton("menu/main/quit-button.png", new Vector2f(-0.9f, -0.74f), new Vector2f(0.4f, 0.17f)));

        backgroundAudio = new Audio("menu/main/background_audio.wav");
        backgroundAudio.Play();
    }

    @Override
    public void Update()
    {
        super.Update();

        if (((GuiButton)GetNode("play-button")).IsPressed())
        {
            System.out.println("TEST");
        }

        if (((GuiButton)GetNode("quit-button")).IsPressed())
        {
            main.Stop();
        }
    }

    @Override
    public void CleanUp()
    {
        backgroundAudio.CleanUp();
        super.CleanUp();
    }
}