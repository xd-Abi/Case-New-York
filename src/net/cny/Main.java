package net.cny;

import net.cny.platform.Window;

public class Main implements Runnable
{

    private boolean isRunning;

    private Thread thread;
    private Window window;

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
     //   window.Create();
        window.Show();
    }

    @Override
    public void run()
    {

        Initialize();

        while (isRunning)
        {
            window.Update();
            window.Render();

            if (window.IsCloseRequested())
                Stop();
        }

        OnShutdown();
    }

    private void OnShutdown()
    {
        window.Shutdown();
    }
}
