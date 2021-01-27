package net.fny.gui;

import org.lwjgl.glfw.GLFW;

import net.fny.audio.SoundManager;
import net.fny.core.math.Vec2f;
import net.fny.core.platform.Mouse;
import net.fny.core.scenegraph.NodeComponent;

public class GuiClickListener extends NodeComponent
{
	
	private boolean isHover;
	private boolean isPressed;
	
	private Vec2f position;
	private Vec2f scale;
	
	public GuiClickListener(Vec2f position, Vec2f scale)
	{
		this.position = position;
		this.scale = scale;
	}
	
	@Override
	public void Update() 
	{
		
		if (Mouse.GetOpenGLX() >= position.getX() && position.getX() + scale.getX() >= Mouse.GetOpenGLX() && 
			Mouse.GetOpenGLY() >= position.getY() && position.getY() + scale.getY() >= Mouse.GetOpenGLY())
		{
			isHover = true;
			moveX(0.01f);
			
			if (Mouse.IsButtonPushed(GLFW.GLFW_MOUSE_BUTTON_1)) {
				
				isPressed = true;
				try {
					SoundManager.Initalize();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		}
		else if (!isPressed)
		{
			moveX(-0.01f);
			isHover = false;
		}
	}
	
	private void moveX(float dx)
	{
		getParent().GetTransform().SetTranslation(position.getX() + scale.getX() / 2 + dx, position.getY() + scale.getY() / 2, 0);
	}

	public boolean IsHover() 
	{
		return isHover;
	}
	
	public boolean IsPressed() 
	{
		return isPressed;
	}
	
	@Override
	public String GetType() {
		return "gui-click-listener";
	}
}
