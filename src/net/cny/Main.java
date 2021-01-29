package net.cny;

import net.cny.audio.SoundManager;
import net.cny.gui.menu.MainMenu;
import net.cny.platform.Keyboard;
import net.cny.platform.Mouse;
import net.cny.platform.Window;
import net.cny.renderer.MasterRenderer;

public class Main
{

    private static boolean isRunning;

    private static void Initialize()
    {
        // Creating the Window

        Window.Initialize();
        Window.CreateWindow();

        // Creating input

        Keyboard.Create();
        Mouse.Create();

        // Initializing the SoundManager

        SoundManager.Initialize();

        MasterRenderer.SetScene(new MainMenu());
    }

    public static void Run()
    {
        if (isRunning)
            return;

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

    public static void Stop()
    {
        if (!isRunning)
            return;

        isRunning = false;
    }

    private static void Update(float delta)
    {
        MasterRenderer.Update(delta);
        Keyboard.Update();
        Mouse.Update();
        Window.Update();
    }

    private static void CleanUp()
    {

        MasterRenderer.CleanUp();
        SoundManager.CleanUp();

        // Destroy Input and Window

        Keyboard.Destroy();
        Mouse.Destroy();
        Window.Dispose();
    }
}
