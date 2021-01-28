package net.cny.gui;

import net.cny.core.math.Vec2f;
import net.cny.core.pipeline.ShaderProgram;
import net.cny.core.scenegraph.GameObject;
import net.cny.core.texturing.Texture2D;

public class GuiBackground extends GameObject
{

	public GuiBackground(Texture2D texture, ShaderProgram shaderProgram) 
	{
		super(texture, shaderProgram);
		super.SetTraformation(new Vec2f(-1,-1), new Vec2f(2, 2));
	}

	public GuiBackground(String texturePath) 
	{
		super(new Texture2D(texturePath));
		super.SetTraformation(new Vec2f(-1,-1), new Vec2f(2, 2));
	}

	public GuiBackground(String texturePath, Vec2f position, Vec2f scale) 
	{
		super(new Texture2D(texturePath));
		super.SetTraformation(position, scale);
	}
	
}
