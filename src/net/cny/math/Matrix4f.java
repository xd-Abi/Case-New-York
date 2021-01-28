package net.cny.math;

public class Matrix4f 
{

    private float[][] m;

    public Matrix4f() 
    {
        setM(new float[4][4]);
    }

    public Matrix4f Translation(Vec3f translation) 
    {
        m[0][0] = 1; m[0][1] = 0; m[0][2] = 0; m[0][3] = translation.getX();
        m[1][0] = 0; m[1][1] = 1; m[1][2] = 0; m[1][3] = translation.getY();
        m[2][0] = 0; m[2][1] = 0; m[2][2] = 1; m[2][3] = translation.getZ();
        m[3][0] = 0; m[3][1] = 0; m[3][2] = 0; m[3][3] = 1;

        return this;
    }

    public Matrix4f Rotation(Vec3f rotation) 
    {
        Matrix4f rx = new Matrix4f();
        Matrix4f ry = new Matrix4f();
        Matrix4f rz = new Matrix4f();

        float x = (float)Math.toRadians(rotation.getX());
        float y = (float)Math.toRadians(rotation.getY());
        float z = (float)Math.toRadians(rotation.getZ());

        rz.m[0][0] = (float)Math.cos(z); rz.m[0][1] = -(float)Math.sin(z); 	 rz.m[0][2] = 0; 				   rz.m[0][3] = 0;
        rz.m[1][0] = (float)Math.sin(z); rz.m[1][1] = (float)Math.cos(z);  	 rz.m[1][2] = 0; 				   rz.m[1][3] = 0;
        rz.m[2][0] = 0; 				 rz.m[2][1] = 0; 				   	 rz.m[2][2] = 1; 				   rz.m[2][3] = 0;
        rz.m[3][0] = 0; 				 rz.m[3][1] = 0; 				   	 rz.m[3][2] = 0; 				   rz.m[3][3] = 1;

        rx.m[0][0] = 1; 				 rx.m[0][1] = 0;					 rx.m[0][2] = 0; 				   rx.m[0][3] = 0;
        rx.m[1][0] = 0; 				 rx.m[1][1] = (float)Math.cos(x); 	 rx.m[1][2] = -(float)Math.sin(x); rx.m[1][3] = 0;
        rx.m[2][0] = 0; 				 rx.m[2][1] = (float)Math.sin(x); 	 rx.m[2][2] = (float)Math.cos(x);  rx.m[2][3] = 0;
        rx.m[3][0] = 0; 				 rx.m[3][1] = 0; 				 	 rx.m[3][2] = 0;				   rx.m[3][3] = 1;

        ry.m[0][0] = (float)Math.cos(y); ry.m[0][1] = 0; 					 ry.m[0][2] = (float)Math.sin(y);  ry.m[0][3] = 0;
        ry.m[1][0] = 0; 				 ry.m[1][1] = 1; 				 	 ry.m[1][2] = 0; 				   ry.m[1][3] = 0;
        ry.m[2][0] = -(float)Math.sin(y);ry.m[2][1] = 0;					 ry.m[2][2] = (float)Math.cos(y);  ry.m[2][3] = 0;
        ry.m[3][0] = 0; 				 ry.m[3][1] = 0; 					 ry.m[3][2] = 0; 				   ry.m[3][3] = 1;

        m =  rz.mul(ry.mul(rx)).getM();

        return this;
    }

    public Matrix4f Scaling(Vec3f scaling) 
    {
        m[0][0] = scaling.getX(); 	m[0][1] = 0; 				m[0][2] = 0; 				m[0][3] = 0;
        m[1][0] = 0; 			 	m[1][1] = scaling.getY();	m[1][2] = 0; 				m[1][3] = 0;
        m[2][0] = 0; 				m[2][1] = 0; 				m[2][2] = scaling.getZ(); 	m[2][3] = 0;
        m[3][0] = 0; 				m[3][1] = 0; 				m[3][2] = 0; 				m[3][3] = 1;

        return this;
    }

    public Matrix4f mul(Matrix4f r)
    {

        Matrix4f res = new Matrix4f();

        for (int i=0; i<4; i++)
        {
            for (int j=0; j<4; j++)
            {
                res.set(i, j, m[i][0] * r.get(0, j) +
                        m[i][1] * r.get(1, j) +
                        m[i][2] * r.get(2, j) +
                        m[i][3] * r.get(3, j));
            }
        }

        return res;
    }

    public void set(int x, int y, float value) 
    {
        this.m[x][y] = value;
    }

    public float get(int x, int y) 
    {
        return  this.m[x][y];
    }

    public float [][] getM() 
    {
        return m;
    }

    public void setM(float [][] m) 
    {
        this.m = m;
    }
}