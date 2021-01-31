package net.cny.gui;

import static net.cny.util.ResourceManager.ButtonAudio.*;

import net.cny.audio.Sound;
import net.cny.platform.Mouse;
import net.cny.scenegraph.NodeComponent;
import org.joml.Vector2f;
import org.lwjgl.glfw.GLFW;

public class GuiClickListener extends NodeComponent
{

	private boolean isHover;
	private boolean isPressed;

	private Vector2f position;
	private Vector2f scale;
	private final Sound clickSound;
	private final Sound hoverSound;

	private boolean hoverSoundPlayed;

	public GuiClickListener(Vector2f position, Vector2f scale)
	{
		this.position = position;
		this.scale = scale;
		clickSound = new Sound(CLICK_SOUND, false);
		clickSound.SetPosition(position.x, position.y);

		hoverSound = new Sound(CLICK_SOUND, false);
		hoverSound.SetPosition(position.x, position.y);
		hoverSound.SetPitch(4);
	}
	
	@Override
	public void Update(float delta)
	{
		
		if (Mouse.GetOpenGLX() >= position.x && position.x + scale.x >= Mouse.GetOpenGLX() &&
			Mouse.GetOpenGLY() >= position.y && position.y + scale.y >= Mouse.GetOpenGLY())
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
		getParent().SetTranslation(position.x + scale.x / 2 + dx, position.y + scale.y / 2, 0);
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
	public void CleanUp() {
		clickSound.CleanUp();
		hoverSound.CleanUp();
		super.CleanUp();
	}

	@Override
	public String GetType() {
		return "gui-click-listener";
	}
}
