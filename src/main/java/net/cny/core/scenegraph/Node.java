package net.cny.core.scenegraph;

import net.cny.core.image.Image;
import net.cny.core.math.Transform;

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

	public void Update()
	{
		for (String type : components.keySet())
			GetComponent(type).Update();
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

	public void SetTransformation(float p1, float p2, float s1, float s2)
	{
		SetTranslation(p1 + s1 / 2, p2 + s2 / 2, 0);
		SetScaling(s1, s2, 1);
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
