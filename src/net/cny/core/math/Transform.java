package net.cny.core.math;

public class Transform 
{

    private Vec3f translation;
    private Vec3f rotation;
    private Vec3f scaling;

    public Transform() 
    {
        SetTranslation(new Vec3f(0,0,0));
        SetRotation(new Vec3f(0,0,0));
        SetScaling(new Vec3f(1,1,1));
    }

    public Matrix4f GetWorldMatrix() 
    {
        Matrix4f translationMatrix = new Matrix4f().Translation(translation);
        Matrix4f rotationMatrix = new Matrix4f().Rotation(rotation);
        Matrix4f scalingMatrix = new Matrix4f().Scaling(scaling);

        return translationMatrix.mul(scalingMatrix.mul(rotationMatrix));
    }

   	public Vec3f GetTranslation() 
   	{
        return translation;
    }

    public void SetTranslation(Vec3f translation) 
    {
        this.translation = translation;
    }

    public Vec3f GetRotation() 
    {
        return rotation;
    }

    public void SetRotation(Vec3f rotation) 
    {
        this.rotation = rotation;
    }

    public Vec3f GetScaling() 
    {
        return scaling;
    }

    public void SetScaling(Vec3f scaling) 
    {
        this.scaling = scaling;
    }

    public void SetScaling(float x, float y, float z) 
    {
        this.scaling = new Vec3f(x, y, z);
    }

    public void SetTranslation(float x, float y, float z) 
    {
        this.translation = new Vec3f(x, y, z);
    }

    public void SetRotation(float x, float y, float z) 
    {
        this.rotation = new Vec3f(x,y,z);
    }
}
