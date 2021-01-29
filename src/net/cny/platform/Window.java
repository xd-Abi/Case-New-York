package net.cny.platform;

import static org.lwjgl.glfw.GLFW.*;

import net.cny.util.ImageLoader;
import org.lwjgl.glfw.GLFWImage;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;

import java.nio.ByteBuffer;

public class Window 
{

	private static long id;
	private static int width;
	private static int height;
	
	public static void CreateWindow()
	{
		if (!glfwInit())
		{
			throw new IllegalStateException("Error: GLFW couldn't initialize");
		}
		
		glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
		glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 2);
		glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
		glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GLFW_TRUE);
		glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
		
		GLFWVidMode vidMode = glfwGetVideoMode(glfwGetPrimaryMonitor());
		width = vidMode.width();
		height = vidMode.height();

		id = glfwCreateWindow(width, height, "Fall New York", glfwGetPrimaryMonitor(), 0);
		
		if (id == 0)
		{
			throw new IllegalStateException("Error: Failed to create the GLFW window");
		}

		ByteBuffer bufferedImage = ImageLoader.LoadImageToByteBuffer("icon.png");
		GLFWImage image = GLFWImage.malloc();
		image.set(235, 300, bufferedImage);

		GLFWImage.Buffer images = GLFWImage.malloc(1);
		images.put(0, image);

		glfwSetWindowIcon(id, images);

		glfwMakeContextCurrent(id);
		GL.createCapabilities();
	}

	public static void Update()
	{
		glfwPollEvents();
	}
	
	public static void Render()
	{
		glfwSwapBuffers(id);
	}
	
	public static void Dispose()
	{
		glfwDestroyWindow(id);
		glfwTerminate();
	}
	
	public static boolean IsCloseRequested()
	{
		return glfwWindowShouldClose(id);
	}
	
	public static long GetId() {
		return id;
	}
	
	public static int GetWidth() {
		return width;
	}
	
	public static int GetHeight() {
		return height;
	}
}
