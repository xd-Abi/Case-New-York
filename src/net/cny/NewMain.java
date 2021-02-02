package net.cny;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.NULL;


import net.cny.image.Image;
import net.cny.platform.Keyboard;
import net.cny.platform.Mouse;
import net.cny.util.ImageLoader;
import net.cny.util.ResourceLoader;
import org.lwjgl.glfw.GLFWImage;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.glfw.GLFWWindowSizeCallback;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Properties;

public class NewMain implements Runnable
{

    public static final double FRAME_CAP = 60;
    public static final double FRAME_TIME = 1.0 / FRAME_CAP;

    // Static instance of Main

    public static final NewMain cny = new NewMain();

    private boolean isRunning;

    // Window Variables

    private boolean isFullscreen;

    private long windowId;
    private int displayWidth;
    private int displayHeight;
    private String displayTitle;
    private String displayIconPath;

    // Cursor Variables

    private long windowCursorId;
    private String windowCursorPath;

    // Primary Monitor Size

    private int maxScreenWidth;
    private int maxScreenHeight;

    // Config

    private Properties config;

    // Classes

    private Thread thread;
    private Keyboard keyboard;
    private Mouse mouse;

    private GLFWWindowSizeCallback windowSizeCallback;

    private NewMain()
    {
        try
        {
            LoadFromConfig();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    private void LoadFromConfig() throws IOException
    {
        config = new Properties();
        config.load(ResourceLoader.GetResource("cny-properties.net"));

        // Loading Window Configs

        displayWidth = Integer.parseInt(config.getProperty("display-width"));
        displayHeight = Integer.parseInt(config.getProperty("display-height"));
        displayTitle = config.getProperty("display-title");
        displayIconPath = config.getProperty("display-icon-path");

        // Window Cursor

        windowCursorPath = config.getProperty("cursor-path");
    }

    public void Start()
    {
        if (isRunning)
        {
            return;
        }

        isRunning = true;
        thread = new Thread(this, "CNY: Client");
        thread.start();
    }

    public void Stop()
    {
        if (!isRunning)
        {
            return;
        }

        isRunning = false;
    }

    private void InitGLFW()
    {
        if (!glfwInit())
        {
            throw new IllegalStateException("Error: GLFW failed to initialize");
        }

        // Setting up window hints

        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 2);
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
        glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GLFW_TRUE);
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
    }

    private void CreateWindow()
    {
        // Initialize GLFW

        InitGLFW();

        // Creating the Window

        windowId = glfwCreateWindow(displayWidth, displayHeight, displayTitle, NULL, NULL);

        if (windowId == NULL)
        {
            throw new IllegalStateException("Error: Failed to create the GLFW window");
        }

        // Centering the window

        GLFWVidMode vidMode = glfwGetVideoMode(glfwGetPrimaryMonitor());

        if (vidMode != null)
        {
            maxScreenWidth = vidMode.width();
            maxScreenHeight = vidMode.height();

            glfwSetWindowPos(windowId, (maxScreenWidth - displayWidth) / 2, (maxScreenHeight - displayHeight) / 2);
        }

        // Creating a Context
        // OpenGL Context works only for a single Thread

        glfwMakeContextCurrent(windowId);
        createCapabilities();

        // Creating GLFWImage for the window icon

        ByteBuffer buffer = ImageLoader.LoadImageToByteBuffer(displayIconPath);
        GLFWImage image = GLFWImage.malloc();
        Image imageSize = new Image(displayIconPath);
        image.set(imageSize.GetWidth(), imageSize.GetHeight(), buffer);

        // Putting the GLFWImage in a buffer

        GLFWImage.Buffer IconImages = GLFWImage.malloc(1);
        IconImages.put(0, image);

        // Setting up the window icon

        glfwSetWindowIcon(windowId, IconImages);

        // Setting up some useful functions
        // Changing the AspectRatio to be always 16:9

        glfwSetWindowAspectRatio(windowId, 16, 9);
        glfwSetWindowSizeCallback(windowId, windowSizeCallback = new GLFWWindowSizeCallback()
        {

            // Changing the window Size and Viewport if player resizes

            @Override
            public void invoke(long window, int width, int height)
            {
                if ((displayWidth != width || displayHeight != height) && !isFullscreen)
                {
                    ResizeWindow(width, height);
                    ChangeViewPort(width, height);
                }
            }
        });

        // Turing off Vsync for better Performance

        glfwSwapInterval(0);

        // Showing Window

        glfwShowWindow(windowId);
    }

