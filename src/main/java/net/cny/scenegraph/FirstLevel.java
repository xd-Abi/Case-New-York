package net.cny.scenegraph;

import net.cny.Main;
import net.cny.core.audio.Audio;
import net.cny.core.scenegraph.Scenegraph;
import net.cny.gui.GuiBackground;
import net.cny.gui.GuiButton;
import net.cny.state.GameState;

public class FirstLevel extends Scenegraph
{

    private Audio backgroundAudio;

    public FirstLevel()
    {

    }

    @Override
    public void Initialize()
    {

        GameState.SetState(GameState.LEVEL_1);

        Add("background", new GuiBackground("level/one/background.png"));
        Add("background2", new GuiBackground("level/one/msg.png"));

        backgroundAudio = new Audio("level/one/bg_audio.wav");
        backgroundAudio.Play();

        Add("invent_one", new GuiBackground("inventory/inventory.png", -0.95f, 0.1f, 0.15f, 0.25f));
        Add("invent_second", new GuiBackground("inventory/inventory.png", -0.95f, -0.11f, 0.15f, 0.25f));
        Add("invent_third", new GuiBackground("inventory/inventory.png", -0.95f, -0.32f, 0.15f, 0.25f));

    }

    @Override
    public void Update() {
        super.Update();
    }

    @Override
    public void CleanUp()
    {
        backgroundAudio.CleanUp();
        super.CleanUp();
    }
}