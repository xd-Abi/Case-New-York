package net.cny.scenegraph;

public abstract class NodeComponent 
{
	
	private Node parent;
	
	public void Update() {};
	
	public void CleanUp() {};
	
	public abstract String GetType();
	
	public Node GetParent()
	{
		return parent;
	}
	
	public void setParent(Node parent) 
	{
		this.parent = parent;
	}
}
