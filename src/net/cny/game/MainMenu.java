package net.cny.game;

import net.cny.Main;
import net.cny.audio.Audio;
import net.cny.gui.GuiBackground;
import net.cny.gui.GuiButton;
import net.cny.scenegraph.Scenegraph;
import net.cny.state.GameState;
import org.joml.Vector2f;
import org.joml.Vector3f;

public class MainMenu extends Scenegraph
{

    private Audio backgroundAudio;
    private Main main;
    private float xPos;

    private boolean isAnimationFinished;
    private GuiButton[] buttons;

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

        buttons = new GuiButton[3];
        xPos = -0.9f;

        buttons[0] = new GuiButton("menu/main/play-button.png", new Vector2f(xPos, -0.3f),  new Vector2f(0.4f, 0.17f));
        buttons[1] =new GuiButton("menu/main/settings-button.png", new Vector2f(xPos, -0.52f), new Vector2f(0.4f, 0.17f));
        buttons[2] =new GuiButton("menu/main/quit-button.png", new Vector2f(xPos, -0.74f), new Vector2f(0.4f, 0.17f));

        backgroundAudio = new Audio("menu/main/background_audio.wav");
        backgroundAudio.Play();

        for (GuiButton button : buttons)
            button.SetTranslation(xPos, button.GetTranslation().y, 0);

        xPos = xPos * 3;

        Add("play-button", buttons[0]);
        Add("settings-button", buttons[1]);
        Add("quit-button", buttons[2]);
    }

    @Override
    public void Update()
    {
        super.Update();

        if (!isAnimationFinished)
        {
            for (GuiButton button : buttons)
                button.SetTransformation(new Vector2f(xPos, button.GetTranslation().y), new Vector2f(0.4f, 0.17f));
        }

        if (xPos != -0.9 && !(xPos > -0.9) && !isAnimationFinished)
        {
            System.out.println(xPos);
            xPos+= 0.05;
        }else if (!isAnimationFinished)
        {
            for (GuiButton button : buttons)
                button.SetTransformation(new Vector2f(xPos, button.GetTranslation().y), new Vector2f(0.4f, 0.17f));
            isAnimationFinished = true;
        }


        if (buttons[0].IsPressed())
        {
            main.SetScenegraph(new FirstScene());
        }

        if (buttons[2].IsPressed())
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