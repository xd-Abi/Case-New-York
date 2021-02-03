package old.cny;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.NULL;


import old.cny.gui.menu.MainMenu;
import old.cny.gui.menu.PauseMenu;
import old.cny.image.Image;
import old.cny.level.FirstLevel;
import old.cny.model.Mesh;
import old.cny.pipeline.ShaderProgram;
import old.cny.platform.Keyboard;
import old.cny.platform.Mouse;
import old.cny.scenegraph.Scenegraph;
import old.cny.util.ImageLoader;
import old.cny.util.ResourceLoader;
import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFWImage;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.glfw.GLFWWindowSizeCallback;
import org.lwjgl.openal.*;
import org.lwjgl.opengl.GL13;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.Properties;

public class Main implements Runnable
{

    public static final double FRAME_CAP = 60;
    public static final double FRAME_TIME = 1.0 / FRAME_CAP;

    // Static instance of Main

    public static final Main cny = new Main();

    private boolean isRunning;
    private boolean canPause;
    private float delta;


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

    // Classes

    private Thread thread;
    private Keyboard keyboard;
    private Mouse mouse;
    private GameState state;

    private Mesh mesh;
    private ShaderProgram shaderProgram;
    private Scenegraph scenegraph;

    private GLFWWindowSizeCallback windowSizeCallback;

    private Main()
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
        Properties config = new Properties();
        config.load(ResourceLoader.GetResource("cny-properties.net"));

        // Loading Window Configs

        displayWidth = Integer.parseInt(config.getProperty("display-width"));
        displayHeight = Integer.parseInt(config.getProperty("display-height"));
        displayTitle = config.getProperty("display-title");
        displayIconPath = config.getProperty("display-icon-path");

        // Window Cursor

        windowCursorPath = config.getProperty("cursor-path");

        config.clear();
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

    private void InitOpenAL()
    {
        // Getting the current OpenAL Device

        long device = ALC10.alcOpenDevice((ByteBuffer) null);

        ALCCapabilities deviceCapabilities = ALC.createCapabilities(device);

        // Creating new Context and setting it up

        long newContext = ALC10.alcCreateContext(device, CreateOpenALContext());

        if (!ALC10.alcMakeContextCurrent(newContext))
        {
            throw new IllegalStateException("Error: Failed to create OpenAL context current");
        }

        AL.createCapabilities(deviceCapabilities);
    }

    private IntBuffer CreateOpenALContext()
    {
        IntBuffer openALContext = BufferUtils.createIntBuffer(16);

        openALContext.put(ALC10.ALC_REFRESH);
        openALContext.put(60);

        openALContext.put(ALC10.ALC_SYNC);
        openALContext.put(ALC10.ALC_FALSE);

        openALContext.put(EXTEfx.ALC_MAX_AUXILIARY_SENDS);
        openALContext.put(2);

        openALContext.put(0);
        openALContext.flip();

        return openALContext;
    }

    private void InitRendering()
    {
        // Creating the 2D mesh for rendering the hole scene

        CreateMesh();

        // Creating the default ShaderProgram

        CreateShaderProgram();

        // Setting the first scene to be
        // the MainMenu

        SetScenegraph(new MainMenu(), true);

        // These function will allow a texture to be transparent

        glDisable(GL_DEPTH_TEST);
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);


