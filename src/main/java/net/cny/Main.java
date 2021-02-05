package net.cny;

import net.cny.core.CoreEngine;
import net.cny.scenegraph.MainMenu;

public class Main extends CoreEngine implements Runnable
{

    private final Thread thread;

    public Main()
    {

        thread = new Thread(this, "CNY: Client");
        thread.start();
    }

    @Override
    public void run()
    {
        CreateWindow(1280, 720, "TEST", 60);
        CreateIcon("icon.png", 250, 250);
        Start();
    }

    @Override
    public void Initialize()
    {
        super.Initialize();

        SetScenegraph(new MainMenu(this));
        ShowWindow();
    }

    @Override
    public void OnShutdown()
    {
        super.OnShutdown();
        try
        {
            thread.join(100);
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
    }
}
