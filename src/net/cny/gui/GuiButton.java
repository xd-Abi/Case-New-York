package net.cny.gui;

import net.cny.core.math.Vec2f;

public class GuiButton extends GuiBackground {
    private final GuiClickListener listener;

    public GuiButton(String texturePath, float p1, float p2, float s1, float s2, boolean a) {
        super(texturePath, p1, p2, s1, s2);

        listener = new GuiClickListener(new Vec2f(p1, p2), new Vec2f(s1, s2), a);
        AddComponent(listener);
    }

    public GuiButton(float p1, float p2, float s1, float s2, boolean a) {
        super(p1, p2, s1, s2);

        listener = new GuiClickListener(new Vec2f(p1, p2), new Vec2f(s1, s2), a);
        AddComponent(listener);
    }

    public boolean IsHover() {
        return listener.IsHover();
    }

    public boolean IsPressed() {
        return listener.IsPressed();
    }

    public void SetPressed(boolean a) {
        listener.SetPressed(a);
    }
}