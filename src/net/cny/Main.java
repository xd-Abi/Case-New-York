package net.cny;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

import net.cny.gui.menu.MainMenu;
import net.cny.gui.menu.PauseMenu;
import net.cny.image.Image;
import net.cny.model.Mesh;
import net.cny.pipeline.DefaultShaderProgram;
import net.cny.pipeline.ShaderProgram;
import net.cny.platform.Keyboard;
import net.cny.platform.Mouse;
import net.cny.scenegraph.Node;
import net.cny.scenegraph.Scenegraph;
import net.cny.util.ImageLoader;
import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWImage;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.glfw.GLFWWindowSizeCallback;
import org.lwjgl.openal.*;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL13;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;


public class Main extends GLFWWindowSizeCallback implements Runnable
{

    public static final double FRAME_CAP = 60;
    private static final double FRAME_TIME = 1.0 / FRAME_CAP;

    // Window AspectRatio

    private static final int ASPECT_RATIO_WIDTH = 16;
    private static final int ASPECT_RATIO_HEIGHT = 9;

    public static final Main cny = new Main();

    private boolean isRunning;
    private boolean canPause;

    private long window;
    private long cursor;
    private int width;
    private int height;
    private int maxScreenWidth;
    private int maxScreenHeight;
    private boolean isFullscreen;

    private float delta;

    private Thread thread;
    private GameState state;

    // Input

    private Keyboard keyboard;
    private Mouse mouse;

    // Rendering Objects

    private Scenegraph scenegraph;
    private Mesh mesh;
    private ShaderProgram shaderProgram;

    private Main() { }

    public void Start()
    {
        if (this.isRunning)
            return;

        this.thread = new Thread(this, "CNY: Client");
        this.thread.start();
    }

    public void Stop()
    {
        if (!this.isRunning)
            return;

        this.isRunning = false;
    }

