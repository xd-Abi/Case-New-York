package net.cny.platform;

import net.cny.util.ImageLoader;
import org.lwjgl.glfw.GLFWImage;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.glfw.GLFWWindowSizeCallback;
import org.lwjgl.opengl.GL;

import java.nio.ByteBuffer;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

public class Window extends GLFWWindowSizeCallback
{

    private long id;
    private int width;
    private int height;
    private final String title;

    public Window(int width, int height, String title)
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

    public void Create(String iconPath, int iconWidth, int iconHeight)
    {
        id = glfwCreateWindow(width, height, title, 0, 0);

        if (id == 0) {
            throw new IllegalStateException("Error: GLFW failed to create window");
        }

        GLFWVidMode vidMode = glfwGetVideoMode(glfwGetPrimaryMonitor());

        if (vidMode != null)
        {
            glfwSetWindowPos(id, (vidMode.width() - width) / 2, (vidMode.height() - height) / 2);
        }

        ByteBuffer buffer = ImageLoader.LoadImageToByteBuffer(iconPath);
        GLFWImage image = GLFWImage.malloc();
        image.set(iconWidth, iconHeight, buffer);

        // Putting the GLFWImage in a buffer

        GLFWImage.Buffer IconImages = GLFWImage.malloc(1);
        IconImages.put(0, image);

        glfwSetWindowIcon(id, IconImages);
        glfwSetWindowSizeCallback(id, this);
        glfwSetWindowAspectRatio(id, 16, 9);
        glfwMakeContextCurrent(id);
        GL.createCapabilities();

        // Disabling Vsync
        glfwSwapInterval(0);
    }

    @Override
    public void invoke(long id, int width, int height)
    {
        if (this.width != width || this.height != height)
        {
            Resize(width, height);
        }
    }

    public void Show()
    {
        glfwShowWindow(id);
    }

    public void Update()
    {
        glfwPollEvents();
    }

    public void Render()
    {
        glfwSwapBuffers(id);
    }

    public void Shutdown()
    {
        free();

        glfwDestroyWindow(id);
        glfwTerminate();
    }

    public boolean IsCloseRequested()
    {
        return glfwWindowShouldClose(id);
    }

    public void Resize(int width, int height)
    {
        this.width = width;
        this.height = height;

        glViewport(0,0,width, height);
    }

    public long GetId()
    {
        return id;
    }

    public int GetWidth()
    {
        return width;
    }

    public int GetHeight()
    {
        return height;
    }
}