        System.out.println("OpenGL Version: " + glGetString(GL_VERSION));
        System.out.println("OpenAL Version: " + AL10.alGetString(AL10.AL_VERSION));
    }

    private void CreateMesh()
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

        mesh = new Mesh();
        mesh.Create(vertices, textureCoordinates, indices);
    }

    private void CreateShaderProgram()
    {
        shaderProgram = new ShaderProgram();
        shaderProgram.AddVertexShader(GetVertexShader());
        shaderProgram.AddFragmentShader(GetFragmentShader());
        shaderProgram.CompileShader();

        // Adding the worldMatrix

        shaderProgram.AddUniform("worldMatrix");
    }

    private String GetVertexShader()
    {
        return
        """
           #version 330

           layout (location = 0) in vec2 position;
           layout (location = 1) in vec2 textureCoordinates;

           out vec2 passTextureCoordinates;

           uniform mat4 worldMatrix;

           void main()
           {
                gl_Position = worldMatrix * vec4(position.x, position.y, 0, 1);
                passTextureCoordinates = textureCoordinates;
           }
        """;
    }


    private String GetFragmentShader()
    {
        return
        """
            #version 330

            in vec2 passTextureCoordinates;

            out vec4 FragColor;

            uniform sampler2D sampler;

            void main()
            {
                FragColor = texture(sampler, passTextureCoordinates);
            }
        """;
    }

    @Override
    public void run()
    {
        // Initialising Core

        CreateWindow();
        CreateWindowCursor();
        CreateInput();

        // Initialising OpenAL and OpenGL

        InitOpenAL();
        InitRendering();

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

                // Setting delta and Updating game

                SetDelta();
                Update();

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

    private void Update()
    {

        // Update Game

        UpdateGame();

        // Update Inputs

        keyboard.Update();
        mouse.Update();

        glfwPollEvents();
    }

    private void UpdateGame()
    {
        // Shortcuts

        if (Keyboard.IsKeyPushed(GLFW_KEY_F1))
        {
            SetScenegraph(new FirstLevel(), true);
        }

        if (this.scenegraph != null)
        {
            this.scenegraph.Update(delta);
        }

        switch (state)
        {

            // Disable PauseMenu

            case MAIN_MENU, SETTINGS_MENU, FIRST_SCENE -> canPause = false;

            // Enable PauseMenu on levels

            case LEVEL_1, LEVEL_2, LEVEL_3, LEVEL_4 -> canPause = true;
        }

        if (this.canPause && Keyboard.IsKeyPushed(org.lwjgl.glfw.GLFW.GLFW_KEY_ESCAPE) && this.state != GameState.PAUSE_MENU)
        {
            if (scenegraph != null)
            {
                scenegraph.OnPause();
            }

            Scenegraph oldScene = scenegraph;
            SetScenegraph(new PauseMenu(oldScene), true);
        }

        if (Keyboard.IsKeyPushed(GLFW_KEY_F11))
        {
            ToggleFullscreen();
        }
    }

    private void ClearScreen()
    {
        glClear(GL_COLOR_BUFFER_BIT);
    }

    private void Render()
    {
        ClearScreen();

        for (String key : scenegraph.GetNodeObjects().keySet())
        {
            GL13.glActiveTexture(GL13.GL_TEXTURE0);

            scenegraph.GetNode(key).GetImage().Bind();
            shaderProgram.Bind();
            shaderProgram.SetUniform("worldMatrix", scenegraph.GetNode(key).GetWorldMatrix());
            mesh.Draw();
            scenegraph.GetNode(key).GetImage().Unbind();
        }

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
        // Delete the game objects

        if (scenegraph != null)
        {
            scenegraph.CleanUp();
        }

        // Deleting the mesh and ShaderProgram

        mesh.Delete();
        shaderProgram.Delete();

        // Destroy OpenAL

        long openALContext = ALC10.alcGetCurrentContext();

        ALC10.alcDestroyContext(openALContext);
        ALC10.alcCloseDevice(openALContext);

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


    public void ForceWindowToClose()
    {
        glfwSetWindowShouldClose(windowId, true);
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

    public void SetScenegraph(Scenegraph newScene, boolean shouldInit)
    {
        if (this.scenegraph != null)
        {
            this.scenegraph.CleanUp();
        }

        if (shouldInit)
        {
            newScene.Initialize();
        }

        this.scenegraph = newScene;
    }

    public void SetState(GameState state)
    {
        this.state = state;
    }

    private void SetDelta()
    {
        this.delta = (float) 0.016666668;
    }

    public static class ResourceLocation
    {
        public static final String SCENE_ONE = "scene/first/";
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
