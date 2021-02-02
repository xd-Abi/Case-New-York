package net.cny.level;

import net.cny.Main;
import net.cny.Main;
import net.cny.gui.GuiBackground;
import net.cny.scenegraph.Scenegraph;

public class FirstScene extends Scenegraph
{
    private GuiBackground background;

    public FirstScene()
    {
        super(Main.GameState.FIRST_SCENE);
    }

    @Override
    public void Initialize()
    {

        background = new GuiBackground("scene1/background_letter.png");


        AddNode(background);
    }

    @Override
    public void Update(float delta) {

    }
}
