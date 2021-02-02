package net.cny.scenegraph;

import net.cny.Main;

import java.util.HashMap;

public class Scenegraph
{

	private final HashMap<String, Node> objects;
	private final Main.GameState state;

	public Scenegraph(Main.GameState state)
	{
		this.objects = new HashMap<>();
		this.state = state;

		Main.cny.SetState(state);
	}
	
	public void Initialize() {}

	public void OnPause() {}

	public void OnResume() {}

	public void Update(float delta)
	{
		for (String key : objects.keySet())
			GetNode(key).Update(delta);
	}
	
	public void CleanUp() 
	{
		for (String key : objects.keySet())
			GetNode(key).CleanUp();
	}

	public Node GetNode(String key)
	{
		return objects.get(key);
	}

	public void AddNode(String key, Node object)
	{
		objects.put(key, object);
	}

	public void Replace(String key, Node newObject)
	{
		objects.get(key).CleanUp();
		objects.replace(key, newObject);
	}

	public void RemoveNode(String key)
	{
		objects.get(key).CleanUp();
		objects.remove(key);
	}

	public HashMap<String, Node> GetNodeObjects() {
		return objects;
	}

	public Main.GameState GetState()
	{
		return state;
	}
}
