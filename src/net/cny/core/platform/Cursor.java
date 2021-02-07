package net.cny.core.platform;

import net.cny.core.image.Image;
import net.cny.core.util.ImageLoader;
import org.lwjgl.glfw.GLFWCursorPosCallback;
import org.lwjgl.glfw.GLFWImage;

import java.nio.ByteBuffer;

import static org.lwjgl.glfw.GLFW.*;

public class Cursor extends GLFWCursorPosCallback
{

    private long id;
    private double xPos;
    private double yPos;

    public Cursor(Window window, String path)
    {
        ByteBuffer buffer = ImageLoader.LoadImageToByteBuffer(path);
        GLFWImage image = GLFWImage.malloc();
        Image imageSize = new Image(path);
        image.set(imageSize.GetWidth(), imageSize.GetHeight(), buffer);

        id = glfwCreateCursor(image, 0, 0);

        glfwSetCursor(window.GetId(), id);
        glfwSetCursorPosCallback(window.GetId(), this);
    }

    @Override
    public void invoke(long window, double xPos, double yPos)
    {
        this.xPos = xPos;
        this.yPos = yPos;
    }

    public void Destroy()
    {
        free();
        glfwDestroyCursor(id);
    }

    public double GetXPos()
    {
        return xPos;
    }

    public double GetYPos()
    {
        return yPos;
    }
}
