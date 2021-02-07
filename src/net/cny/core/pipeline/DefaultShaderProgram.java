package net.cny.core.pipeline;

import net.cny.core.scenegraph.Node;
import net.cny.core.util.ResourceLoader;

public class DefaultShaderProgram extends ShaderProgram
{
	
	public DefaultShaderProgram()
	{
		super();
		AddVertexShader(ResourceLoader.LoadShader("shaders/gui-vertex.net"));
		AddFragmentShader(ResourceLoader.LoadShader("shaders/gui-fragment.net"));
		CompileShader();
		AddUniform("worldMatrix");
	}
	
	@Override
	public void UpdateUniforms(Node object)
	{
		SetUniform("worldMatrix", object.GetWorldMatrix());
	}

}
