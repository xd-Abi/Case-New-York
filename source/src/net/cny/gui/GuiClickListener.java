package net.cny.gui;

import net.cny.core.audio.Audio;
import net.cny.core.math.Vec2f;
import net.cny.core.platform.Mouse;
import net.cny.core.scenegraph.NodeComponent;
import org.lwjgl.glfw.GLFW;

public class GuiClickListener extends NodeComponent
{

    private boolean isHover;
    private boolean isPressed;

    private final Vec2f position;
    private final Vec2f scale;
    private Audio clickSound;
    private Audio hoverSound;

    private boolean hoverSoundPlayed;
    private final boolean playSound;

    public GuiClickListener(Vec2f position, Vec2f scale, boolean playSound)
    {
        this.position = position;
        this.scale = scale;
        this.playSound = playSound;

       if (!playSound)
           return;

        clickSound = new Audio("gui/button/click_sound.wav");
        clickSound.SetPosition(position.getX(), position.getY());

        hoverSound = new Audio("gui/button/click_sound.wav");
        hoverSound.SetPosition(position.getX(), position.getY());
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

                if(playSound)
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

          if (playSound)
            hoverSound.Play();

            hoverSoundPlayed = true;
        }

        if (!isHover)
            hoverSoundPlayed = false;
    }

    private void moveX(float dx)
    {
        GetParent().SetTranslation(position.getX() + scale.getX() / 2 + dx, position.getY() + scale.getY() / 2, 0);
    }

    public boolean IsHover()
    {
        return isHover;
    }

    public boolean IsPressed()
    {
        return isPressed;
    }

    public void SetPressed(boolean a)
    {
         isPressed = a;
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