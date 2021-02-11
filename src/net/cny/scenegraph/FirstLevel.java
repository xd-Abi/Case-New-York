package net.cny.scenegraph;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_0;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_1;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_2;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_3;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_4;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_5;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_6;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_7;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_8;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_9;

import java.util.HashMap;
import java.util.Map;

import net.cny.Main;
import net.cny.core.audio.Audio;
import net.cny.core.platform.Keyboard;
import net.cny.core.scenegraph.Node;
import net.cny.core.scenegraph.Scenegraph;
import net.cny.gui.GuiBackground;
import net.cny.gui.GuiButton;
import net.cny.state.GameState;

public class FirstLevel extends Scenegraph
{

	private static final String PATH = "level/one/";
	private static final String AUDIOPATH = "level/one/audio/";
	private static final String INVENTORY_PATH = "inventory/inventory.png";
	
	private boolean isLockShown;
	
	private int[] numKeys = new int[]
			{
					GLFW_KEY_0,
					GLFW_KEY_1,
					GLFW_KEY_2,
					GLFW_KEY_3,
					GLFW_KEY_4,
					GLFW_KEY_5,
					GLFW_KEY_6,
					GLFW_KEY_7,
					GLFW_KEY_8,
					GLFW_KEY_9,
			};
	
	private boolean is6Pressed = false;
	private boolean is2Pressed = false;
	private boolean is5Pressed = false;
	
	
	private Map<String, GuiButton> buttons;	
    private Audio backgroundAudio;
     private Audio beepSound;
    private GuiBackground codeLock;

    private final Main main;
    
    public FirstLevel(Main main)
    {
    	this.main = main;
        GameState.SetState(GameState.LEVEL_1);
    }

    public Map<String, Node> CreateMap()
    {

    	Map<String, Node> objects = new HashMap<>();
        
        objects.put("background", new GuiBackground(PATH + "background.png"));
        objects.put("background2", new GuiBackground(PATH + "msg.png"));
        objects.put("invent_one", new GuiBackground(INVENTORY_PATH, -0.95f, 0.35f, 0.14f, 0.25f));
        objects.put("invent_second", new GuiBackground(INVENTORY_PATH, -0.95f, 0.05f, 0.14f, 0.25f));
        objects.put("invent_third", new GuiBackground(INVENTORY_PATH, -0.95f, -0.25f, 0.14f, 0.25f));
           
        return objects;
    }

    @Override
    public void Initialize() 
    {
    	super.Initialize();
    	
        backgroundAudio = new Audio(AUDIOPATH + "bg_audio.wav");
        backgroundAudio.SetGain(0.4f);
        backgroundAudio.Play();

        
        beepSound = new Audio(AUDIOPATH + "beep_sound.wav");
        
        buttons = CreateClickListeners();
        codeLock = new GuiBackground("level/one/code_lock.png", 0.1f, -0.9f, 0.6f, 1.8f);
    }
    
    private Map<String, GuiButton> CreateClickListeners()
    {
    	Map<String, GuiButton> map = new HashMap<>();
    	map.put("shoe-print", new GuiButton(-0.65f, 0.06f, 0.06f, 0.06f, false));
    	map.put("rope-2-m", new GuiButton(-0.05f, -0.07f, 0.03f, 0.034f, false));
    	map.put("rope-5-m",  new GuiButton(-0.29f, -0.67f, 0.06f, 0.06f, false));
    	map.put("door-lock", new GuiButton(0.77f, 0.26f, 0.03f,0.11f, false));
    	
    	return map;
    }
    
    @Override
    public void Update() 
    {
        
    	super.Update();

    	if (is5Pressed)
    			main.SetScenegraph(new SecondLevel(main));
 
        if (isLockShown)
        {
        	UpdateLock();
        	return;
        }
        
        for (String key : buttons.keySet())
        {
        	UpdateButton(key, buttons.get(key));
        }
    }
    
    private void UpdateLock()
    {
    	for (int key : numKeys)
    	{
    		if (Keyboard.IsKeyPushed(key))
    		{
    			beepSound.Play();
    		}
    	}
    	  	
		if (Keyboard.IsKeyPushed(GLFW_KEY_6) && !is6Pressed)
		{
			is6Pressed = true;
		}	
		else if (Keyboard.IsKeyPushed(GLFW_KEY_2) && !is2Pressed && is6Pressed)
		{
			is2Pressed = true;
		}
		else if (Keyboard.IsKeyPushed(GLFW_KEY_5) && !is5Pressed && is6Pressed && is2Pressed)
		{
			is5Pressed = true;
			System.out.println("TEST");
		}
    }
    
    private void UpdateButton(String key, GuiButton button)
    {
    	button.Update();

    	if (button.IsPressed() && button.GetTag() == null)
    	{
    		
    		switch (key) 
    		{
				case "shoe-print":
					
					Replace("invent_one", new GuiBackground(PATH + "inventory/shoe_print.png", -0.95f, 0.35f, 0.14f, 0.25f));					
					button.SetTag("replaced");
					break;
				case "rope-2-m":
			       	Replace("invent_second", new GuiBackground(PATH + "inventory/2_m_rope.png", -0.95f, 0.05f, 0.14f, 0.25f));	
			       	button.SetTag("replaced");
		        	break;
				case "rope-5-m":
			      	Replace("invent_third", new GuiBackground(PATH + "inventory/5_m_rope.png", -0.95f, -0.25f, 0.14f, 0.25f));	
			      	      	
			      	button.SetTag("replaced");
		        	break;
		        
				case "door-lock":
					
					if (buttons.get("shoe-print").IsPressed() && buttons.get("rope-2-m").IsPressed() &&
						buttons.get("rope-5-m").IsPressed())
					{
					
						Remove("background2");
			        	Add("door-lock", codeLock);
				      	button.SetTag("replaced");
									      	
				      	isLockShown = true;
					}
					else
					{
						button.SetPressed(false);
					}
			}
    	}
    }

    @Override
    public void CleanUp()
    {
    	beepSound.CleanUp();
        backgroundAudio.CleanUp();
        super.CleanUp();
    }
}