package net.cny;

import net.cny.core.CoreEngine;
import net.cny.core.util.ResourceLoader;
import net.cny.scenegraph.MainMenu;

import java.io.IOException;
import java.util.Properties;

public class Main extends CoreEngine implements Runnable
{

	public static double frameCap;
    private final Thread thread;

    public Main()
    {

        thread = new Thread(this, "CNY: Client");
        thread.start();
    }

    @Override
    public void run()
    {
        Properties properties = new Properties();
        try
        {
            properties.load(ResourceLoader.GetResource("cny-properties.net"));
        } catch (IOException e)
        {
            e.printStackTrace();
        }

        int displayWidth = Integer.parseInt(properties.getProperty("displayWidth"));
        int displayHeight = Integer.parseInt(properties.getProperty("displayHeight"));
        String displayTittle = properties.getProperty("displayTitle");
        String version = properties.getProperty("version");

        String iconPath = properties.getProperty("iconPath");
        String cursorPath = properties.getProperty("cursorPath");

        frameCap = Double.parseDouble(properties.getProperty("frameRate"));

        CreateWindow(displayWidth, displayHeight, displayTittle + " | " + version, frameCap);
        CreateIcon(iconPath, cursorPath);
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
