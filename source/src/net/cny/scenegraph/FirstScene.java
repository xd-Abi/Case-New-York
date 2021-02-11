package net.cny.scenegraph;

import java.util.Map;

import net.cny.core.CoreEngine;
import net.cny.core.audio.Audio;
import net.cny.gui.GuiBackground;
import net.cny.core.scenegraph.Node;
import net.cny.core.scenegraph.Scenegraph;
import net.cny.state.GameState;

public class FirstScene extends Scenegraph
{

    private GuiBackground background;
    private Audio audio;
    private int timer;

    public FirstScene()
    {
        GameState.SetState(GameState.FIRST_SCENE);
    }

    @Override
    public void Initialize()
    {
        background = new GuiBackground("scene/first/background/intro.png");
        background.SetTag("nf-intro");
        Add("background", background);

        timer = 3 * (int)CoreEngine.frameCap;
    }

    @Override
    public void Update()
    {
        super.Update();

        if (timer == 0 && background.GetTag().equals("nf-intro"))
        {
            background = new GuiBackground("scene/first/background/letter.png");
            background.SetTag("nf-letter");

            audio = new Audio("scene/first/audio/williams.wav");
            audio.Play();

            timer = 64 * (int) CoreEngine.frameCap;

            Replace("background", background);
        }

        if (timer == 0 && background.GetTag().equals("nf-letter"))
        {
            background = new GuiBackground("scene/first/background/train_driving.jpg");
            background.SetTag("nf-train-driving");

            audio = new Audio("scene/first/audio/train_driving.wav");
            audio.Play();

            timer = (int)audio.GetTime() * (int)CoreEngine.frameCap;

            Replace("background", background);
        }

        if (timer == 0 && background.GetTag().equals("nf-train-driving"))
        {
            background = new GuiBackground("scene/first/background/ts_ny.png");
            background.SetTag("nf-train-stopping");

            audio = new Audio("scene/first/audio/train_stopping.wav");
            audio.Play();

            timer = (int)audio.GetTime() * (int)CoreEngine.frameCap;

            Replace("background", background);
        }


        if (timer != -1)
            timer --;
    }

	@Override
	public Map<String, Node> CreateMap() {
		// TODO Auto-generated method stub
		return null;
	}
}
