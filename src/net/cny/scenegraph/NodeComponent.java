package net.cny.scenegraph;

public abstract class NodeComponent 
{
	
	private Node parent;
	
	public void Update() {};
	
	public void Render() {};
	
	public void CleanUp() {};
	
	public abstract String GetType();
	
	public Node getParent() 
	{
		return parent;
	}
	
	public void setParent(Node parent) 
	{
		this.parent = parent;
	}
}
