package old.cny.level;

import old.cny.Main;
import old.cny.gui.GuiBackground;
import old.cny.player.Player;
import old.cny.scenegraph.Scenegraph;

public class FirstLevel extends Scenegraph
{

    public FirstLevel()
    {
        super(Main.GameState.LEVEL_1);
    }

    @Override
    public void Initialize() {
        super.Initialize();
        AddNode("key", new GuiBackground("menu/main/background.png"));
        AddNode("player",  new Player());
    }
}