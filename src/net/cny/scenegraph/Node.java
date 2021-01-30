package net.cny.scenegraph;

import java.util.HashMap;

import net.cny.math.Transform;
import net.cny.model.Mesh;
import net.cny.pipeline.DefaultShaderProgram;
import net.cny.image.Image;
import org.joml.Vector2f;

public class Node extends Transform
{
	private final HashMap<String, NodeComponent> components;
	private final Image image;

	public Node(Image image)
	{
		this.components = new HashMap<>();
		this.image = image;
	}

	public void Update(float delta)
	{
		for (String type : components.keySet())
			GetComponent(type).Update(delta);
	}
	
	public void Render()
	{
		for (String type : components.keySet())
			GetComponent(type).Render();	
	}
	
	public void CleanUp()
	{
		for (String type : components.keySet())
			GetComponent(type).CleanUp();
	}
	
	public NodeComponent GetComponent(String type)
	{
		return components.get(type);
	}
	
	public void AddComponent(NodeComponent component)
	{
		component.setParent(this);
		components.put(component.GetType(), component);
	}

	public void SetTransformation(Vector2f position, Vector2f scale)
	{
		SetTranslation(position.x + scale.x / 2, position.y + scale.y / 2, 0);
		SetScaling(scale.x, scale.y, 0);
	}

	public Image GetImage()
	{
		return image;
	}
}
