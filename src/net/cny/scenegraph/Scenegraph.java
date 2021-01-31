package net.cny.scenegraph;

import net.cny.GameState;
import net.cny.Main;

import java.util.ArrayList;

public class Scenegraph
{

	private final ArrayList<Node> objects;
	private final GameState state;

	public Scenegraph(GameState state)
	{
		this.objects = new ArrayList<>();
		this.state = state;

		Main.cny.SetState(state);
	}
	
	public void Initialize() {}

	public void OnPause() {}

	public void OnResume() {}

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

	public GameState GetState()
	{
		return state;
	}
}
