package net.cny.level;

import net.cny.GameState;
import net.cny.renderer.MasterRenderer;
import net.cny.gui.GuiBackground;
import net.cny.gui.menu.PauseMenu;
import net.cny.platform.Keyboard;
import net.cny.scenegraph.Scene;
import org.lwjgl.glfw.GLFW;

public class FirstScene extends Scene
{

    public FirstScene()
    {
        GameState.SetState(GameState.State.FIRST_SCENE);
    }

    @Override
    public void Initialize()
    {
        GuiBackground background = new GuiBackground("menu/main/background.png");

        AddNode(background);
    }

    @Override
    public void Update(float delta)
    {
        super.Update(delta);
        if (Keyboard.IsKeyPushed(GLFW.GLFW_KEY_ESCAPE))
            MasterRenderer.SetScene(new PauseMenu(this));


    }
}
