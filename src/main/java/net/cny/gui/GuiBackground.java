package net.cny.gui;

import net.cny.core.image.Image;
import net.cny.core.scenegraph.Node;
import org.joml.Vector2f;

public class GuiBackground extends Node
{

    public GuiBackground(String texturePath)
    {
        super(new Image(texturePath));

        SetTransformation(-1, -1, 2, 2);
    }

    public GuiBackground(String texturePath, float p1, float p2, float s1, float s2)
    {
        super(new Image(texturePath));

        SetTransformation(p1,p2,s1,s2);
    }

    public GuiBackground(float p1, float p2, float s1, float s2)
    {
        super(null);

        SetTransformation(p1,p2,s1,s2);
    }


}
