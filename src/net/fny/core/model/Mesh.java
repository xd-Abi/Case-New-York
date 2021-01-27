package net.fny.core.model;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

import net.fny.core.util.BufferUtil;

public class Mesh 
{

    private final int vao;
    private final int vbo;
    private final int tbo;
    private final int ibo;
    private int size;
    
    public Mesh()
    {
        vao = glGenVertexArrays();
        vbo = glGenBuffers();
        tbo = glGenBuffers();
        ibo = glGenBuffers();
        size = 0;
    }
    
    public void Create(float[] vertices, float[] texCoords, int[] indices)
    {
        this.size = indices.length;

        glBindVertexArray(vao);

        glBindBuffer(GL_ARRAY_BUFFER, vbo);
        glBufferData(GL_ARRAY_BUFFER, BufferUtil.CreateFlippedBuffer(vertices), GL_STATIC_DRAW);
        glVertexAttribPointer(0, 2, GL_FLOAT, false, 0, 0);
        
        
        glBindBuffer(GL_ARRAY_BUFFER, tbo);
        glBufferData(GL_ARRAY_BUFFER, BufferUtil.CreateFlippedBuffer(texCoords), GL_STATIC_DRAW);
        glVertexAttribPointer(1, 2, GL_FLOAT, false, 0, 0);

        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ibo);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, BufferUtil.CreateFlippedBuffer(indices), GL_STATIC_DRAW);


        glBindVertexArray(0);
    }
    
    public void Draw()
    {
        glBindVertexArray(vao);

        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);
        
        glDrawElements(GL_TRIANGLES, size, GL_UNSIGNED_INT, 0);

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
}
