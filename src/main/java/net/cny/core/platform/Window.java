package net.cny.core.platform;

import net.cny.core.config.WindowConfig;
import net.cny.core.image.Image;
import net.cny.core.util.ImageLoader;
import org.lwjgl.glfw.GLFWImage;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;

import java.nio.ByteBuffer;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

public class Window extends WindowConfig
{

    public Window(int width, int height, String title)
    {
        super(width, height, title);
    }

    public void Create(String iconPath)
    {
        SetId(glfwCreateWindow(GetWidth(), GetHeight(), GetTitle(), 0, 0));

        if (GetId() == 0)
        {
            throw new IllegalStateException("Error: GLFW failed to create window");
        }

        glfwMakeContextCurrent(GetId());
        GL.createCapabilities();

        GLFWVidMode vidMode = glfwGetVideoMode(glfwGetPrimaryMonitor());

        assert vidMode != null;
        glfwSetWindowPos(GetId(), (vidMode.width() - GetWidth()) / 2, (vidMode.height() - GetHeight()) / 2);
        ByteBuffer buffer = ImageLoader.LoadImageToByteBuffer(iconPath);
        Image imageSize = new Image(iconPath);
        GLFWImage image = GLFWImage.malloc();
        image.set(imageSize.GetWidth(), imageSize.GetHeight(), buffer);

        // Putting the GLFWImage in a buffer

        GLFWImage.Buffer IconImages = GLFWImage.malloc(1);
        IconImages.put(0, image);

        glfwSetWindowIcon(GetId(), IconImages);
        glfwSetWindowSizeCallback(GetId(), this);
        glfwSetWindowAspectRatio(GetId(), 16, 9);

        // Disabling Vsync
        glfwSwapInterval(0);
    }

    @Override
    public void invoke(long id, int width, int height)
    {
        if (GetWidth() != width || GetHeight() != height)
        {
            Resize(width, height);
        }
    }

    public void Show()
    {
        glfwShowWindow(GetId());
    }

    public void Update()
    {
        glfwPollEvents();
    }

    public void Render()
    {
        glfwSwapBuffers(GetId());
    }

    public void Shutdown()
    {
        free();

        glfwDestroyWindow(GetId());
        glfwTerminate();
    }

    public boolean IsCloseRequested()
    {
        return glfwWindowShouldClose(GetId());
    }

    public void Resize(int width, int height)
    {
        SetWidth(width);
        SetHeight(height);

        glViewport(0,0,width, height);
    }
}