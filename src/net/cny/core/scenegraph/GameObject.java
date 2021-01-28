package net.cny.core.scenegraph;

import net.cny.core.math.Vec2f;
import net.cny.core.pipeline.DefaultShaderProgram;
import net.cny.core.pipeline.ShaderProgram;
import net.cny.core.renderer.Renderer;
import net.cny.core.texturing.Texture2D;

public class GameObject extends Node
{
	public GameObject(Texture2D texture)
	{
		Renderer renderer = new Renderer();
		renderer.SetTexture(texture);
		renderer.SetShaderProgram(new DefaultShaderProgram());
		
		AddComponent(renderer);	
	}
	
	public GameObject(Texture2D texture, ShaderProgram shaderProgram)
	{
		Renderer renderer = new Renderer();
		renderer.SetTexture(texture);
		renderer.SetShaderProgram(shaderProgram);
		
		AddComponent(renderer);	
	}
	
	public void SetTraformation(Vec2f position, Vec2f scale)
	{
		GetTransform().SetTranslation(position.getX() + scale.getX() / 2, position.getY() + scale.getY() / 2, 0);
		GetTransform().SetScaling(scale.getX(), scale.getY(), 0);
	}
}
