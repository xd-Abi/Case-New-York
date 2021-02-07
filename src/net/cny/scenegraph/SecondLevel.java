package net.cny.scenegraph;

import java.awt.Button;
import java.util.HashMap;
import java.util.Map;

import net.cny.core.platform.Mouse;
import net.cny.core.scenegraph.Node;
import net.cny.core.scenegraph.Scenegraph;
import net.cny.gui.GuiBackground;
import net.cny.gui.GuiButton;

public class SecondLevel extends Scenegraph{

	private static final String PATH = "level/two/";
	private static final String INVENTORY_PATH = "inventory/inventory.png";
	
	private GuiButton ko_cl;
	
	@Override
	public Map<String, Node> CreateMap() 
	{
		
		Map<String, Node> objects = new HashMap<>();
		
		objects.put("test", new GuiBackground(PATH + "background.png"));
	    objects.put("invent_one", new GuiBackground(INVENTORY_PATH, -0.95f, 0.35f, 0.14f, 0.25f));
		
		return objects;
	}
	
	
	@Override
	public void Initialize() 
	{
		super.Initialize();
		ko_cl = new GuiButton(0.42f, -0.45f, 0.03f, 0.031f, false);
	}
	
	@Override
	public void Update() 
	{
		ko_cl.Update();
		
		if (ko_cl.IsPressed() && ko_cl.GetTag() == null)
		{
			Replace("invent_one", new GuiBackground(PATH + "inventory/ko.png", -0.95f, 0.35f, 0.14f, 0.25f));	
			System.out.print("TEST");
		}
		
		super.Update();
		System.out.println(Mouse.GetOpenGLX() + " " + Mouse.GetOpenGLY());
	}
}
