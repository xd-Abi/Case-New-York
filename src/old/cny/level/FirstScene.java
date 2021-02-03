package old.cny.level;

import old.cny.Main;
import static old.cny.Main.ResourceLocation.SCENE_ONE;
import old.cny.audio.Sound;
import old.cny.gui.GuiTimedBackground;
import old.cny.scenegraph.Scenegraph;

public class FirstScene extends Scenegraph
{
    private GuiTimedBackground introBackground;
    private GuiTimedBackground letterBackground;
    private GuiTimedBackground trainDrivingBackground;
    private GuiTimedBackground tsBackground;
    private Sound currentSound;

    public FirstScene()
    {
        super(Main.GameState.FIRST_SCENE);
    }

    @Override
    public void Initialize()
    {
        introBackground = new GuiTimedBackground( SCENE_ONE + "background/intro.png", 3);
        introBackground.SetTag("notFinished");
        currentSound = new Sound(SCENE_ONE + "audio/williams.wav");
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
            letterBackground = new GuiTimedBackground(SCENE_ONE + "background/letter.png", 2);
            letterBackground.SetTag("notFinished");

            AddNode("letter-background", letterBackground);
            currentSound.Play();
        }

        if (letterBackground != null && letterBackground.IsTimeFinished() && letterBackground.GetTag().equals("notFinished"))
        {
            RemoveNode("letter-background");
            letterBackground.SetTag("Finished");

            currentSound.Stop();
            currentSound = new Sound(SCENE_ONE + "audio/train_driving.wav");
            trainDrivingBackground = new GuiTimedBackground(SCENE_ONE + "background/train_driving.png", (int)currentSound.GetTime());
            trainDrivingBackground.SetTag("notFinished");

            AddNode("train-driving-background", trainDrivingBackground);
            currentSound.Play();
        }

        if (trainDrivingBackground != null && trainDrivingBackground.IsTimeFinished() && trainDrivingBackground.GetTag().equals("notFinished"))
        {
            RemoveNode("train-driving-background");
            trainDrivingBackground.SetTag("Finished");

            currentSound.Stop();
            currentSound = new Sound(SCENE_ONE + "audio/train_stopping.wav");
            tsBackground = new GuiTimedBackground(SCENE_ONE + "background/ts_ny.png", (int)currentSound.GetTime());
            tsBackground.SetTag("notFinished");

            AddNode("train-driving-background", tsBackground);
            currentSound.Play();

            Main.cny.SetScenegraph(new FirstLevel(), true);
        }
    }
     @Override
    public void CleanUp()
    {
        currentSound.CleanUp();
        super.CleanUp();
    }
}
