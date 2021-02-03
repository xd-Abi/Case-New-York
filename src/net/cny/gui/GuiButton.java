package net.cny.gui;

import org.joml.Vector2f;

public class GuiButton extends GuiBackground
{
    private final GuiClickListener listener;

    public GuiButton(String texturePath, Vector2f position, Vector2f scale)
    {
        super(texturePath, position, scale);

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
