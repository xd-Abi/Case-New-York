package net.cny.core;

import net.cny.core.config.AudioConfig;
import net.cny.core.config.RenderConfig;
import net.cny.core.platform.Cursor;
import net.cny.core.platform.Keyboard;
import net.cny.core.platform.Mouse;
import net.cny.core.platform.Window;
import net.cny.core.scenegraph.Scenegraph;

public class CoreEngine
{

    public static double frameCap;

    private boolean isRunning;
    private double frameTime;

    private int width;
    private int height;
    private String title;

    private int iconWidth;
    private int iconHeight;
    private String iconPath;

    private Window window;
    private Cursor cursor;
    private Mouse mouse;
    private Keyboard keyboard;

    private AudioConfig audioConfig;
    private RenderConfig renderConfig;

    private Scenegraph scenegraph;


    public void CreateWindow(int width, int height, String title, double frameCap)
    {
        this.width = width;
        this.height = height;
        this.title = title;
        this.frameTime = 1.0 / frameCap;
        CoreEngine.frameCap = frameCap;
    }

    public void CreateIcon(String iconPath, int iconWidth, int iconHeight)
    {
        this.iconPath = iconPath;
        this.iconWidth = iconWidth;
        this.iconHeight = iconHeight;
    }

    public void Start()
    {
        if (isRunning)
        {
            return;
        }

        isRunning = true;
        Run();
    }

    public void Stop()
    {
        if (!isRunning)
        {
            return;
        }

        isRunning = false;
    }

    public void Initialize()
    {
        window = new Window(width, height, title);
        window.Initialize();
        window.Create(iconPath, iconWidth, iconHeight);

        cursor = new Cursor(window, "cursor.png");
        mouse = new Mouse(window, cursor);
        keyboard = new Keyboard(window.GetId());

        audioConfig = new AudioConfig();
        audioConfig.InitOpenAL();

        renderConfig = new RenderConfig();
        renderConfig.InitOpenGL();
    }

    public void Run() {

        Initialize();

        int frames = 0;
        double frameCounter = 0;
        double lastTime = (double) System.nanoTime() / (double) 1000000000L;
        double unprocessedTime = 0;

        // Game Loop

        while (this.isRunning) {
            boolean render = false;

            double startTime = (double) System.nanoTime() / (double) 1000000000L;
            double passedTime = startTime - lastTime;
            lastTime = startTime;

            unprocessedTime += passedTime;
            frameCounter += passedTime;

            while (unprocessedTime > frameTime)
            {
                render = true;
                unprocessedTime -= frameTime;

                if (window.IsCloseRequested())
                {
                    Stop();
                }

                // Setting delta and Updating game

                Update();

                if (frameCounter >= 1.0) {
                    System.out.println(frames);
                    frames = 0;
                    frameCounter = 0;
                }
            }

            // Rendering the game

            Render();
            frames++;

            if (!render) {
                this.SleepThread();
            }
        }


        OnShutdown();
    }

    public void SleepThread()
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

    public void Update()
    {
        scenegraph.Update();
        keyboard.Update();
        mouse.Update();
        window.Update();
    }

    public void Render()
    {
        renderConfig.ClearScreen();
        scenegraph.Render();
        window.Render();
    }

    public void OnShutdown(){

        scenegraph.CleanUp();

        audioConfig.DestroyContext();

        keyboard.Destroy();
        mouse.Destroy();
        cursor.Destroy();

        window.Shutdown();
    }

    public void ShowWindow()
    {
        window.Show();
    }

    public void SetScenegraph(Scenegraph scenegraph)
    {
        if (this.scenegraph != null)
            this.scenegraph.CleanUp();

        scenegraph.Initialize();
        this.scenegraph = scenegraph;
    }
}
