package net.cny.core.platform;

import static org.lwjgl.glfw.GLFW.*;

import java.util.ArrayList;

import org.lwjgl.glfw.GLFWCursorPosCallback;
import org.lwjgl.glfw.GLFWMouseButtonCallback;

public class Mouse 
{

    private static final ArrayList<Integer> pushedButtons = new ArrayList<Integer>();
    private static final ArrayList<Integer> buttonsHolding = new ArrayList<Integer>();
    private static final ArrayList<Integer> releasedButtons = new ArrayList<Integer>();

    private static GLFWMouseButtonCallback mouseButtonCallback;
    private static GLFWCursorPosCallback cursorPosCallback;
    
    private static float cursorX;
    private static float cursorY;
    
    public static void Create()
    {
    	mouseButtonCallback = new GLFWMouseButtonCallback() {
			
            @SuppressWarnings("deprecation")
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
    	};
    	
    	cursorPosCallback = new GLFWCursorPosCallback() {
			
			@Override
			public void invoke(long window, double x, double y) {
				Mouse.cursorX = (float)x;
				Mouse.cursorY = (float)y;
			}
		};
		
		glfwSetMouseButtonCallback(Window.GetId(), mouseButtonCallback);
		glfwSetCursorPosCallback(Window.GetId(), cursorPosCallback);
    }
    
    public static void Update()
    {
        pushedButtons.clear();
        releasedButtons.clear();
    }
    
    public static void Destroy()
    {
    	mouseButtonCallback.free();
    	cursorPosCallback.free();
    }
    
    public static boolean IsButtonPushed(int key)
    {
        return pushedButtons.contains(key);
    }

    public static boolean IsButtonreleased(int key)
    {
        return releasedButtons.contains(key);
    }

    public static boolean IsButtonHolding(int key)
    {
        return buttonsHolding.contains(key);
    }

    public static float getCursorX() {
		return cursorX;
	}
    
    public static float getCursorY() {
		return cursorY;
	}
    
    public static float GetOpenGLX()
    {
    	return (2f* cursorX) / Window.GetWidth() - 1;
    }
    
    public static float GetOpenGLY()
    {
    	return -((2f* cursorY) / Window.GetHeight() - 1);
    }    
}