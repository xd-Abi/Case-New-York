package net.cny.gui;

import net.cny.image.Image;
import net.cny.scenegraph.Node;
import org.joml.Vector2f;

public class GuiBackground extends Node
{

	public GuiBackground(String texturePath) 
	{
		super();

		SetTransformation(new Vector2f(-1,-1), new Vector2f(2, 2));
		AddRenderer(new Image(texturePath));
	}

	public GuiBackground(String texturePath, Vector2f position, Vector2f scale)
	{
		super();

		SetTransformation(position, scale);
		AddRenderer(new Image(texturePath));
	}
	
}
