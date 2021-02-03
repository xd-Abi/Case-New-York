package net.cny.platform;

import static org.lwjgl.glfw.GLFW.*;

import java.util.ArrayList;

import org.lwjgl.glfw.GLFWKeyCallback;

public class Keyboard extends GLFWKeyCallback
{

	private static final ArrayList<Integer> pushedKeys = new ArrayList<>();
	private static final ArrayList<Integer> keysHolding = new ArrayList<>();
	private static final ArrayList<Integer> releasedKeys = new ArrayList<>();

	public Keyboard(long window)
	{
		glfwSetKeyCallback(window, this);
	}

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

	public void Update()
	{
		pushedKeys.clear();
		releasedKeys.clear();
		pushedKeys.clear();
	}
	
	public void Destroy()
	{
		this.free();
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
