package net.cny.gui;

import net.cny.math.Vec2f;
import net.cny.pipeline.ShaderProgram;
import net.cny.scenegraph.GameObject;
import net.cny.image.Image;

public class GuiBackground extends GameObject
{

	public GuiBackground(Image image, ShaderProgram shaderProgram)
	{
		super(image, shaderProgram);
		super.SetTransformation(new Vec2f(-1,-1), new Vec2f(2, 2));
	}

	public GuiBackground(String texturePath) 
	{
		super(new Image(texturePath));
		super.SetTransformation(new Vec2f(-1,-1), new Vec2f(2, 2));
	}

	public GuiBackground(String texturePath, Vec2f position, Vec2f scale) 
	{
		super(new Image(texturePath));
		super.SetTransformation(position, scale);
	}
	
}
