package net.fny.gui;

import net.fny.core.math.Vec2f;
import net.fny.core.pipeline.ShaderProgram;
import net.fny.core.scenegraph.GameObject;
import net.fny.core.texturing.Texture2D;

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
