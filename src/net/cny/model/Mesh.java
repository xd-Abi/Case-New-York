package net.cny.model;

import static net.cny.model.QuadVertices.*;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

import net.cny.util.BufferUtil;

public class Mesh 
{

    private static Mesh instance;

    private final int vao;
    private final int vbo;
    private final int tbo;
    private final int ibo;

    public Mesh()
    {
        vao = glGenVertexArrays();
        vbo = glGenBuffers();
        tbo = glGenBuffers();
        ibo = glGenBuffers();
    }
    
    public void Create()
    {
        glBindVertexArray(vao);

        glBindBuffer(GL_ARRAY_BUFFER, vbo);
        glBufferData(GL_ARRAY_BUFFER, BufferUtil.CreateFlippedBuffer(VERTICES), GL_STATIC_DRAW);
        glVertexAttribPointer(0, 2, GL_FLOAT, false, 0, 0);
        
        
        glBindBuffer(GL_ARRAY_BUFFER, tbo);
        glBufferData(GL_ARRAY_BUFFER, BufferUtil.CreateFlippedBuffer(TEXTURE_CORDS), GL_STATIC_DRAW);
        glVertexAttribPointer(1, 2, GL_FLOAT, false, 0, 0);

        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ibo);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, BufferUtil.CreateFlippedBuffer(INDICES), GL_STATIC_DRAW);

        glBindVertexArray(0);
    }
    
    public void Draw()
    {
        glBindVertexArray(vao);

        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);
        
        glDrawElements(GL_TRIANGLES, INDICES.length, GL_UNSIGNED_INT, 0);

        glDisableVertexAttribArray(1);
        glDisableVertexAttribArray(0);

        glBindVertexArray(0);
    }

    public void Delete()
    {
        glBindVertexArray(vao);
        glDeleteBuffers(vbo);
        glDeleteBuffers(tbo);
        glDeleteBuffers(ibo);
        glDeleteVertexArrays(vao);
        glBindVertexArray(0);
    }

    public static Mesh GetInstance()
    {
        if (instance == null)
        {
            instance = new Mesh();
            instance.Create();
        }

        return instance;
    }
}
