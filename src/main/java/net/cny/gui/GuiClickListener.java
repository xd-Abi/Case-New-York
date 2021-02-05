package net.cny.gui;

import net.cny.core.audio.Audio;
import net.cny.core.platform.Mouse;
import net.cny.core.scenegraph.NodeComponent;
import org.joml.Vector2f;
import org.lwjgl.glfw.GLFW;

public class GuiClickListener extends NodeComponent
{

    private boolean isHover;
    private boolean isPressed;

    private final Vector2f position;
    private final Vector2f scale;
    private final Audio clickSound;
    private final Audio hoverSound;

    private boolean hoverSoundPlayed;

    public GuiClickListener(Vector2f position, Vector2f scale)
    {
        this.position = position;
        this.scale = scale;
        clickSound = new Audio("gui/button/click_sound.wav");
        clickSound.SetPosition(position.x, position.y);

        hoverSound = new Audio("gui/button/click_sound.wav");
        hoverSound.SetPosition(position.x, position.y);
        hoverSound.SetPitch(4);
    }

    @Override
    public void Update()
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
        GetParent().SetTranslation(position.x + scale.x / 2 + dx, position.y + scale.y / 2, 0);
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