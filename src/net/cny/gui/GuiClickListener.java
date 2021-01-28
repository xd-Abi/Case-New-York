package net.cny.gui;

import static net.cny.util.ResourceManager.ButtonAudio.*;

import net.cny.audio.Sound;
import net.cny.math.Vec2f;
import net.cny.platform.Mouse;
import net.cny.scenegraph.NodeComponent;
import org.lwjgl.glfw.GLFW;

public class GuiClickListener extends NodeComponent
{

	private boolean isHover;
	private boolean isPressed;

	private Vec2f position;
	private Vec2f scale;
	private Sound clickSound;
	private Sound hoverSound;

	private boolean hoverSoundPlayed;

	public GuiClickListener(Vec2f position, Vec2f scale)
	{
		this.position = position;
		this.scale = scale;
		clickSound = new Sound(CLICK_SOUND);
		clickSound.SetPosition(position.getX() + scale.getX() / 2, position.getY() + scale.getY() / 2);

		hoverSound = new Sound(CLICK_SOUND);
		hoverSound.SetPosition(position.getX() + scale.getX() / 2, position.getY() + scale.getY() / 2);
		hoverSound.SetPitch(4);
	}
	
	@Override
	public void Update() 
	{
		
		if (Mouse.GetOpenGLX() >= position.getX() && position.getX() + scale.getX() >= Mouse.GetOpenGLX() &&
			Mouse.GetOpenGLY() >= position.getY() && position.getY() + scale.getY() >= Mouse.GetOpenGLY())
		{
			isHover = true;
			moveX(0.01f);
			
			if (Mouse.IsButtonPushed(GLFW.GLFW_MOUSE_BUTTON_1))
			{
				clickSound.Play();
				isPressed = true;
			}

		}
		else if (!isPressed)
		{
			moveX(-0.01f);
			isHover = false;
		}

		if (isHover && !hoverSoundPlayed){
			hoverSound.Play();
			hoverSoundPlayed = true;
		}

		if (!isHover)
			hoverSoundPlayed = false;
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
