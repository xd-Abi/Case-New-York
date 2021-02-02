package net.cny.scenegraph;

import net.cny.image.Image;
import net.cny.math.Transform;
import org.joml.Vector2f;

import java.util.HashMap;

public class Node extends Transform
{
	private final HashMap<String, NodeComponent> components;
	private final Image image;
	private String tag;

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

	public String GetTag()
	{
		return tag;
	}

	public void SetTag(String tag)
	{
		this.tag = tag;
	}
}
