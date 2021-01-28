package net.cny.platform;

import static org.lwjgl.glfw.GLFW.*;

import java.util.ArrayList;

import org.lwjgl.glfw.GLFWKeyCallback;

public class Keyboard 
{
	private static final ArrayList<Integer> pushedKeys = new ArrayList<>();
	private static final ArrayList<Integer> keysHolding = new ArrayList<>();
	private static final ArrayList<Integer> releasedKeys = new ArrayList<>();
	
	private static GLFWKeyCallback keyCallback;
	
	public static void Create()
	{
		keyCallback = new GLFWKeyCallback() {
			
			@SuppressWarnings("deprecation")
			@Override
			public void invoke(long window, int key, int scancode, int action, int mods) {
				if (action == GLFW_PRESS){
					if (!pushedKeys.contains(key)){
						pushedKeys.add(key);
						keysHolding.add(key);
					}
				}

				if (action == GLFW_RELEASE){
					keysHolding.remove(new Integer(key));
					releasedKeys.add(key);
				}
			}
		}; 
		
		glfwSetKeyCallback(Window.GetId(), keyCallback);
	}
	
	public static void Update()
	{
		pushedKeys.clear();
		releasedKeys.clear();
		pushedKeys.clear();
	}
	
	public static void Destroy()
	{
		keyCallback.free();
	}
	
	 public static boolean IsKeyPushed(int key)
	 {
		 return pushedKeys.contains(key);
	 }

	 public static boolean IsKeyReleased(int key)
	 {
		 return releasedKeys.contains(key);
	 }

	 public static boolean IsKeyHold(int key)
	 {
		 return keysHolding.contains(key);
	 }
}
