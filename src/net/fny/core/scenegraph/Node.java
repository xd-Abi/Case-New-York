package net.fny.core.scenegraph;

import java.util.HashMap;

import net.fny.core.RenderingEngine;
import net.fny.core.math.Transform;
import net.fny.core.model.Mesh;

public class Node 
{
	private HashMap<String, NodeComponent> components;
	private Transform transform;
	private Mesh mesh;
	
	public Node()
	{
		components = new HashMap<>();
		transform = new Transform();
		mesh = RenderingEngine.getQuad();
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
