package net.cny.level;

import net.cny.Main;
import net.cny.audio.Sound;
import net.cny.gui.GuiBackground;
import net.cny.scenegraph.Scenegraph;
import org.joml.Vector2f;

public class FirstScene extends Scenegraph
{

    private Sound trainStoppingSound;
    private Sound footStepsSound;
    private GuiBackground trainStationBackground;
    private GuiBackground letterBackground;
    private int trainStoppingTime;
    private GuiBackground london_1997;

    public FirstScene()
    {
        super(Main.GameState.FIRST_SCENE);
    }

    @Override
    public void Initialize()
    {
        trainStationBackground = new GuiBackground("scene1/background_train_station.png");
        trainStoppingSound = new Sound("scene1/train_sound.wav", true);
        trainStoppingSound.Play();
        trainStationBackground.AddComponent(trainStoppingSound);

        AddNode(trainStationBackground);

        footStepsSound = new Sound("scene1/footsteps.wav", false);
        footStepsSound.SetLoop(true);
        footStepsSound.Play();

        trainStationBackground.AddComponent(footStepsSound);

        letterBackground = new GuiBackground("scene1/background_table_letter.png");

        london_1997 = new GuiBackground("scene1/london_1997.png", new Vector2f(0.9f, -0.8f), new Vector2f(0.35f, 0.15f));

        trainStoppingTime = (int) trainStoppingSound.GetTime();
    }

    @Override
    public void OnPause()
    {
        if (trainStoppingTime != -2)
        {
            trainStoppingSound.Pause();
            footStepsSound.Pause();
        }
    }

    @Override
    public void OnResume()
    {
        if (trainStoppingTime != -2)
        {
            trainStoppingSound.Play();
            footStepsSound.Play();
        }
    }

    @Override
    public void Update(float delta)
    {
        super.Update(delta);

        if (trainStoppingTime <= 8 * Main.FRAME_CAP && !GetNodeObjects().contains(london_1997))
        {
            AddNode(london_1997);
            System.out.println("ADDED");
        }

        if (trainStoppingTime <= 7.9999f * Main.FRAME_CAP && trainStoppingTime != 6 * Main.FRAME_CAP)
        {
            london_1997.SetTranslation(0.7f, -0.8f, 0);
        }
        else if (trainStoppingTime == (int)(4 * Main.FRAME_CAP))
        {
            london_1997.SetTranslation(1.7f, -0.8f, 0);
        }

        if (trainStoppingTime == 0)
        {
            trainStationBackground.CleanUp();
            london_1997.CleanUp();
            RemoveNode(london_1997);
            RemoveNode(trainStationBackground);


           AddNode(letterBackground);


            trainStoppingTime = -2;
        }

        if (trainStoppingTime > -1)
        {
            trainStoppingTime--;
        }

        System.out.println(trainStoppingTime);
    }

    @Override
    public void CleanUp() {
        super.CleanUp();
    }
}
