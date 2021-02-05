package net.cny.core.platform;

import static org.lwjgl.glfw.GLFW.*;

import java.util.ArrayList;

import org.lwjgl.glfw.GLFWMouseButtonCallback;

public class Mouse extends GLFWMouseButtonCallback
{

    private static final ArrayList<Integer> pushedButtons = new ArrayList<Integer>();
    private static final ArrayList<Integer> buttonsHolding = new ArrayList<Integer>();
    private static final ArrayList<Integer> releasedButtons = new ArrayList<Integer>();

    private static Window window;
    private static Cursor cursor;

    public Mouse(Window window, Cursor cursor)
    {
        Mouse.window = window;
        Mouse.cursor = cursor;

		glfwSetMouseButtonCallback(window.GetId(), this);
	 }

    @Override
    public void invoke(long window, int button, int action, int mods) {
        if (action == GLFW_PRESS){
            if (!pushedButtons.contains(button)){
                pushedButtons.add(button);
                buttonsHolding.add(button);
            }
        }

        if (action == GLFW_RELEASE){
            releasedButtons.add(button);
            buttonsHolding.remove(new Integer(button));
        }
    }

    public void Update()
    {
        pushedButtons.clear();
        releasedButtons.clear();
    }
    
    public void Destroy()
    {
       free();
       buttonsHolding.clear();
       releasedButtons.clear();
       pushedButtons.clear();
    }
    
    public static boolean IsButtonPushed(int key)
    {
        return pushedButtons.contains(key);
    }

    public static boolean IsButtonReleased(int key)
    {
        return releasedButtons.contains(key);
    }

    public static boolean IsButtonHolding(int key)
    {
        return buttonsHolding.contains(key);
    }

    public static float GetCursorX()
    {
		return (float)cursor.GetXPos();
	}
    
    public static float GetCursorY()
    {
		return (float)cursor.GetYPos();
	}
    
    public static float GetOpenGLX()
    {
    	return (2f * GetCursorX()) / window.GetWidth() - 1;
    }
    
    public static float GetOpenGLY()
    {
    	return -((2f* GetCursorY()) / window.GetHeight() - 1);
    }    
}