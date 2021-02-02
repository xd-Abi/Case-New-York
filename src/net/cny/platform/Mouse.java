package net.cny.platform;

import static org.lwjgl.glfw.GLFW.*;

import java.util.ArrayList;

import net.cny.Main;
import net.cny.NewMain;
import org.lwjgl.glfw.GLFWCursorPosCallback;
import org.lwjgl.glfw.GLFWMouseButtonCallback;

public class Mouse extends GLFWMouseButtonCallback
{

    private static final ArrayList<Integer> pushedButtons = new ArrayList<Integer>();
    private static final ArrayList<Integer> buttonsHolding = new ArrayList<Integer>();
    private static final ArrayList<Integer> releasedButtons = new ArrayList<Integer>();

    private static GLFWCursorPosCallback cursorPosCallback;
    
    private static float cursorX;
    private static float cursorY;
    
    public Mouse()
    {
    	cursorPosCallback = new GLFWCursorPosCallback() {
			
			@Override
			public void invoke(long window, double x, double y) {
				Mouse.cursorX = (float)x;
				Mouse.cursorY = (float)y;
			}
		};
		
		glfwSetMouseButtonCallback(NewMain.cny.GetWindowId(), this);
		glfwSetCursorPosCallback(NewMain.cny.GetWindowId(), cursorPosCallback);
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
        this.free();
    	cursorPosCallback.free();
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

    public static float getCursorX() {
		return cursorX;
	}
    
    public static float getCursorY() {
		return cursorY;
	}
    
    public static float GetOpenGLX()
    {
    	return (2f* cursorX) / NewMain.cny.GetDisplayWidth() - 1;
    }
    
    public static float GetOpenGLY()
    {
    	return -((2f* cursorY) / NewMain.cny.GetDisplayHeight() - 1);
    }    
}