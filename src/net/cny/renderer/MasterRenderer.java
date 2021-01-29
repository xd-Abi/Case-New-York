package net.cny.renderer;

import static org.lwjgl.opengl.GL11.*;

import net.cny.platform.Window;
import net.cny.model.Mesh;
import net.cny.scenegraph.Scene;

public class MasterRenderer
{

    private static Scene scene;

    public static void Update(float delta)
    {
        if (scene != null)
            scene.Update(delta);
    }

    public static void Render()
    {
        glClear(GL_COLOR_BUFFER_BIT);

        if (scene != null)
            scene.Render();

        Window.Render();
    }

    public static void CleanUp()
    {
        if (scene != null)
            scene.CleanUp();

        Mesh.GetInstance().Delete();
    }

    public static void SetScene(Scene newScene)
    {
        if (scene != null)
        {
            scene.CleanUp();
        }

        newScene.Initialize();
        scene = newScene;
    }
}
