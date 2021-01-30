package net.cny.renderer;

import static org.lwjgl.opengl.GL11.*;

import net.cny.pipeline.DefaultShaderProgram;
import net.cny.pipeline.ShaderProgram;
import net.cny.platform.Window;
import net.cny.model.Mesh;
import net.cny.scenegraph.Node;
import net.cny.scenegraph.Scenegraph;
import org.lwjgl.opengl.GL13;

public class MasterRenderer
{

    private static Scenegraph scene;
    private static Mesh mesh;
    private static ShaderProgram shaderProgram;

    public static void Initialize()
    {
        mesh = new Mesh();
        mesh.Create();

        shaderProgram = new DefaultShaderProgram();
    }

    public static void Update(float delta)
    {
        if (scene != null)
            scene.Update(delta);
    }

    public static void Render()
    {
        glClear(GL_COLOR_BUFFER_BIT);
        glDisable(GL_DEPTH_TEST);
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

        for (Node node : scene.GetNodeObjects())
        {
            GL13.glActiveTexture(GL13.GL_TEXTURE0);

            node.GetImage().Bind();
            shaderProgram.Bind();
            shaderProgram.UpdateUniforms(node);
            mesh.Draw();
            node.GetImage().Unbind();
        }

        glEnable(GL_DEPTH_TEST);
        glDisable(GL_BLEND);

        Window.Render();
    }

    public static void CleanUp()
    {
        if (scene != null)
            scene.CleanUp();

        mesh.Delete();
    }

    public static void SetScene(Scenegraph newScene)
    {
        if (scene != null)
        {
            scene.CleanUp();
        }

        newScene.Initialize();
        scene = newScene;
    }

    public static Scenegraph GetScene()
    {
        return scene;
    }
}
