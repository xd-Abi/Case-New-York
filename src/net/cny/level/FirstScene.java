package net.cny.level;

import net.cny.GameState;
import net.cny.Main;
import net.cny.audio.Sound;
import net.cny.gui.GuiBackground;
import net.cny.scenegraph.Scenegraph;

import static org.lwjgl.opengl.GL11.*;

public class FirstScene extends Scenegraph
{

    private Sound trainStoppingSound;
    private Sound footStepsSound;
    private GuiBackground trainStationBackground;
    private GuiBackground letterBackground;
    private GuiBackground iconBackground;
    private int trainStoppingTime;

    public FirstScene()
    {
        Main.cny.SetState(GameState.FIRST_SCENE);
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

    }

    @Override
    public void CleanUp() {
        super.CleanUp();
    }
}
