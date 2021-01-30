package net.cny.scenegraph;

import java.util.ArrayList;

public class Scenegraph
{
	
	private final ArrayList<Node> objects;
	
	public Scenegraph()
	{
		objects = new ArrayList<>();
	}
	
	public void Initialize() {}
	
	public void Update(float delta)
	{
		for (Node object : objects)
			object.Update(delta);
	}
	
	public void Render() 
	{
		for (Node object : objects)
			object.Render();
	}
	
	public void CleanUp() 
	{
		for (Node object : objects)
			object.CleanUp();
	}

	public Node GetNode(int index)
	{
		return objects.get(index);
	}

	public void AddNode(Node object)
	{
		objects.add(object);
	}

	public void RemoveNode(Node node)
	{
		node.CleanUp();
		objects.remove(node);
	}

	public ArrayList<Node> GetNodeObjects() {
		return objects;
	}
}
