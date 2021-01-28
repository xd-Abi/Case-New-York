package net.cny.model;

public class QuadVertices
{

    public static final float[] VERTICES = new float[] {
            -0.5f,  0.5f,
            -0.5f, -0.5f,
             0.5f, -0.5f,
             0.5f,  0.5f};
    public static final float[] TEXTURE_CORDS = new float[] {
             0, 0,
             0, 1,
             1, 1,
             1, 0};
    public static final int[] INDICES = new int[] { 3, 2, 1, 1, 0, 3};
}
