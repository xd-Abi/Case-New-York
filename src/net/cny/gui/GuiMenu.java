package net.cny.gui;

import net.cny.audio.Sound;
import net.cny.math.Vec2f;
import net.cny.scenegraph.Scene;

public class GuiMenu extends Scene
{

    private Sound backgroundSound;
    private GuiBackground background;


    public GuiMenu(String backgroundPath, String soundPath)
    {
        SetBackground(new GuiBackground(backgroundPath));
        SetBackgroundSound(new Sound(soundPath));
    }

    public void SetTitle(String path, Vec2f position, Vec2f scale)
    {
        AddObject(new GuiBackground(path, position, scale));
    }

    @Override
    public void Initialize() {

        AddObject(background);
        backgroundSound.Play();
    }

    @Override
    public void CleanUp() {
        backgroundSound.Stop();
        super.CleanUp();
    }

    public Sound GetBackgroundSound()
    {
        return backgroundSound;
    }

    public void SetBackgroundSound(Sound backgroundSound)
    {
        this.backgroundSound = backgroundSound;
    }

    public GuiBackground GetBackground()
    {
        return background;
    }

    public void SetBackground(GuiBackground background)
    {
        this.background = background;
    }
}
