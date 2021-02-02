package net.cny.level;

import net.cny.Main;
import net.cny.audio.Sound;
import net.cny.gui.GuiBackground;
import net.cny.gui.GuiTimedBackground;
import net.cny.scenegraph.Scenegraph;

import java.sql.Time;

public class FirstScene extends Scenegraph
{
    private GuiTimedBackground introBackground;
    private GuiTimedBackground letterBackground;
    private GuiTimedBackground tsLondonBackground;
    private Sound currentSound;

    public FirstScene()
    {
        super(Main.GameState.FIRST_SCENE);
    }

    @Override
    public void Initialize()
    {
        introBackground = new GuiTimedBackground("scene1/background/intro.png", 3);
        introBackground.SetTag("notFinished");
        currentSound = new Sound("scene1/williams.wav");
        AddNode("intro-background", introBackground);
    }

    @Override
    public void Update(float delta)
    {
        super.Update(delta);

        if (introBackground.IsTimeFinished() && introBackground.GetTag().equals("notFinished"))
        {
            RemoveNode("intro-background");
            introBackground.SetTag("Finished");
            System.out.println(currentSound.GetTime());
            letterBackground = new GuiTimedBackground("scene1/background/letter.png", 64);
            letterBackground.SetTag("notFinished");

            AddNode("letter-background", letterBackground);
            currentSound.Play();
        }

        if (letterBackground != null && letterBackground.IsTimeFinished() && letterBackground.GetTag().equals("notFinished"))
        {
            RemoveNode("letter-background");
            letterBackground.SetTag("Finished");

            currentSound = new Sound("scene1/train_sound.wav");
            tsLondonBackground = new GuiTimedBackground("scene1/background/train_station.png", (int)currentSound.GetTime());
            tsLondonBackground.SetTag("notFinished");

            AddNode("ts-london-background", tsLondonBackground);
            currentSound.Play();
        }
    }

    @Override
    public void CleanUp()
    {
        currentSound.CleanUp();
        super.CleanUp();
    }
}
