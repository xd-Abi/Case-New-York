package net.cny.core.model;

import net.cny.core.util.BufferUtil;
import org.lwjgl.opengl.GL15;

import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_INT;
import static org.lwjgl.opengl.GL11.glDrawElements;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL15.glDeleteBuffers;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.opengl.GL30.glBindVertexArray;

public class Mesh
{

    private static final float[] VERTICES = new float[] {-0.5f, 0.5f, -0.5f, -0.5f, 0.5f, -0.5f, 0.5f, 0.5f};
    private static final float[] TEXTURE_COORDINATES = new float[] {0, 0, 0, 1, 1, 1,  1, 0};
    private static final int[] INDICES = new int[] {3, 2, 1, 1, 0, 3};

    private final int vao;
    private final int vbo;
    private final int tbo;
    private final int ibo;
    private final int size;

    public Mesh()
    {
        vao = glGenVertexArrays();
        vbo = glGenBuffers();
        tbo = glGenBuffers();
        ibo = glGenBuffers();
        size = INDICES.length;
    }

    public void Create()
    {
        glBindVertexArray(vao);

        glBindBuffer(GL_ARRAY_BUFFER, vbo);
        GL15.glBufferData(GL_ARRAY_BUFFER, BufferUtil.CreateFlippedBuffer(VERTICES), GL_STATIC_DRAW);
        glVertexAttribPointer(0, 2, GL_FLOAT, false, 0, 0);


        glBindBuffer(GL_ARRAY_BUFFER, tbo);
        glBufferData(GL_ARRAY_BUFFER, BufferUtil.CreateFlippedBuffer(TEXTURE_COORDINATES), GL_STATIC_DRAW);
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
