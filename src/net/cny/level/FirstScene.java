package net.cny.level;

import net.cny.GameState;
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

    public FirstScene()
    {
        GameState.SetState(GameState.FIRST_SCENE);
    }

    @Override
    public void Initialize()
    {
        trainStationBackground = new GuiBackground("scene1/background_train_station.png");
        AddNode(trainStationBackground);

        trainStoppingSound = new Sound("scene1/train_sound.wav");
        trainStoppingSound.Play();

        footStepsSound = new Sound("scene1/footsteps.wav");
        footStepsSound.SetLoop(true);
        footStepsSound.Play();

        letterBackground = new GuiBackground("scene1/background_table_letter.png");

    }

    int trainStoppingTime = 540;

    @Override
    public void Update(float delta)
    {
        super.Update(delta);

        if (trainStoppingTime == 0)
        {


            RemoveNode(trainStationBackground);
            AddNode(letterBackground);

            footStepsSound.Stop();
        }

        if (trainStoppingTime != 0)
            trainStoppingTime--;

    }

    @Override
    public void Render() {
        glDisable(GL_DEPTH_TEST);
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

        super.Render();

        glEnable(GL_DEPTH_TEST);
        glDisable(GL_BLEND);
    }
}
