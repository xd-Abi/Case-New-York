package net.cny.scenegraph;

import java.util.HashMap;

import net.cny.math.Transform;
import net.cny.model.Mesh;
import net.cny.RenderingEngine;

public class Node 
{
	private final HashMap<String, NodeComponent> components;
	private final Transform transform;
	private final Mesh mesh;
	
	public Node()
	{
		components = new HashMap<>();
		transform = new Transform();
		mesh = Mesh.GetInstance();
	}
	
	public void Update()
	{
		for (String type : components.keySet())
			GetComponent(type).Update();
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
	
	public Transform GetTransform() {
		return transform;
	}
	
	public Mesh GetMesh() {
		return mesh;
	}
}
