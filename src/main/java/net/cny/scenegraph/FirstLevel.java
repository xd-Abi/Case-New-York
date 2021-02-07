package net.cny.scenegraph;

import net.cny.Main;
import net.cny.core.audio.Audio;
import net.cny.core.platform.Mouse;
import net.cny.core.scenegraph.Scenegraph;
import net.cny.gui.GuiBackground;
import net.cny.gui.GuiButton;
import net.cny.state.GameState;

public class FirstLevel extends Scenegraph
{

    private Audio backgroundAudio;
    private GuiButton doorLock;
    private GuiBackground codeLock;

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

        Add("invent_one", new GuiBackground("inventory/inventory.png", -0.95f, 0.4f, 0.15f, 0.25f));
        Add("invent_second", new GuiBackground("inventory/inventory.png", -0.95f, 0.1f, 0.15f, 0.25f));
        Add("invent_third", new GuiBackground("inventory/inventory.png", -0.95f, -0.2f, 0.15f, 0.25f));

        doorLock = new GuiButton(0.77f, 0.26f, 0.8f,0.37f, false);
        codeLock = new GuiBackground("level/one/code_lock.png", -1, -1, 1.9f, 2f);
    }

    @Override
    public void Update() {
        super.Update();

        doorLock.Update();

        if (doorLock.IsPressed())
        {
            System.out.println("TEST");
            Add("background_lock", codeLock);
        }

      //  System.out.println(Mouse.GetOpenGLX() + "  " + Mouse.GetOpenGLY());
    }

    @Override
    public void CleanUp()
    {
        backgroundAudio.CleanUp();
        super.CleanUp();
    }
}