package net.cny.scenegraph;

import net.cny.Main;
import net.cny.core.audio.Audio;
import net.cny.gui.GuiBackground;
import net.cny.gui.GuiButton;
import net.cny.core.scenegraph.Scenegraph;
import net.cny.state.GameState;

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

        backgroundAudio = new Audio("menu/main/background_audio.wav");
        backgroundAudio.Play();

        Add("title", new GuiBackground("menu/main/title.png", -0.95f, 0.1f, 0.7f, 0.7f));

        buttons = new GuiButton[3];

        buttons[0] = new GuiButton("menu/main/play-button.png", -0.9f, -0.3f,0.4f, 0.17f);
        buttons[1] =new GuiButton("menu/main/settings-button.png", -0.9f, -0.52f, 0.4f, 0.17f);
        buttons[2] =new GuiButton("menu/main/quit-button.png", -0.9f, -0.74f, 0.4f, 0.17f);

        for (GuiButton button : buttons)
            button.SetTranslation(xPos, button.GetTranslation().y, 0);

        Add("play-button", buttons[0]);
        Add("settings-button", buttons[1]);
        Add("quit-button", buttons[2]);
    }

    @Override
    public void Update() {
        super.Update();

    /*   for (GuiButton button : buttons)
       {

           if (button.GetTranslation().x != -0.9 && !(button.GetTranslation().x > -0.9) && !isAnimationFinished) {
               button.SetTranslation(button.GetTranslation().x += 0.05, button.GetTranslation().y, 0);
           } else {
               isAnimationFinished = true;
           }
       }
*/
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