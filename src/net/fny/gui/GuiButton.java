package net.fny.gui;

import net.fny.core.math.Vec2f;
import net.fny.core.scenegraph.GameObject;
import net.fny.core.texturing.Texture2D;

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
