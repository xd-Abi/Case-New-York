package old.cny.pipeline;

import old.cny.scenegraph.Node;

public class DefaultShaderProgram extends ShaderProgram
{
	
	public DefaultShaderProgram()
	{
		super();
		AddVertexShader(GetVertexShader());
		AddFragmentShader(GetFragmentShader());
		CompileShader();
		AddUniform("worldMatrix");
	}
	
	@Override
	public void UpdateUniforms(Node object) {
		SetUniform("worldMatrix", object.GetWorldMatrix());
	}

	private String GetVertexShader()
	{
		return """
				#version 330

				layout (location = 0) in vec2 position;
				layout (location = 1) in vec2 textureCoordinates;

				out vec2 passTextureCoordinates;

				uniform mat4 worldMatrix;

				void main()
				{
				    gl_Position = worldMatrix * vec4(position.x, position.y, 0, 1);
					passTextureCoordinates = textureCoordinates;
				}""";
	}

	private String GetFragmentShader()
	{
		return """
				#version 330

				in vec2 passTextureCoordinates;

				out vec4 FragColor;

				uniform sampler2D sampler;

				void main(){
				    FragColor = texture(sampler, passTextureCoordinates);
				}""";
	}
}
