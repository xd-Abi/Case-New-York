package net.cny;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

import net.cny.gui.menu.MainMenu;
import net.cny.gui.menu.PauseMenu;
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
import org.lwjgl.openal.*;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL13;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;


public class Main implements Runnable
{

    public static final double FRAME_CAP = 60;
    private static final double FRAME_TIME = 1.0 / FRAME_CAP;

    public static final Main cny = new Main();

    private boolean isRunning;
    private boolean isPausable;

    private long window;
    private int width;
    private int height;

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
        if (isRunning)
            return;

        thread = new Thread(this, "CNY: Client");
        thread.start();
    }

    public void Stop()
    {
        if (!isRunning)
            return;

        isRunning = false;
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

       /* GLFWVidMode vidMode = glfwGetVideoMode(glfwGetPrimaryMonitor());
        assert vidMode != null;
        width = vidMode.width();
        height = vidMode.height();
        */
        //TODO: Temporary
        width = 1280;
        height = 720;

        window = glfwCreateWindow(width, height, "Fall New York", 0, 0);

        if (window == 0)
        {
            throw new IllegalStateException("Error: Failed to create the GLFW window");
        }


        // Setting up the Window Icon

        ByteBuffer bufferedImage = ImageLoader.LoadImageToByteBuffer("icon.png");
        GLFWImage image = GLFWImage.malloc();
        image.set(235, 300, bufferedImage);

        GLFWImage.Buffer images = GLFWImage.malloc(1);
        images.put(0, image);

        glfwSetWindowIcon(window, images);

        glfwMakeContextCurrent(window);
        GL.createCapabilities();
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
        mesh = new Mesh();
        mesh.Create();

        shaderProgram = new DefaultShaderProgram();

        SetScenegraph(new MainMenu(), true);

        // Disabling for transparent textures

        glDisable(GL_DEPTH_TEST);
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
    }

    private void Initialize()
    {
        CreateWindow();

        // Creating input

        keyboard = new Keyboard(window);
        mouse = new Mouse();

        // Initializing the SoundManager / OpenAL

        InitializeOpenAL();

        InitializeRendering();

        isPausable = true;


        // Show Window

        glfwShowWindow(window);
    }

    @Override
    public void run()
    {
        isRunning = true;

        Initialize();

        int frames = 0;
        double frameCounter = 0;
        double lastTime = (double)System.nanoTime()/(double)1000000000L;
        double unprocessedTime = 0;

        while (isRunning)
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

                if (glfwWindowShouldClose(window))
                    Stop();


                SetDelta((float) FRAME_TIME);
                Update();

                if (frameCounter >= 1.0)
                {
                    System.out.println(frames);
                    frames = 0;
                    frameCounter = 0;
                }
            }

            Render();
            frames++;

            if (!render)
            {
                SleepThread();
            }
        }

        CleanUp();
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
        UpdateGame();

        keyboard.Update();
        mouse.Update();

        glfwPollEvents();
    }

    private void UpdateGame()
    {
        if (scenegraph != null)
            scenegraph.Update(delta);

        switch (state) {

            // Disable PauseMenu

            case MAIN_MENU, SETTINGS_MENU, FIRST_SCENE -> isPausable = false;

            // Enable PauseMenu on levels
            case LEVEL_1, LEVEL_2, LEVEL_3, LEVEL_4 -> isPausable = true;
        }

        if (isPausable && Keyboard.IsKeyPushed(GLFW.GLFW_KEY_ESCAPE) && state != GameState.PAUSE_MENU)
        {
            assert scenegraph != null;
            scenegraph.OnPause();

            Scenegraph oldScene = scenegraph;
            SetScenegraph(new PauseMenu(oldScene), true);
        }
    }

    private void Render()
    {
        // Clearing the screen
        glClear(GL_COLOR_BUFFER_BIT);

        for (Node node : scenegraph.GetNodeObjects())
        {
            GL13.glActiveTexture(GL13.GL_TEXTURE0);

            node.GetImage().Bind();
            shaderProgram.Bind();
            shaderProgram.UpdateUniforms(node);
            mesh.Draw();
            node.GetImage().Unbind();
        }

        // Rendering the GLFW window
        glfwSwapBuffers(window);
    }


    private void CleanUp()
    {

        // Delete the game objects

        if (scenegraph != null)
            scenegraph.CleanUp();

        mesh.Delete();
        shaderProgram.Delete();

        // Destroy OpenAL

        long openALContext = ALC10.alcGetCurrentContext();

        ALC10.alcDestroyContext(openALContext);
        ALC10.alcCloseDevice(openALContext);

        // Destroy Input and Window

        keyboard.Destroy();
        mouse.Destroy();

        glfwDestroyWindow(window);
        glfwTerminate();

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
        glfwSetWindowShouldClose(window, true);
    }

    public long GetWindow()
    {
        return window;
    }

    public int GetWidth()
    {
        return width;
    }

    public int GetHeight()
    {
        return height;
    }

    public void SetDelta(float delta)
    {
        this.delta = delta;
    }

    public void SetScenegraph(Scenegraph newScene, boolean shouldInit)
    {
        if (scenegraph != null)
        {
            scenegraph.CleanUp();
        }

        if (shouldInit)
            newScene.Initialize();

        scenegraph = newScene;
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
        LEVEL_4;
    }
}