package net.cny.gui;

import net.cny.Main;

public class GuiTimedBackground extends GuiBackground
{

    private int time;
    private boolean isTimeFinished;

    public GuiTimedBackground(String path, int time)
    {
        super(path);
        this.time = time * (int) Main.FRAME_CAP;
    }

    @Override
    public void Update(float delta)
    {

        if (isTimeFinished)
            return;

        super.Update(delta);

        if (time == 0)
        {
            isTimeFinished = true;
        }
        else if (time != -2)
            time--;
    }

    @Override
    public void Render()
    {
        if (!isTimeFinished)
            super.Render();
    }

    public boolean IsTimeFinished()
    {
        return isTimeFinished;
    }
}
