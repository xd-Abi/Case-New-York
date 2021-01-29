package net.cny.scenegraph;

import java.util.HashMap;

import net.cny.math.Transform;
import net.cny.model.Mesh;
import net.cny.pipeline.DefaultShaderProgram;
import net.cny.renderer.Renderer;
import net.cny.image.Image;
import org.joml.Vector2f;

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

	public void AddRenderer(Image image)
	{
		Renderer renderer = new Renderer();
		renderer.SetImage(image);
		renderer.SetShaderProgram(new DefaultShaderProgram());

		AddComponent(renderer);
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
		GetTransform().SetTranslation(position.x + scale.x / 2, position.y + scale.y / 2, 0);
		GetTransform().SetScaling(scale.x, scale.y, 0);
	}

	public Transform GetTransform() {
		return transform;
	}
	
	public Mesh GetMesh() {
		return mesh;
	}
}
