package net.cny.scenegraph;

import java.util.HashMap;
import java.util.Map;

import net.cny.Main;
import net.cny.core.audio.Audio;
import net.cny.gui.GuiBackground;
import net.cny.gui.GuiButton;
import net.cny.core.scenegraph.Node;
import net.cny.core.scenegraph.Scenegraph;
import net.cny.state.GameState;

public class MainMenu extends Scenegraph
{

	private static final String PATH = "menu/main/";
	
    private Audio backgroundAudio;
    private final Main main;

    public MainMenu(Main main)
    {
        this.main = main;
    }

    @Override
    public Map<String, Node> CreateMap()
    {
        Map<String, Node> objects = new HashMap<>();
        
        objects.put("background", new GuiBackground(PATH + "background.png"));
        objects.put("title", new GuiBackground(PATH + "title.png", -0.95f, 0.1f, 0.7f, 0.7f));
        objects.put("play-button", new GuiButton(PATH + "play-button.png", -0.9f, -0.3f,0.4f, 0.17f, true));
        objects.put("settings-button", new GuiButton(PATH + "settings-button.png", -0.9f, -0.52f, 0.4f, 0.17f, true));
        objects.put("quit-button", new GuiButton(PATH + "quit-button.png", -0.9f, -0.74f, 0.4f, 0.17f, true));
        
        return objects;
    }
    
    @Override
    public void Initialize() 
    {
    	super.Initialize();
        
    	GameState.SetState(GameState.MAIN_MENU);
        
        backgroundAudio = new Audio(PATH + "background_audio.wav");
        backgroundAudio.Play();
    }

    @Override
    public void Update() 
    {
        super.Update();

        if (((GuiButton)GetNode("play-button")).IsPressed())
        {
            main.SetScenegraph(new FirstLevel(main));
        }

        if (((GuiButton)GetNode("quit-button")).IsPressed())
        {
            main.Stop();
        }

    }

    @Override
    public void CleanUp()
    {
        backgroundAudio.CleanUp();
        super.CleanUp();
    }
}