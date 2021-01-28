package net.cny.scenegraph;

import java.util.ArrayList;

public class Scene 
{
	
	private ArrayList<GameObject> objects;
	
	public Scene()
	{
		objects = new ArrayList<>();
	}
	
	public void Initialize() {};
	
	public void Update() 
	{
		for (GameObject object : objects)
			object.Update();
	}
	
	public void Render() 
	{
		for (GameObject object : objects)
			object.Render();		
	}
	
	public void CleanUp() 
	{
		for (GameObject object : objects)
			object.CleanUp();
	}
	
	public void AddObject(GameObject object)
	{
		objects.add(object);
	}
}
