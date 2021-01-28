package net.cny.core.util;

import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;

import net.cny.core.math.Matrix4f;

public class BufferUtil {

    public static FloatBuffer CreateFloatBuffer(int size) 
    {
        return BufferUtils.createFloatBuffer(size);
    }

    public static IntBuffer CreateIntBuffer(int size) 
    {
        return BufferUtils.createIntBuffer(size);
    }

    public static DoubleBuffer CreateDoubleBuffer(int size) 
    {
        return BufferUtils.createDoubleBuffer(size);
    }

    public static FloatBuffer CreateFlippedBuffer(float... values) 
    {
        FloatBuffer buffer = CreateFloatBuffer(values.length);
        buffer.put(values);
        buffer.flip();

        return buffer;
    }

    
    public static IntBuffer CreateFlippedBuffer(int... values) 
    {
        IntBuffer buffer = CreateIntBuffer(values.length);
        buffer.put(values);
        buffer.flip();

        return buffer;
    }

    public static FloatBuffer CreateFlippedBuffer(Matrix4f matrix) 
    {
        FloatBuffer buffer = CreateFloatBuffer(4 * 4);

        for (int i = 0; i < 4; i++)
            for (int j = 0; j < 4; j++)
                buffer.put(matrix.get(i, j));

        buffer.flip();

        return buffer;
    }
}