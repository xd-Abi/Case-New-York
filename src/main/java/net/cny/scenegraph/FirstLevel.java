package net.cny.scenegraph;

import net.cny.core.scenegraph.Scenegraph;
import net.cny.gui.GuiBackground;
import net.cny.state.GameState;

public class FirstLevel extends Scenegraph
{

    public FirstLevel()
    {
        GameState.SetState(GameState.LEVEL_1);
    }

    @Override
    public void Initialize() {

        Add("background", new GuiBackground("level/one/background.png", -1.2f, -1.05f, 2.3f, 2.3f));
    }
}