    private void CreateWindowCursor()
    {
        // Creating GLFWImage for the window cursor

        ByteBuffer buffer = ImageLoader.LoadImageToByteBuffer(windowCursorPath);
        GLFWImage image = GLFWImage.malloc();
        Image imageSize = new Image(windowCursorPath);
        image.set(imageSize.GetWidth(), imageSize.GetHeight(), buffer);

        // Creating the window Cursor and
        // Setting it to the GLFW Window

        windowCursorId = glfwCreateCursor(image, 0, 0);
        glfwSetCursor(windowId, windowCursorId);
    }

    private void CreateInput()
    {
        keyboard = new Keyboard(windowId);
        mouse = new Mouse();
    }

    @Override
    public void run()
    {
        // Initialising

        CreateWindow();
        CreateWindowCursor();
        CreateInput();

        int frames = 0;
        double frameCounter = 0;
        double lastTime = (double)System.nanoTime()/(double)1000000000L;
        double unprocessedTime = 0;

        // Game Loop

        while (this.isRunning)
        {
            boolean render = false;

            double startTime = (double)System.nanoTime()/(double)1000000000L;
            double passedTime = startTime - lastTime;
            lastTime = startTime;

            unprocessedTime += passedTime;
            frameCounter += passedTime;

            while (unprocessedTime > FRAME_TIME)
            {
                render = true;

                unprocessedTime -= FRAME_TIME;

                if (glfwWindowShouldClose(windowId))
                {
                    Stop();
                }

                Update((float) FRAME_TIME);

                if (frameCounter >= 1.0)
                {
                    System.out.println(frames);
                    frames = 0;
                    frameCounter = 0;
                }
            }

            // Rendering the game

            Render();
            frames++;

            if (!render)
            {
                this.SleepThread();
            }
        }

        Shutdown();
    }

    private void SleepThread()
    {
        try
        {
            Thread.sleep(1);
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
    }

    private void Update(float delta)
    {

        // Update Game

        UpdateGame(delta);

        // Update Inputs

        keyboard.Update();
        mouse.Update();

        glfwPollEvents();
    }

    private void UpdateGame(float delta)
    {
        if (Keyboard.IsKeyPushed(GLFW_KEY_F11))
        {
            ToggleFullscreen();
        }
    }

    private void Render()
    {


        // Rendering the GLFW window

        glfwSwapBuffers(windowId);
    }

    private void ToggleFullscreen()
    {
        if (!isFullscreen)
        {
            isFullscreen = true;
            glfwSetWindowMonitor(windowId, glfwGetPrimaryMonitor(), 0, 0, maxScreenWidth, maxScreenHeight, 60);
            ChangeViewPort(maxScreenWidth, maxScreenHeight);
        }
        else
        {
            isFullscreen = false;
            ChangeViewPort(displayWidth, displayHeight);
            glfwSetWindowMonitor(windowId, 0, (maxScreenWidth-displayWidth) / 2, (maxScreenHeight-displayHeight) / 2, displayWidth, displayHeight, 60);
        }
    }

    private void Shutdown()
    {

        // Destroy Input

        keyboard.Destroy();
        mouse.Destroy();

        // Setting glfw Callbacks free

        windowSizeCallback.free();

        // Destroy all GLFW function used by the game

        glfwDestroyCursor(windowCursorId);
        glfwDestroyWindow(windowId);
        glfwTerminate();

        JoinThread();
    }

    private void JoinThread()
    {
        while (thread.isAlive())
        {
            try
            {
                thread.join(100);
                break;
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }
        }
    }

    private void ResizeWindow(int width, int height)
    {
        displayWidth = width;
        displayHeight = height;
    }

    private void ChangeViewPort(int width, int height)
    {
        glViewport(0,0, width, height);
    }

    public long GetWindowId()
    {
        return windowId;
    }

    public int GetDisplayWidth()
    {
        if (isFullscreen)
        {
            return maxScreenWidth;
        }

        return displayWidth;
    }

    public int GetDisplayHeight()
    {
        if (isFullscreen)
        {
            return maxScreenHeight;
        }

        return displayHeight;
    }
}
