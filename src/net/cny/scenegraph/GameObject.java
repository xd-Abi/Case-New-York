package net.cny.scenegraph;

import net.cny.math.Vec2f;
import net.cny.pipeline.DefaultShaderProgram;
import net.cny.pipeline.ShaderProgram;
import net.cny.renderer.Renderer;
import net.cny.image.Image;

public class GameObject extends Node
{
	public GameObject(Image image)
	{
		Renderer renderer = new Renderer();
		renderer.SetImage(image);
		renderer.SetShaderProgram(new DefaultShaderProgram());
		
		AddComponent(renderer);	
	}
	
	public GameObject(Image image, ShaderProgram shaderProgram)
	{
		Renderer renderer = new Renderer();
		renderer.SetImage(image);
		renderer.SetShaderProgram(shaderProgram);
		
		AddComponent(renderer);	
	}
	
	public void SetTransformation(Vec2f position, Vec2f scale)
	{
		GetTransform().SetTranslation(position.getX() + scale.getX() / 2, position.getY() + scale.getY() / 2, 0);
		GetTransform().SetScaling(scale.getX(), scale.getY(), 0);
	}
}
