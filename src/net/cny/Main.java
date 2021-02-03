package net.cny;

import net.cny.config.AudioConfig;
import net.cny.config.RenderConfig;
import net.cny.game.MainMenu;
import net.cny.platform.Cursor;
import net.cny.platform.Keyboard;
import net.cny.platform.Mouse;
import net.cny.platform.Window;
import net.cny.scenegraph.Scenegraph;

public class Main implements Runnable
{

    public static final double FRAME_CAP = 60;
    public static final double FRAME_TIME = 1.0 / FRAME_CAP;

    private boolean isRunning;

    private Thread thread;
    private Window window;

    private Cursor cursor;
    private Mouse mouse;
    private Keyboard keyboard;

    private AudioConfig audioConfig;
    private RenderConfig renderConfig;

    private Scenegraph scenegraph;

    public Main()
    {

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

    private void Initialize()
    {
        window = new Window(1280, 720, "Case New York | v.2");
        window.Initialize();
        window.Create("icon.png", 250, 250);

        cursor = new Cursor(window, "cursor.png");
        mouse = new Mouse(window, cursor);
        keyboard = new Keyboard(window.GetId());

        audioConfig = new AudioConfig();
        audioConfig.InitOpenAL();

        renderConfig = new RenderConfig();
        renderConfig.InitOpenGL();

        SetScenegraph(new MainMenu(this));

        window.Show();
    }

    @Override
    public void run() {

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

            while (unprocessedTime > FRAME_TIME)
            {
                render = true;
                unprocessedTime -= FRAME_TIME;

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

        try {
            OnShutdown();
        } catch (InterruptedException e) {

            e.printStackTrace();
        }
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
        scenegraph.Update();
        keyboard.Update();
        mouse.Update();
        window.Update();
    }

    private void Render()
    {
        renderConfig.ClearScreen();
        scenegraph.Render();
        window.Render();
    }

    private void OnShutdown() throws InterruptedException {

        scenegraph.CleanUp();

        audioConfig.DestroyContext();

        keyboard.Destroy();
        mouse.Destroy();
        cursor.Destroy();

        window.Shutdown();
        thread.join(100);
    }

    public void SetScenegraph(Scenegraph scenegraph)
    {
        if (this.scenegraph != null)
            this.scenegraph.CleanUp();

        scenegraph.Initialize();
        this.scenegraph = scenegraph;
    }
}
