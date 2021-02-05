package net.cny.gui;

import org.joml.Vector2f;

public class GuiButton extends GuiBackground {
    private final GuiClickListener listener;

    public GuiButton(String texturePath, float p1, float p2, float s1, float s2) {
        super(texturePath, p1, p2, s1, s2);

        listener = new GuiClickListener(new Vector2f(p1, p2), new Vector2f(s1, s2));
        AddComponent(listener);
    }

    public boolean IsHover() {
        return listener.IsHover();
    }

    public boolean IsPressed() {
        return listener.IsPressed();
    }
}