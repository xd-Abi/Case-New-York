package net.cny.gui;

import net.cny.core.math.Vec2f;
import net.cny.core.scenegraph.GameObject;
import net.cny.core.texturing.Texture2D;

public class GuiButton extends GameObject
{
	
	private GuiClickListener listener;
	
	public GuiButton(String texturePath, Vec2f position, Vec2f scale) 
	{
		super(new Texture2D(texturePath));
		super.SetTraformation(position, scale);
		
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
