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


public class Main implements Game
{

    public static final double FRAME_CAP = 60;
    public static final double FRAME_TIME = 1.0 / FRAME_CAP;

    public static final Main cny = new Main();

    private boolean canPause;

    private GameThread thread;
    private GameState state;

    private Window window;
    private WindowCursor windowCursor;
    private FullscreenManager fullscreenManager;

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
        thread = new GameThread(this, "CNY: Client");
        thread.Start();
    }

    public void Stop()
    {
        thread.Stop();
    }

    private void CreateWindow()
    {
        window = new Window(1280, 720);
        window.Initialize();
        window.Create();

        windowCursor = new WindowCursor(window.GetId());
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

    @Override
    public void Initialize()
    {
        this.CreateWindow();

        // Creating input

        this.keyboard = new Keyboard(this.window.GetId());
        this.mouse = new Mouse();

        fullscreenManager = new FullscreenManager(window);

        // Initializing the SoundManager / OpenAL

        this.InitializeOpenAL();

        this.InitializeRendering();

        this.canPause = true;


        // Show Window

        window.Show();
    }

    @Override
    public void Update(float delta)
    {
        this.UpdateGame(delta);

        fullscreenManager.Update();

        this.keyboard.Update();
        this.mouse.Update();

        if (window.IsCloseRequested())
            Stop();

        window.Update();
    }

    private void UpdateGame(float delta)
    {
        if (this.scenegraph != null)
            this.scenegraph.Update(delta);

        switch (this.state)
        {

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
    }

    @Override
    public void Render()
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
        window.Render();
    }


    @Override
    public void CleanUp()
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


        windowCursor.Destroy();
        window.Dispose();
    }

    public void ForceWindowToClose()
    {
        glfwSetWindowShouldClose(window.GetId(), true);
    }

    public long GetWindow()
    {
        return window.GetId();
    }

    public int GetWidth()
    {
        if (fullscreenManager.IsFullscreen())
            return fullscreenManager.GetMaxScreenWidth();

        return window.GetWidth();
    }

    public int GetHeight()
    {
        if (fullscreenManager.IsFullscreen())
            return fullscreenManager.GetMaxScreenHeight();

        return window.GetHeight();
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

class Window extends GLFWWindowSizeCallback
{

    // Window AspectRatio

    private static final int ASPECT_RATIO_WIDTH = 16;
    private static final int ASPECT_RATIO_HEIGHT = 9;

    private long id;
    private int width;
    private int height;

    public Window(int width, int height)
    {
        this.width = width;
        this.height = height;
    }

    public void Initialize()
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
    }

    public void Create()
    {

        this.id = glfwCreateWindow(this.width, this.height, "Case New York | 1.0", 0, 0);

        if (this.id == 0)
        {
            throw new IllegalStateException("Error: Failed to create the GLFW window");
        }

        GLFWVidMode vidMode = glfwGetVideoMode(glfwGetPrimaryMonitor());
        assert vidMode != null;
        glfwSetWindowPos(this.id, (vidMode.width() - this.width) / 2, (vidMode.height() - this.height) / 2);

        glfwMakeContextCurrent(this.id);
        GL.createCapabilities();

        // Setting up the Window Icon and Setting up a custom Cursor

        ByteBuffer buffer = ImageLoader.LoadImageToByteBuffer("icon.png");
        GLFWImage image = GLFWImage.malloc();
        Image imageSize = new Image("icon.png");
        image.set(imageSize.GetWidth(), imageSize.GetHeight(), buffer);

        GLFWImage.Buffer images = GLFWImage.malloc(1);
        images.put(0, image);

        glfwSetWindowIcon(id, images);

        // Setting up Size callback

        glfwSetWindowSizeCallback(this.id, this);

        // This function will resize the window if the aspect ratio changes.
        //   The current AspectRatio is 16:9 and the highest


        glfwSetWindowAspectRatio(this.id, ASPECT_RATIO_WIDTH, ASPECT_RATIO_HEIGHT);
    }

    public void Show()
    {
        glfwShowWindow(id);
    }

    // WindowSize callback

    @Override
    public void invoke(long window, int width, int height)
    {
        if (this.width != width || this.height != height)
            Resize(width, height);
    }

    public void Update()
    {
        glfwPollEvents();
    }

    public void Render()
    {
        glfwSwapBuffers(id);
    }

    public void Dispose()
    {
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

        ChangeViewPort(width, height);
    }

    public void ChangeViewPort(int width, int height)
    {
        glViewport(0,0, width, height);
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

class FullscreenManager
{

    private boolean isFullscreen;

    private final Window window;
    private final int maxScreenWidth;
    private final int maxScreenHeight;
    private final int xPosition;
    private final int yPosition;

    public FullscreenManager(Window window)
    {
        this.window = window;

        GLFWVidMode vidMode = glfwGetVideoMode(glfwGetPrimaryMonitor());
        assert vidMode != null;
        maxScreenWidth = vidMode.width();
        maxScreenHeight = vidMode.height();
        xPosition = (maxScreenWidth - window.GetWidth()) / 2;
        yPosition = (maxScreenHeight - window.GetHeight()) / 2;
    }

    public void Update()
    {
        if (Keyboard.IsKeyPushed(GLFW_KEY_F11))
            ToggleFullscreen();
    }

    private void ToggleFullscreen()
    {
        if (!isFullscreen)
        {
            glfwSetWindowSizeCallback(window.GetId(), null);
            glfwSetWindowSize(window.GetId(), maxScreenWidth, maxScreenHeight);

            window.ChangeViewPort(maxScreenWidth, maxScreenHeight);

            glfwSetWindowMonitor(window.GetId(), glfwGetPrimaryMonitor(), 0,0, this.maxScreenWidth, this.maxScreenHeight, 60);

            isFullscreen = true;
        }
        else
        {
            glfwSetWindowSizeCallback(window.GetId(), window);
            glfwSetWindowSize(window.GetId(), window.GetWidth(), window.GetHeight());

            window.ChangeViewPort(window.GetWidth(), window.GetHeight());

            glfwSetWindowMonitor(window.GetId(), 0, xPosition, yPosition, window.GetWidth(), window.GetHeight(), (int)Main.FRAME_CAP);

            isFullscreen = false;
        }
    }

    public boolean IsFullscreen()
    {
        return isFullscreen;
    }

    public int GetMaxScreenWidth()
    {
        return maxScreenWidth;
    }

    public int GetMaxScreenHeight()
    {
        return maxScreenHeight;
    }
}

class WindowCursor
{
    private final long id;

    public WindowCursor(long window)
    {
        ByteBuffer buffer = ImageLoader.LoadImageToByteBuffer("cursor.png");
        GLFWImage image = GLFWImage.malloc();
        Image imageSize = new Image("cursor.png");
        image.set(imageSize.GetWidth(), imageSize.GetHeight(), buffer);


        id = glfwCreateCursor(image, 0, 0);
        glfwSetCursor(window, this.id);

        glfwSwapInterval(0);
    }

    public void Destroy()
    {
        glfwDestroyCursor(this.id);
    }
}

interface Game
{
    void Initialize();

    // Updating game using the Game Thread

    void Update(float delta);

    // Rendering using a different Thread;

    void Render();

    // CleanUp Method. Only used by the RenderThread

    void CleanUp();
}

class GameThread implements Runnable
{
    private boolean isRunning;
    private final Thread thread;
    private final Game game;

    public GameThread(Game game, String name)
    {
        this.thread = new Thread(this, name);
        this.game = game;
    }

    public void Start()
    {
        if (isRunning)
            return;

        thread.start();
    }

    public void Stop()
    {
        if (!isRunning)
            return;

        isRunning = false;
    }

    @Override
    public void run()
    {
        System.out.println("TEST");
        isRunning = true;

        game.Initialize();

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



            while (unprocessedTime > Main.FRAME_TIME)
            {
                render = true;

                unprocessedTime -= Main.FRAME_TIME;

                game.Update((float) Main.FRAME_TIME);

                if (frameCounter >= 1.0)
                {
                    System.out.println(frames);
                    frames = 0;
                    frameCounter = 0;
                }
            }

            game.Render();
            frames++;

            if (!render)
            {
                this.SleepThread();
            }
        }

        game.CleanUp();
        JoinThread();
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
}
