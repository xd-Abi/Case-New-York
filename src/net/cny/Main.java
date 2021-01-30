package net.cny;

import net.cny.audio.SoundManager;
import net.cny.gui.menu.MainMenu;
import net.cny.gui.menu.PauseMenu;
import net.cny.platform.Keyboard;
import net.cny.platform.Mouse;
import net.cny.platform.Window;
import net.cny.renderer.MasterRenderer;
import net.cny.scenegraph.Scenegraph;
import org.lwjgl.glfw.GLFW;

public class Main implements Runnable
{
    public static final Main cny = new Main();

    private boolean isRunning;
    private boolean isPausable;

    private Thread thread;
    private GameState state;

    private Main() { }

    public synchronized void Start()
    {
        if (isRunning)
            return;

        thread = new Thread(this,"Case New York: Client");
        thread.start();
    }

    public synchronized void Stop()
    {
        if (!isRunning)
            return;

        isRunning = false;
    }

    private void Initialize()
    {
        // Creating the Window

        Window.Initialize();
        Window.CreateWindow();

        // Creating input

        Keyboard.Create();
        Mouse.Create();

        // Initializing the SoundManager

        SoundManager.Initialize();

        MasterRenderer.Initialize();
        MasterRenderer.SetScene(new MainMenu());
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

            double frameTime = 1.0 / 60;

            while (unprocessedTime > frameTime)
            {
                render = true;

                unprocessedTime -= frameTime;

                if (Window.IsCloseRequested())
                    Stop();

                Update((float)frameTime);

                if (frameCounter >= 1.0)
                {
                    System.out.println(frames);
                    frames = 0;
                    frameCounter = 0;
                }
            }

            if (render)
            {
                MasterRenderer.Render();
                frames++;
            }
            else
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
        }

        CleanUp();
    }

    private void Update(float delta)
    {
        MasterRenderer.Update(delta);

        UpdateGame(delta);

        Keyboard.Update();
        Mouse.Update();
        Window.Update();
    }

    private void UpdateGame(float delta)
    {
        if (isPausable && Keyboard.IsKeyPushed(GLFW.GLFW_KEY_ESCAPE))
        {
            Scenegraph oldScenegraph = MasterRenderer.GetScene();
            MasterRenderer.SetScene(new PauseMenu(oldScenegraph));
        }
    }

    private void CleanUp()
    {

        MasterRenderer.CleanUp();
        SoundManager.CleanUp();

        // Destroy Input and Window

        Keyboard.Destroy();
        Mouse.Destroy();
        Window.Dispose();

        boolean isThreadJoined = false;

        while (!isThreadJoined)
        {

           try
            {
                thread.join();
                isThreadJoined = true;
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }

        }
        System.out.println("TEST");

    }

    public void setIsPausable(boolean isPausable)
    {
        this.isPausable = isPausable;
    }

    public GameState GetState()
    {
        return state;
    }

    public void SetState(GameState state)
    {
        this.state = state;
    }
}
