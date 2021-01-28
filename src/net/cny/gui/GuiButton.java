package net.cny.gui;

import net.cny.math.Vec2f;
import net.cny.scenegraph.GameObject;
import net.cny.image.Image;

public class GuiButton extends GameObject
{
	
	private final GuiClickListener listener;
	
	public GuiButton(String texturePath, Vec2f position, Vec2f scale) 
	{
		super(new Image(texturePath));
		super.SetTransformation(position, scale);
		
		listener = new GuiClickListener(position, scale);
		AddComponent(listener);
	}
	
	public boolean IsHover()
	{
		return listener.IsHover();
	}
	
	public boolean IsPressed()
	{
		return listener.IsPressed();
	}
}
