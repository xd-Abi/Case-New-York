package net.cny.core.scenegraph;

import net.cny.core.model.Mesh;
import net.cny.core.pipeline.DefaultShaderProgram;
import net.cny.core.pipeline.ShaderProgram;
import org.lwjgl.opengl.GL13;

import java.util.HashMap;

public class Scenegraph
{

    private final HashMap<String, Node> nodeObjects;
    private final Mesh mesh;
    private final ShaderProgram shaderProgram;

    public Scenegraph()
    {
        nodeObjects = new HashMap<>();
        mesh = new Mesh();
        mesh.Create();
        shaderProgram = new DefaultShaderProgram();
    }

    public void Initialize() {}

    public void Update()
    {
        for (String key : nodeObjects.keySet())
            GetNode(key).Update();
    }

    public void Render()
    {
        for (String key : nodeObjects.keySet())
        {
            GL13.glActiveTexture(GL13.GL_TEXTURE0);
            GetNode(key).GetImage().Bind();

            shaderProgram.Bind();
            shaderProgram.UpdateUniforms(GetNode(key));
            mesh.Draw();

            GetNode(key).GetImage().Unbind();
        }
    }

    public void CleanUp()
    {
        for (String key : nodeObjects.keySet())
            GetNode(key).CleanUp();
    }

    public Node GetNode(String key)
    {
        return nodeObjects.get(key);
    }

    public void Add(String key, Node node)
    {
        nodeObjects.put(key, node);
    }

    public void Remove(String key)
    {
        nodeObjects.remove(key);
    }

    public void Replace(String key, Node replaceNode)
    {
        GetNode(key).CleanUp();
        nodeObjects.replace(key, replaceNode);
    }
}