    private void CreateWindow()
    {
        if (!glfwInit())
        {
            throw new IllegalStateException("Error: GLFW couldn't initialize");
        }

        // Setting up window hints

        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 2);
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
        glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GLFW_TRUE);
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);

        // Maximize the width and height to match the max screen size

        GLFWVidMode vidMode = glfwGetVideoMode(glfwGetPrimaryMonitor());
        assert vidMode != null;
        this.maxScreenWidth = vidMode.width();
        this.maxScreenHeight = vidMode.height();

        this.width = 1280;
        this.height = 720;

        this.window = glfwCreateWindow(this.width, this.height, "Case New York | 1.0", 0, 0);

        if (this.window == 0)
        {
            throw new IllegalStateException("Error: Failed to create the GLFW window");
        }

        glfwSetWindowPos(this.window, (this.maxScreenWidth - this.width) / 2, (this.maxScreenHeight - this.height) / 2);

        glfwMakeContextCurrent(this.window);
        GL.createCapabilities();

        // Setting up the Window Icon and Setting up a custom Cursor

        CreateWindowIcon();
        CreateCustomWindowCursor();

        // Setting up Size callback

        glfwSetWindowSizeCallback(this.window, this);

        // This function will resize the window if the aspect ratio changes.
        // The current AspectRatio is 16:9 and the highest

        glfwSetWindowAspectRatio(this.window, ASPECT_RATIO_WIDTH, ASPECT_RATIO_HEIGHT);
    }

    private void CreateWindowIcon()
    {
        GLFWImage.Buffer images = GLFWImage.malloc(1);
        images.put(0, CreateGLFWImage("icon.png"));

        glfwSetWindowIcon(window, images);
    }

    private void CreateCustomWindowCursor()
    {
        cursor = glfwCreateCursor(CreateGLFWImage("cursor.png"), 0, 0);
        glfwSetCursor(this.window, this.cursor);
    }

    private GLFWImage CreateGLFWImage(String imagePath)
    {
        ByteBuffer buffer = ImageLoader.LoadImageToByteBuffer(imagePath);
        GLFWImage image = GLFWImage.malloc();
        Image imageSize = new Image(imagePath);
        image.set(imageSize.GetWidth(), imageSize.GetHeight(), buffer);

        return image;
    }

    // WindowSize callback

    @Override
    public void invoke(long window, int width, int height)
    {
        if (this.width != width || this.height != height)
            Resize(width, height);
    }

    private void InitializeOpenAL()
    {
        long device = ALC10.alcOpenDevice((ByteBuffer) null);

        ALCCapabilities deviceCapabilities = ALC.createCapabilities(device);

        IntBuffer openALContext = BufferUtils.createIntBuffer(16);

        openALContext.put(ALC10.ALC_REFRESH);
        openALContext.put(60);

        openALContext.put(ALC10.ALC_SYNC);
        openALContext.put(ALC10.ALC_TRUE);

        openALContext.put(EXTEfx.ALC_MAX_AUXILIARY_SENDS);
        openALContext.put(2);

        openALContext.put(0);
        openALContext.flip();

        long newContext = ALC10.alcCreateContext(device, openALContext);

        if (!ALC10.alcMakeContextCurrent(newContext)) {
            throw new IllegalStateException("Error: Failed to make context current");
        }

        AL.createCapabilities(deviceCapabilities);
    }

    private void InitializeRendering()
    {
        float[] vertices = new float[]
        {
            -0.5f,  0.5f,      // Top Left
            -0.5f, -0.5f,      // Bottom Left
             0.5f, -0.5f,      // Bottom Right
             0.5f,  0.5f       // Top Right
         };

        float[] textureCoordinates = new float[]
        {
             0, 0,
             0, 1,
             1, 1,
             1, 0
        };

        int[] indices = new int[] {3, 2, 1, 1, 0, 3};

        this.mesh = new Mesh();
        this.mesh.Create(vertices, textureCoordinates, indices);

        this.shaderProgram = new DefaultShaderProgram();

        SetScenegraph(new MainMenu(), true);

        // Disabling for transparent textures

        glDisable(GL_DEPTH_TEST);
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

        System.out.println("OpenGL Version: " + glGetString(GL_VERSION));
        System.out.println("OpenAL Version: " + AL10.alGetString(AL10.AL_VERSION));
    }

    private void Initialize()
    {
        this.CreateWindow();

        // Creating input

        this.keyboard = new Keyboard(this.window);
        this.mouse = new Mouse();

        // Initializing the SoundManager / OpenAL

        this.InitializeOpenAL();

        this.InitializeRendering();

        this.canPause = true;


        // Show Window

        glfwShowWindow(this.window);
    }

    @Override
    public void run()
    {
        this.isRunning = true;

        this.Initialize();

        int frames = 0;
        double frameCounter = 0;
        double lastTime = (double)System.nanoTime()/(double)1000000000L;
        double unprocessedTime = 0;

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

                if (glfwWindowShouldClose(this.window))
                    Stop();


                this.SetDelta((float) FRAME_TIME);
                this.Update();

                if (frameCounter >= 1.0)
                {
                    System.out.println(frames);
                    frames = 0;
                    frameCounter = 0;
                }
            }

            this.Render();
            frames++;

            if (!render)
            {
                this.SleepThread();
            }
        }

        this.CleanUp();
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

    private void Update()
    {
        this.UpdateGame();

        this.keyboard.Update();
        this.mouse.Update();

        glfwPollEvents();
    }

    private void UpdateGame() {
        if (this.scenegraph != null)
            this.scenegraph.Update(this.delta);

        switch (this.state) {

            // Disable PauseMenu

            case MAIN_MENU, SETTINGS_MENU, FIRST_SCENE -> canPause = false;

            // Enable PauseMenu on levels
            case LEVEL_1, LEVEL_2, LEVEL_3, LEVEL_4 -> canPause = true;
        }

        if (this.canPause && Keyboard.IsKeyPushed(GLFW.GLFW_KEY_ESCAPE) && this.state != GameState.PAUSE_MENU) {
            assert this.scenegraph != null;
            this.scenegraph.OnPause();

            Scenegraph oldScene = this.scenegraph;
            SetScenegraph(new PauseMenu(oldScene), true);
        }

        // Setting Window to fullscreen if F11 is pressed

        if (Keyboard.IsKeyPushed(GLFW_KEY_F11))
        {
            ToggleFullscreen();
        }
    }

    private void ToggleFullscreen()
    {
       if (!this.isFullscreen)
       {
           glfwSetWindowSizeCallback(this.window, null);
           glfwSetWindowSize(this.window, this.maxScreenWidth, this.maxScreenHeight);
           ChangeViewPort(this.maxScreenWidth, this.maxScreenHeight);
           glfwSetWindowMonitor(this.window, glfwGetPrimaryMonitor(), 0,0, this.maxScreenWidth, this.maxScreenHeight, 60);

           this.isFullscreen = true;
       }
       else
       {
           glfwSetWindowSizeCallback(this.window, this);
           glfwSetWindowSize(this.window, this.width, this.height);
           ChangeViewPort(this.width, this.height);

           glfwSetWindowMonitor(this.window, 0, (this.maxScreenWidth - this.width) / 2, (this.maxScreenHeight - this.height) / 2, this.width, this.height, 60);


           this.isFullscreen = false;
       }
    }

    private void Render()
    {
        // Clearing the screen
        glClear(GL_COLOR_BUFFER_BIT);

        for (Node node : this.scenegraph.GetNodeObjects())
        {
            GL13.glActiveTexture(GL13.GL_TEXTURE0);

            node.GetImage().Bind();
            this.shaderProgram.Bind();
            this.shaderProgram.UpdateUniforms(node);
            this.mesh.Draw();
            node.GetImage().Unbind();
        }

        // Rendering the GLFW window
        glfwSwapBuffers(window);
    }


    private void CleanUp()
    {

        // Delete the game objects

        if (this.scenegraph != null)
            this.scenegraph.CleanUp();

        this.mesh.Delete();
        this.shaderProgram.Delete();

        // Destroy OpenAL

        long openALContext = ALC10.alcGetCurrentContext();

        ALC10.alcDestroyContext(openALContext);
        ALC10.alcCloseDevice(openALContext);

        // Destroy Input and Window

        this.keyboard.Destroy();
        this.mouse.Destroy();

        glfwDestroyCursor(this.cursor);
        glfwDestroyWindow(this.window);
        glfwTerminate();

        while (this.thread.isAlive())
        {
            try
            {
                this.thread.join(100);
                break;
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }
        }

    }

    public void ForceWindowToClose()
    {
        glfwSetWindowShouldClose(this.window, true);
    }

    public long GetWindow()
    {
        return this.window;
    }

    public int GetWidth()
    {
        if (this.isFullscreen)
            return this.maxScreenWidth;

        return this.width;
    }

    public int GetHeight()
    {
        if (this.isFullscreen)
            return this.maxScreenHeight;

        return this.height;
    }

    public void Resize(int width, int height)
    {
        this.width = width;
        this.height = height;

        ChangeViewPort(width, height);
    }

    private void ChangeViewPort(int width, int height)
    {
        glViewport(0,0, width, height);
    }

    public void SetDelta(float delta)
    {
        this.delta = delta;
    }

    public void SetScenegraph(Scenegraph newScene, boolean shouldInit)
    {
        if (this.scenegraph != null)
        {
            this.scenegraph.CleanUp();
        }

        if (shouldInit)
            newScene.Initialize();

        this.scenegraph = newScene;
    }

    public void SetState(GameState state)
    {

        this.state = state;
    }

    public enum GameState
    {
        // Menus

        MAIN_MENU,
        SETTINGS_MENU,
        PAUSE_MENU,

        // Scenes

        FIRST_SCENE,

        // Levels

        LEVEL_1,
        LEVEL_2,
        LEVEL_3,
        LEVEL_4
    }
}