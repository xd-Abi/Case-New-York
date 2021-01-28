package net.cny;

import static org.lwjgl.opengl.GL11.*;

import net.cny.gui.GuiBackground;
import net.cny.model.Mesh;
import net.cny.platform.Window;
import net.cny.scenegraph.Scene;

public class RenderingEngine 
{

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

		Mesh.GetInstance().Delete();
	}

	public static void SetScene(Scene scene) 
	{
		scene.Initialize();
		RenderingEngine.scene = scene;
	}
}
