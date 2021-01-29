package net.cny.level;

import net.cny.GameState;
import net.cny.Main;
import net.cny.RenderingEngine;
import net.cny.gui.GuiBackground;
import net.cny.gui.menu.PauseMenu;
import net.cny.platform.Keyboard;
import net.cny.scenegraph.Scene;
import org.lwjgl.glfw.GLFW;

public class FirstLevel extends Scene
{

    public FirstLevel()
    {
        Main.setGameState(GameState.LEVEL_1);
    }

    @Override
    public void Initialize() {
        super.Initialize();
        AddObject(new GuiBackground("icon.png"));
    }

    @Override
    public void Update() {
        super.Update();

        //Open PauseMenu by Clicking ESC
        if (Keyboard.IsKeyReleased(GLFW.GLFW_KEY_ESCAPE))
        {
            RenderingEngine.SetScene(new PauseMenu(this));
        }

    }
}
