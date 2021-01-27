package net.fny.core.pipeline;

import net.fny.core.scenegraph.Node;
import net.fny.util.ResourceManager;

public class DefaultShaderProgram extends ShaderProgram
{
	
	public DefaultShaderProgram()
	{
		super();
		AddVertexShader(ResourceManager.DEFAULT_VERTEX_SHADER);
		AddFragmentShader(ResourceManager.DEFAULT_FRAGMENT_SHADER);
		CompileShader();
		AddUniform("worldMatrix");
	}
	
	@Override
	public void UpdateUniforms(Node object) {
		SetUniform("worldMatrix", object.GetTransform().GetWorldMatrix());
	}
}
