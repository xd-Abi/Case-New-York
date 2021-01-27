
package net.fny.core.renderer;

import org.lwjgl.opengl.GL13;

import net.fny.core.pipeline.ShaderProgram;
import net.fny.core.scenegraph.NodeComponent;
import net.fny.core.texturing.Texture2D;

public class Renderer extends NodeComponent
{
	
	private Texture2D texture;
	private ShaderProgram shaderProgram;
	
	@Override
	public void Render() 
	{
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		
		texture.Bind();
		shaderProgram.Bind();
		shaderProgram.UpdateUniforms(getParent());
		getParent().GetMesh().Draw();
		texture.Unbind();
	}
	
	@Override
	public void CleanUp() 
	{
		shaderProgram.Delete();
		texture.Delete();
	}
	
	public void SetTexture(Texture2D texture) 
	{
		this.texture = texture;
	}
	
	public void SetShaderProgram(ShaderProgram shaderProgram) 
	{
		this.shaderProgram = shaderProgram;
	}
	
	@Override
	public String GetType() {
		return "renderer";
	}
}
