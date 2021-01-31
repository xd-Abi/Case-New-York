package net.cny.level;

import net.cny.GameState;
import net.cny.audio.Sound;
import net.cny.gui.GuiBackground;
import net.cny.scenegraph.Scenegraph;

public class FirstScene extends Scenegraph
{

    private Sound trainStoppingSound;
    private Sound footStepsSound;
    private GuiBackground trainStationBackground;
    private GuiBackground letterBackground;
    private int trainStoppingTime;

    public FirstScene()
    {
        super(GameState.FIRST_SCENE);
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

        if (trainStoppingTime == 0)
        {
            trainStationBackground.CleanUp();
            RemoveNode(trainStationBackground);

           AddNode(letterBackground);

            trainStoppingTime = -2;
        }

        if (trainStoppingTime > -1)
            trainStoppingTime--;


        System.out.println(trainStoppingTime);
    }

    @Override
    public void CleanUp() {
        super.CleanUp();
    }
}
