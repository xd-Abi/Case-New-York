package net.fny.core;

import static org.lwjgl.opengl.GL11.*;

import net.fny.core.model.Mesh;
import net.fny.core.platform.Window;
import net.fny.core.scenegraph.Scene;

public class RenderingEngine 
{
	private static Mesh quad;
	private static Scene scene;
	
	public static void Initialize()
	{
		glFrontFace(GL_CULL_FACE);
		glFrontFace(GL_CW);
		glEnable(GL_CULL_FACE);
		glCullFace(GL_BACK); 
		glEnable(GL_DEPTH_TEST);
		glEnable(GL_TEXTURE_2D);
	
		glClearColor(0f,0f,0f,1f);
	}
	
	private static void ClearScreen()
	{
		glClearDepth(1.0);
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);	
	}
	
	public static void Update()
	{
		if (scene != null)
			scene.Update();
	}
	
	public static void Render()
	{
		ClearScreen();
	
		if (scene != null)
			scene.Render();
		
		Window.Render();
	}
	
	public static void CleanUp()
	{
		if (scene != null)
			scene.CleanUp();
		quad.Delete();
	}
	
	private static void CreateQuad()
	{
		float[] vertices = new float[]
		{
				-0.5f,  0.5f,
				-0.5f, -0.5f,
				 0.5f,  -0.5f,
				 0.5f,   0.5f
		};
		
		float[] texCoords = new float[]
        {
        		0, 0,
        		0, 1,
        		1, 1,
        		1, 0
        };
		
		int[] indices = new int[] {
	    		3, 2, 1, 
	    		1, 0, 3
	    };
		
		quad = new Mesh();
		quad.Create(vertices, texCoords, indices);
	}
	
	public static Mesh getQuad()
	{
		if (quad == null)
		{
			CreateQuad();
		}
		
		return quad;
	}
		
	public static void SetScene(Scene scene) 
	{
		scene.Initialize();
		RenderingEngine.scene = scene;
	}
}
