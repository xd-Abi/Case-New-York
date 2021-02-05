package net.cny.core.math;

import org.joml.Vector3f;

public class Transform
{

    private Vector3f translation;
    private Vector3f rotation;
    private Vector3f scaling;

    public Transform()
    {
        SetTranslation(new Vector3f(0,0,0));
        SetRotation(new Vector3f(0,0,0));
        SetScaling(new Vector3f(1,1,1));
    }

    public Matrix4f GetWorldMatrix()
    {
        Matrix4f translationMatrix = new Matrix4f().InitTranslation(translation.x, translation.y, translation.z);
        Matrix4f rotationMatrix = new Matrix4f().InitRotation(rotation.x, rotation.y, rotation.z);
        Matrix4f scalingMatrix = new Matrix4f().InitScale(scaling.x, scaling.y, scaling.z);

        return translationMatrix.Mul(scalingMatrix.Mul(rotationMatrix));
//        return Create();
    }

    /*   private Matrix4f Create()
       {
           Matrix4f matrix4f = new Matrix4f();
           matrix4f.identity();
           matrix4f.translate(translation, matrix4f);
           matrix4f.rotate((float) Math.toRadians(rotation.x), new Vector3f(1,0,0), matrix4f);
           matrix4f.rotate((float) Math.toRadians(rotation.y), new Vector3f(0,1,0), matrix4f);
           matrix4f.rotate((float) Math.toRadians(rotation.z), new Vector3f(0,0,1), matrix4f);
           matrix4f.scale(scaling, matrix4f);
           return matrix4f;
       }
   */
    public Vector3f GetTranslation()
    {
        return translation;
    }

    public void SetTranslation(Vector3f translation)
    {
        this.translation = translation;
    }

    public Vector3f GetRotation()
    {
        return rotation;
    }

    public void SetRotation(Vector3f rotation)
    {
        this.rotation = rotation;
    }

    public Vector3f GetScaling()
    {
        return scaling;
    }

    public void SetScaling(Vector3f scaling)
    {
        this.scaling = scaling;
    }

    public void SetScaling(float x, float y, float z)
    {
        this.scaling = new Vector3f(x, y, z);
    }

    public void SetTranslation(float x, float y, float z)
    {
        this.translation = new Vector3f(x, y, z);
    }

    public void SetRotation(float x, float y, float z)
    {
        this.rotation = new Vector3f(x,y,z);
    }
}