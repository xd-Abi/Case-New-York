package net.cny.core.config;

import org.lwjgl.glfw.GLFWWindowSizeCallback;

import static org.lwjgl.glfw.GLFW.*;

public abstract class WindowConfig extends GLFWWindowSizeCallback
{

    private long id;
    private int width;
    private int height;
    private String title;

    public WindowConfig(int width, int height, String title)
    {
        this.width = width;
        this.height = height;
        this.title = title;
    }

    public void Initialize()
    {
        if (!glfwInit())
        {
            throw new IllegalStateException("Error: GLFW failed to initialize");
        }

        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 2);
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
        glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GLFW_TRUE);
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
    }

    public long GetId()
    {
        return id;
    }

    public void SetId(long id)
    {
        this.id = id;
    }

    public int GetWidth()
    {
        return width;
    }

    public void SetWidth(int width)
    {
        this.width = width;
    }

    public int GetHeight()
    {
        return height;
    }

    public void SetHeight(int height)
    {
        this.height = height;
    }

    public String GetTitle()
    {
        return title;
    }

    public void SetTitle(String title)
    {
        this.title = title;
    }
}
